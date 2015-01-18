/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tag_project;

import bropals.lib.simplegame.animation.Animation;
import bropals.lib.simplegame.animation.Track;
import bropals.lib.simplegame.entity.BaseEntity;
import bropals.lib.simplegame.entity.block.BlockEntity;
import bropals.lib.simplegame.gui.Gui;
import bropals.lib.simplegame.gui.GuiGroup;
import bropals.lib.simplegame.gui.GuiImage;
import bropals.lib.simplegame.gui.GuiProgressBar;
import bropals.lib.simplegame.gui.GuiText;
import bropals.lib.simplegame.logger.InfoLogger;
import bropals.lib.simplegame.state.GameState;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import tag_project.factory.HouseLoader;

/**
 *
 * @author Jonathon
 */
public class HouseState extends GameState {

    private Gui gui;
    private IsometricGameWorld world;
    private boolean paused = false;
    private boolean movingUp, movingDown, movingRight, movingLeft;
    private GuiText biscuits, furniture;
    private Camera camera;
    private PlayerValues playerValues;
    private FurnitureTearManager furnitureTearManager;

    /**
     * Super special reference to the player (dog)
     */
    private AnimatedIsometricEntity dog, boy;
    private float DOG_SPEED_DIAG = 28;
    private float DOG_SPEED = DOG_SPEED_DIAG * (float) Math.sqrt(2);
    private float BOY_SPEED = DOG_SPEED * 0.4f;
    //How far do you need to be from a piece of furniture's center to tear it?
    private float TEAR_DISTANCE = 40;
    private FurnitureEntity couldTear = null;

    ///Use R to swap rendering modes
    private boolean developmentRendering = false;
    private final boolean developmentCameraControls = false;

    private NavigationNode[][] navigationNodes;
    
    @Override
    public void update() {
        Point mp = getWindow().getMousePosition();
        if (!paused) {
            if (!furnitureTearManager.isTearing()) {
                updateDogMovement();
            }
            centerCameraOnDog();
            updateTearing();
            world.updateEntities();
            if (boy != null && dog != null && navigationNodes != null) { 
                boy.chase(dog, navigationNodes, BOY_SPEED); // boy chases the dog
            }
            gui.update(mp.x, mp.y);
            if (hasWon()) {
                win();
            }
        }
    }

    @Override
    public void render(Object o) {
        Graphics g = (Graphics) o;
        clearBackground(g);
        if (!developmentRendering) {
            drawInIsometricMode(o);
        } else {
            drawInDevelopmentMode(g);
        }
        if (couldTear != null) {
            drawTearIcon(g);
        }
        gui.render(o);
    }

