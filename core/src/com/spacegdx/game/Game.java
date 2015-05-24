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
	FileHandle highScore, fontHomespun, fontEroded;
	int lastHighScore;
	BitmapFont font, menuFont;
	static public int score;
	float backgroundY;
	boolean menuFlag = false;
	public enum State{
		MENU,
		GAME,
		END
	}
	State state = State.MENU;
	@Override
	public void create(){
		gameSetup();
	}

	@Override
	public void render(){
		switch(state){
			case MENU:
				menuRender();
				break;
			case GAME:
				gameRender();
				break;
			case END:
				break;
		}

	}


	@Override
	public void resume(){
		endGame();
	}
	public void menuRender(){

		camera.update();
		sBatch.setProjectionMatrix(camera.combined);
		iterateBackground();
		menuFlag = (!Gdx.input.isTouched() && !menuFlag) ? true : menuFlag;
		if(Gdx.input.isTouched() && menuFlag){
			menuFlag = false;
			state = State.GAME;
		}
		//---------------------------------------------------------------------------------------
		sBatch.begin();

		sBatch.draw(background, 0, backgroundY);
		sBatch.draw(background, 0, backgroundY + 800);

		menuFont.draw(sBatch, "SPACE GAME", 100, 500);
		menuFont.draw(sBatch, "touch to start", 50, 400);
		sBatch.end();
		//---------------------------------------------------------------------------------------


	}
	public void gameRender(){
		//Gdx.gl.glClearColor(0, 0, 0, 1);
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		sBatch.setProjectionMatrix(camera.combined);

		if(Gdx.input.isTouched()){
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			ship.moveTo(touchPos.x, touchPos.y);
			ship.spawnLaser();

		}
		ship.iterateShip();
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
		font.draw(sBatch, "Score: " + score, 0, 800);
		font.draw(sBatch, "Highscore: " + ((score > lastHighScore) ? score : lastHighScore), 200, 800);
		sBatch.end();
		//-----------------------------------------------------------------------------------------
	}

	public void gameSetup(){
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
		fontHomespun = Gdx.files.internal("fonts/homespun/homespun.fnt");
		fontEroded =  Gdx.files.internal("fonts/Eroded R-2014/Eroded_R-2014.fnt");
		if(!highScore.exists()){
			highScore.writeString("0", false);
		}else{
			lastHighScore = Integer.parseInt(highScore.readString());
		}
		font = new BitmapFont(fontHomespun);
		menuFont = new BitmapFont(fontEroded);
		Color c = new Color(0,1,1,1);
		font.setColor(c);
		menuFont.setColor(c);
	}

	@Override
	public void dispose() {
		ship.dispose();
		background.dispose();
		eHand.dispose();
	}

	public void iterateBackground(){
		backgroundY -= 10 * Gdx.graphics.getDeltaTime();
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
		state = State.MENU;
		create();
	}
}
