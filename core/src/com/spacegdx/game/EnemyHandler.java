package com.spacegdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.spacegdx.game.Enemies.Asteroid;
import com.spacegdx.game.Enemies.BasicEnemy;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Jeremy on 5/19/2015.
 */
public class EnemyHandler {
    ArrayList<Enemy> enemies;
    ArrayList<Laser> lasers;
    Sound explosion, laserFire;
    int speed;
    long spawnDelayBasic;
    long lastSpawnTimeBasic;
    long lastSpawnTimeAster;
    long speedConst;
    Game game;

    public EnemyHandler(Game game){
        enemies = new ArrayList<Enemy>();
        lasers= new ArrayList<Laser>();
        this.game = game;
        speed = 100;
        spawnDelayBasic = 1000000000;
        speedConst = TimeUtils.nanoTime();
        explosion = Gdx.audio.newSound(Gdx.files.internal("sound/170144__timgormly__8-bit-explosion2.wav"));
        laserFire = Gdx.audio.newSound(Gdx.files.internal("sound/39459__the-bizniss__laser.wav"));

    }

    public void iterate(){
        //spawn stuff
        if (TimeUtils.timeSinceNanos(lastSpawnTimeBasic) > spawnDelayBasic){
            lastSpawnTimeBasic = TimeUtils.nanoTime();
            enemies.add(new BasicEnemy(speed));
        }
        if (TimeUtils.timeSinceNanos(lastSpawnTimeAster) > 3 * spawnDelayBasic){
            lastSpawnTimeAster = TimeUtils.nanoTime();
            enemies.add(Asteroid.createAsteroid(speed / 2));
        }

        //test for collisions and move
        Iterator<Enemy> iter = enemies.iterator();
        while(iter.hasNext()){
            Enemy enemy = iter.next();
            if(MathUtils.random(0,100) > 99){
                Laser e = enemy.spawnLaser();
                if(e != null){
                    laserFire.play();
                    lasers.add(e);
                }
            }
            if(enemy.isOffScreen()){
                iter.remove();
                speed += 2;
                spawnDelayBasic *= Math.pow(.95,  (TimeUtils.timeSinceNanos(speedConst) / Math.pow(10, 9)));
                speedConst = TimeUtils.nanoTime();
            }else if(game.ship.overlaps(enemy.hitbox)){
                game.endGame();
            }else{
                enemy.move();
            }
        }
        Iterator<Laser> iterL = lasers.iterator();
        while(iterL.hasNext()){
            Laser laser = iterL.next();
            if(laser.hitbox.y < 0){
                iterL.remove();
            }else if(game.ship.overlaps(laser.hitbox)){
                game.endGame();
            }else{
                laser.move();
            }
        }
    }

    public void draw(SpriteBatch sb){
        Iterator<Enemy> iter = enemies.iterator();
        while(iter.hasNext()){
            iter.next().draw(sb);
        }
        Iterator<Laser> iterL = lasers.iterator();
        while(iterL.hasNext()){
            iterL.next().draw(sb);
        }
    }

    public void dispose(){
        for(Enemy enemy:enemies){
            enemy.dispose();
        }
    }

    public void kill(Enemy e){
        try{
            explosion.play();
            enemies.remove(e);
            Game.spawnBoom(e.hitbox.x + e.width/2, e.hitbox.y + e.height/2);
            speed += 2;
            spawnDelayBasic *= Math.pow(.95,  (TimeUtils.timeSinceNanos(speedConst) / Math.pow(10, 9)));
            speedConst = TimeUtils.nanoTime();
            Game.score++;
        }catch(Exception ex){
            //don't do anything
        }
    }

}
