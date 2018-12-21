package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Rocket {
    Vector2 position;
    private TextureRegion texture;
    float speed;
    private float angle; // вращение
    private final int WIDTH = 200;
    private final int HEIGHT = 330;

    public Rocket(TextureRegion texture) {
        this.texture = texture;
        position = new Vector2(1100, 500);
        speed = 10.0f;
        angle = 90.0f;
    }

    public void render(SpriteBatch batch) {
            batch.draw(texture, position.x, position.y, 100, 165, WIDTH, HEIGHT, 0.3f, 0.3f, angle);
    }

        public void update(float dt) {
        angle -= dt;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            position.x -= speed;
            angle = 90.0f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            position.x += speed;
            angle = -90.0f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            position.y -= speed;
            angle = 0.0f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            position.y += speed;
            angle = 0.0f;
        }

        if (position.x < 0) {
            position.x = 0;
        }
        if (position.x > 1100) {
            position.x = 1100;
        }
        if (position.y < 0) {
            position.y = 0;
        }
        if (position.y > 420) {
            position.y = 420;
        }
    }
}
