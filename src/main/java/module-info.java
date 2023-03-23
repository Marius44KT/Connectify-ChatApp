module com.example.social_network {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.connectify to javafx.fxml;
    opens com.example.connectify.controller to javafx.fxml;

    exports com.example.connectify;
    exports com.example.connectify.domain;
    exports com.example.connectify.controller;
}