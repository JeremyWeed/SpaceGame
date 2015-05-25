package com.spacegdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.spacegdx.game.Enemies.Asteroid;
import com.spacegdx.game.Enemies.BasicEnemy;
import com.spacegdx.game.Enemies.BasicLaser;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Jeremy on 5/19/2015.
 */
public class EnemyHandler {
    ArrayList<Enemy> enemies;
    ArrayList<EnemyLaser> lasers;
    int speed;
    long spawnDelayBasic;
    long lastSpawnTimeBasic;
    long lastSpawnTimeAster;
    long speedConst;
    Game game;

    public EnemyHandler(Game game){
        enemies = new ArrayList<Enemy>();
        lasers= new ArrayList<EnemyLaser>();
        this.game = game;
        speed = 100;
        spawnDelayBasic = 1000000000;
        speedConst = TimeUtils.nanoTime();
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
                EnemyLaser e = enemy.spawnLaser();
                if(e != null){
                    lasers.add(e);
                }
            }
            if(enemy.isOffScreen()){
                iter.remove();
                speed += 2;
                spawnDelayBasic *= Math.pow(.95,  (TimeUtils.timeSinceNanos(speedConst) / Math.pow(10, 9)));
                speedConst = TimeUtils.nanoTime();
            }else if(enemy.hitbox.overlaps(game.ship.getHitbox())){
                game.endGame();
            }else{
                enemy.move();
            }
        }
        Iterator<EnemyLaser> iterL = lasers.iterator();
        while(iterL.hasNext()){
            EnemyLaser laser = iterL.next();
            if(laser.hitbox.y < 0){
                iterL.remove();
            }else if(laser.hitbox.overlaps(game.ship.getHitbox())){
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
        Iterator<EnemyLaser> iterL = lasers.iterator();
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
