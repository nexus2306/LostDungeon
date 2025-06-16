package com.example.lostdungeon;

import java.sql.*;

public class DataBase {

    private static final String URL = "jdbc:sqlite:player.db";

    // Создание таблицы
    public static void initialize() {
        try (Connection conn = DriverManager.getConnection(URL)) {
            String sql = """
                CREATE TABLE IF NOT EXISTS players (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL UNIQUE,
                    email TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL,
                    level INTEGER DEFAULT 1,
                    score INTEGER DEFAULT 0
                );
            """;
            conn.createStatement().execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Регистрация нового игрока
    public static boolean register(String name, String email, String password) {
        try (Connection conn = DriverManager.getConnection(URL)) {
            String sql = "INSERT INTO players (name, email, password) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Ошибка при регистрации: " + e.getMessage());
            return false;
        }
    }

    // Проверка входа
    public static boolean checkLogin(String email, String password) {
        try (Connection conn = DriverManager.getConnection(URL)) {
            String sql = "SELECT * FROM players WHERE email = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Получение ID по email
    public static int getPlayerIdByEmail(String email) {
        try (Connection conn = DriverManager.getConnection(URL)) {
            String sql = "SELECT id FROM players WHERE email = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Получить прогресс (уровень) по ID
    public static int getLevel(int playerId) {
        try (Connection conn = DriverManager.getConnection(URL)) {
            String sql = "SELECT level FROM players WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, playerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("level");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    // Получить уровень по email
    public static int getLevelByEmail(String email) {
        try (Connection conn = DriverManager.getConnection(URL)) {
            String sql = "SELECT level FROM players WHERE email = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("level");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    // Получить ник по email
    public static String getPlayerName(String email) {
        try (Connection conn = DriverManager.getConnection(URL)) {
            String sql = "SELECT name FROM players WHERE email = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Обновить прогресс
    public static void updateProgress(int playerId, int newLevel, int newScore) {
        try (Connection conn = DriverManager.getConnection(URL)) {
            String sql = "UPDATE players SET level = ?, score = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, newLevel);
            ps.setInt(2, newScore);
            ps.setInt(3, playerId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
