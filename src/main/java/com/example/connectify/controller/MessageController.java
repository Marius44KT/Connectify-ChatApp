package com.example.connectify.controller;

import com.example.connectify.domain.Message;
import com.example.connectify.domain.User;
import com.example.connectify.events.UserChangeEvent;
import com.example.connectify.observer.Observer;
import com.example.connectify.service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

public class MessageController implements Observer<UserChangeEvent>{
    @FXML
    private TextField searchMessageField;
    @FXML
    private TextField sendMessageField;
    @FXML
    private VBox messagesBox;
    @FXML
    private Label friendNameLabel;



    private Service service;
    private User userC;
    private User prieten;


    @FXML
    public void setUsers(User userC,User prieten){
        this.userC=userC;
        this.prieten=prieten;
    }


    @FXML
    public void setMessageService(Service service)
    {
        this.service=service;
        service.addObserver(this);

        String friendName=prieten.getFirstName()+" "+prieten.getLastName();
        friendNameLabel.setText(friendName);
        initModel();
        getConversationBetweenUsers();
    }


    @FXML
    public void initialize() {
        initModel();
    }


    public void initModel()
    {
        TextFormatter<String> textFormatter=new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 50 ? change : null);
        sendMessageField.setTextFormatter(textFormatter);
    }


    public void sendMessage(ActionEvent actionEvent) throws IOException {
        Label label=new Label(sendMessageField.getText());
        HBox layer=new HBox();
        label.setStyle("-fx-label-padding: 5px; -fx-background-radius: 15 0 15 15 ;-fx-background-color: #16FF00;");
        layer.setAlignment(Pos.CENTER_RIGHT);
        layer.getChildren().add(label);
        messagesBox.getChildren().add(layer);

        service.sendMessage(userC.getId(),prieten.getId(),sendMessageField.getText(),LocalDateTime.now());
        sendMessageField.setText("");
    }


    public void getConversationBetweenUsers() {
        Map<Long,Message> messages=service.getMessages(userC.getId(),prieten.getId());
        showMessages(messages);
    }


    public void getSearchedMessages(ActionEvent actionEvent) throws IOException
    {
        String string=searchMessageField.getText();
        Map<Long,Message> messages=service.searchMessages(userC.getId(),prieten.getId(),string);
        messagesBox.getChildren().clear();
        showMessages(messages);
    }


    public void showMessages(Map<Long,Message> messages)
    {
        for (Map.Entry<Long,Message> mesaj: messages.entrySet())
        {
            Label label=new Label(mesaj.getValue().getMessage());
            HBox layer=new HBox();

            if (mesaj.getValue().getUser1().equals(userC) && mesaj.getValue().getMessage().length() > 0) {
                label.setStyle("-fx-label-padding: 5px; -fx-background-radius: 15 0 15 15 ;-fx-background-color: #16FF00;");
                layer.setAlignment(Pos.CENTER_RIGHT);
                layer.getChildren().add(label);
                messagesBox.getChildren().add(layer);

            } else if (mesaj.getValue().getUser2().equals(userC) && mesaj.getValue().getMessage().length() > 0) {
                label.setStyle("-fx-label-padding: 5px; -fx-background-radius: 0 15 15 15 ;-fx-background-color: #16FF00;");
                layer.setAlignment(Pos.CENTER_LEFT);
                layer.getChildren().add(label);
                messagesBox.getChildren().add(layer);
            }
        }
    }


    @Override
    public void update(UserChangeEvent userChangeEvent)
    {
        initModel();
    }
}
