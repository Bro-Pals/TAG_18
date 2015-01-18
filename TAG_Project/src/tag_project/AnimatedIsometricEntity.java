/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tag_project;

import bropals.lib.simplegame.animation.Animation;
import bropals.lib.simplegame.entity.GameWorld;
import bropals.lib.simplegame.logger.ErrorLogger;
import bropals.lib.simplegame.logger.InfoLogger;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author Owner
 */
public class AnimatedIsometricEntity extends IsometricEntity {

    private Animation animation;
    private NavigationNode goal;
    private ArrayList<NavigationNode> pathToGetThere;
    private int nodeOnPathOn, pathFindTimerOn;
    private final int PATHFIND_TIME_LIMIT = 30; // once every 30 frames
            
    public AnimatedIsometricEntity(GameWorld parent, float x, float y, float width, 
            float height, boolean anchored, IsometricDirection facing, 
            BufferedImage north, BufferedImage south, BufferedImage east, 
            BufferedImage west, Animation anim) {
        super(parent, x, y, width, height, anchored, facing, north, south, east, west);
        this.animation = anim; // the animation
        animation.setTrack(0);
        setFacing(IsometricDirection.NORTH);
        nodeOnPathOn = 0;
        pathFindTimerOn = 0;
    }
    
    @Override
    public void update() {
        animation.update();
        pathFindTimerOn--; // update the timer for path finding
        if (getVelocity().getY() > 0 && getVelocity().getY() > getVelocity().getX()) {
            setFacing(IsometricDirection.SOUTH);   
        } else if (getVelocity().getY() < 0 && getVelocity().getY() < getVelocity().getX()) {
            setFacing(IsometricDirection.NORTH);
        } else if (getVelocity().getX() > 0 && getVelocity().getY() < getVelocity().getX()) {
            setFacing(IsometricDirection.WEST);
        } else if (getVelocity().getX() < 0 && getVelocity().getY() > getVelocity().getX()) {
            setFacing(IsometricDirection.EAST);
        }
        boolean notMoving = getVelocity().getX() == 0 && 
                getVelocity().getY() == 0;
        switch(getFacing()) {
            case NORTH: // animation track 0
                if (notMoving) {
                    if (animation.getCurrentTrackIndex() != 4)
                        animation.setTrack(4); // change to the north animaton
                } else {
                    if (animation.getCurrentTrackIndex() != 0)
                        animation.setTrack(0); // change to the north animaton
                }
                if (getNorth() != animation.getCurrentImage())
                    setNorth(animation.getCurrentImage());
                break;
            case SOUTH: // animation track 1 (0 for now)
                if (notMoving) {
                    if (animation.getCurrentTrackIndex() != 5)
                        animation.setTrack(5);
                } else {
                    if (animation.getCurrentTrackIndex() != 1)
                        animation.setTrack(1);
                }
                if (getSouth() != animation.getCurrentImage())
                    setSouth(animation.getCurrentImage());
                break;
            case EAST: // animation track 2
                if (notMoving) {
                    if (animation.getCurrentTrackIndex() != 6)
                        animation.setTrack(6);
                } else {
                    if (animation.getCurrentTrackIndex() != 2)
                        animation.setTrack(2);
                }
                if (getEast() != animation.getCurrentImage())
                    setEast(animation.getCurrentImage());
                break;
            case WEST: // animation track 3
                if (notMoving) {
                    if (animation.getCurrentTrackIndex() != 7)
                        animation.setTrack(7);
                } else {
                    if (animation.getCurrentTrackIndex() != 3)
                        animation.setTrack(3);
                }
                if (getWest() != animation.getCurrentImage())
                    setWest(animation.getCurrentImage());
                break;
        }
        super.update();
    }
    
    private void followPathToGetThere(float speed) {
        if (pathToGetThere == null || nodeOnPathOn >= pathToGetThere.size())
            return;
       // System.out.println("Going to follow our path now");
        NavigationNode nodeOn = pathToGetThere.get(nodeOnPathOn);
        if (distanceBetween(nodeOn.getCenterX(), nodeOn.getCenterY()) < 15) {
            nodeOnPathOn++;
        } else {
            setVelocityTowards(nodeOn.getCenterX(), nodeOn.getCenterY(), speed);
            //System.out.println("Heading to the node on path numbered: " +nodeOnPathOn);
        }
    }
    
