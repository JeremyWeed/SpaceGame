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
import com.spacegdx.game.Ships.BasicShip;

import java.util.ArrayList;
import java.util.Iterator;


public class Game extends ApplicationAdapter {
	Texture background;
	Texture enemy;
	Texture boom;
	OrthographicCamera camera;
	SpriteBatch sBatch;
	Vector3 touchPos;
	Ship ship;
	ArrayList<Rectangle> enemies;
	static ArrayList<Flash> booms;
	BitmapFont font;
	long lastEnemySpawnTime;
	static public int eSpeed;
	static public long enemySpawnDelay;
	static public int score;
	float backgroundY;
	
	@Override
	public void create(){
		background = new Texture("background.png");
		enemy = new Texture("enemy.png");
		boom = new Texture("boom.png");
		lastEnemySpawnTime = 0;
		eSpeed = 100;
		enemySpawnDelay = 1000000000;
		score = 0;
		camera =  new OrthographicCamera();
		camera.setToOrtho(false, 480, 800);
		sBatch =  new SpriteBatch();
		enemies = new ArrayList();
		booms = new ArrayList();
		ship =  new BasicShip();
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
			ship.moveTo(touchPos.x, touchPos.y);
			ship.spawnLaser();

		}

		ship.iterateLaser(enemies);
		iterateEnemy(enemies);
		iterateBoom(booms);
		iterateBackground();

		//-----------------------------------------------------------------------------------------
		sBatch.begin();

		sBatch.draw(background, 0, backgroundY);
		sBatch.draw(background, 0, backgroundY + 800);
		ship.draw(sBatch);
		ship.drawLasers(sBatch);
		for(Rectangle enemy: enemies){
			sBatch.draw(this.enemy,enemy.x, enemy.y);
		}
		for(Flash f: booms){
			sBatch.draw(boom, f.r.x, f.r.y);
		}
		font.draw(sBatch, "Score: " + score, 0, 800);
		sBatch.end();
		//-----------------------------------------------------------------------------------------
	}

	@Override
	public void dispose() {
		ship.dispose();
		background.dispose();
		enemy.dispose();
	}

	public void spawnEnemy(){
		Rectangle enemy = new Rectangle();

		enemy.width = 34;
		enemy.height = 14;

		lastEnemySpawnTime = TimeUtils.nanoTime();
	}

	public void iterateEnemy(ArrayList<Rectangle> enemies){
		Iterator<Rectangle> iter = enemies.iterator();
		while(iter.hasNext()){
			Rectangle enemy = iter.next();
			enemy.y -= eSpeed * Gdx.graphics.getDeltaTime();
			if(enemy.y < 0){
				iter.remove();
				enemySpawnDelay = (enemySpawnDelay != 0) ? enemySpawnDelay -= 10000000: 10000000;
				eSpeed += 10;
			}else if(enemy.overlaps(ship.getHitbox())){
				create();
			}
		}
	}
	public void iterateBackground(){
		backgroundY -= 1;
		if(backgroundY < -800){
			backgroundY = 0;
		}
	}

	public static void spawnBoom(float x, float y){
		Rectangle boom = new Rectangle();
		boom.x = x + 24;
		boom.y = y - 24;
		boom.width = 48;
		boom.height = 48;
		Flash f = new Flash(boom);
		booms.add(f);
	}

	public void iterateBoom(ArrayList<Flash> booms){
		Iterator<Flash> iter = booms.iterator();
		while(iter.hasNext()){
			Flash f = iter.next();
			if(f.time < TimeUtils.nanoTime()){
				iter.remove();
			}
		}
	}
}
