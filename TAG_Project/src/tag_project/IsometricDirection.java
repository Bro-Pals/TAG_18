/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tag_project;

/**
 * The direction for isometric land.
 * @author Jonathon
 */
public enum IsometricDirection {
    NORTH, SOUTH, EAST, WEST;
    
    public static IsometricDirection parseDirection(String str) {
        if (str.equals("north")) {
            return NORTH;
        } else if (str.equals("south")) {
            return SOUTH;
        } else if (str.equals("east")) {
            return EAST;
        } else if (str.equals("west")) {
            return WEST;
        }
        return null;
    }
}
