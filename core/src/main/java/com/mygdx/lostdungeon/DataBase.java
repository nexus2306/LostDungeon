package com.mygdx.lostdungeon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.sql.Database;
import com.badlogic.gdx.sql.DatabaseFactory;
import com.badlogic.gdx.sql.SQLiteGdxException;


import com.badlogic.gdx.sql.DatabaseCursor;
import com.badlogic.gdx.utils.Array;


import models.enemy.Enemy;
import models.player.HealthComponent;
import models.player.Player;

public class DatabaseHelper {
    private Database db;
    private static final String DATABASE_NAME = "statistic.db";
    private static final int DATABASE_VERSION = 2;
    // Конструктор
    public DatabaseHelper() {
        // Подключаем базу данных или создаем новую
        db = DatabaseFactory.getNewDatabase(DATABASE_NAME,DATABASE_VERSION , null, null);
    }

    // Метод для открытия базы данных
    public void openDatabase() {
        try {
            db.setupDatabase();
            db.openOrCreateDatabase();
        } catch (SQLiteGdxException e) {
            Gdx.app.error("DB_ERROR", "Error in open database: " + e.getMessage());
        }
    }

    // Метод для создания таблиц player_stats и enemy_stats
    public void createTables() {
        try {
            // SQL запрос для создания таблицы player_stats
            String createPlayerStatsTable = "CREATE TABLE IF NOT EXISTS player_stats (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "health INTEGER," +
                "attack INTEGER," +
                "defense INTEGER" +
                ");";
            // SQL запрос для создания таблицы enemy_stats
            String createEnemyStatsTable = "CREATE TABLE IF NOT EXISTS enemy_stats (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "type TEXT," +
                "health INTEGER," +
                "attack INTEGER," +
                "defense INTEGER" +
                ");";

            // Выполнение запросов
            db.execSQL(createPlayerStatsTable);
            db.execSQL(createEnemyStatsTable);
            Gdx.app.log("DB_INFO", "Таблицы player_stats и enemy_stats успешно созданы");
        } catch (SQLiteGdxException e) {
            Gdx.app.error("DB_ERROR", "Error on create tables: " + e.getMessage());
        }
    }

    // Метод для закрытия базы данных
    public void closeDatabase() {
        try {
            db.closeDatabase();
        } catch (SQLiteGdxException e) {
            Gdx.app.error("DB_ERROR", "Error in close database: " + e.getMessage());
        }
    }
    public void saveData(models.player.HealthComponent statsPlayer,Array<models.enemy.HealthComponent> statsEnemy){
        String savePlayerString="insert into player_stats(health,attack,defense) values(%s,%s,%s)";
        String savePlayerSQl=String.format(savePlayerString,statsPlayer.getHealth(),statsPlayer.getAttack(),statsPlayer.getProtection());
        String saveEnemyStrings="insert into enemy_stats(type,health,attack,defense) values('%s',%s,%s,%s)";
        Array<String> saveEnemySql=new Array<String>();
        int i=0;
        for(var currentStats:statsEnemy) {
            saveEnemySql.add(String.format(saveEnemyStrings,currentStats.type,currentStats.getHealth(),currentStats.getAttack(),currentStats.getProtection()));
        }
        try{
            db.execSQL(savePlayerSQl);
            for(String save:saveEnemySql)
                db.execSQL(save);
        }catch (SQLiteGdxException e){
            Gdx.app.error("DB_ERROR", "Error in save data : " + e.getMessage());
        }
    }
    public HealthComponent loadDataPlayer() {
        HealthComponent healthComponent = null;
        String selectPlayerString = "SELECT * FROM player_stats ORDER BY id DESC LIMIT 1;";

        DatabaseCursor cursor = null;
        try {
            cursor = db.rawQuery(selectPlayerString);

            if(cursor.next()) {
                var health=Integer.valueOf(cursor.getString(1));
                var attack=Integer.valueOf(cursor.getString(2));
                var protection=Integer.valueOf(cursor.getString(3));
                healthComponent=new HealthComponent (health,attack,protection);
            }else return null;
        } catch (SQLiteGdxException e) {
            Gdx.app.error("DB_ERROR", "Error in loading data: " + e.getMessage());
        } finally {

            if (cursor != null) {
                cursor.close(); // Закрываем курсор, чтобы избежать утечек памяти
            }
        }

        return healthComponent;
    }
    public Array<models.enemy.HealthComponent> loadDataEnemies() {
        models.enemy.HealthComponent healthComponent = null;
        String selectPlayerString = "SELECT * FROM enemy_stats ORDER BY id DESC LIMIT 1;";

        DatabaseCursor cursor = null;
        try {
            cursor = db.rawQuery(selectPlayerString);

            if (cursor.next()) {
                var type=String.valueOf(cursor.getString(0));
                var health=Integer.valueOf(cursor.getString(2));
                var attack=Integer.valueOf(cursor.getString(3));
                var protection=Integer.valueOf(cursor.getString(4));
                healthComponent=new models.enemy.HealthComponent(type,health,attack,protection);
            }
            else return null;
        } catch (SQLiteGdxException e) {
            Gdx.app.error("DB_ERROR", "Error in loading data: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close(); // Закрываем курсор, чтобы избежать утечек памяти
            }
        }
        Array<models.enemy.HealthComponent>enemystats=new Array<models.enemy.HealthComponent>();
        enemystats.add(healthComponent);
        return enemystats;
    }
    public void deleteData(){
        try{
            db.execSQL("delete from player_stats");
            db.execSQL("delete from enemy_stats");
        }catch(SQLiteGdxException e){
            Gdx.app.error("DB_ERROR", "Error in deleting data: " + e.getMessage());
        }
    }
}
