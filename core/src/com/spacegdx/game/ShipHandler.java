package com.spacegdx.game;

import com.spacegdx.game.Ships.BasicShip;
import com.spacegdx.game.Ships.PowerShip;
import com.spacegdx.game.Ships.TurretShip;

/**
 * Created by Jeremy on 5/30/2015.
 */
public class ShipHandler {
    Ship ship;
    Game game;
    int shipSelect;
    static int maxShips = 2; //should be one less than amount of ships
    static ShipHandler shipH;

    private ShipHandler(Game game){
        this.game = game;
        ship = new BasicShip(game);
    }

    public static ShipHandler getHandler(Game game){
        if(shipH == null){
            shipH = new ShipHandler(game);
        }
        return shipH;
    }

    public Ship getCurrentShip(){
        return ship;
    }

    public void increment(){
        shipSelect = (shipSelect == maxShips) ? 0 : shipSelect + 1;
        ship =  setShip(shipSelect);

    }

    public void decrement(){
        shipSelect = (shipSelect == 0) ? maxShips : shipSelect - 1;
        ship = setShip(shipSelect);

    }

    Ship setShip(int i){
        ship.dispose();
        switch(i){
            case 0:
                return new BasicShip(game);
            case 1:
                return new PowerShip(game);
            case 2:
                return new TurretShip(game);
            default:
                return new BasicShip(game);
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
        ship = setShip(shipSelect);
    }
}
