package com.spacegdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.spacegdx.game.Enemies.BasicEnemy;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Jeremy on 5/19/2015.
 */
public class EnemyHandler {
    ArrayList<Enemy> enemies;
    int speed;
    long spawnDelay;
    long lastSpawnTime;
    long speedConst;
    Game game;

    public EnemyHandler(Game game){
        enemies = new ArrayList<Enemy>();
        this.game = game;
        speed = 100;
        spawnDelay = 1000000000;
        speedConst = TimeUtils.nanoTime();
    }

    public void iterate(){
        //spawn stuff
        if (TimeUtils.timeSinceNanos(lastSpawnTime) > spawnDelay){
            lastSpawnTime = TimeUtils.nanoTime();
            enemies.add(new BasicEnemy(speed));
        }

        //test for collisions and move
        Iterator<Enemy> iter = enemies.iterator();
        while(iter.hasNext()){
            Enemy enemy = iter.next();
            if(enemy.hitbox.y < 0){
                iter.remove();
                speed += 2;
                spawnDelay *= Math.pow(.95,  (TimeUtils.timeSinceNanos(speedConst) / Math.pow(10, 9)));
                speedConst = TimeUtils.nanoTime();
            }else if(enemy.hitbox.overlaps(game.ship.getHitbox())){
                game.endGame();
            }else{
                enemy.move();
            }
        }
    }

    public void draw(SpriteBatch sb){
        Iterator<Enemy> iter = enemies.iterator();
        while(iter.hasNext()){
            iter.next().draw(sb);
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
            game.spawnBoom(e.hitbox.x - e.width/2, e.hitbox.y + e.height/2);
            speed += 2;
            spawnDelay *= Math.pow(.95,  (TimeUtils.timeSinceNanos(speedConst) / Math.pow(10, 9)));
            speedConst = TimeUtils.nanoTime();
            Game.score++;
        }catch(Exception ex){
            //don't do anything
        }
    }

}
