package com.spacegdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

/**
 * Created by Jeremy on 5/18/2015.
 */
public abstract class Ship {

    public Texture t;
    public float width, height;
    public Rectangle hitbox;
    public Game game;

    public Ship(Texture t, float width, float height, float recX, float recY, Game game){
        this.t = t;
        this.width = width;
        this.height = height;
        hitbox = new Rectangle( width/2 + 480/2 - recX/2, height/2 + 100 - recY/2, recX, recY);
        this.game = game;
    }
    public abstract void moveTo(float x, float y);
    public abstract void draw(SpriteBatch sb);
    public abstract void drawLasers(SpriteBatch sb);
    public abstract void spawnLaser();
    public abstract void iterateLaser(ArrayList<Enemy> enemies);
    public abstract void iterateShip();
    public void dispose(){
        t.dispose();
    }
    public Rectangle getHitbox(){
        return hitbox;
    }

}
