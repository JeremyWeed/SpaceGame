package com.spacegdx.game;

import com.spacegdx.game.Ships.BasicShip;
import com.spacegdx.game.Ships.PowerShip;

/**
 * Created by Jeremy on 5/30/2015.
 */
public class ShipHandler {
    static Ship ship;
    Game game;
    int shipSelect;
    static int maxShips = 1; //should be one less than amount of ships
    static ShipHandler shipH;

    private ShipHandler(Game game){
        this.game = game;
    }

    public static ShipHandler getNewHandler(Game game){
        if(shipH == null){
            return new ShipHandler(game);
        }else{
            return shipH;
        }
    }

    public Ship getCurrentShip(){
        return ship;
    }

    public void increment(){
        maxShips = (shipSelect == maxShips) ? 0 : shipSelect + 1;
        ship =  setShip(shipSelect);

    }

    public void decrement(){
        maxShips = (shipSelect == 0) ? shipSelect - 1 : 0;
        ship = setShip(shipSelect);

    }

    Ship setShip(int i){
        switch(i){
            case 0:
                return new BasicShip(game);
            case 1:
                return new PowerShip(game);
            default:
                return new BasicShip(game);
        }
    }
}