    /**
     * The A* algorithm. I used the wikipedia page for the algorithm to help me.
     * @param end The goal node
     * @param start The starting node
     * @param nodes The array of all the nodes
     */
    private void calculatePath(NavigationNode end,
            NavigationNode start,
            NavigationNode[][] nodes) {
        if (start == null || end == null) {
            return;
        }
        if (pathToGetThere != null && pathFindTimerOn > 0) {
            return; // need to wait for the timer to let you do it again
        }
        pathFindTimerOn = PATHFIND_TIME_LIMIT; // reset timer
        ArrayList<NavigationNode> openSet = new ArrayList<>();
        openSet.add(start);
        start.setNavParent(null);
        start.setgVal(0); // 0 movement to reach the starting node
        ArrayList<NavigationNode> closedSet = new ArrayList<>();
        for (int i=0; i<nodes.length; i++) {
            for (int j=0; j<nodes[i].length; j++) {
                if (nodes[i][j] != start) {
                    if (nodes[i][j] == null) {
                        ErrorLogger.println("The node at (" + i + ", " + j + ") was null");
                        continue;
                    }
                     // set estimated distance to reach the goal from this node
                    nodes[i][j].sethVal(Math.abs(end.getXGrid() - i) 
                            + Math.abs(end.getYGrid() - j));
                }
            }
        }
        //System.out.println("I actually made it this far");
        while (!openSet.isEmpty()) {
            //System.out.println("Running with an open set of size " + openSet.size());
            // find the node in the open set with the lowest f value
            NavigationNode lowestF = openSet.get(0);
            for (int i=1; i<openSet.size(); i++) {
                if (openSet.get(i).getfVal() < lowestF.getfVal()) {
                    lowestF = openSet.get(i);
                }
            }
            if (lowestF == null) {
                return;
            }
            if (lowestF == end) {
                //System.out.println("Found a path!");
                // build the path
                pathToGetThere = new ArrayList<>();
                nodeOnPathOn = 0; // restart this
                NavigationNode nodeOn = lowestF.getNavParent();
                if (nodeOn == null) {
                    return;
                }
                while (nodeOn.getNavParent() != null) { // start node has no parent
                    pathToGetThere.add(0, nodeOn);
                    nodeOn = nodeOn.getNavParent();
                }
                //System.out.println("*******");
               // System.out.println("Got a path of " + pathToGetThere.size() + " nodes");
               // System.out.println("*******");
                return;
            }
                
            openSet.remove(lowestF);
            closedSet.add(lowestF); // so we don't check this guy again
            int[][] neighborOffsets = new int[][]{{1, 0}, {-1, 0}, {0, 1},
                 {0, -1}}; //{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
            for (int i=0; i<4; i++) {
                int xCoord = lowestF.getXGrid() + neighborOffsets[i][0];
                int yCoord = lowestF.getYGrid() + neighborOffsets[i][1];
                if (xCoord >= 0 && xCoord < nodes.length &&
                        yCoord >= 0 && yCoord < nodes[xCoord].length) {
                    NavigationNode neighbor = nodes[xCoord][yCoord];
                    if (closedSet.contains(neighbor)) // no need to check again
                        continue;
                    // set how many moves it takes to get to this node
                    //float increaseMoveBy = //i > 3 ? neighbor.getMoveCost() * 
                            //(float)Math.sqrt(2) : neighbor.getMoveCost(); // more distance for diagnols
                    float tenativeGVal = lowestF.getgVal() + neighbor.getMoveCost();
                    
                    if (!openSet.contains(neighbor) || tenativeGVal < neighbor.getgVal()) {
                        neighbor.setgVal(tenativeGVal);
                        neighbor.setNavParent(lowestF);
                        if (!openSet.contains(neighbor))
                            openSet.add(neighbor);
                    }
                }
            }
            
        }
        
    }
    
    private void pathfindTo(IsometricEntity other, NavigationNode[][] nodes, float speed) {
        NavigationNode findGoal = null;
        NavigationNode yourNode = null;
        // find the nodes
        for (int x=0; x<nodes.length; x++) {
            for (int y=0; y<nodes[x].length; y++) {
                NavigationNode thisNode = nodes[x][y];
                
                if (thisNode.handleCollide(other)) {
                    findGoal = thisNode;
                }
                if (thisNode.handleCollide(this)) {
                    yourNode = thisNode;
                }
                // all done searching now
                if (findGoal != null && yourNode != null)
                    break;
            }
        }
        if (findGoal == null || yourNode == null) {
            ErrorLogger.println("Did not have all the boy or the dog contained"
                    + " in the navigation nodes array");
            return;
        }
        
        // don't need to recalculate if we already have that as the goal
        if (goal == findGoal) {
            followPathToGetThere(speed);
            return;
        }
        goal = findGoal; // new goal
        
        // time for some A* pathfinding
        calculatePath(goal, yourNode, nodes);
        followPathToGetThere(speed);
    }
    
    /**
     * Chase another entity (the dog). If it can see the other entity, it will 
     * head straight towards them. Otherwise, it will pathfind and follow 
     * the path to where the other entity is. If the other entity is
     * in sight and close enough, this entity will attack the other entity.
     * 
     * The boy should have this called on him in the update loop
     * @param other The other entity (should be the dog)
     * @param nodes The navigation nodes to use to pathfind if it
     * @param speed The speed to chase the dog at
     * doesn't have line of sight of the other entity.
     */
    public void chase(IsometricEntity other, NavigationNode[][] nodes, float speed) {
        if (canSee(other)) {
            setVelocityTowards(other.getCenterX(), other.getCenterY(), speed);
            if (distanceBetween(other) < 150) {
            }
        } else {
            pathfindTo(other, nodes, speed);
        }
    }
    
    /**
     * Gets the distance between this and a given entity. Measured from their centers
     * @param other The given entity
     * @return The distance between this and a given entity from their centers
     */
    public float distanceBetween(IsometricEntity other) {
        return distanceBetween(other.getCenterX(), other.getCenterY());
    }
    
    /**
     * Gets the distance between this and a given point. Measured from the center
     * @param x The x position
     * @param y The y position
     * @return The distance between this entity's center and the given point
     */
    public float distanceBetween(float x, float y) {
        float diffX = x - getCenterX();
        float diffY = y - getCenterY();
        return (float)Math.sqrt((diffX*diffX)+(diffY*diffY));
    }
    
    public boolean canSee(IsometricEntity other) {
        if (getParent()==null) {
            return false;
        }
        for (int i=0; i<getParent().getEntities().size(); i++) {
            if (getParent().getEntities().get(i) instanceof IsometricEntity) {
                IsometricEntity isoent = (IsometricEntity)getParent().getEntities().get(i);
                if (isoent == this || isoent == other) // don't check yourself
                    continue;
                
                if (isoent instanceof DecorationEntity || isoent instanceof BiscuitEntity)
                    continue;
                
                if (isoent.toRect().intersectsLine((int)getCenterX(), (int)getCenterY(), 
                        (int)other.getCenterX(), (int)other.getCenterY())) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public ArrayList<NavigationNode> getPath() {
        return pathToGetThere;
    }
}
