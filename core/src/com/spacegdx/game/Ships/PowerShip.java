package com.spacegdx.game.Ships;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.spacegdx.game.Enemy;
import com.spacegdx.game.Game;
import com.spacegdx.game.Ship;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Jeremy on 5/27/2015.
 */
public class PowerShip extends Ship {
    ArrayList<Rectangle> lasers;
    Texture laser;
    public long lastLaserFireTime, laserFireDelay;
    int laserSpeed;
    double p = 4;
    double i = .01;
    double d = .3;
    double desiredX, desiredY;
    double xSpeed, ySpeed;
    double xI, yI;
    float maxSpeed = 300;

    public PowerShip(Game game) {
        super(new Texture("ship2.png"), 40, 65, 40, 55, 1.5f, game);
        lasers = new ArrayList();
        laser = new Texture("long_laser.png");
        laserSpeed = 400;
        laserFireDelay = 500000000;
    }

    @Override
    public void moveTo(float x, float y) {
        x -= hitbox.width / 2;
        y -= hitbox.height / 2 - 64;
        y = Math.min(y, 800 - height);
        desiredX = x;
        desiredY = y;
    }

    @Override
    public void draw(SpriteBatch sb) {
        sb.draw(super.t, super.hitbox.x, super.hitbox.y - 6 * scale, width, height);
    }

    @Override
    public void draw(SpriteBatch sb, float x, float y){
        sb.draw(super.t, x - width/2, y - height/2 + 4, width, height);
    }

    @Override
    public void drawLasers(SpriteBatch sb) {
        for(Rectangle laserD : lasers){
            sb.draw(laser, laserD.x, laserD.y, laserD.width, laserD.height);
        }
    }

    @Override
    public void spawnLaser() {
        if(TimeUtils.timeSinceNanos(lastLaserFireTime) > laserFireDelay){
            Rectangle laserD = new Rectangle();

            laserD.x = hitbox.x + 4 * scale;
            laserD.y = hitbox.y + - 5 * scale + height - 13 * scale;
            laserD.height = 14 * scale;
            laserD.width = 34 * scale;
            lasers.add(laserD);

            lastLaserFireTime = TimeUtils.nanoTime();
        }
    }

    @Override
    public void iterateLaser(ArrayList<Enemy> enemies) {
        ArrayList<Enemy> livingDead = new ArrayList<Enemy>();
        Iterator<Rectangle> iter = lasers.iterator();
        ArrayList<Rectangle> lostLasers = new ArrayList<Rectangle>();
        while(iter.hasNext()){
            Rectangle laser = iter.next();
            Iterator<Enemy> iterE = enemies.iterator();
            while(iterE.hasNext()) {
                Enemy enemy = iterE.next();
                if (laser.overlaps(enemy.hitbox)) {
                    livingDead.add(enemy);
                }
            }
            laser.y += laserSpeed * Gdx.graphics.getDeltaTime();
            if(laser.y > 821){
                lostLasers.add(laser);
            }
        }
        for(Rectangle laserD : lostLasers){
            lasers.remove(laserD);
        }
        for(Enemy enemy : livingDead){
            game.eHand.kill(enemy);
        }
    }

    @Override
    public void iterateShip() {
        double deltaX = (hitbox.x - desiredX);
        xI += deltaX * Gdx.graphics.getDeltaTime();
        double xD = xSpeed;

        double deltaY = (hitbox.y - desiredY);
        yI += deltaY * Gdx.graphics.getDeltaTime();
        double yD = ySpeed;
        /*
        xSpeed = Math.min(Math.max(p * deltaX + i * xI + d * xD, -maxSpeed), maxSpeed);
        ySpeed = Math.min(Math.max(p * deltaY + i * yI + d * yD, -maxSpeed), maxSpeed);
        */
        xSpeed = p * deltaX + i * xI + d * xD;
        ySpeed = p * deltaY + i * yI + d * yD;

        hitbox.x -= xSpeed * Gdx.graphics.getDeltaTime();
        hitbox.y -= ySpeed * Gdx.graphics.getDeltaTime();
    }

    @Override
    public boolean overlaps(Rectangle r) {
        return hitbox.overlaps(r);
    }

    @Override
    public void dispose(){
        super.dispose();
        laser.dispose();
    }
}
