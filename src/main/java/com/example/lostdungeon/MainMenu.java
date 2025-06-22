package com.example.lostdungeon;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Timer;
import java.util.TimerTask;

public class MainMenu extends Application {

    @Override
    public void start(Stage primaryStage) {
        Platform.setImplicitExit(false);
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

        // === Получение данных игрока ===
        String playerName = SessionManager.getCurrentPlayerName();
        int level = 1;
        int steps = 30;

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:player.db")) {
            PreparedStatement stmt = conn.prepareStatement("SELECT level, steps FROM ProgressData WHERE name = ?");
            stmt.setString(1, playerName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                level = rs.getInt("level");
                steps = rs.getInt("steps");
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String progressText = "Игрок: %s\nУровень: %d | Шаги: %d".formatted(playerName, level, steps);

        // === Кнопки ===
        Button startButton = createMenuButton("Начать игру");
        Button progressButton = createMenuButton(progressText);
        progressButton.setPrefHeight(70);
        Button logoutButton = createMenuButton("Выйти из аккаунта");
        Button exitButton = createMenuButton("Выход из игры");

        // === Сетка кнопок ===
        GridPane buttonGrid = new GridPane();
        buttonGrid.setVgap(15);
        buttonGrid.setAlignment(Pos.CENTER);
        buttonGrid.add(startButton, 0, 0);
        buttonGrid.add(progressButton, 0, 1);
        buttonGrid.add(logoutButton, 0, 2);
        buttonGrid.add(exitButton, 0, 3);
        GridPane.setHalignment(startButton, HPos.CENTER);
        GridPane.setHalignment(progressButton, HPos.CENTER);
        GridPane.setHalignment(logoutButton, HPos.CENTER);
        GridPane.setHalignment(exitButton, HPos.CENTER);

        VBox rootBox = new VBox(40, title, buttonGrid);
        rootBox.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(backgroundView, rootBox);
        Scene scene = new Scene(root, 1080, 700);

        // === Обработчики ===
        startButton.setOnAction(e -> {
            File exe = new File("MyGame.exe");
            if (exe.exists()) {
                try {
                    ProcessBuilder builder = new ProcessBuilder(exe.getAbsolutePath());
                    builder.redirectErrorStream(true);
                    Process process = builder.start();

                    primaryStage.hide();

                    // Запускаем таймер слежения за статусом
                    startStatusFileWatcher(primaryStage);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                System.err.println("MyGame.exe не найден!");
            }
        });

        logoutButton.setOnAction(e -> {
            SessionManager.clearSession();
            primaryStage.close();
            Platform.runLater(() -> {
                try {
                    new RegistrationForm().start(new Stage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        });

        exitButton.setOnAction(e -> System.exit(0));

        backgroundView.fitWidthProperty().bind(scene.widthProperty());
        backgroundView.fitHeightProperty().bind(scene.heightProperty());

        primaryStage.setTitle("Lost Dungeon — Главное меню");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void startStatusFileWatcher(Stage primaryStage) {
        Timer statusTimer = new Timer(true);
        statusTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                File statusFile = new File("game.txt");
                if (statusFile.exists()) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(statusFile))) {
                        String line = reader.readLine();
                        if ("status=request_exit".equals(line)) {
                            // Обновляем статус
                            try (BufferedWriter writer = new BufferedWriter(new FileWriter(statusFile, false))) {
                                writer.write("status=closed");
                            }

                            Platform.runLater(() -> {
                                primaryStage.show();
                                primaryStage.toFront();
                                System.out.println("Unity запросила выход. Главное меню открыто.");
                            });

                            statusTimer.cancel(); // остановим таймер
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 0, 1000); // проверка каждую секунду
    }

    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(240);
        btn.setPrefHeight(50);
        btn.setWrapText(true);
        btn.setStyle("""
                -fx-background-color: linear-gradient(to right, #ffbb33, #ff8800);
                -fx-text-fill: black;
                -fx-font-size: 16px;
                -fx-font-weight: bold;
                -fx-background-radius: 15px;
                -fx-cursor: hand;
            """);

        btn.setOnMouseEntered(e -> btn.setStyle("""
                -fx-background-color: linear-gradient(to right, #ff8800, #ffbb33);
                -fx-text-fill: black;
                -fx-font-size: 16px;
                -fx-font-weight: bold;
                -fx-background-radius: 15px;
                -fx-cursor: hand;
            """));

        btn.setOnMouseExited(e -> btn.setStyle("""
                -fx-background-color: linear-gradient(to right, #ffbb33, #ff8800);
                -fx-text-fill: black;
                -fx-font-size: 16px;
                -fx-font-weight: bold;
                -fx-background-radius: 15px;
                -fx-cursor: hand;
            """));

        return btn;
    }

    public static void main(String[] args) {
        Application.launch(MainMenu.class, args);
    }
}
