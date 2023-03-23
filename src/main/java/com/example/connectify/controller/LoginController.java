package com.example.connectify.controller;

import com.example.connectify.HelloApplication;
import com.example.connectify.domain.User;
import com.example.connectify.events.UserChangeEvent;
import com.example.connectify.observer.Observer;
import com.example.connectify.service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController implements Observer<UserChangeEvent> {
    Service service;
    private User userCurent;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label warning;

    public void setUserService(Service service)
    {
        this.service=service;
        service.addObserver(this);
    }



    @FXML
    public void initialize()
    {
        warning.setAlignment(Pos.CENTER);
    }

    @Override
    public void update(UserChangeEvent userChangeEvent)
    {
        initialize();
    }


    public void handleLoginButton(ActionEvent actionEvent) throws IOException
    {
        boolean existsEmail=false;
        String email=String.valueOf(emailField.getText());
        String password=String.valueOf(passwordField.getText());
        userCurent=service.getUserByEmailAndPassword(email,password);
        if(userCurent!=null)
        {
                FXMLLoader fxmlLoader=new FXMLLoader(HelloApplication.class.getResource("views/UsersView.fxml"));
                Parent root=fxmlLoader.load();
                UserController userController=fxmlLoader.getController();
                userController.setUserCurent(userCurent);
                userController.setUserService(service);
                service.setScene(root,actionEvent,800,350,"Users");
                return;
        }
        else if(service.getUserByEmail(email)!=null)
                existsEmail=true;
        if(!existsEmail)
        {
            warning.setText("Non-existing user.Please sign up first!");
            warning.setVisible(true);
        }
        else
        {
            warning.setText("Incorrect password!");
            warning.setVisible(true);
        }
    }



    public void handleSignUpButton(ActionEvent actionEvent) throws IOException
    {
        FXMLLoader fxmlLoader=new FXMLLoader(HelloApplication.class.getResource("views/SignUpView.fxml"));
        Parent root=fxmlLoader.load();
        SignUpController signUpController=fxmlLoader.getController();
        signUpController.setUserService(service);
        service.setScene(root,actionEvent,610,525,"SignUp");
    }
}