    private void clearBackground(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWindow().getScreenWidth(), getWindow().getScreenHeight());
    }

    private void drawInIsometricMode(Object graphicsObject) {
        for (int j=0; j<world.getEntities().size(); j++) {
            IsometricEntity be = world.getEntities().get(j);
            be.calcRenderCoords();
            if (be == dog || be == boy) {
                AnimatedIsometricEntity aie = (AnimatedIsometricEntity)be;
                
                aie.render(graphicsObject);
                
            } else {
                ((BaseEntity) be).render(graphicsObject);
            }
        }
    }

    private void drawInDevelopmentMode(Graphics g) {
        for (Object be : world.getEntities()) {
            BlockEntity block = ((BlockEntity) be);
            if (block instanceof FurnitureEntity) {
                g.setColor(Color.RED);
            } else if (block instanceof BiscuitEntity) {
                g.setColor(Color.YELLOW);
            } else if (block instanceof AnimatedIsometricEntity) {
                g.setColor(Color.GREEN);
            } else if (block instanceof DecorationEntity) {
                g.setColor(Color.WHITE);
            } else {
                g.setColor(Color.BLUE);
            }
            int x = (int) (block.getX() - camera.getX());
            int y = (int) (block.getY() - camera.getY());
            int w = (int) block.getWidth();
            int h = (int) block.getHeight();
            g.fillRect(
                    x,
                    y,
                    w, h);
            //g.fillRect(300, 300, 80, 80);
        }
        // draw the nodes
        for (NavigationNode[] nodes : navigationNodes) {
            for (NavigationNode node : nodes) {
                if (node.getMoveCost() > 10) {
                    g.setColor(Color.RED);
                } else {
                    g.setColor(Color.BLACK);
                }
                g.drawRect((int)(node.getX() - camera.getX()), (int)(node.getY() - camera.getY()),  
                       (int)node.getWidth(), (int)node.getHeight());
            }
        }
        // draw the boy's pathfinding path
        g.setColor(Color.MAGENTA);
        ArrayList<NavigationNode> boyPath = boy.getPath();
        if (boyPath != null) {
            for (int i=1; i<boyPath.size(); i++) {
                g.drawLine((int)(boyPath.get(i).getCenterX() - camera.getX()), 
                        (int)(boyPath.get(i).getCenterY() - camera.getY()), 
                        (int)(boyPath.get(i-1).getCenterX() - camera.getX()), 
                        (int)(boyPath.get(i-1).getCenterY() - camera.getY()));
            }
        }
    }

    @Override
    public void onEnter() {
        playerValues = new PlayerValues();
        movingUp = false;
        movingDown = false;
        movingRight = false;
        movingLeft = false;
        initHousePlan();
        initNavigationMesh();
        camera = new Camera();
        gui = new Gui();
        initGUI();
        initTearFeature();
        initDog();
        initBoy();
    }

    private void initHousePlan() {
        ((HouseLoader) (getAssetManager().getAssetLoader(IsometricGameWorld.class))).setHouseState(this);
        getAssetManager().loadAsset("assets/data/house.data", "The House", IsometricGameWorld.class);
        world = getAssetManager().getAsset("The House", IsometricGameWorld.class);
    }

    private void initNavigationMesh() {
        int sizeOfNode = 120;
        int worldWidth = 8640/sizeOfNode; // in nodes
        int worldHeight = 3280/sizeOfNode;
        navigationNodes = new NavigationNode[worldWidth][worldHeight];
        for (int x=0; x<worldWidth; x++) {
            for (int y=0; y<worldHeight; y++) {
                navigationNodes[x][y] = 
                    new NavigationNode(world,(x * sizeOfNode) - 1360, 
                            (y * sizeOfNode), sizeOfNode, x, y);
            }
        }
        System.out.println("Number of nodes: " + (worldWidth * worldHeight));
    }
    
    private void initTearFeature() {
        GuiGroup tearGroup = new GuiGroup();
        GuiProgressBar tearBar = new GuiProgressBar(250, 440, 300, 40, 0, 0);
        tearGroup.addElement(tearBar);
        tearGroup.setEnabled(false);
        GuiText label = new GuiText("Tearing furniture", 350, 440, 0, 0, false);
        tearGroup.addElement(label);
        gui.addGroup("tear", tearGroup);
        furnitureTearManager = new FurnitureTearManager(tearBar, this);
    }

    public void biscuitCollected() {
        playerValues.biscuitsCollected++;
        biscuits.setText("" + playerValues.biscuitsCollected + " / " + playerValues.biscuitsTotal);
        gui.disable("tear");
    }

    public boolean isTheDog(BlockEntity blockEntity) {
        return blockEntity == dog;
    }

    public Camera getCamera() {
        return camera;
    }

    private void updateDogMovement() {
        if (!developmentCameraControls) {
            // handle the key presses for dog moving
            if (movingUp && !movingDown) {
                if (dog.getVelocity().getX() == 0) {
                    dog.getVelocity().setY(-DOG_SPEED);
                } else {
                    dog.getVelocity().setY(-DOG_SPEED_DIAG);
                }
            } else if (movingDown && !movingUp) {
                if (dog.getVelocity().getX() == 0) {
                    dog.getVelocity().setY(DOG_SPEED);
                } else {
                    dog.getVelocity().setY(DOG_SPEED_DIAG);
                }
            } else if (!movingDown && !movingUp) {
                dog.getVelocity().setY(0);
            }
            if (movingRight && !movingLeft) {
                if (dog.getVelocity().getY() == 0) {
                    dog.getVelocity().setX(-DOG_SPEED);
                } else {
                    dog.getVelocity().setX(-DOG_SPEED_DIAG);
                }
            } else if (movingLeft && !movingRight) {
                if (dog.getVelocity().getY() == 0) {
                    dog.getVelocity().setX(DOG_SPEED);
                } else {
                    dog.getVelocity().setX(DOG_SPEED_DIAG);
                }
            } else if (!movingRight && !movingLeft) {
                dog.getVelocity().setX(0);
            }
        }
    }

    private void updateTearing() {
        if (furnitureTearManager.isTearing()) {
            furnitureTearManager.updateTearing();
        }
        checkTearTargets();
    }

    private void initDog() {
        Animation dogAnimation = new Animation();
        BufferedImage[] dogImages = new Track(
                getAssetManager().getImage("spanielSprites"), 139, 200, 3).getImages();
        getAssetManager().createHorizontialFlipCopy(
                getAssetManager().getImage("spanielSprites"), "reverseSpanielSprites");
        BufferedImage[] dogImagesReverse = new Track(
                getAssetManager().getImage("reverseSpanielSprites"), 139, 200, 3).getImages();

        Track northMove = new Track(new BufferedImage[]{
            dogImages[7], dogImages[4], dogImages[5], dogImages[6],
            dogImages[5], dogImages[4]
        }, 2);
        Track eastMove = new Track(new BufferedImage[]{
            dogImagesReverse[4], dogImagesReverse[7], dogImagesReverse[6], dogImagesReverse[5],
            dogImagesReverse[6], dogImagesReverse[7]
        }, 2);
        Track eastStand = new Track(new BufferedImage[]{
            dogImagesReverse[8], dogImagesReverse[9]}, 15);
        Track northStand = new Track(new BufferedImage[]{
            dogImages[10], dogImages[11]}, 15);

        Track southMove = new Track(new BufferedImage[]{
            dogImages[3], dogImages[0], dogImages[1], dogImages[2],
            dogImages[1], dogImages[0]
        }, 2);
        Track westMove = new Track(new BufferedImage[]{
            dogImagesReverse[0], dogImagesReverse[3], dogImagesReverse[2], dogImagesReverse[1],
            dogImagesReverse[2], dogImagesReverse[3]
        }, 2);
        Track southStand = new Track(new BufferedImage[]{
            dogImages[8], dogImages[9]}, 15);
        Track westStand = new Track(new BufferedImage[]{
            dogImagesReverse[10], dogImagesReverse[11]}, 15);
        // running animations
        dogAnimation.addTrack(northMove); // 0
        dogAnimation.addTrack(southMove); // 1
        dogAnimation.addTrack(eastMove); // 2
        dogAnimation.addTrack(westMove); // 3
        // standing animations
        dogAnimation.addTrack(northStand); // 4
        dogAnimation.addTrack(southStand); // 5
        dogAnimation.addTrack(eastStand); // 6
        dogAnimation.addTrack(westStand); // 7

        dog = new AnimatedIsometricEntity(world,
                -900, 1160,  80, 80, false, IsometricDirection.SOUTH,
                null, null, null, null, dogAnimation);
    }

    private void initBoy() {
        Animation boyAnimation = new Animation();
        BufferedImage[] boyImages = new Track(
            getAssetManager().getImage("boySprites"), 139, 200).getImages();
        getAssetManager().createHorizontialFlipCopy(
                getAssetManager().getImage("boySprites"), "reverseBoySprites");
        BufferedImage[] boyImagesReverse = new Track(
                getAssetManager().getImage("reverseBoySprites"), 139, 200, 3).getImages();
        
        Track boyRunSouth = new Track(new BufferedImage[]{
            boyImages[0], boyImages[1], boyImages[0], boyImages[2]
        }, 3);
        Track boyRunWest = new Track(new BufferedImage[]{
            boyImagesReverse[2], boyImagesReverse[1], boyImagesReverse[2], boyImagesReverse[0]
        }, 3);
        
        Track boyRunNorth = new Track(new BufferedImage[]{
            boyImages[3], boyImages[4], boyImages[3], boyImages[5]
        }, 3);
        Track boyRunEast = new Track(new BufferedImage[]{
            boyImagesReverse[5], boyImagesReverse[4], boyImagesReverse[5], boyImagesReverse[3]
        }, 3);
        // boy moving animatons
        boyAnimation.addTrack(boyRunNorth);
        boyAnimation.addTrack(boyRunSouth);
        boyAnimation.addTrack(boyRunEast);
        boyAnimation.addTrack(boyRunWest);
        // boy standing animations
        boyAnimation.addTrack(
                new Track(new BufferedImage[]{boyImages[3]}, 20)); // stand north
        boyAnimation.addTrack(
                new Track(new BufferedImage[]{boyImages[0]}, 20)); // stand south
        boyAnimation.addTrack(
                new Track(new BufferedImage[]{boyImagesReverse[2]}, 20)); // stand west
        boyAnimation.addTrack(
                new Track(new BufferedImage[]{boyImagesReverse[5]}, 20)); // stand east
        
        // gotta change the position to be more fair
        boy = new AnimatedIsometricEntity(world,
                -900, 900, 80, 80, false, IsometricDirection.SOUTH,
                null, null, null, null, boyAnimation);
    }
    
    private void centerCameraOnDog() {
        // move camera over the dog
        if (!developmentCameraControls) {
            if (!developmentRendering) {
                camera.set(dog.getRenderCoordX() - (getWindow().getScreenWidth() / 2),
                        dog.getRenderCoordY() - (getWindow().getScreenHeight() / 2));
            } else {
                camera.set(dog.getX() - (getWindow().getScreenWidth() / 2),
                        dog.getY() - (getWindow().getScreenHeight() / 2));
            }
        }
    }

    /**
     * Draws the tear icon. Only call if couldTear != null
     *
     * @param g the graphics object to use in drawing
     */
    public void drawTearIcon(Graphics g) {
        if (developmentRendering) {
            g.setColor(Color.ORANGE);
            g.fillRect((int) (couldTear.getX() - camera.getX()),
                    (int) (couldTear.getY() - camera.getY()), 50, 50);
        } else {
            //Need to implement
        }
    }

    public void checkTearTargets() {
        for (IsometricEntity ie : world.getEntities()) {
            if (ie instanceof FurnitureEntity) {
                FurnitureEntity fe = (FurnitureEntity) ie;
                if (fe.isDefaceable() && !fe.isRippedToShreds()) {
                    if (closeEnoughTo(fe)) {
                        couldTear = fe;
                        return;
                    }
                }
            }
        }
        couldTear = null;
    }

    private boolean closeEnoughTo(FurnitureEntity fe) {
        return ((closeEnoughTop(fe) || closeEnoughBottom(fe)) && inXRange(fe)) ||
                ((closeEnoughRight(fe) || closeEnoughLeft(fe)) && inYRange(fe));
    }
    
    private boolean inXRange(FurnitureEntity fe) {
        return dog.getCenterX() > fe.getX() && dog.getCenterX() < fe.getX() 
                + fe.getWidth();
    }
    
    private boolean inYRange(FurnitureEntity fe) {
        return dog.getCenterY() > fe.getY() && dog.getCenterY() < fe.getY() 
                + fe.getHeight();
    }
    
    private boolean closeEnoughTop(FurnitureEntity fe) {
        return abs( dog.getY() - (fe.getY() + fe.getHeight()) ) < TEAR_DISTANCE;
    }
    
    private boolean closeEnoughBottom(FurnitureEntity fe) {
        return abs( dog.getY()+dog.getHeight() - (fe.getY()) ) < TEAR_DISTANCE;
    }
    
    private boolean closeEnoughLeft(FurnitureEntity fe) {
        return abs( dog.getX() - (fe.getX() + fe.getWidth()) ) < TEAR_DISTANCE;
    }
    
    private boolean closeEnoughRight(FurnitureEntity fe) {
        return abs( dog.getX()+dog.getWidth() - (fe.getX()) ) < TEAR_DISTANCE;
    }
    
    private float abs(float v) {
        if  (v < 0) {
            return -v;
        } else {
            return v;
        }
    }
    
    public void handleTearKeyInput(KeyEvent ke) {
        if (furnitureTearManager.isTearing())
            return;
        if (couldTear != null) {
            if (ke.getKeyCode() == KeyEvent.VK_X) {
                furnitureTearManager.startTearing(couldTear);
                gui.enable("tear");
            }
        }
    }

    public float getDistanceBetween(IsometricEntity e1, IsometricEntity e2) {
        float diffX = e1.getCenterX() - e2.getCenterX();
        float diffY = e1.getCenterY() - e2.getCenterY();
        return (float) (Math.sqrt((diffX * diffX) + (diffY * diffY)));
    }

    /**
     * Reloads the house place. For development purposes.
     */
    private void reloadHousePlan() {
        getAssetManager().unloadAsset("The House", IsometricGameWorld.class);
        getAssetManager().loadAsset("assets/data/house.data", "The House", IsometricGameWorld.class);
        world = getAssetManager().getAsset("The House", IsometricGameWorld.class);
    }

    private void initGUI() {
        GuiGroup main = new GuiGroup();

        GuiImage biscuitIcon = new GuiImage(20, 20, 40, 40,
                getAssetManager().getImage("biscuitIcon"));
        biscuits = new GuiText("0", 70, 20, 80, 40, false);
        GuiImage furnitureIcon = new GuiImage(150, 20, 40, 40,
                getAssetManager().getImage("furnitureIcon"));
        furniture = new GuiText("0", 200, 20, 80, 40, false);

        main.addElement(biscuitIcon);
        main.addElement(biscuits);
        main.addElement(furnitureIcon);
        main.addElement(furniture);

        this.biscuits.setText("" + playerValues.biscuitsCollected + " / " + playerValues.biscuitsTotal);
        this.furniture.setText("" + playerValues.furnitureDestroyed + " / " + playerValues.furnitureTotal);
        
        gui.addGroup("main", main);
        gui.enable("main");
    }

    @Override
    public void onExit() {

    }

    @Override
    public void key(KeyEvent ke, boolean pressed) {
        if (!developmentCameraControls) {
            handleDogControls(ke, pressed);
        }
        if (pressed) {
            handleCloseKey(ke);
            handleDevelopmentCameraControls(ke);
            handleReloadLevelKey(ke);
            handleRenderSwitchKey(ke);
            handleTearKeyInput(ke);
            handleResetGameKey(ke);
        }
    }

    private void handleRenderSwitchKey(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_R) {
            developmentRendering = !developmentRendering;
            if (developmentRendering) {
                InfoLogger.println("Set to development rendering");
            } else {
                InfoLogger.println("Set to game rendering");

            }
        }
    }

    private void handleDevelopmentCameraControls(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_W) {
            camera.move(0, -100);
        } else if (ke.getKeyCode() == KeyEvent.VK_S) {
            camera.move(0, 100);
        } else if (ke.getKeyCode() == KeyEvent.VK_A) {
            camera.move(-100, 0);
        } else if (ke.getKeyCode() == KeyEvent.VK_D) {
            camera.move(100, 0);
        }
    }

    private void handleCloseKey(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
            getWindow().requestToClose();
        }
    }

    private void handleReloadLevelKey(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_G) {
            reloadHousePlan();
            InfoLogger.println("Reloaded house plan");
        }
    }
    
    private void handleResetGameKey(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_P) {
            resetPlay();
            InfoLogger.println("Reset game ");
        }
    }

    public void resetPlay() {
        reloadHousePlan();
        dog.setX(-900);
        dog.setY(1160);
        world.addEntity(dog);
        this.biscuits.setText("" + playerValues.biscuitsCollected + " / " + playerValues.biscuitsTotal);
        this.furniture.setText("" + playerValues.furnitureDestroyed + " / " + playerValues.furnitureTotal);
    }
    
    private void handleDogControls(KeyEvent ke, boolean pressed) {
        switch (ke.getKeyCode()) {
            case KeyEvent.VK_UP:
                movingUp = pressed;
                break;
            case KeyEvent.VK_DOWN:
                movingDown = pressed;
                break;
            case KeyEvent.VK_RIGHT:
                movingRight = pressed;
                break;
            case KeyEvent.VK_LEFT:
                movingLeft = pressed;
                break;
        }
    }

    @Override
    public void mouse(MouseEvent me, boolean pressed) {
        if (pressed) {
            gui.mouseInput(me.getX(), me.getY());
        }
    }

    public void finishedTearing() {
        gui.disable("tear");
        playerValues.furnitureDestroyed++;
        furniture.setText("" + playerValues.furnitureDestroyed + " / " + playerValues.furnitureTotal);
    }
    
    public void resetProgressAndSetTotals(int biscuits, int furniture) {
        playerValues.biscuitsTotal = biscuits;
        playerValues.furnitureTotal = furniture;
        playerValues.biscuitsCollected = 0;
        playerValues.furnitureDestroyed = 0;
    }
    
    public void win() {
        InfoLogger.println("YOU WIN");
    }
    
    public boolean hasWon() {
        return playerValues.biscuitsCollected == playerValues.biscuitsTotal &&
               playerValues.furnitureDestroyed == playerValues.furnitureTotal; 
    }
}
