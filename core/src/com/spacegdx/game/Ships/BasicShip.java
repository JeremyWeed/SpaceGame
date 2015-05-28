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
 * Created by Jeremy on 5/18/2015.
 */
public class BasicShip extends Ship {
    ArrayList<Rectangle> lasersR;
    ArrayList<Rectangle> lasersL;
    Texture laserR, laserL;
    int laserSpeed;
    public long lastLaserFireTime, laserFireDelay;
    double p = 8;
    double i = .01;
    double d = .5;
    double desiredX, desiredY;
    double xSpeed, ySpeed;
    double xI, yI;

    public BasicShip(Game game){
        super(new Texture("ship.png"), 28 * 2, 31 * 2, 12, 52, 1, game);
        lasersR = new ArrayList();
        lasersL = new ArrayList();
        laserR = new Texture("laserR.png");
        laserL = new Texture("laserL.png");
        laserSpeed = 500;
        laserFireDelay = 375000000;
    }

    public void moveTo(float x, float y){
        x -= hitbox.width / 2;
        y -= hitbox.height / 2 - 64;
        desiredX = x;
        desiredY = y;
    }
    public void draw(SpriteBatch sb){
        sb.draw(super.t, super.hitbox.x + hitbox.width/2 - width/2, super.hitbox.y + hitbox.height/2 - height/2 + 4, width, height);
    }
    public void draw(SpriteBatch sb, float x, float y){
        sb.draw(super.t, x - width/2, y - height/2 + 4, width, height);
    }
    public void drawLasers(SpriteBatch sb){
        for(Rectangle laser: lasersR){
            sb.draw(laserR, laser.x, laser.y);
        }
        for(Rectangle laser: lasersL){
            sb.draw(laserL, laser.x, laser.y);
        }
    }
    public void iterateShip(){
        double deltaX = (hitbox.x - desiredX);
        xI += deltaX * Gdx.graphics.getDeltaTime();
        double xD = xSpeed;

        double deltaY = (hitbox.y - desiredY);
        yI += deltaY * Gdx.graphics.getDeltaTime();
        double yD = ySpeed;

        xSpeed = p * deltaX + i * xI + d * xD;
        ySpeed = p * deltaY + i * yI + d * yD;

        hitbox.x -= xSpeed * Gdx.graphics.getDeltaTime();
        hitbox.y -= ySpeed * Gdx.graphics.getDeltaTime();
    }

    public void spawnLaser(){
        if(TimeUtils.timeSinceNanos(lastLaserFireTime) > laserFireDelay){
            Rectangle laserL = new Rectangle();
            Rectangle laserR = new Rectangle();

            laserR.x = hitbox.x + (width * 2) / 5;
            laserR.y = hitbox.y + 4;
            laserR.height = 8;
            laserR.width = 6;
            lasersR.add(laserR);

            laserL.x = hitbox.x - (width * 2) / 5;
            laserL.y = hitbox.y + 4;
            laserL.height = 8;
            laserL.width = 6;
            lasersL.add(laserL);

            lastLaserFireTime = TimeUtils.nanoTime();
        }

    }
    public void iterateLaser(ArrayList<Enemy> enemies){
        ArrayList<Enemy> livingDead = new ArrayList<Enemy>();
        Iterator<Rectangle> iter = lasersL.iterator();
        ArrayList<Rectangle> lostLasersL = new ArrayList<Rectangle>();
        while(iter.hasNext()){
            Rectangle laser = iter.next();
            Iterator<Enemy> iterE = enemies.iterator();
            while(iterE.hasNext()) {
                Enemy enemy = iterE.next();
                if (laser.overlaps(enemy.hitbox)) {
                    livingDead.add(enemy);
                    lostLasersL.add(laser);
                }
            }
            laser.y += laserSpeed * Gdx.graphics.getDeltaTime();
            if(laser.y > 821){
                lostLasersL.add(laser);
            }
        }

        iter = lasersR.iterator();
        ArrayList<Rectangle> lostLasersR = new ArrayList<Rectangle>();
        while(iter.hasNext()){
            Rectangle laser = iter.next();
            Iterator<Enemy> iterE = enemies.iterator();
            while(iterE.hasNext()) {
                Enemy enemy = iterE.next();
                if (laser.overlaps(enemy.hitbox)) {
                    livingDead.add(enemy);
                    lostLasersR.add(laser);
                }
            }
            laser.y += laserSpeed * Gdx.graphics.getDeltaTime();
            if(laser.y > 821){
                lostLasersR.add(laser);
            }
        }
        for(Rectangle laser : lostLasersL){
            lasersL.remove(laser);
        }
        for(Rectangle laser : lostLasersR){
            lasersR.remove(laser);
        }
        for(Enemy enemy : livingDead){
            game.eHand.kill(enemy);
        }
    }
    public void dispose(){
        super.dispose();
        laserR.dispose();
        laserL.dispose();
    }
}
