module com.example.javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;
    requires org.json;

    exports javafx;
    opens javafx to javafx.fxml;
    exports javafx.view;
    opens javafx.view to javafx.fxml;
    exports javafx.controller;
    opens javafx.controller to javafx.fxml;
}