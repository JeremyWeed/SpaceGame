package com.spacegdx.game;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.NumberUtils;
import com.spacegdx.game.Ships.BasicShip;
import com.spacegdx.game.Ships.PowerShip;
import com.spacegdx.game.Ships.ToastShip;
import com.spacegdx.game.Ships.TurretShip;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeremy on 5/30/2015.
 */
public class ShipHandler {
    Ship ship;
    Game game;
    ArrayList<String> lines;
    ArrayList<Ship> ships;
    int shipSelect;
    static int maxShips = 3; //should be one less than amount of ships
    static ShipHandler shipH;
    FileHandle shipFile;

    private class ShipSON{
        boolean uToast = false, uPower = false, uTurret = false;
        int shipSelect = 0;
    }

    private ShipHandler(Game game){
        this.game = game;
        ship = new BasicShip(game);
    }

    public static ShipHandler getHandler(Game game, FileHandle sf, boolean forceReload){
        if(shipH == null || forceReload){
            shipH = new ShipHandler(game);
            shipH.shipFile = sf;
            shipH.lines = new ArrayList<String>();
            Json json = new Json();
            if(!shipH.shipFile.exists()) {

            }
            try {
                BufferedReader reader = new BufferedReader(shipH.shipFile.reader());
                String line = reader.readLine();
                while(line != null){
                    shipH.lines.add(line);
                    line = reader.readLine();
                }
                if(shipH.lines.size() <= maxShips){
                    shipH.shipFile.writeString("0\n", false);
                    for (int i = 1; i <= maxShips; i++) {
                        shipH.shipFile.writeString("0\n", true);
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

        }
        return shipH;
    }

    public void genShips(){
        ships.set(0, new BasicShip(this.game));
        ships.set(1, new ToastShip(this.game));
        ships.set(2, new PowerShip(this.game));
        ships.set(3, new TurretShip(this.game));
    }

    public Ship getPlayerShip(){
        return ship;
    }

    public Ship getNewShip(){
        setShip(shipSelect);
        return ship;
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

        shipFile.writeString("", false);
        for(String line : lines) {
            shipFile.writeString(line + "\n", true);
        }
    }

    public void unlock(int i){
        lines.set(i, "1");
    }

    public boolean isUnlocked(int i){
        if(i == 0){
            return true;
        }
        return Integer.parseInt(lines.get(i)) == 1;
    }

    public int getPrice(int i){
        return ship.price;
    }

    Ship setShip(int i){
        //ship.dispose();

        switch(i){
            case 0:
                lines.set(0, "0");
                ship = new BasicShip(game);
                return ship;
            case 1:
                if(Integer.parseInt(lines.get(1)) == 1){
                    ship = new PowerShip(game);
                    lines.set(0, "1");
                    return ship;
                }
                return new PowerShip(game);
            case 2:
                if(Integer.parseInt(lines.get(2)) == 1){
                    ship = new TurretShip(game);
                    lines.set(0, "2");
                    return ship;
                }
                return new TurretShip(game);
            case 3:
                if(Integer.parseInt(lines.get(3)) == 1){
                    ship = new ToastShip(game);
                    lines.set(0, "3");
                    return ship;
                }
                return new ToastShip(game);
            default:
                ship = new BasicShip(game);
                lines.set(0, "0");
                return ship;
        }
    }

    public int getSS(){
        return shipSelect;
    }

    public Ship getSSShip(){
        return setShip(shipSelect);
    }

    public void setSS(int i){
        if(i > maxShips || i < 0){
            shipSelect = 0;
        }else{
            shipSelect = i;
        }
        ship = setShip(shipSelect);
    }
}
