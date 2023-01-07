module com.example.javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;
    requires org.json;
    requires mysql.connector.j;
    requires javafx.graphics;
    requires java.mail;

    exports javafx.view;
    opens javafx.view to javafx.fxml;
    exports javafx.controller;
    opens javafx.controller to javafx.fxml;
    exports app;
    opens app to javafx.fxml;
}