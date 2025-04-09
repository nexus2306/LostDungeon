package com.mygdx.lostdungeon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenuScreen implements Screen {

    private final LostDungeonGame game;
    private Stage stage;
    private Texture backgroundTexture;
    private SpriteBatch batch;

    public MainMenuScreen(final LostDungeonGame game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.backgroundTexture = new Texture(Gdx.files.internal("MainMenu/background.png"));

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // --- Загружаем шрифт с кириллицей ---
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("MainMenu/OpenSans_Condensed-Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 32;
        parameter.characters = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.,:;!?()[]{}<>|/@\\\"'+-*=#%&^_~` ";
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();

        // --- Стиль кнопок ---
        TextButtonStyle buttonStyle = new TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;

        // --- Кнопки ---
        TextButton startButton = new TextButton("Начать игру", buttonStyle);
        TextButton settingButton = new TextButton("Настройки", buttonStyle);
        TextButton exitButton = new TextButton("Выход", buttonStyle);

        startButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("start push");
            }
        });

        settingButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("setting push");
            }
        });

        exitButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        // --- Таблица для центрирования ---
        Table table = new Table();
        table.setFillParent(true);      // заполнит экран
        table.center();                 // центрирование
        table.add(startButton).pad(10).width(250).height(70).row();
        table.add(settingButton).pad(10).width(250).height(70).row();
        table.add(exitButton).pad(10).width(250).height(70).row();

        // --- Добавляем таблицу на сцену ---
        stage.addActor(table);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        backgroundTexture.dispose();
    }
}
