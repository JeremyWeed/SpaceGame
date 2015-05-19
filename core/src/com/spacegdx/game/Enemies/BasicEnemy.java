package com.spacegdx.game.Enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.spacegdx.game.Enemy;
import com.spacegdx.game.Ship;

/**
 * Created by Jeremy on 5/18/2015.
 */
public class BasicEnemy extends Enemy {
    static long lastSpawenTime;
    static long spawnDelay;

    public BasicEnemy(){
        super(new Texture("enemy.png"), 34, 14);
        hitbox.x = MathUtils.random(32, 480 - 64);
        hitbox.y = 800;
        spawnDelay = 1000000000;
        lastSpawenTime = TimeUtils.nanoTime();
        enemies.add(this);
    }

    @Override
    public void draw(SpriteBatch sb) {
        sb.draw(t,hitbox.x, hitbox.y);
    }

    @Override
    public void move() {
        hitbox.y -= speed * Gdx.graphics.getDeltaTime();

    }
}
