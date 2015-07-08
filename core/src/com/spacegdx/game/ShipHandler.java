package com.spacegdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.NumberUtils;
import com.spacegdx.game.Ships.BasicShip;
import com.spacegdx.game.Ships.PowerShip;
import com.spacegdx.game.Ships.ToastShip;
import com.spacegdx.game.Ships.TurretShip;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Jeremy on 5/30/2015.
 */
public class ShipHandler {
    Ship currentShip, playerShip;
    Game game;
    ArrayList<String> lines;
    int shipSelect;
    static int maxShips = 3; //should be one less than amount of ships
    static ShipHandler shipH;
    FileHandle shipFile;
    FileHandle jsonSave = Gdx.files.local("ship_save.json");
    ShipSON shipSave = new ShipSON();

    private static class ShipSON{
        boolean uToast = false, uPower = false, uTurret = false;
        int shipSelect = 0;
    }

    private ShipHandler(Game game){
        this.game = game;
        currentShip = new BasicShip(game);
        playerShip = new BasicShip(game);
    }

    public static ShipHandler getHandler(Game game, FileHandle sf, boolean forceReload){
        if(shipH == null || forceReload){
            shipH = new ShipHandler(game);
            shipH.shipFile = sf;
            shipH.lines = new ArrayList<String>();
            Json json = new Json();
            if(shipH.jsonSave.exists()){
                shipH.shipSave = json.fromJson(ShipHandler.ShipSON.class, shipH.jsonSave);
                shipH.shipSelect = shipH.shipSave.shipSelect;
            }else if(shipH.shipFile.exists()) {
                try {
                    BufferedReader reader = new BufferedReader(shipH.shipFile.reader());
                    String line = reader.readLine();
                    while(line != null){
                        shipH.lines.add(line);
                        line = reader.readLine();
                    }
                    if(shipH.lines.size() <= maxShips){
                        for (int i = shipH.lines.size(); i <= maxShips; i++) {
                            shipH.shipFile.writeString("\n0", true);
                            shipH.lines.add("0");
                        }
                    }
                    for(int i = 0; i <= maxShips; i++){
                        if(shipH.lines.get(i).equals("")){
                            shipH.lines.set(i, "0");
                        }
                    }

                    shipH.setSS(Integer.parseInt(shipH.lines.get(0)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                shipH.shipSave.shipSelect = Integer.parseInt(shipH.lines.get(0));

                if(shipH.lines.get(1).equals("1")){
                    shipH.shipSave.uPower = true;
                }
                if(shipH.lines.get(2).equals("1")){
                    shipH.shipSave.uTurret = true;
                }
                if(shipH.lines.get(3).equals("1")){
                    shipH.shipSave.uToast = true;
                }
                shipH.shipFile.file().deleteOnExit();
                shipH.shipFile.delete();
            }
        }
        return shipH;
    }

    private boolean isUnlocked(int i){
        switch (i){
            case 0:
                return true;
            case 1:
                return shipSave.uPower;
            case 2:
                return shipSave.uTurret;
            case 3:
                return shipSave.uToast;
            default:
                return false;
        }

    }

    public Ship getPlayerShip(){
        return playerShip;
    }

    public Ship getCurrentShip(){
        return currentShip;
    }
    public void newShip(){
        setShip(shipSelect);
    }

    public void increment(){
        shipSelect = (shipSelect == maxShips) ? 0 : shipSelect + 1;
        setShip(shipSelect);
    }

    public void decrement(){
        shipSelect = (shipSelect == 0) ? maxShips : shipSelect - 1;
        setShip(shipSelect);
    }

    public void updateFile(){
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setUsePrototypes(false);
        json.setTypeName(null);
        shipSave.shipSelect = shipSelect;
        jsonSave.writeString(json.prettyPrint(shipSave), false);
    }

    public void unlock(){
        switch (shipSelect){
            case 0:
                break;
            case 1:
                shipSave.uPower = true;
                break;
            case 2:
                shipSave.uTurret = true;
                break;
            case 3:
                shipSave.uToast = true;
                break;
            default:
                break;
        }
    }

    public boolean isUnlocked(){
        switch(shipSelect){
            case 0:
                return true;
            case 1:
                return shipSave.uPower;
            case 2:
                return shipSave.uTurret;
            case 3:
                return shipSave.uToast;
            default:
                return false;
        }
    }

    public int getPrice(){
        return currentShip.price;
    }

    void setShip(int i){
        //ship.dispose();

        switch(i){
            case 0:
                playerShip = new BasicShip(game);
                currentShip = new BasicShip(game);
                break;
            case 1:
                if(shipSave.uPower){
                    playerShip = new PowerShip(game);
                }
                currentShip = new PowerShip(game);
                break;
            case 2:
                if(shipSave.uTurret){
                    playerShip = new TurretShip(game);
                }
                currentShip = new TurretShip(game);
                break;
            case 3:
                if(shipSave.uToast){
                    playerShip = new ToastShip(game);
                }
                currentShip = new ToastShip(game);
                break;
            default:
                playerShip = new BasicShip(game);
                currentShip = new BasicShip(game);
        }
    }

    public int getSS(){
        return shipSelect;
    }

    public void setSS(int i){
        if(i > maxShips || i < 0){
            shipSelect = 0;
        }else{
            shipSelect = i;
        }
        setShip(shipSelect);
    }
}
