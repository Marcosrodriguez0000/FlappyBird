package io.github.some_example_FlappyBird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture background;
    private Texture bird;
    private Texture fish;
    private Texture gameOverImage;
    private BitmapFont font;
    private float birdY = 0;
    private float velocity = 0;
    private float gravity = 2;
    private float[] fishX;
    private float[] fishY;
    private float fishWidth;
    private float fishHeight;
    private Rectangle birdRectangle;
    private Rectangle[] fishRectangles;
    private int numberOfFishes = 5;
    private boolean gameActive = true;
    private int score = 0;
    private float timeElapsed = 0;

    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("background.jpg");
        bird = new Texture("bird.png");
        fish = new Texture("fish.png");
        gameOverImage = new Texture("gameover.png");
        font = new BitmapFont();
        font.getData().setScale(4); // Ajusta el tamaño de la fuente

        birdY = Gdx.graphics.getHeight() / 2 - bird.getHeight() / 2;
        fishWidth = fish.getWidth() * 2;
        fishHeight = fish.getHeight() * 2;
        fishX = new float[numberOfFishes];
        fishY = new float[numberOfFishes];
        fishRectangles = new Rectangle[numberOfFishes];

        for (int i = 0; i < numberOfFishes; i++) {
            fishX[i] = Gdx.graphics.getWidth() + i * (Gdx.graphics.getWidth() / numberOfFishes);
            fishY[i] = (float) Math.random() * (Gdx.graphics.getHeight() - fishHeight);
            fishRectangles[i] = new Rectangle();
        }

        birdRectangle = new Rectangle();
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(bird, Gdx.graphics.getWidth() / 2 - bird.getWidth() / 2, birdY);

        for (int i = 0; i < numberOfFishes; i++) {
            batch.draw(fish, fishX[i], fishY[i], fishWidth, fishHeight);
        }

        if (!gameActive) {
            float imageX = (Gdx.graphics.getWidth() - gameOverImage.getWidth()) / 2;
            float imageY = (Gdx.graphics.getHeight() - gameOverImage.getHeight()) / 2;
            batch.draw(gameOverImage, imageX, imageY);
        } else {
            timeElapsed += Gdx.graphics.getDeltaTime();
            if (timeElapsed >= 5) {
                score += 100;
                timeElapsed = 0;
            }
            String scoreText = "Score: " + score;
            float textWidth = font.getRegion().getRegionWidth();
            float textX = (Gdx.graphics.getWidth() - textWidth) / 2;
            font.draw(batch, scoreText, textX, 50); // Ajusta la coordenada Y para mover el texto más arriba
        }

        batch.end();

        if (gameActive) {
            if (Gdx.input.justTouched()) {
                velocity = -30;
            }

            velocity += gravity;
            birdY -= velocity;

            if (birdY < 0) {
                birdY = 0;
            }

            birdRectangle.set(Gdx.graphics.getWidth() / 2 - bird.getWidth() / 2, birdY, bird.getWidth(), bird.getHeight());

            for (int i = 0; i < numberOfFishes; i++) {
                fishX[i] -= 4;
                if (fishX[i] < -fishWidth) {
                    fishX[i] = Gdx.graphics.getWidth();
                    fishY[i] = (float) Math.random() * (Gdx.graphics.getHeight() - fishHeight);
                }

                fishRectangles[i].set(fishX[i], fishY[i], fishWidth, fishHeight);

                if (birdRectangle.overlaps(fishRectangles[i])) {
                    gameActive = false;
                }
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        bird.dispose();
        fish.dispose();
        gameOverImage.dispose();
        font.dispose();
    }
}
