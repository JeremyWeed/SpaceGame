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
    public float width, height, scale;
    public Rectangle hitbox;
    public Game game;

    public Ship(Texture t, float width, float height, float recX, float recY, float scale, Game game){
        this.t = t;
        this.width = width * scale;
        this.height = height * scale;
        this.scale = scale;
        hitbox = new Rectangle(480/2 - recX * scale/2, 100 - recY * scale/2, recX * scale, recY * scale);
        this.game = game;
    }
    public abstract void moveTo(float x, float y);
    public abstract void draw(SpriteBatch sb);
    public abstract void draw(SpriteBatch sb, float x, float y);
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
