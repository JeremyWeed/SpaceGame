package com.spacegdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
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
	Texture boom;
	OrthographicCamera camera;
	SpriteBatch sBatch;
	Vector3 touchPos;
	public Ship ship;
	public EnemyHandler eHand;
	static ArrayList<Flash> booms;
	FileHandle highScore;
	int lastHighScore;
	BitmapFont font;
	static public int score;
	float backgroundY;
	
	@Override
	public void create(){
		background = new Texture("background.png");
		boom = new Texture("boom.png");
		score = 0;
		camera =  new OrthographicCamera();
		camera.setToOrtho(false, 480, 800);
		sBatch =  new SpriteBatch();
		booms = new ArrayList();
		ship =  new BasicShip(this);
		eHand = new EnemyHandler(this);
		touchPos = new Vector3();
		highScore = Gdx.files.local("highScore.txt");
		if(!highScore.exists()){
			highScore.writeString("0", false);
		}else{
			lastHighScore = Integer.parseInt(highScore.readString());
		}
		font = new BitmapFont();
		Color c = new Color(0,1,1,1);
		font.setColor(c);
		//Gdx.input.setCursorCatched(true);
	}

	@Override
	public void render(){
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		sBatch.setProjectionMatrix(camera.combined);

		if(Gdx.input.isTouched()){
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			ship.moveTo(touchPos.x, touchPos.y);
			ship.spawnLaser();

		}

		ship.iterateLaser(eHand.enemies);
		eHand.iterate();
		iterateBoom(booms);
		iterateBackground();

		//-----------------------------------------------------------------------------------------
		sBatch.begin();

		sBatch.draw(background, 0, backgroundY);
		sBatch.draw(background, 0, backgroundY + 800);
		ship.draw(sBatch);
		ship.drawLasers(sBatch);
		eHand.draw(sBatch);
		for(Flash f: booms){
			sBatch.draw(boom, f.r.x, f.r.y);
		}
		font.draw(sBatch, "Score: " + score + "    Highscore: " + ((score > lastHighScore) ? score : lastHighScore), 0, 800);
		sBatch.end();
		//-----------------------------------------------------------------------------------------
	}

	@Override
	public void dispose() {
		ship.dispose();
		background.dispose();
		eHand.dispose();
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

	public void endGame(){
		if(score > lastHighScore) {
			highScore.writeString(Integer.toString(score), false);
		}
		create();
	}
}
