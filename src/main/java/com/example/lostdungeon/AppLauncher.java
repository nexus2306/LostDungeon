package com.example.lostdungeon;

import javafx.application.Application;

public class AppLauncher {
    public static void main(String[] args) {
        DataBase.initialize();

        if (SessionManager.getCurrentEmail() != null) {
            Application.launch(MainMenu.class, args);
        } else {
            Application.launch(RegistrationForm.class, args);
        }
    }
}