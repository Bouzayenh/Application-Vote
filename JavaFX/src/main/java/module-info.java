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

    exports controller.communication;
    exports controller.stockage;
    exports controller;
    exports dataobject;
    exports dataobject.exception;
    exports dataobject.paquet;
    exports dataobject.paquet.feedback;

    opens javafx.view to javafx.fxml;
    exports javafx.controller;
    opens javafx.controller to javafx.fxml;
    exports app;
    opens app to javafx.fxml;
}