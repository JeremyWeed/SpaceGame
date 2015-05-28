package com.spacegdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.spacegdx.game.Ships.BasicShip;
import com.spacegdx.game.Ships.PowerShip;

import java.util.ArrayList;
import java.util.Iterator;


public class Game extends ApplicationAdapter {
	Texture background;
	Texture boom;
	Texture shipMenu;
	Texture arrow;
	OrthographicCamera camera;
	SpriteBatch sBatch;
	Vector3 touchPos;
	public Ship ship;
	public EnemyHandler eHand;
	static ArrayList<Flash> booms;
	FileHandle highScore, fontHomespun, fontEroded, fontHomeLarge, shipFile;
	int lastHighScore;
	BitmapFont font, menuFont, titleFont;
	static public int score;
	float backgroundY;
	boolean menuFlag = false;
	boolean endFlag = false;
	boolean dbFlag = false;

	// fun with enums!

	public enum State{
		MENU,
		SHIP_MENU,
		GAME,
		END
	}
	public enum ShipSelect{
		BASIC,
		POWER
	}

	void decrementSS(){
		switch(shipSelect){
			case BASIC:
				shipSelect = ShipSelect.POWER;
				break;
			case POWER:
				shipSelect = ShipSelect.BASIC;
				break;
			default:
				shipSelect = ShipSelect.BASIC;
				break;
		}
	}

	void incrementSS(){
		switch(shipSelect){
			case BASIC:
				shipSelect = ShipSelect.POWER;
				break;
			case POWER:
				shipSelect = ShipSelect.BASIC;
				break;
			default:
				shipSelect = ShipSelect.BASIC;
				break;
		}
	}

	void setSS(int i){
		switch (i){
			case 1:
				shipSelect = ShipSelect.POWER;
				break;
			case 2:
				shipSelect = ShipSelect.BASIC;
				break;
			default:
				shipSelect = ShipSelect.BASIC;
				break;

		}
	}

