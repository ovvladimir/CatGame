package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MenuScreen implements Screen {
    private MyGame myGame;
    private SpriteBatch batch;
    private BitmapFont font24;
    private BitmapFont font48;
    private BitmapFont font96;

    private TextureAtlas atlas;
    private TextureRegion[][] anim;
    private TextureRegion[][] anim2;
    private TextureRegion[][] anim3;
    private TextureRegion[][] anim4;
    private  float time;

    private TextureRegion textureRegionBackground;
    private Stage stage;
    private Skin skin;

    public MenuScreen(MyGame myGame, SpriteBatch batch) {
        this.myGame = myGame;
        this.batch = batch;
    }

    @Override
    public void show() {
        atlas = new TextureAtlas("runner.pack");
        textureRegionBackground = atlas.findRegion("bg");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("zorque.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 48;
        parameter.borderColor = Color.BLACK;
        parameter.shadowOffsetX = 3;
        parameter.shadowOffsetY = -3;
        parameter.shadowColor = Color.BLACK;
        font48 = generator.generateFont(parameter);
        parameter.size = 96;
        font96 = generator.generateFont(parameter);
        parameter.size = 24;
        font24 = generator.generateFont(parameter);
        generator.dispose();

        anim = atlas.findRegion("cat").split(170,190);
        anim2 = atlas.findRegion("bat").split(150,150);
        anim3 = atlas.findRegion("frog").split(96,61);
        anim4 = atlas.findRegion("mouse").split(115,61);

        createGUI();
    }

    public void createGUI() {
        stage = new Stage(myGame.getViewport(), batch);
        skin = new Skin(atlas);
        Gdx.input.setInputProcessor(stage);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("button");
        textButtonStyle.font = font48;
        skin.add("tbs", textButtonStyle);

        TextButton btnNeuGame = new TextButton("START", skin, "tbs");
        TextButton btnExitGame = new TextButton("EXIT", skin, "tbs");
        btnNeuGame.setPosition(330, 300);
        btnExitGame.setPosition(330, 100);
        stage.addActor(btnNeuGame);
        stage.addActor(btnExitGame);
        btnNeuGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                myGame.switchScreen(MyGame.Screens.GAME);
            }
        });
        btnExitGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void render(float delta) {
        update(delta);
        batch.begin();
        batch.draw(textureRegionBackground, 0, 0);

        int frame = (int)(time / 0.15f);
        frame = frame % 8;
        batch.draw(anim[0][frame],980,200,280,280);
        int frame2 = (int)(time / 0.2f);
        frame2 = frame2 % 2;
        batch.draw(anim2[0][frame2],40,400,150,150);
        int frame3 = (int)(time / 0.2f);
        frame3 = frame3 % 2;
        batch.draw(anim3[0][frame3],170,294,96,61);
        int frame4 = (int)(time / 0.2f);
        frame4 = frame4 % 2;
        batch.draw(anim4[0][frame4],120,140,115,61);

        font96.draw(batch, "CAT GAME", 0,600,1280,1,false);
        font24.draw(batch, "ENEMIES", 120, 390);
        font24.draw(batch, "BONUS", 125, 125);
        batch.end();
        stage.draw();
    }

    public void update(float dt) {
        stage.act(dt);
        time += dt;
    }

    @Override
    public void resize(int width, int height) {
        myGame.getViewport().update(width, height, true);
        myGame.getViewport().apply();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        font96.dispose();
        font48.dispose();
        font24.dispose();
        atlas.dispose();
        skin.dispose();
        stage.dispose();
    }
}
