package com.example.lostdungeon;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.application.Platform;



import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MainMenu extends Application {

    @Override
    public void start(Stage primaryStage) {
        // === Фон ===
        Image backgroundImage = new Image(getClass().getResource("/images/background.png").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setPreserveRatio(false);
        backgroundView.setFitWidth(800);
        backgroundView.setFitHeight(600);

        // === Заголовок ===
        Text title = new Text("Lost Dungeon");
        title.setFont(Font.font("Georgia", FontWeight.EXTRA_BOLD, 48));
        LinearGradient gradient = new LinearGradient(
                0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#ff9933")),
                new Stop(1, Color.web("#ffcc00"))
        );
        title.setFill(gradient);

        // === Кнопки ===
        Button startButton = createMenuButton("Начать игру");
        Button settingsButton = createMenuButton("Настройки");
        Button logoutButton = createMenuButton("Выйти из аккаунта");
        Button exitButton = createMenuButton("Выход из игры");

        // === Сетка кнопок ===
        GridPane buttonGrid = new GridPane();
        buttonGrid.setVgap(15);
        buttonGrid.setAlignment(Pos.CENTER);
        buttonGrid.add(startButton, 0, 0);
        buttonGrid.add(settingsButton, 0, 1);
        buttonGrid.add(logoutButton, 0, 2);
        buttonGrid.add(exitButton, 0, 3);
        GridPane.setHalignment(startButton, HPos.CENTER);
        GridPane.setHalignment(settingsButton, HPos.CENTER);
        GridPane.setHalignment(logoutButton, HPos.CENTER);
        GridPane.setHalignment(exitButton, HPos.CENTER);

        // === Контейнер ===
        VBox rootBox = new VBox(40, title, buttonGrid);
        rootBox.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(backgroundView, rootBox);
        Scene scene = new Scene(root, 1080, 700);

        // === Обработчики ===
        startButton.setOnAction(e -> {
            int playerId = SessionManager.getCurrentPlayerId();
            int level = SessionManager.getCurrentLevel();

            // Сохраняем прогресс
            String json = String.format("""
        {
            "playerId": %d,
            "level": %d
        }
    """, playerId, level);

            File jsonFile = new File("progress.txt");
            try {
                Files.writeString(jsonFile.toPath(), json);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            // Запускаем Unity.exe
            File exe = new File("MyGame.exe");
            if (exe.exists()) {
                try {
                    Process process = new ProcessBuilder(exe.getAbsolutePath()).start();
                    Stage stageToHide = primaryStage;
                    stageToHide.hide(); // Скрываем JavaFX

                    // После закрытия Unity возвращаем JavaFX
                    new Thread(() -> {
                        try {
                            process.waitFor(); // ждём завершения Unity
                            Platform.runLater(stageToHide::show); // возвращаем JavaFX в UI-потоке
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }).start();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                System.err.println("MyGame.exe не найден!");
            }
        });

        settingsButton.setOnAction(e -> System.out.println("Настройки"));

        logoutButton.setOnAction(e -> {
            System.out.println("Выход из аккаунта...");
            SessionManager.clearSession();
            primaryStage.close(); // Закрываем главное меню
            Platform.runLater(() -> {
                try {
                    new RegistrationForm().start(new Stage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        });

        exitButton.setOnAction(e -> System.exit(0));

        // === Привязка фона ===
        backgroundView.fitWidthProperty().bind(scene.widthProperty());
        backgroundView.fitHeightProperty().bind(scene.heightProperty());

        primaryStage.setTitle("Lost Dungeon — Главное меню");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(240);
        btn.setPrefHeight(50);
        btn.setStyle(
                "-fx-background-color: linear-gradient(to right, #ffbb33, #ff8800);" +
                        "-fx-text-fill: black;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 15px;" +
                        "-fx-cursor: hand;"
        );

        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: linear-gradient(to right, #ff8800, #ffbb33);" +
                        "-fx-text-fill: black;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 15px;" +
                        "-fx-cursor: hand;"
        ));

        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: linear-gradient(to right, #ffbb33, #ff8800);" +
                        "-fx-text-fill: black;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 15px;" +
                        "-fx-cursor: hand;"
        ));

        return btn;
    }

    public static void main(String[] args) {
//        DataBase.initialize();
//
//        if (SessionManager.getCurrentEmail() != null) {
//            Application.launch(MainMenu.class, args);
//        } else {
//            Application.launch(RegistrationForm.class, args);
//        }
    }
}
