module com.example.javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;
    requires org.json;
    requires mysql.connector.j;
    requires javafx.graphics;
    requires com.google.api.client.auth;
    requires com.google.api.client.extensions.java6.auth;
    requires com.google.api.client.extensions.jetty.auth;
    requires google.api.client;
    requires com.google.api.client;
    requires com.google.api.client.json.gson;
    requires com.google.api.services.gmail;
    requires org.apache.commons.codec;
    requires mail;

    exports javafx;
    opens javafx to javafx.fxml;
    exports javafx.view;
    opens javafx.view to javafx.fxml;
    exports javafx.controller;
    opens javafx.controller to javafx.fxml;
}