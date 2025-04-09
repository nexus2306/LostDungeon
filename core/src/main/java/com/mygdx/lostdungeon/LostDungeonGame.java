package com.mygdx.lostdungeon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
//
///** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
//public class LostDungeonGame extends ApplicationAdapter {
//    private SpriteBatch batch;
//    private Texture image;
//
//    @Override
//    public void create() {
//        batch = new SpriteBatch();
//        image = new Texture("libgdx.png");
//    }
//
//    @Override
//    public void render() {
//        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
//        batch.begin();
//        batch.draw(image, 10, 10);
//        batch.end();
//    }
//
//    @Override
//    public void dispose() {
//        batch.dispose();
//        image.dispose();
//    }
//}


import com.badlogic.gdx.Game;
import jdk.internal.event.Event;

public class LostDungeonGame extends Game {

    private SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new Launcher(this)); // Запускаем экран регистрации
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        super.dispose();
    }


    public SpriteBatch getBatch() {
        return batch;
    }
}


