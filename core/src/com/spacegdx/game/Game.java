package com.spacegdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Iterator;


public class Game extends ApplicationAdapter {
	Texture ship;
	Texture background;
	Texture laserR, laserL;
	Texture enemy;
	OrthographicCamera camera;
	SpriteBatch sBatch;
	Rectangle shipRec;
	Vector3 touchPos;
	ArrayList<Rectangle> lasersR;
	ArrayList<Rectangle> lasersL;
	ArrayList<Rectangle> enemies;
	BitmapFont font;
	long lastEnemySpawnTime;
	long lastLaserFireTime;
	int speed = 300;
	long enemySpawnDelay = 1000000000;
	int score = 0;
	
	@Override
	public void create(){
		ship = new Texture("ship.png");
		background = new Texture("background.png");
		laserR = new Texture("laserR.png");
		laserL = new Texture("laserL.png");
		enemy = new Texture("enemy.png");
		camera =  new OrthographicCamera();
		camera.setToOrtho(false, 480, 800);
		sBatch =  new SpriteBatch();
		shipRec =  new Rectangle();
		shipRec.width = 64;
		shipRec.height = 128;
		shipRec.x = 480 / 2 - 64 / 2;
		shipRec.y = 100;
		lasersR = new ArrayList();
		lasersL = new ArrayList();
		enemies = new ArrayList();
		touchPos = new Vector3();
		font = new BitmapFont();
		Color c = new Color(0,1,1,1);
		font.setColor(c);
		spawnEnemy();
		//Gdx.input.setCursorCatched(true);
	}

	@Override
	public void render(){
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		sBatch.setProjectionMatrix(camera.combined);

		if(TimeUtils.timeSinceNanos(lastEnemySpawnTime) > enemySpawnDelay){
			spawnEnemy();
		}

		if(Gdx.input.isTouched()){
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			shipRec.x = touchPos.x - shipRec.width/2;
			shipRec.y = touchPos.y + 64;
			if(TimeUtils.timeSinceNanos(lastLaserFireTime) > 375000000){  //10^9 = 1 sec
				spawnLaser();
			}
		}

		iterateLaser(lasersR);
		iterateLaser(lasersL);
		iterateEnemy(enemies);

		//-----------------------------------------------------------------------------------------
		sBatch.begin();

		sBatch.draw(background, 0,0);
		sBatch.draw(ship, shipRec.x, shipRec.y, shipRec.width, shipRec.height);
		for(Rectangle laser: lasersR){
			sBatch.draw(laserR, laser.x, laser.y);
		}
		for(Rectangle laser: lasersL){
			sBatch.draw(laserL, laser.x, laser.y);
		}
		for(Rectangle enemy: enemies){
			sBatch.draw(this.enemy,enemy.x, enemy.y);

		}
		font.draw(sBatch, "Score: " + score, 0, 800);
		sBatch.end();
		//-----------------------------------------------------------------------------------------
	}

	public void spawnLaser(){
		Rectangle laserL = new Rectangle();
		Rectangle laserR = new Rectangle();

		laserR.x = shipRec.x +shipRec.width - 6 - (shipRec.width/2) / 5;
		laserR.y = shipRec.y + shipRec.height/5;
		laserR.height = 8;
		laserR.width = 6;
		lasersR.add(laserR);

		laserL.x = shipRec.x + (shipRec.width/2) / 5;
		laserL.y = shipRec.y + shipRec.height/5;
		laserL.height = 8;
		laserL.width = 6;
		lasersL.add(laserL);

		lastLaserFireTime = TimeUtils.nanoTime();
	}

	public void iterateLaser(ArrayList<Rectangle> lasers){
		Iterator<Rectangle> iter = lasers.iterator();
		while(iter.hasNext()){
			Rectangle laser = iter.next();
			Iterator<Rectangle> iterE = enemies.iterator();
			while(iterE.hasNext()) {
				if (laser.overlaps(iterE.next())) {
					iterE.remove();
					iter.remove();
					enemySpawnDelay = (enemySpawnDelay != 0) ? enemySpawnDelay -= 10000000: 0;
					score++;
				}
			}
			laser.y += speed *Gdx.graphics.getDeltaTime();
			if(laser.y > 814){
				iter.remove();
			}
		}
	}

	public void spawnEnemy(){
		Rectangle enemy = new Rectangle();
		enemy.x = MathUtils.random(32,480-64);
		enemy.y = 800;
		enemy.width = 34;
		enemy.height = 14;
		enemies.add(enemy);
		lastEnemySpawnTime = TimeUtils.nanoTime();
	}

	public void iterateEnemy(ArrayList<Rectangle> enemies){
		Iterator<Rectangle> iter = enemies.iterator();
		while(iter.hasNext()){
			Rectangle enemy = iter.next();
			enemy.y -= 100 *Gdx.graphics.getDeltaTime();
			if(enemy.y < 0){
				iter.remove();
				enemySpawnDelay = (enemySpawnDelay != 0) ? enemySpawnDelay -= 10000000: 0;
			}
		}
	}
}
