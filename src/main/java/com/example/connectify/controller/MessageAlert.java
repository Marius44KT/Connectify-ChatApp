package com.example.connectify.controller;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class MessageAlert
{
    static void showMessage(Stage owner,String text)
    {
        Alert message = new Alert(Alert.AlertType.INFORMATION);
        message.setTitle("Information");
        message.setContentText(text);
        message.initOwner(owner);
        message.showAndWait();
    }
    static void showErrorMessage(Stage owner,String text)
    {
        Alert message=new Alert(Alert.AlertType.ERROR);
        message.initOwner(owner);
        message.setTitle("Mesaj eroare");
        message.setContentText(text);
        message.showAndWait();
    }
}