package com.example.lostdungeon;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SessionManager {
    private static final Path SESSION_FILE = Path.of("current_user.txt");

    private static String currentEmail;
    private static int currentPlayerId;
    private static int currentLevel;
    private static String currentPlayerName;

    public static void setCurrentEmail(String email) {
        currentEmail = email;
        currentPlayerId = DataBase.getPlayerIdByEmail(email);
        currentLevel = DataBase.getLevel(currentPlayerId);
        currentPlayerName = DataBase.getPlayerNameByEmail(email); // имя из БД

        try {
            Files.writeString(SESSION_FILE, currentPlayerName);
        } catch (IOException e) {
            System.err.println("Не удалось записать имя в current_user.txt: " + e.getMessage());
        }
    }

    public static String getCurrentEmail() {
        return currentEmail;
    }

    public static int getCurrentPlayerId() {
        return currentPlayerId;
    }

    public static int getCurrentLevel() {
        return currentLevel;
    }

    public static void setCurrentLevel(int level) {
        currentLevel = level;
    }

    public static void clearSession() {
        currentEmail = null;
        currentPlayerId = -1;
        currentLevel = 1;
        currentPlayerName = null;

        try {
            Files.deleteIfExists(SESSION_FILE);
        } catch (IOException e) {
            System.err.println("Не удалось удалить current_user.txt: " + e.getMessage());
        }
    }

    public static String getCurrentPlayerName() {
        if (currentPlayerName == null) {
            try {
                currentPlayerName = Files.readString(SESSION_FILE).trim();
            } catch (IOException e) {
                System.err.println("Не удалось прочитать current_user.txt: " + e.getMessage());
            }
        }
        return currentPlayerName;
    }
}
