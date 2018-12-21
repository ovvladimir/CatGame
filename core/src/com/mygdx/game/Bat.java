package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bat {
    private TextureRegion[][] texture;
    private Vector2 position;
    private Rectangle rectangle;
    private final int WIDTH = 150;
    private final int HEIGHT = 150;
    private float time;

    public Rectangle getRectangle() {
        return rectangle;
    }
    public Vector2 getPosition() {
        return position;
    }
    public Bat (TextureRegion textureBat, Vector2 position) {
        this.texture = textureBat.split(WIDTH, HEIGHT);
        this.position = position;
        this.rectangle = new Rectangle(position.x, position.y, WIDTH, HEIGHT);
    }
    public void setPosition(float x, float y) {
        position.set(x, y);
        rectangle.setPosition(position);
    }
    public void render(SpriteBatch batch, float worldX) {
        time += Gdx.graphics.getDeltaTime();
        int frame = (int)(time / 0.2f);
        frame = frame % 2;
        batch.draw(texture[0][frame], position.x - worldX, position.y, 75, 75, WIDTH, HEIGHT, 1, 1, 0);
    }
    public void recreate() {
        position.x = 0;
        position.y = MathUtils.random(400, 570);
    }
}
