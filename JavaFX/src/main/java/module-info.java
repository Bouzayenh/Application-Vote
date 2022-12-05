module com.example.javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;
    requires org.json;

    exports javafx;
    opens javafx to javafx.fxml;
}