/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tag_project.factory;

import bropals.lib.simplegame.entity.BaseEntity;
import bropals.lib.simplegame.io.AssetLoader;
import bropals.lib.simplegame.io.AssetManager;
import bropals.lib.simplegame.logger.ErrorLogger;
import bropals.lib.simplegame.logger.InfoLogger;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Pattern;
import tag_project.HouseState;
import tag_project.IsometricDirection;
import tag_project.IsometricEntity;
import tag_project.IsometricGameWorld;

/**
 *
 * @author Jonathon
 */
public class HouseLoader extends AssetLoader<IsometricGameWorld> {

    private AssetManager assetManager;
    private HouseState houseState = null;

    public HouseLoader(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public HouseState getHouseState() {
        return houseState;
    }

    public void setHouseState(HouseState houseState) {
        this.houseState = houseState;
    }

    @Override
    public void loadAsset(String key, InputStream inputStream) {
        IsometricGameWorld igw = new IsometricGameWorld(houseState);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                IsometricEntity ie = parseLine(line);
                if (ie != null) {
                    igw.addEntity(ie);
                }
            }
            reader.close();
            reader = new BufferedReader(new FileReader(new File("assets/data/biscuits.data")));
            while ((line = reader.readLine()) != null) {
                igw.addEntity(parseBiscuit(line));
            }
            reader.close();
            InfoLogger.println("Loaded house plan: " + key + " " + System.currentTimeMillis());
        } catch (IOException e) {
            ErrorLogger.println("Unable to load house plan " + key);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException adasdaas) {
            ErrorLogger.println("That line is not formatted correctly! (" + adasdaas + ")");
        }
        add(key, igw);
    }

    private IsometricEntity parseBiscuit(String line) {
        String[] split = line.split(Pattern.quote(" "));
        return EntityFactory.makeBiscuit(
                assetManager,
                Float.parseFloat(split[1]),
                Float.parseFloat(split[2])
        );
    }

    private IsometricEntity parseLine(String line) {
        String[] split = line.split(Pattern.quote(" "));
        IsometricEntity ie = null;
        if (split[0].equals("Comment")) {
            return ie;
        }
        if (split[0].equals("Wall")) {
            ie = EntityFactory.makeWall(assetManager,
                    Float.parseFloat(split[1]),
                    Float.parseFloat(split[2]),
                    Float.parseFloat(split[3]),
                    Float.parseFloat(split[4])
            );
            if (ie.getWidth() % 80 != 0 || ie.getHeight() % 80 != 0) {
                ErrorLogger.println("Size not divisible by 80:");
                ErrorLogger.println(line);
            }
            if (ie.getX() % 80 != 0 || ie.getY() % 80 != 0) {
                ErrorLogger.println("Position not divisible by 80:");
                ErrorLogger.println(line);
            }
        } else if (split[0].equals("Decoration")) {
            ie = EntityFactory.makeDecoration(assetManager,
                    Float.parseFloat(split[1]),
                    Float.parseFloat(split[2]),
                    Float.parseFloat(split[3]),
                    Float.parseFloat(split[4]),
                    split[5]
            );
            if (ie.getWidth() % 80 != 0 || ie.getHeight() % 80 != 0) {
                ErrorLogger.println("Size not divisible by 80:");
                ErrorLogger.println(line);
            }
            if (ie.getX() % 80 != 0 || ie.getY() % 80 != 0) {
                ErrorLogger.println("Position not divisible by 80:");
                ErrorLogger.println(line);
            }
        } else {
            ie = EntityFactory.makeFurniture(
                    split[0],
                    Float.parseFloat(split[1]),
                    Float.parseFloat(split[2]),
                    IsometricDirection.parseDirection(split[3]));
        }
        return ie;
    }

    @Override
    public void unload(String key) {
        for (IsometricEntity be : (ArrayList<IsometricEntity>) getAsset(key).getEntities()) {
            be.removeParent();
        }
        getAsset(key).getEntities().clear();
        super.unload(key);
    }

}
