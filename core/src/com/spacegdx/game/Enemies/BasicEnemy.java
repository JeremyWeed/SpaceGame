package com.spacegdx.game.Enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.spacegdx.game.Enemy;
import com.spacegdx.game.EnemyLaser;
import com.spacegdx.game.Ship;

/**
 * Created by Jeremy on 5/18/2015.
 */
public class BasicEnemy extends Enemy {

    public BasicEnemy(int speed){
        super(new Texture("enemy.png"), 34, 14, speed);
        hitbox = new Rectangle(MathUtils.random(0, 480-width), 800, width, height);
    }

    @Override
    public void draw(SpriteBatch sb) {
        sb.draw(t,hitbox.x, hitbox.y);
    }

    @Override
    public void move() {
        hitbox.y -= speed * Gdx.graphics.getDeltaTime();
    }

    @Override
    public boolean isOffScreen() {
        return hitbox.y < 0;
    }

    @Override
    public EnemyLaser spawnLaser() {
        return new BasicLaser(hitbox.x + width/2, hitbox.y, speed + 200);
    }

}
