module com.example.lostdungeon {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.prefs;


    opens com.example.lostdungeon to javafx.fxml;
    exports com.example.lostdungeon;
}