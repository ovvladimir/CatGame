package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {
    private TextureRegion[][] texture;
    private GameScreen gameScreen;
    private Vector2 position;
    private Vector2 velocity;
    private Rectangle rectangle;
    private Sound jumpSound;
    private float time;
    private final int WIDTH = 172;
    private final int HEIGHT = 186;
    private float lay;
    private float up;
    private float angle;
    private boolean jumpAngle;

    public void jumpToAngle() {
        jumpAngle = true;
    }
    public Vector2 getPosition() {
        return position;
    }
    public Rectangle getRectangle() {
        return rectangle;
    }

    public Player(GameScreen gameScreen) {
        this.texture = gameScreen.getAtlas().findRegion("CatAnim").split(WIDTH, HEIGHT);
        this.gameScreen = gameScreen;
        this.position = new Vector2(200, gameScreen.getGroundHeight());
        this.velocity = new Vector2(240.0f, 0.0f); // скорость земли 240 пикселей в сек.
        this.rectangle = new Rectangle(position.x + WIDTH / 4, position.y, WIDTH / 3, HEIGHT - 10);
        this.jumpSound = Gdx.audio.newSound(Gdx.files.internal("1j.wav"));
    }

    public void restart() {
        position.set(0, gameScreen.getGroundHeight());
        velocity.set(240.0f, 0.0f);
        rectangle.setPosition(position);
    }

    public void render(SpriteBatch batch) {
        int frame = (int)(time / 0.1f);
        if (up == 0) {
            frame = frame % 8;
        } else {
            if (up == 1) {frame = 8;}
            if (up == 2) {frame = 9;}
            if (up == -1) {frame = 10;}
            frame = frame % 11;
        }
        batch.draw(texture[0][frame], gameScreen.getPlayerCat(), position.y - 5, 86, 93, WIDTH, HEIGHT, 1, 1, angle);
    }
    public void update(float dt) {
        if (jumpAngle && position.y > gameScreen.getGroundHeight() + 20) {
                if (angle < -360.0f) {
                    angle -= 80.0f * dt;
                } else {
                    angle -= 270.0f * dt;
                }
        }
        if (position.y > gameScreen.getGroundHeight()) {
            velocity.y -= 720.0f * dt; // гравитация
            if (velocity.y >= 0) up = 1;
            if (velocity.y < 0) up = 2;
        } else {
            position.y = gameScreen.getGroundHeight();
            velocity.y = 0.0f;
            up = 0;
            angle = 0;
            jumpAngle = false;
            time += dt; // время для смены костюмов

            if (Gdx.input.isKeyPressed(Input.Keys.UP) || (Gdx.input.isTouched() && (720 - Gdx.input.getY() > position.y + 85))) {
                velocity.y = 530.0f; // прыжок
                jumpSound.play();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                velocity.y = 530.0f; // прыжок
                jumpAngle = true; // вращение
                jumpSound.play();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || (Gdx.input.isTouched() && (720 - Gdx.input.getY() < position.y + 85) && Gdx.input.getX() < 1000)) {
                lay = 1;
                position.y = gameScreen.getGroundHeight() - 5;
                up = -1;
            } else {
                lay = 0;
            }
        }
        position.mulAdd(velocity, dt);
        velocity.x += 4.0f * dt; // увеличение скорости
        if (lay == 1) {
            rectangle.setPosition(position.x + WIDTH / 4, position.y - HEIGHT / 2);
        } else {
            rectangle.setPosition(position.x + WIDTH / 4, position.y);
        }
    }
    public void dispose() {
        jumpSound.dispose();
    }
}
