package com.spacegdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Main Class of the game
 * @author Jeremy Weed
 */
public class Game extends ApplicationAdapter {
	Texture background;
	Texture boom;
	Texture shipMenu;
	Texture arrow;
	Texture lock;
	Texture speakerLoud, speakerMute;
	OrthographicCamera camera;
	SpriteBatch sBatch;
	Vector3 touchPos;
	Music gameTheme;
	Music menuTheme;
	Sound endExplosion;
	Sound button;
	public Ship ship;
	public EnemyHandler eHand;
	public ShipHandler sHand;
	static ArrayList<Flash> booms;
	FileHandle highScore, fontHomespun, fontEroded, fontHomeLarge, shipFile, creditFile;
	int lastHighScore;
	int credits;
	BitmapFont font, menuFont, titleFont;
	static public int score;
	float backgroundY;
	boolean menuFlag = true;
	boolean endFlag = true;
	boolean dbFlag = true;
	boolean touchdb = false;
	boolean music = true;
	String versionString = "1.11";

	// fun with enums!

	public enum State{
		MENU,
		SHIP_MENU,
		GAME,
		END
	}

	State state = State.MENU;

	/**
	 * game creation, class <code>gameSetup()</code>
	 */
	@Override
	public void create(){
		gameSetup();

	}

	/**
	 * basic switch based render method
	 */
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

	/**
	 * Rendering method to handle the end game screen
	 */
	public void endRender(){
		if(score > lastHighScore) {
			highScore.writeString(Integer.toString(score), false);
			lastHighScore = score;
		}
		camera.update();
		sBatch.setProjectionMatrix(camera.combined);

		spawnBoom(ship.hitbox.x + ship.hitbox.width / 2, ship.hitbox.y + ship.hitbox.height / 2, 128, 128);

		endFlag = (!Gdx.input.isTouched() && endFlag) ? false : endFlag;
		if(Gdx.input.isTouched() && !endFlag){
			button.play();
			endFlag = true;
			touchdb = true;

		}else if(!Gdx.input.isTouched() && touchdb){
			state = State.MENU;
			touchdb = false;
			endFlag = true;
		}

		//endFlag = Gdx.input.isTouched();

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
		menuFont.draw(sBatch, "credits: " + credits + " (+" + score / 10 + ")",150, 200 );
		sBatch.end();
		//---------------------------------------------------------------------------------------
	}

	public void menuRender(){

		camera.update();
		sBatch.setProjectionMatrix(camera.combined);
		iterateBackground();

		if(!menuTheme.isPlaying() && music){
			menuTheme.play();
			gameTheme.stop();
			menuTheme.setVolume(.5f);
			menuTheme.setLooping(true);
		}
		if(!music && (gameTheme.isPlaying() || menuTheme.isPlaying())){
			gameTheme.stop();
			menuTheme.stop();
		}
		if(Gdx.input.isTouched() && !menuFlag){
			button.play();
			touchdb = true;
			menuFlag = false;
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);

		}else if(!Gdx.input.isTouched() && touchdb){
			touchdb = false;
			if(touchPos.x > 360 && touchPos.y > 720){
				state = State.SHIP_MENU;
			}else if(touchPos.x > (480 - 64 - 30) && touchPos.y < (30 + 64)){
				music = !music;
			}else{
				resetGame();
				state = State.GAME;
			}
		}

		menuFlag = Gdx.input.isTouched();
		//---------------------------------------------------------------------------------------
		sBatch.begin();

		sBatch.draw(background, 0, backgroundY);
		sBatch.draw(background, 0, backgroundY + 800);
		if(music){
			sBatch.draw(speakerLoud, 401, 15);
		}else{
			sBatch.draw(speakerMute, 401, 15);
		}

		//sBatch.draw(shipMenu, 480 - 28 * 2 - 24, 800 - 31 * 2 - 24, 28 * 2, 31 * 2);

		menuFont.draw(sBatch, "CHANGE", 370, 780);
		menuFont.draw(sBatch, "SHIP", 390, 750);
		titleFont.draw(sBatch, "black_space", 60, 500);
		menuFont.draw(sBatch, "TOUCH TO START", 130, 400);
		menuFont.draw(sBatch, "highscore: " + lastHighScore, 155, 250);
		menuFont.draw(sBatch, "credits: " + credits, 190, 200);
		menuFont.draw(sBatch, "ver " + versionString, 3, 30);

