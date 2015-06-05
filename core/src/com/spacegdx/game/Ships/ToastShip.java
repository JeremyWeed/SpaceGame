package com.spacegdx.game.Ships;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
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
 * Created by Jeremy on 6/4/2015.
 */
public class ToastShip extends Ship {
    ArrayList<Rectangle> lasers;
    Texture laser;
    Rectangle hitbox2;
    Sound laserFire;
    int laserSpeed;
    boolean spawnSide = false;
    public long lastLaserFireTime, laserFireDelay;
    double p = 8;
    double i = .01;
    double d = .5;
    double desiredX, desiredY;
    double xSpeed, ySpeed;
    double xI, yI;

    public ToastShip(Game game){
        super(new Texture("PolishToast.png"), 46, 48, 8, 42, 1.5f, game);
        lasers = new ArrayList();
        laser = new Texture("blueLaser.png");
        laserFire = Gdx.audio.newSound(Gdx.files.internal("sound/128229__kafokafo__laser_short.wav"));
        laserSpeed = 700;
        laserFireDelay = 175000000;
        hitbox2 = new Rectangle(0,0,width, 5);
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
        sb.draw(super.t, super.hitbox.x + hitbox.width/2 - width/2, super.hitbox.y + hitbox.height/2 - height/2 + 4 * scale, width, height);

    }

    @Override
    public void draw(SpriteBatch sb, float x, float y) {
        sb.draw(super.t, x - width/2, y - height/2 + 4 * scale, width, height);

    }

    @Override
    public void drawLasers(SpriteBatch sb) {
        for(Rectangle laser: lasers){
            sb.draw(this.laser, laser.x, laser.y);
        }
    }

    @Override
    public void spawnLaser() {
        if(TimeUtils.timeSinceNanos(lastLaserFireTime) > laserFireDelay){
            laserFire.play(.5f);
            Rectangle laser = new Rectangle();
            if(spawnSide){
                laser.x = hitbox2.x;
            }else{
                laser.x = hitbox2.x + width;

            }
            spawnSide = !spawnSide;
            laser.y = hitbox2.y;
            laser.height = 4 * scale;
            laser.width = 1 * scale;
            lasers.add(laser);

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
                    lostLasers.add(laser);
                }
            }
            laser.y += laserSpeed * Gdx.graphics.getDeltaTime();
            if(laser.y > 821){
                lostLasers.add(laser);
            }
        }

        for(Rectangle laser : lostLasers){
            lasers.remove(laser);
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

        xSpeed = p * deltaX + i * xI + d * xD;
        ySpeed = p * deltaY + i * yI + d * yD;

        hitbox.x -= xSpeed * Gdx.graphics.getDeltaTime();
        hitbox.y -= ySpeed * Gdx.graphics.getDeltaTime();

        hitbox2.x = hitbox.x + hitbox.width/2 - hitbox2.width/2;
        hitbox2.y = hitbox.y + hitbox.height/2;
    }

    @Override
    public boolean overlaps(Rectangle r) {
        return hitbox.overlaps(r) || hitbox2.overlaps(r);
    }
}
