package io.github.some_example_FlappyBird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class MainGameScreen implements Screen {
    private final Main game;
    private final String playerName;
    private SpriteBatch batch;
    private Texture background;
    private Texture bird;
    private Texture fish;
    private Texture gameOverImage;
    private Texture ground;
    private Texture newBackground;
    private Texture newFish;
    private Texture newGround;
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
    private Rectangle groundRectangle;
    private int numberOfFishes = 5;
    private boolean gameActive = true;
    private int score = 0;
    private float timeElapsed = 0;
    private float newFishRotation = 0;

    public MainGameScreen(Main game, String playerName) {
        this.game = game;
        this.playerName = playerName;
        create();
    }

    public void create() {
        batch = new SpriteBatch();
        background = new Texture("background.png");
        bird = new Texture("bird.png");
        fish = new Texture("fish.png");
        gameOverImage = new Texture("gameover.png");
        ground = new Texture("ground.png");
        newBackground = new Texture("new_background.png");
        newFish = new Texture("new_fish.png");
        newGround = new Texture("new_ground.png");
        font = new BitmapFont();
        font.getData().setScale(4);

        birdY = Gdx.graphics.getHeight() / 2 - bird.getHeight() / 2;
        fishWidth = fish.getWidth();
        fishHeight = fish.getHeight();
        fishX = new float[numberOfFishes];
        fishY = new float[numberOfFishes];
        fishRectangles = new Rectangle[numberOfFishes];

        for (int i = 0; i < numberOfFishes; i++) {
            fishX[i] = Gdx.graphics.getWidth() + i * (Gdx.graphics.getWidth() / numberOfFishes);
            fishY[i] = (float) Math.random() * (Gdx.graphics.getHeight() - fishHeight);
            fishRectangles[i] = new Rectangle();
        }

        birdRectangle = new Rectangle();
        groundRectangle = new Rectangle(0, 0, Gdx.graphics.getWidth(), ground.getHeight());
    }

    @Override
    public void show() {
        // Implement show logic if needed
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        // Change background, fish, and ground textures based on score
        if (score >= 300) {
            batch.draw(newBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            batch.draw(newGround, 0, 0, Gdx.graphics.getWidth(), newGround.getHeight());
        } else {
            batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            batch.draw(ground, 0, 0, Gdx.graphics.getWidth(), ground.getHeight());
        }

        // Draw the bird rotated to the right (90 degrees)
        batch.draw(bird, Gdx.graphics.getWidth() / 2 - bird.getWidth() / 2, birdY, bird.getWidth() / 2, bird.getHeight() / 2, bird.getWidth(), bird.getHeight(), 1, 1, 90, 0, 0, bird.getWidth(), bird.getHeight(), false, false);

        // Draw the fish rotated to the left (270 degrees)
        for (int i = 0; i < numberOfFishes; i++) {
            if (score >= 300) {
                newFishRotation += Gdx.graphics.getDeltaTime() * 50; // Adjust rotation speed as needed
                batch.draw(newFish, fishX[i], fishY[i], fishWidth / 2, fishHeight / 2, fishWidth * 1.5f, fishHeight * 1.5f, 1, 1, newFishRotation, 0, 0, newFish.getWidth(), newFish.getHeight(), false, false);
            } else {
                batch.draw(fish, fishX[i], fishY[i], fishWidth / 2, fishHeight / 2, fishWidth, fishHeight, 1, 1, 270, 0, 0, fish.getWidth(), fish.getHeight(), false, false);
            }
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
            font.draw(batch, scoreText, textX, 50);
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

            if (birdRectangle.overlaps(groundRectangle)) {
                gameActive = false;
            }
        }
    }

    @Override
    public void resize(int width, int height) {
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
        batch.dispose();
        background.dispose();
        bird.dispose();
        fish.dispose();
        gameOverImage.dispose();
        ground.dispose();
        newBackground.dispose();
        newFish.dispose();
        newGround.dispose();
        font.dispose();
    }
}
