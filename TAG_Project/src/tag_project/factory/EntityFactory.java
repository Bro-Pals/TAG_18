/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tag_project.factory;

import bropals.lib.simplegame.io.AssetManager;
import bropals.lib.simplegame.io.PropertiesReader;
import bropals.lib.simplegame.logger.ErrorLogger;
import bropals.lib.simplegame.logger.InfoLogger;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import tag_project.BiscuitEntity;
import tag_project.DecorationEntity;
import tag_project.FurnitureEntity;
import tag_project.HouseState;
import tag_project.IsometricDirection;
import tag_project.IsometricEntity;
import tag_project.IsometricGameWorld;
import tag_project.Size;

/**
 *
 * @author Jonathon
 */
public class EntityFactory {
    
    private final static HashMap<String, FurnitureEntity> loadedFurniture
            = new HashMap<>();

    /**
     * Loads all furniture data. Be sure to load images before calling this
     * method
     *
     * @param assetManager the asset manager which contains the images that the
     * furniture needs
     */
    public static void loadFurnitureData(AssetManager assetManager) {
        PropertiesReader pr = new PropertiesReader();
        File[] furniture = new File("assets/data/furniture").listFiles();
        for (File furnitureDataFile : furniture) {
            InfoLogger.println("Reading furniture data: " + furnitureDataFile.toString());
            pr.readProperties(furnitureDataFile);
            loadedFurniture.put(
                    (String) pr.getProperty("name"),
                    new FurnitureEntity(
                            null,
                            0,
                            0,
                            (int) pr.getProperty("width"),
                            (int) pr.getProperty("height"),
                            true,
                            IsometricDirection.NORTH,
                            assetManager.getImage((String) pr.getProperty("northImageNormal")),
                            assetManager.getImage((String) pr.getProperty("southImageNormal")),
                            assetManager.getImage((String) pr.getProperty("eastImageNormal")),
                            assetManager.getImage((String) pr.getProperty("westImageNormal")),
                            assetManager.getImage((String) pr.getProperty("northImageDefaced")),
                            assetManager.getImage((String) pr.getProperty("southImageDefaced")),
                            assetManager.getImage((String) pr.getProperty("eastImageDefaced")),
                            assetManager.getImage((String) pr.getProperty("westImageDefaced")),
                            parseSize((String) pr.getProperty("size")),
                            (boolean) pr.getProperty("defaceable"),
                            (boolean) pr.getProperty("moveunderable"))
            );
        }
    }
    
    /**
     * Creates a new instance of the specified type of furniture.
     * @param name the name of the furniture- this dictates what the furniture
     * will be. Use the name that was specified by the furniture's <code>name</code>
     * property in its data file.
     * @param x the X position of the furniture's centerpoint
     * @param y the Y position of the furniture's centerpoint
     * @param facing which direction the furniture is facing
     * @return 
     */
    public static FurnitureEntity makeFurniture(String name, float x, float y, IsometricDirection facing) {
        if (loadedFurniture.get(name)!=null) {
            FurnitureEntity fe = (FurnitureEntity)loadedFurniture.get(name).clone();
            fe.setFacing(facing);
            fe.setX(x - (fe.getWidth()/2));
            fe.setY(y - (fe.getHeight()/2));
            return fe;
        } else {
            ErrorLogger.println("Furniture \"" + name + "\" does not exist");
            return null;
        }
    }

    private static Size parseSize(String str) {
        if (str.equals("tall")) {
            return Size.TALL;
        } else if (str.equals("short")) {
            return Size.SHORT;
        } else if (str.equals("walkoverable")) {
            return Size.WALK_OVER_ABLE;
        } else {
            return null;
        }
    }

    /**
     * Makes a biscuit
     * @param assetManager the asset manager
     * @param houseState the house state
     * @param x the x position of the center of the biscuit
     * @param y the y position of the center of the biscuit
     * @return the created biscuit
     */
    public static IsometricEntity makeBiscuit(AssetManager assetManager, HouseState houseState, float x, float y) {
        return new BiscuitEntity(null, houseState, x, y);
    }
    
    /**
     * Makes a wall
     * @param assetManager the asset manager
     * @param x the x position of the center of the wall
     * @param y the y position of the center of the wall
     * @param w the wall width
     * @param h the wall height
     * @return the created wall
     */
    public static IsometricEntity makeWall(AssetManager assetManager, float x, float y, float w, float h) {
        return new IsometricEntity(null, x, y, w, h, 
                true, 
                IsometricDirection.SOUTH, null,
                assetManager.getImage("wallSprite"), null, null);
    }
    
    /**
     * Makes a wall
     * @param assetManager the asset manager
     * @param x the x position of the center of the decor
     * @param y the y position of the center of the decor
     * @param w the wall width
     * @param h the wall height
     * @param imageKey they key of the image to use for the decoration
     * @return the created decor
     */
    public static DecorationEntity makeDecoration(AssetManager assetManager, float x, float y, float w, float h, String imageKey) {
        return new DecorationEntity(null, x, y, w, h, 
                        assetManager.getImage(imageKey));
    }
    
    public static void saveBiscuitsFileWithWorld(IsometricGameWorld igw) {
        try {
            PrintStream ps = new PrintStream(new File("assets/data/biscuits.data"));
            for (IsometricEntity ie : igw.getEntities()) {
                if (ie instanceof BiscuitEntity) {
                    ps.println("Biscuit " + ie.getX() + " " + ie.getY());
                }
            }
            ps.flush();
            ps.close();
        } catch(IOException e) {
            ErrorLogger.println("Error saving biscuits: " + e);
        }
    }
}
