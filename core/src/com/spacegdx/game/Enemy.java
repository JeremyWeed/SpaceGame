package com.spacegdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Jeremy on 5/18/2015.
 */
public abstract class Enemy {
    public Texture t;
    public float width,height;
    public Rectangle hitbox;
    public int speed;


    public Enemy(Texture t, float width, float height, int speed){
        this.t = t;
        this.width = width;
        this.height = height;
        this.speed = speed;
    }

    public abstract void draw(SpriteBatch sb);
    public void dispose(){
        t.dispose();
    }
    public abstract void move();

}
