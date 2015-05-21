package com.spacegdx.game.Enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.spacegdx.game.EnemyLaser;

/**
 * Created by Jeremy on 5/20/2015.
 */
public class BasicLaser extends EnemyLaser{
    int speed = 300;

    public BasicLaser(float x, float y) {
        super(new Texture("eLaser.png"), 2, 10, x, y);
    }

    @Override
    public void move() {
        hitbox.y -= speed * Gdx.graphics.getDeltaTime();
    }

    @Override
    public void draw(SpriteBatch sb) {
        sb.draw(t,hitbox.x, hitbox.y);
    }
}