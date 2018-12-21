package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class GameScreen implements Screen {
    private MyGame myGame;
    private SpriteBatch batch;
    private TextureAtlas atlas;
    private TextureRegion textureRocket;
    private TextureRegion textureBat;
    private TextureRegion textureFrog;
    private TextureRegion textureMouse;
    private boolean paused;
    private BitmapFont font48;
    private BitmapFont font96;
    private TextureRegion textureMars;
    private Music music;
    private Sound playerBatSound;
    private Sound playerMouseSound;
    private Sound playerFrogSound;
    //+++++++++++++++++++++++++++++++++++
    private TextureRegion textureFon;
    private Texture textureStar;
    private Star[] stars;
    private final int STARS_COUNT = 400;
    float t = 0;
    //+++++++++++++++++++++++++++++++++++
    private float groundHeight = 320.0f;
    private float playerCat = 200.0f; // позиция кота
    private float score;
    private float score2;
    private boolean gameover;
    private Player player;
    private Rocket rocket;
    private Bat[] bats;
    private Frog[] frogs;
    private Mouse[] mouses;
    private float time;
    private Stage stage;
    private Skin skin;

    public float getGroundHeight() {
        return groundHeight;
    }
    public float getPlayerCat() {
        return playerCat;
    }
    public float getScore() {
        return score;
    }
    public float getScore2() {
        return score2;
    }
    public TextureAtlas getAtlas() {
        return atlas;
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    private class Star {
        private Vector2 position;
        private float speed;

        public Star() {
            position = new Vector2((float) Math.random() * 1280, MathUtils.random(320, 720));
            speed = 3.0f + (float) Math.random() * 60.0f;
        }

        public void update(float dt) {
            position.x -= speed * dt;
            if (position.x < -20) {
                position.x = 1280;
                position.y = MathUtils.random(320, 720);
                speed = 3.0f + (float) Math.random() * 60.0f;
            }
        }
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public GameScreen(MyGame myGame, SpriteBatch batch) {
        this.myGame = myGame;
        this.batch = batch;
    }

    @Override
    public void show() {
        atlas = new TextureAtlas("runner.pack");
        textureMars = atlas.findRegion("Mars");
        textureRocket = atlas.findRegion("Rocket");
        rocket = new Rocket(textureRocket);
        player = new Player(this);
        bats = new Bat[7];
        frogs = new Frog[3];
        mouses = new Mouse[5];
        textureBat = atlas.findRegion("bat");
        textureFrog = atlas.findRegion("frog");
        textureMouse = atlas.findRegion("mouse");

        bats[0] = new Bat(textureBat, new Vector2(1280, MathUtils.random(400, 570)));
        for (int i=1; i < bats.length; i++) {
        bats[i] = new Bat(textureBat, new Vector2(bats[i-1].getPosition().x + MathUtils.random(500, 2000), MathUtils.random(400, 570)));
        }
        frogs[0] = new Frog(textureFrog, new Vector2(4000, groundHeight));
        for (int n=1; n < frogs.length; n++) {
            frogs[n] = new Frog(textureFrog, new Vector2(frogs[n-1].getPosition().x + MathUtils.random(2000, 4000), groundHeight));
        }
        mouses[0] = new Mouse(textureMouse, new Vector2(2000, groundHeight));
        for (int j=1; j < mouses.length; j++) {
            mouses[j] = new Mouse(textureMouse, new Vector2(mouses[j-1].getPosition().x + MathUtils.random(500, 1000), groundHeight));
        }

        playerBatSound = Gdx.audio.newSound(Gdx.files.internal("1n.wav"));
        playerMouseSound = Gdx.audio.newSound(Gdx.files.internal("1e.wav"));
        playerFrogSound = Gdx.audio.newSound(Gdx.files.internal("1frog.wav"));
        music = Gdx.audio.newMusic(Gdx.files.internal("1.mp3"));
        music.setLooping(true); // зацикливание музыки
        music.setVolume(0.2f); // громкость музыки
        music.play();
        //++++++++++++++++++++++++++++++++++++++++++++++++
        textureFon = atlas.findRegion("bg");
        textureStar = new Texture("star16.png");
        this.stars = new Star[STARS_COUNT];
        for (int i = 0; i < stars.length; i++) {
            this.stars[i] = new Star();
        }
        //++++++++++++++++++++++++++++++++++++++++++++++++
        gameover = false;
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
        generator.dispose();
        this.score2 = 9;
        this.score = 0;
        HighScoreSystem.createTable();
        HighScoreSystem.loadTable();
        Gdx.input.setInputProcessor(null);
        paused = false;
        createGUI();
    }

    public void createGUI() {
        stage = new Stage(myGame.getViewport(), batch);
        skin = new Skin(atlas);
        Gdx.input.setInputProcessor(stage);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        TextButton.TextButtonStyle textButtonStyle2 = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("bat");
        textButtonStyle2.up = skin.getDrawable("angle");
        textButtonStyle.font = font48;
        textButtonStyle2.font = font48;
        skin.add("tbs", textButtonStyle);
        skin.add("tbs2", textButtonStyle2);
        TextButton btnPause = new TextButton("PAUSE", skin, "tbs");
        TextButton btnAngle = new TextButton("", skin, "tbs2");
        btnPause.setPosition(1000, 0);
        btnAngle.setPosition(612, 650);
        stage.addActor(btnPause);
        stage.addActor(btnAngle);
        btnPause.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                paused = !paused;
            }
        });
        btnAngle.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                player.jumpToAngle();
            }
        });
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        batch.draw(textureFon, -t, 320);
        batch.draw(textureFon, -t + 1280, 320);
        for (int i = 0; i < stars.length; i++) {
            batch.draw(textureStar, stars[i].position.x, stars[i].position.y, 6, 6, 12, 12, stars[i].speed / 100.0f + 0.2f, stars[i].speed / 100.0f + 0.2f, 0, 0, 0, 12, 12, false, false);
        }
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        batch.draw(textureMars, 0, 0);
        for (int i = 0; i < 3; i++) {
            batch.draw(textureMars, i * 950 - player.getPosition().x % 950, 0);
        }
        player.render(batch);
        rocket.render(batch);

        for (int i = 0; i < bats.length; i++) {
            bats[i].render(batch, player.getPosition().x - playerCat);
        }
        for (int n = 0; n < frogs.length; n++) {
            frogs[n].render(batch, player.getPosition().x - playerCat);
        }
        for (int j = 0; j < mouses.length; j++) {
            mouses[j].render(batch, player.getPosition().x - playerCat);
        }

        font48.draw(batch, "RECORD: " + HighScoreSystem.topPlayerScore, 22, 702);
        font48.draw(batch, "SCORE: " + (int)getScore(), 22, 662);
        font48.draw(batch, "LIFE: " + (int)getScore2(), 1130, 702);
        if (gameover) {
            font96.draw(batch,"GAME OVER", 0, 500, 1280, 1, false);
            font48.setColor(1, 1, 1, 0.5f + 0.5f * (float) Math.sin(time * 5.0f));
            font48.draw(batch,"Tab to RESTART", 0, 380, 1280,1, false);
            font48.setColor(1, 1, 1, 1);
        }
        batch.end();
        stage.draw();
    }
    public void restart() {
        gameover = false;
        bats[0].setPosition(1280, MathUtils.random(400, 570));
        for (int i=1; i < bats.length; i++) {
            bats[i].setPosition(bats[i - 1].getPosition().x + MathUtils.random(500, 2000), MathUtils.random(400, 570));
        }
        frogs[0].setPosition(4000, groundHeight);
        for (int n=1; n < frogs.length; n++) {
            frogs[n].setPosition(frogs[n - 1].getPosition().x + MathUtils.random(2000, 4000), groundHeight);
        }
        mouses[0].setPosition(2000, groundHeight);
        for (int j=1; j < mouses.length; j++) {
            mouses[j].setPosition(mouses[j - 1].getPosition().x + MathUtils.random(500, 1000), groundHeight);
        }
        player.restart();
        score2 = 9;
        score = 0;
    }
    public float getRightestEnemy() {
        float maxValue = 0.0f;
        for (int i = 0; i < bats.length; i++) {
            if (bats[i].getPosition().x > maxValue) {
                maxValue = bats[i].getPosition().x;
            }
        }
        for (int n = 0; n < frogs.length; n++) {
            if (frogs[n].getPosition().x > maxValue) {
                maxValue = frogs[n].getPosition().x;
            }
        }
        for (int j = 0; j < mouses.length; j++) {
            if (mouses[j].getPosition().x > maxValue) {
                maxValue = mouses[j].getPosition().x;
            }
        }
        return maxValue;
    }

    public void update(float dt) {
        stage.act(dt);
        if (!paused) {
            time += dt;
            if (!gameover) {
                player.update(dt);
                rocket.update(dt);

                for (int i = 0; i < bats.length; i++) {
                    if (bats[i].getPosition().x < player.getPosition().x - playerCat - 150) {
                        bats[i].setPosition(getRightestEnemy() + MathUtils.random(500, 2000), MathUtils.random(400, 570));
                    }
                }
                for (int i = 0; i < bats.length; i++) {
                    if (bats[i].getRectangle().overlaps(player.getRectangle())) {
                        bats[i].recreate();
                        score2--;
                        playerBatSound.play();
                        if (score2 <= 0) {
                            gameover = true; // игра закончена
                            HighScoreSystem.updateTable("Player", (int) getScore());
                            break; // больше не обращаться
                        }
                    }
                }
                for (int n = 0; n < frogs.length; n++) {
                    if (frogs[n].getPosition().x < player.getPosition().x - playerCat - 96) {
                        frogs[n].setPosition(getRightestEnemy() + MathUtils.random(2000, 4000), groundHeight);
                    }
                }
                for (int n = 0; n < frogs.length; n++) {
                    if (frogs[n].getRectangle().overlaps(player.getRectangle())) {
                        frogs[n].recreate();
                        score2--;
                        playerFrogSound.play();
                        if (score2 <= 0) {
                            gameover = true; // игра закончена
                            HighScoreSystem.updateTable("Player", (int) getScore());
                            break; // больше не обращаться
                        }
                    }
                }
                for (int j = 0; j < mouses.length; j++) {
                    if (mouses[j].getPosition().x < player.getPosition().x - playerCat - 115) {
                        mouses[j].setPosition(getRightestEnemy() + MathUtils.random(500, 1000), groundHeight);
                    }
                }
                for (int j = 0; j < mouses.length; j++) {
                    if (mouses[j].getRectangle().overlaps(player.getRectangle())) {
                        mouses[j].recreate();
                        score++;
                        playerMouseSound.play();
                    }
                }
                //++++++++++++++++++++++++++++++++++++++
                t += dt * 40.0f;
                if (t > 1280) t -= 1280;
                for (int i = 0; i < stars.length; i++) {
                    stars[i].update(dt);
                }
                //++++++++++++++++++++++++++++++++++++++
            } else {
                if (Gdx.input.justTouched()) {
                    restart();
                }
            }
        }
        stage.act(dt);
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
        player.dispose();
        textureStar.dispose();
        music.dispose();
        playerMouseSound.dispose();
        playerBatSound.dispose();
        playerFrogSound.dispose();
        font48.dispose();
        font96.dispose();
        atlas.dispose();
        stage.dispose();
        skin.dispose();
    }
}