		sBatch.end();
		//---------------------------------------------------------------------------------------


	}

	public void shipMenuRender(){
		camera.update();
		sBatch.setProjectionMatrix(camera.combined);

		if(!menuTheme.isPlaying() && music){
			menuTheme.play();
			gameTheme.stop();
			menuTheme.setVolume(.5f);
			menuTheme.setLooping(true);
		}
		if(!music && (gameTheme.isPlaying() || menuTheme.isPlaying())){
			gameTheme.stop();
			menuTheme.stop();
		}

		if(Gdx.input.isTouched() && !dbFlag) {
			touchdb = true;
			dbFlag = true;
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);

		}else if(!Gdx.input.isTouched() && touchdb){
			touchdb = false;
			if(touchPos.y > 700 && touchPos.x < 128){
				button.play();
				state = State.MENU;
				sHand.updateFile();

			}else if((touchPos.y > 400 - 64 && touchPos.y < 400 + 64)){
				if(touchPos.x < 24 + 64){
					button.play();
					sHand.decrement();
				}else if(touchPos.x > 400 - 24 - 64){
					button.play();
					sHand.increment();
				}else{
					button.play();
					state = State.MENU;
					sHand.updateFile();
				}
			}else if((touchPos. y < 300 && touchPos.y > 200) && touchPos.x > 170 && touchPos.x < 310){
				if(credits >= sHand.getPrice() && !sHand.isUnlocked()){
					button.play();
					credits -= sHand.getPrice();
					sHand.unlock();
				}
			}

		}

		dbFlag = Gdx.input.isTouched();

		iterateBackground();

		//---------------------------------------------------------------------------------------
		sBatch.begin();

		sBatch.draw(background, 0, backgroundY);
		sBatch.draw(background, 0, backgroundY + 800);

		sHand.getCurrentShip().draw(sBatch, 480 / 2, 800 / 2);

		if(!sHand.isUnlocked()){
			sBatch.draw(lock, 200 + 20 , 400 - 20);
			menuFont.draw(sBatch, "Unlock", 200, 300);
			menuFont.draw(sBatch, "for", 220, 262);
			menuFont.draw(sBatch, sHand.getPrice() + " credits", 170, 225);
		}

		sBatch.draw(arrow, 24, 400 - 64, 64, 128, 0, 0, 64, 128, false, false);
		sBatch.draw(arrow, 480 - 24 - 64, 400 - 64, 64, 128, 0, 0, 64, 128, true, false);
		menuFont.draw(sBatch, "BACK", 24, 800 - 24);
		menuFont.draw(sBatch, "credits: " + credits, 160, 650);

		sBatch.end();
		//---------------------------------------------------------------------------------------
	}

	public void gameRender(){
		camera.update();
		sBatch.setProjectionMatrix(camera.combined);
		if(!gameTheme.isPlaying() && music){
			gameTheme.play();
			menuTheme.stop();
			gameTheme.setLooping(true);
		}
		if(!music && (gameTheme.isPlaying() || menuTheme.isPlaying())){
			gameTheme.stop();
			menuTheme.stop();
		}

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
		lock = new Texture("lock.png");
		speakerLoud = new Texture("speaker_loud.png");
		speakerMute = new Texture("speaker_mute.png");
		camera =  new OrthographicCamera();
		camera.setToOrtho(false, 480, 800);
		sBatch =  new SpriteBatch();
		menuTheme = Gdx.audio.newMusic(Gdx.files.internal("sound/22453__nathanshadow__space-ambient.wav"));
		gameTheme = Gdx.audio.newMusic(Gdx.files.internal("sound/251461__joshuaempyre__arcade-music-loop.wav"));
		endExplosion = Gdx.audio.newSound(Gdx.files.internal("sound/104397__kantouth__dark-energy-exp.wav"));
		button = Gdx.audio.newSound(Gdx.files.internal("sound/146717__fins__button.wav"));
		touchPos = new Vector3();
		highScore = Gdx.files.local("highScore.txt");
		shipFile = Gdx.files.local("ships.txt");
		creditFile = Gdx.files.local("credits");
		fontHomespun = Gdx.files.internal("fonts/homespun/homespun.fnt");
		fontEroded =  Gdx.files.internal("fonts/spaceship/spaceship.fnt");
		fontHomeLarge = Gdx.files.internal("fonts/homeLarge/homeLarge.fnt");
		sHand = ShipHandler.getHandler(this, shipFile, true);
		if(!highScore.exists()){
			highScore.writeString("0", false);
		}else{
			lastHighScore = Integer.parseInt(highScore.readString());
		}
		if(!creditFile.exists()){
			creditFile.writeString(Integer.toString(0), false);
		}else{
			credits = Integer.parseInt(creditFile.readString());
		}
		font = new BitmapFont(fontHomespun);
		menuFont = new BitmapFont(fontHomespun);
		titleFont = new BitmapFont(fontHomeLarge);
		resetGame();

	}

	@Override
	public void dispose() {
		background.dispose();
		eHand.dispose();
		gameTheme.dispose();
	}

	@Override
	public void pause(){
		if(score > lastHighScore) {
			highScore.writeString(Integer.toString(score), false);
			lastHighScore = score;
		}
		eHand.pause();
	}

	@Override
	public void resume(){
		eHand.resume();
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
		eHand = new EnemyHandler(this);
		sHand = ShipHandler.getHandler(this, shipFile, false);
		sHand.newShip();
		ship = sHand.getPlayerShip();

	}

	public void endGame(){
		state = State.END;
		endExplosion.play();
		gameTheme.stop();
		credits += score / 10;
		creditFile.writeString(Integer.toString(credits), false);
	}
}
