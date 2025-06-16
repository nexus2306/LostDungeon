package com.example.lostdungeon;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.io.*;

public class RegistrationForm extends Application {

    private static final String SAVE_FILE = "current_user.txt";

    @Override
    public void start(Stage primaryStage) {
        // Если уже есть сохранённый email, сразу в главное меню
        String savedEmail = getSavedEmail();
        if (savedEmail != null) {
            openMainMenu(primaryStage);
            return;
        }

        Label title = new Label("Регистрация");
        title.setFont(Font.font("Georgia", FontWeight.EXTRA_BOLD, 36));
        title.setTextFill(Color.web("#f4c069"));
        title.setEffect(new DropShadow(10, Color.web("#ff8c00")));

        TextField nameField = createStyledTextField("Имя");
        TextField emailField = createStyledTextField("Email");
        PasswordField passwordField = createStyledPasswordField("Пароль");

        Button registerButton = new Button("Зарегистрироваться");
        registerButton.setPrefWidth(240);
        registerButton.setStyle(
                "-fx-background-color: linear-gradient(to right, #ffbb33, #ff8800);" +
                        "-fx-text-fill: black;" +
                        "-fx-font-size: 16px;" +
                        "-fx-background-radius: 15px;" +
                        "-fx-cursor: hand;"
        );

        Button loginButton = new Button("Уже есть аккаунт? Войти");
        loginButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #ffbb33; -fx-underline: true;");
        loginButton.setOnAction(e -> showLoginWindow(primaryStage));

        registerButton.setOnAction(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Пожалуйста, заполните все поля.");
                return;
            }

            if (!(email.endsWith("@gmail.com") || email.endsWith("@mail.ru"))) {
                showAlert(Alert.AlertType.ERROR, "Введите корректный email: @gmail.com или @mail.ru");
                return;
            }

            if (DataBase.register(name, email, password)) {
                SessionManager.setCurrentEmail(email); //  Добавлено: сохраняем текущий email
                openMainMenu(primaryStage);
            } else {
                showAlert(Alert.AlertType.ERROR, "Пользователь с таким email уже существует.");
            }
        });

        VBox form = new VBox(20, title, nameField, emailField, passwordField, registerButton, loginButton);
        form.setPadding(new Insets(30));
        form.setAlignment(Pos.CENTER);
        form.setStyle("-fx-background-color: #2c2c2c; -fx-border-color: #f4c069; -fx-border-width: 2px;");
        form.setEffect(new DropShadow(25, Color.BLACK));

        StackPane root = new StackPane(form);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #1a1a1a, #0d0d0d);");
        StackPane.setAlignment(form, Pos.CENTER);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Lost Dungeon — Регистрация");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showLoginWindow(Stage primaryStage) {
        Label loginTitle = new Label("Вход в аккаунт");
        loginTitle.setFont(Font.font("Georgia", FontWeight.EXTRA_BOLD, 28));
        loginTitle.setTextFill(Color.web("#f4c069"));
        loginTitle.setEffect(new DropShadow(10, Color.web("#ff8c00")));

        TextField emailField = createStyledTextField("Email");
        PasswordField passwordField = createStyledPasswordField("Пароль");

        Button login = new Button("Войти");
        login.setPrefWidth(240);
        login.setStyle(
                "-fx-background-color: linear-gradient(to right, #ffbb33, #ff8800);" + "-fx-text-fill: black;" + "-fx-font-size: 16px;" + "-fx-background-radius: 15px;" + "-fx-cursor: hand;"
        );

        Button backButton = new Button("\u2190 Назад");
        backButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #cccccc; -fx-underline: true;");
        backButton.setOnAction(ev -> start(primaryStage));

        login.setOnAction(e -> {
            String email = emailField.getText();
            String password = passwordField.getText();

            if (DataBase.checkLogin(email, password)) {
                saveEmailToFile(email);
                openMainMenu(primaryStage);
            } else {
                showAlert(Alert.AlertType.ERROR, "Неверный email или пароль.");
            }
        });

        VBox form = new VBox(15, loginTitle, emailField, passwordField, login, backButton);
        form.setPadding(new Insets(30));
        form.setAlignment(Pos.CENTER);
        form.setStyle("-fx-background-color: #2c2c2c; -fx-border-color: #f4c069; -fx-border-width: 2px;");
        form.setEffect(new DropShadow(25, Color.BLACK));

        StackPane root = new StackPane(form);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #1a1a1a, #0d0d0d);");

        Scene loginScene = new Scene(root, 1080, 700);
        primaryStage.setScene(loginScene);
    }

    private void openMainMenu(Stage stage) {
        try {
            MainMenu menu = new MainMenu();
            menu.start(stage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.showAndWait();
    }

    private TextField createStyledTextField(String placeholder) {
        TextField tf = new TextField();
        tf.setPromptText(placeholder);
        tf.setMaxWidth(240);
        tf.setStyle(
                "-fx-background-color: #3b3b3b;" +
                        "-fx-text-fill: #f4f4f4;" +
                        "-fx-background-radius: 5px;" +
                        "-fx-border-color: #666;" +
                        "-fx-font-size: 14px;" +
                        "-fx-padding: 10px;"
        );
        return tf;
    }

    private PasswordField createStyledPasswordField(String placeholder) {
        PasswordField pf = new PasswordField();
        pf.setPromptText(placeholder);
        pf.setMaxWidth(240);
        pf.setStyle(
                "-fx-background-color: #3b3b3b;" +
                        "-fx-text-fill: #f4f4f4;" +
                        "-fx-background-radius: 5px;" +
                        "-fx-border-color: #666;" +
                        "-fx-font-size: 14px;" +
                        "-fx-padding: 10px;"
        );
        return pf;
    }

    private void saveEmailToFile(String email) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SAVE_FILE))) {
            writer.write(email);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getSavedEmail() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) return null;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void clearSavedEmail() {
        File file = new File(SAVE_FILE);
        if (file.exists()) file.delete();
    }

    public static void main(String[] args) {
        DataBase.initialize();
        launch(args);
    }
}
