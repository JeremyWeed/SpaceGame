package com.spacegdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Jeremy on 5/20/2015.
 * parent class that outlines the behavior of all lasers in the game
 * @author Jeremy
 */
public abstract class Laser {
    public Texture t;
    public Rectangle hitbox;
    float width, height;

    /**
     * default constructor for a laser
     * @param t texture of the laser
     * @param width width of the laser sprite
     * @param height height of the laser sprite
     * @param x x location of the original spawn of the laser
     * @param y y location of the original spawn of the laser
     */
    public Laser(Texture t, float width, float height, float x, float y){
        this.t = t;
        this.height = height;
        this.width = width;
        hitbox = new Rectangle(x - width/2, y - height, width, height);
    }

    /**
     * method called when the laser moves
     */
    public abstract void move();

    /**
     * method called to draw the laser
     * @param sb SpriteBatch on which to draw the laser
     */
    public abstract void draw(SpriteBatch sb);

    /**
     * method to remove textures from memory
     */
    public void dispose(){
        t.dispose();
    }
}