	int getSS(){
		switch (shipSelect){
			case BASIC:
				return 0;
			case POWER:
				return 1;
			default:
				return 0;
		}
	}
	ShipSelect shipSelect = ShipSelect.BASIC;
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
			case SHIP_MENU:
				shipMenuRender();
				break;
			case GAME:
				gameRender();
				break;
			case END:
				endRender();
				break;
		}

	}


	@Override
	public void resume(){
		state = State.MENU;
	}

	public void endRender(){
		if(score > lastHighScore) {
			highScore.writeString(Integer.toString(score), false);
			lastHighScore = score;
		}
		camera.update();
		sBatch.setProjectionMatrix(camera.combined);

		spawnBoom(ship.hitbox.x + ship.hitbox.width/2, ship.hitbox.y + ship.hitbox.height / 2, 128, 128);

		endFlag = (!Gdx.input.isTouched() && !endFlag) ? true : endFlag;
		if(Gdx.input.isTouched() && endFlag){
			endFlag = false;
			state = State.MENU;
		}

		//---------------------------------------------------------------------------------------
		sBatch.begin();
		sBatch.draw(background, 0, backgroundY);
		sBatch.draw(background, 0, backgroundY + 800);
		ship.draw(sBatch);
		ship.drawLasers(sBatch);
		eHand.draw(sBatch);
		for(Flash f: booms){
			sBatch.draw(boom, f.r.x, f.r.y, f.r.width, f.r.height);
		}
		titleFont.draw(sBatch, "GAME OVER", 75, 500);
		menuFont.draw(sBatch, "score: " + score, 185, 400);
		menuFont.draw(sBatch, "highscore: " + ((score > lastHighScore) ? score : lastHighScore), 155, 350);
		sBatch.end();
		//---------------------------------------------------------------------------------------
	}
	public void menuRender(){

		camera.update();
		sBatch.setProjectionMatrix(camera.combined);
		iterateBackground();
		menuFlag = (!Gdx.input.isTouched() && !menuFlag) ? true : menuFlag;
		if(Gdx.input.isTouched() && menuFlag){
			menuFlag = false;
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			if(touchPos.x > 480 - 28 * 2 - 24 && touchPos.y > 800 - 31 * 2 - 24){
				state = State.SHIP_MENU;
			}else {
				resetGame();
				state = State.GAME;
			}
		}
		//---------------------------------------------------------------------------------------
		sBatch.begin();

		sBatch.draw(background, 0, backgroundY);
		sBatch.draw(background, 0, backgroundY + 800);

		sBatch.draw(shipMenu, 480 - 28 * 2 - 24, 800 - 31 * 2 - 24, 28 * 2, 31 * 2);

		titleFont.draw(sBatch, "black_space", 60, 500);
		menuFont.draw(sBatch, "TOUCH TO START", 130, 400);
		menuFont.draw(sBatch, "highscore: " + lastHighScore, 155, 200);
		menuFont.draw(sBatch, "ver 0.5", 3, 30);
		sBatch.end();
		//---------------------------------------------------------------------------------------


	}

	public void shipMenuRender(){
		Ship[] ships = new Ship[2];
		ships[0] = new BasicShip(this);
		ships[1] = new PowerShip(this);
		Ship sd =  ships[0];
		camera.update();
		sBatch.setProjectionMatrix(camera.combined);

		if(Gdx.input.isTouched() && !dbFlag) {
			dbFlag = true;
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			if(touchPos.y > 700 && touchPos.x < 128){
				state = State.MENU;
				shipFile.writeString(Integer.toString(getSS()), false);
				for(Ship ship : ships){
					ship.dispose();
				}
			}else if((touchPos.y > 400 - 64 && touchPos.y < 400 + 64)){
				if(touchPos.x < 24 + 64){
					decrementSS();
				}else if(touchPos.x > 400 - 24 - 64){
					incrementSS();
				}
			}
		}
		dbFlag = Gdx.input.isTouched();

		switch(shipSelect){
			case BASIC:
				sd = ships[0];
				break;
			case POWER:
				sd = ships[1];
				break;
		}

		iterateBackground();

		//---------------------------------------------------------------------------------------
		sBatch.begin();

		sBatch.draw(background, 0, backgroundY);
		sBatch.draw(background, 0, backgroundY + 800);

		sd.draw(sBatch, 480/2, 800/2);

		sBatch.draw(arrow, 24, 400 - 64, 64, 128, 0, 0, 64, 128, false, false);
		sBatch.draw(arrow, 480 - 24 - 64, 400 - 64, 64, 128, 0, 0, 64, 128, true, false);
		menuFont.draw(sBatch, "BACK", 24, 800 - 24);

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
		ship.drawLasers(sBatch);
		ship.draw(sBatch);
		eHand.draw(sBatch);
		for(Flash f: booms){
			sBatch.draw(boom, f.r.x, f.r.y, f.r.width, f.r.height);
		}
		font.draw(sBatch, "Score: " + score, 0, 800);
		font.draw(sBatch, "Highscore: " + ((score > lastHighScore) ? score : lastHighScore), 280, 800);
		sBatch.end();
		//-----------------------------------------------------------------------------------------
	}

	public void gameSetup(){
		background = new Texture("background.png");
		boom = new Texture("boom.png");
		shipMenu = new Texture("shipMenu.png");
		arrow = new Texture("arrow.png");
		camera =  new OrthographicCamera();
		camera.setToOrtho(false, 480, 800);
		sBatch =  new SpriteBatch();
		resetGame();
		touchPos = new Vector3();
		highScore = Gdx.files.local("highScore.txt");
		shipFile = Gdx.files.local("ships.txt");
		fontHomespun = Gdx.files.internal("fonts/homespun/homespun.fnt");
		fontEroded =  Gdx.files.internal("fonts/spaceship/spaceship.fnt");
		fontHomeLarge = Gdx.files.internal("fonts/homeLarge/homeLarge.fnt");
		if(!highScore.exists()){
			highScore.writeString("0", false);
		}else{
			lastHighScore = Integer.parseInt(highScore.readString());
		}
		if(!shipFile.exists()){
			shipFile.writeString(Integer.toString(getSS()), false);
		}else{
			setSS(Integer.parseInt(shipFile.readString()));
		}
		font = new BitmapFont(fontHomespun);
		menuFont = new BitmapFont(fontHomespun);
		titleFont = new BitmapFont(fontHomeLarge);
		Color c = new Color(0,1,1,1);
		//font.setColor(c);
		//menuFont.setColor(c);
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
		spawnBoom(x, y, 48, 48);
	}

	public static void spawnBoom(float x, float y, float width, float height){
		Rectangle boom = new Rectangle();
		boom.x = x - width/2;
		boom.y = y - height/2;
		boom.width = width;
		boom.height = height;
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

	public void resetGame(){
		score = 0;
		booms = new ArrayList();
		ship =  getShip();
		eHand = new EnemyHandler(this);
	}

	public Ship getShip(){
		switch (shipSelect){
			case BASIC:
				return new BasicShip(this);
			case POWER:
				return new PowerShip(this);
			default:
				return new BasicShip(this);
		}
	}
	public void endGame(){

		state = State.END;
	}
}
