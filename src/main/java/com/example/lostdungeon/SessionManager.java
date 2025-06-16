package com.example.lostdungeon;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.prefs.Preferences;

public class SessionManager {
    private static final String CURRENT_EMAIL_KEY = "current_email";
    private static String currentEmail;
    private static int currentPlayerId;
    private static int currentLevel;

    public static void setCurrentEmail(String email) {
        currentEmail = email;
        currentPlayerId = DataBase.getPlayerIdByEmail(email);
        currentLevel = DataBase.getLevel(currentPlayerId);
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

        // Очистка файла current_user.txt
        try {
            Files.deleteIfExists(Path.of("current_user.txt"));
        } catch (IOException e) {
            System.err.println("Не удалось удалить файл current_user.txt: " + e.getMessage());
        }
    }
}