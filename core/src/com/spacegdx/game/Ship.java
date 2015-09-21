package com.spacegdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

/**
 * Created by Jeremy on 5/18/2015.
 * parent class that outlines the basic ship player
 * @author Jeremy
 */
public abstract class Ship {

    public Texture t;
    public float width, height, scale;
    public Rectangle hitbox;
    public Game game;
    public int price;

    /**
     * default constructor for the ship class
     * @param t ship texture
     * @param width the width of the ship texture
     * @param height the height of the ship texture
     * @param recX the x length of the ship hitbox rectangle
     * @param recY the y length of the ship hitbox rectangle
     * @param scale amount to scale the ship and hitbox in size
     * @param game reference to the currently executing game
     */
    public Ship(Texture t, float width, float height, float recX, float recY, float scale, Game game){
        this.t = t;
        this.width = width * scale;
        this.height = height * scale;
        this.scale = scale;
        hitbox = new Rectangle(480/2 - recX * scale/2, 100 - recY * scale/2, recX * scale, recY * scale);
        this.game = game;
    }

    /**
     * sets the desired ship position
     * @param x the x position of the desired location
     * @param y the y position of the desired location
     */
    public abstract void moveTo(float x, float y);

    /**
     * default draw method for the <code>ship</code> class
     * @param sb <code>SpriteBatch</code>  with which to add the draw call
     */
    public abstract void draw(SpriteBatch sb);

    /**
     * method to draw the ship at a specified location
     * @param sb <code>SpriteBatch</code>  with which to add the draw call
     * @param x the x location of the draw
     * @param y the y location of the draw
     */
    public abstract void draw(SpriteBatch sb, float x, float y);

    /**
     * Method called to draw the laser created by the ship
     * @param sb <code>SpriteBatch</code>  with which to add the draw call
     */
    public abstract void drawLasers(SpriteBatch sb);

    /**
     * creates a laser originating from the ship
     */
    public abstract void spawnLaser();

    /**
     * method called to update the ship's lasers
     * @param enemies list containing all of the current enemies to check collisions against
     */
    public abstract void iterateLaser(ArrayList<Enemy> enemies);

    /**
     * method to update the ship in respect to time
     */
    public abstract void iterateShip();

    /**
     * basic collision detection for ship
     * @param r the rectangle to check for collisions with
     * @return true if there is a collision, false if not
     */
    public abstract boolean overlaps(Rectangle r);

    /**
     * removes resources from memory
     */
    public void dispose(){
        t.dispose();
    }

    /**
     * gets the hitbox of the ship for collision testing
     * @return the hitbox of the ship
     */
    public Rectangle getHitbox(){
        return hitbox;
    }

}
