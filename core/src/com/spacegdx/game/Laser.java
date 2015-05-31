package com.spacegdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Jeremy on 5/20/2015.
 */
public abstract class Laser {
    public Texture t;
    public Rectangle hitbox;
    float width, height;

    public Laser(Texture t, float width, float height, float x, float y){
        this.t = t;
        this.height = height;
        this.width = width;
        hitbox = new Rectangle(x - width/2, y - height, width, height);
    }
    public abstract void move();
    public abstract void draw(SpriteBatch sb);
    public void dispose(){
        t.dispose();
    }
}
