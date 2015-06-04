package com.spacegdx.game.Ships;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.spacegdx.game.Enemy;
import com.spacegdx.game.Game;
import com.spacegdx.game.Laser;
import com.spacegdx.game.Ship;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Jeremy on 5/30/2015.
 */
public class TurretShip extends Ship {
    ArrayList<TurretLaser> lasers;
    Texture turret, laserT;
    Rectangle hitbox2;
    Sound laserFire;
    public long lastLaserFireTime, laserFireDelay;
    int laserSpeed;
    double p = 8;
    double i = .01;
    double d = .6;
    double desiredX, desiredY;
    double xSpeed, ySpeed;
    double xI, yI;
    float turretAngle;
    long startTime;

    class TurretLaser extends Laser{
        float speed = laserSpeed;
        float angle;
        Polygon hitgon;
        float xSpd, ySpd;
        public TurretLaser(float x, float y, float angle) {
            super(new Texture("turret_ship/turret_laser.png"), 3 * scale, 5 * scale, x, y);
            hitgon = new Polygon(new float[]{0,0,hitbox.width, 0, hitbox.width, hitbox.height, 0, hitbox.height});
            hitgon.setOrigin(hitbox.width / 2, hitbox.height / 2);
            hitgon.setPosition(x, y);
            this.angle = angle;
            hitgon.rotate(MathUtils.degreesToRadians * angle);
            ySpd = speed * MathUtils.cosDeg(angle);
            xSpd = speed * -MathUtils.sinDeg(angle);
        }

        @Override
        public void move() {
            float deltaX = xSpd * Gdx.graphics.getDeltaTime();
            float deltaY = ySpd * Gdx.graphics.getDeltaTime();
            hitgon.translate(deltaX, deltaY);
        }

        @Override
        public void draw(SpriteBatch sb) {
            sb.draw(t, hitgon.getX(), hitgon.getY(), hitgon.getOriginX(), hitgon.getOriginY(), 3 * scale, 5 * scale, 1, 1, angle,
                    0, 0, 3, 5, false, false);
        }
    }

    public TurretShip(Game game) {
        super(new Texture("turret_ship/turret_ship.png"), 26, 34, 4, 30, 2f, game);
        lasers = new ArrayList<TurretLaser>();
        turret = new Texture("turret_ship/turret.png");
        laserSpeed = 600;
        laserFireDelay = 200000000;
        startTime = TimeUtils.millis();
        hitbox2 = new Rectangle(hitbox.x, 15 * scale, width - 2 * scale, 5 * scale);
        laserFire = Gdx.audio.newSound(Gdx.files.internal("sound/39459__the-bizniss__laser.wav"));
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
        draw(sb, super.hitbox.x - 4 * scale + width / 2, super.hitbox.y - 4 * scale + height / 2);

    }

    @Override
    public void draw(SpriteBatch sb, float x, float y) {
        sb.draw(super.t, x - width/2, y - height/2, width, height);
        sb.draw(turret, x + width/2 - 2 * scale, y - 4 * scale, 5 * scale, 5 * scale, 9 * scale,
                24 * scale, 1, 1, turretAngle, 0, 0, 9, 24, false, false);
    }

    @Override
    public void drawLasers(SpriteBatch sb) {
        for(Laser laser : lasers){
            laser.draw(sb);
        }
    }

    @Override
    public void spawnLaser() {
        if(TimeUtils.timeSinceNanos(lastLaserFireTime) > laserFireDelay) {
            laserFire.play();
            lasers.add(new TurretLaser(hitbox.x + width - 3 * scale + 17 * scale * -MathUtils.sinDeg(turretAngle),
                    hitbox.y + height/2 - 8 * scale - 20 * scale * -MathUtils.cosDeg(turretAngle), turretAngle));
            lastLaserFireTime = TimeUtils.nanoTime();
        }
    }

    @Override
    public void iterateLaser(ArrayList<Enemy> enemies) {
        ArrayList<Enemy> livingDead = new ArrayList<Enemy>();
        Iterator<TurretLaser> iter = lasers.iterator();
        ArrayList<TurretLaser> lostLasers = new ArrayList<TurretLaser>();
        while(iter.hasNext()){
            TurretLaser laser = iter.next();
            Iterator<Enemy> iterE = enemies.iterator();
            while(iterE.hasNext()) {
                Enemy enemy = iterE.next();
                if (isCollision(laser.hitgon, enemy.hitbox)) {
                    livingDead.add(enemy);
                    lostLasers.add(laser);
                }
            }
            laser.move();
            if(laser.hitgon.getY() > 821 || laser.hitgon.getX() > 480 || laser.hitgon.getX() - 3 * scale < 0){
                lostLasers.add(laser);
            }
        }
        for(Laser laserD : lostLasers){
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

        xSpeed = p * deltaX + i * xI + d * xD;
        ySpeed = p * deltaY + i * yI + d * yD;

        hitbox.x -= xSpeed * Gdx.graphics.getDeltaTime();
        hitbox.y -= ySpeed * Gdx.graphics.getDeltaTime();

        hitbox2.x = hitbox.x;
        hitbox2.y = hitbox.y + 11 * scale;


        turretAngle = (float)(30 * Math.sin((TimeUtils.nanoTime()) / 250000000.0));
    }

    @Override
    public boolean overlaps(Rectangle r) {
        return hitbox.overlaps(r) || hitbox2.overlaps(r);
    }
        //copied from stack overflow
    private boolean isCollision(Polygon p, Rectangle r) {
        Polygon rPoly = new Polygon(new float[] { 0, 0, r.width, 0, r.width,
                r.height, 0, r.height });
        rPoly.setPosition(r.x, r.y);
        return (Intersector.overlapConvexPolygons(rPoly, p));

    }
}
