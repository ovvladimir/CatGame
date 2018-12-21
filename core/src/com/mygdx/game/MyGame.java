package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MyGame extends Game {
	public enum Screens {
		MENU, GAME
	}
	private SpriteBatch batch;
	private GameScreen gameScreen;
	private MenuScreen menuScreen;
	private Viewport viewport;

	public Viewport getViewport() {
		return viewport;
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		gameScreen = new GameScreen(this, batch);
		menuScreen = new MenuScreen(this, batch);
		viewport = new FitViewport(1280, 720); // стратегия при растягивании экрана (подстраивает картинку)
		//setScreen(menuScreen);
		switchScreen(Screens.MENU);
	}

	@Override
	public void render () {
		float dt = Gdx.graphics.getDeltaTime(); // дельта для расчета скорости в зависимости от fps
		getScreen().render(dt);
	}

	public void switchScreen(Screens type) {
		Screen currentScreen = getScreen();
		if (currentScreen != null) {
			currentScreen.dispose();
		}
		switch (type) {
			case MENU:
				setScreen(menuScreen);
				break;
			case GAME:
				setScreen(gameScreen);
				break;
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		getScreen().dispose();
	}
}
