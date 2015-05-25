package com.spacegdx.game.Enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.spacegdx.game.Enemy;
import com.spacegdx.game.EnemyLaser;

/**
 * Created by jeremy on 5/23/15.
 */
public class Asteroid extends Enemy{
    double xSpeed, ySpeed;
    enum Side{
        left,
        right
    }
    Side side;

    public Asteroid(Texture t, float width, float height, int speed) {
        super(t, width, height, speed);

        side = (MathUtils.randomBoolean()) ? Side.right : Side.left;

        float angle = MathUtils.random(MathUtils.PI / 12, MathUtils.PI / 2  - MathUtils.PI / 12);

        xSpeed = speed * Math.cos(angle);
        ySpeed = speed * Math.sin(angle);

        if(side == Side.right){
            hitbox = new Rectangle(480,MathUtils.random(200, 800), width, height);
            hitbox.x = 480;
        }else{
            xSpeed = -xSpeed;
            hitbox = new Rectangle(-width,MathUtils.random(200, 800), width, height);
        }
    }

    public static Asteroid createAsteroid(int speed){
        if(MathUtils.randomBoolean()){
            return new Asteroid(new Texture("asteroids/asteroid2.png"), 64, 64, speed);
        }else{
            return new Asteroid(new Texture("asteroids/asteroid1.png"), 42, 43, speed);
        }
    }

    @Override
    public void draw(SpriteBatch sb) {
        sb.draw(t,hitbox.x, hitbox.y);
    }

    @Override
    public void move() {
        hitbox.x -= xSpeed * Gdx.graphics.getDeltaTime();
        hitbox.y -= ySpeed * Gdx.graphics.getDeltaTime();
    }

    @Override
    public boolean isOffScreen() {
        return (hitbox.x < - width || hitbox. y < 0 || hitbox.x > 480);
    }

    @Override
    public EnemyLaser spawnLaser() {
        return null;
    }
}
