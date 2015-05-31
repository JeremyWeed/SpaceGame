package com.spacegdx.game.Ships;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.spacegdx.game.Enemy;
import com.spacegdx.game.Game;
import com.spacegdx.game.Laser;
import com.spacegdx.game.Ship;

import java.util.ArrayList;

/**
 * Created by Jeremy on 5/30/2015.
 */
public class TurretShip extends Ship {
    ArrayList<Laser> lasers;
    Texture turret, laserT;
    public TurretShip(Game game) {
        super(new Texture("turret_ship/turret_ship.png"), 26, 34, 4, 30, 1, game);
        lasers = new ArrayList<Laser>();
        turret = new Texture("turret_ship/turret.png");
        laserT = new Texture("turret_ship/turret_laser.png");

    }

    @Override
    public void moveTo(float x, float y) {

    }

    @Override
    public void draw(SpriteBatch sb) {

    }

    @Override
    public void draw(SpriteBatch sb, float x, float y) {

    }

    @Override
    public void drawLasers(SpriteBatch sb) {

    }

    @Override
    public void spawnLaser() {

    }

    @Override
    public void iterateLaser(ArrayList<Enemy> enemies) {

    }

    @Override
    public void iterateShip() {

    }

    @Override
    public boolean overlaps(Rectangle r) {
        return false;
    }
}
