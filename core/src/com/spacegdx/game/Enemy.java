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
    public static ArrayList<Enemy> enemies;
    public static int speed = 100;


    public Enemy(Texture t, float width, float height){
        this.t = t;
        this.width = width;
        this.height = height;
    }

    public abstract void draw(SpriteBatch sb);
    public abstract void move();
    public abstract void descr

    public static void iterate(Ship ship){
        Iterator<Enemy> iter = enemies.iterator();
        while(iter.hasNext()){
            Enemy enemy = iter.next();
            if(enemy.hitbox.y < 0){
                iter.remove();
                enemy.spawnDelay = (enemySpawnDelay != 0) ? enemySpawnDelay -= 10000000: 10000000;
                speed += 10;
            }else if(enemy.overlaps(ship.getHitbox())){
                create();
            }
        }
    }
}
