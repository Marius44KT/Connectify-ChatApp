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
import java.util.Objects;

public class SignUpController implements Observer<UserChangeEvent> {
    Service service;
    private User userCurent;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
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
        FXMLLoader fxmlLoader=new FXMLLoader(HelloApplication.class.getResource("views/LogInView.fxml"));
        Parent root=fxmlLoader.load();
        LoginController loginController=fxmlLoader.getController();
        loginController.setUserService(service);
        service.setScene(root,actionEvent,610,400,"Login");
    }



    public void handleSignUpButton(ActionEvent actionEvent) throws IOException
    {
        String firstName=String.valueOf(firstNameField.getText());
        String lastName=String.valueOf(lastNameField.getText());
        String email=String.valueOf(emailField.getText());
        String password=String.valueOf(passwordField.getText());
        if(Objects.equals(firstName, "") || Objects.equals(lastName, "") || Objects.equals(email, "") ||
                Objects.equals(password, ""))
        {
            warning.setText("Empty fields detected!");
            warning.setVisible(true);
            return;
        }
        if(service.getUserByEmail(email)!=null)
        {
            warning.setText("Email already registered!");
            warning.setVisible(true);
        }
        else
        {
            service.addUser(firstName,lastName,email,password);
            userCurent=service.getUserByEmail(email);

            FXMLLoader fxmlLoader=new FXMLLoader(HelloApplication.class.getResource("views/UsersView.fxml"));
            Parent root=fxmlLoader.load();
            UserController userController=fxmlLoader.getController();
            userController.setUserCurent(userCurent);
            userController.setUserService(service);
            service.setScene(root,actionEvent,800,350,"Users");
        }
    }
}



//            FXMLLoader fxmlLoader=new FXMLLoader(HelloApplication.class.getResource("views/UsersView.fxml"));
//            Scene scene=new Scene(fxmlLoader.load(),800,350);
//            Stage stage=new Stage();
//            stage.setTitle("Users");
//            stage.setScene(scene);
//            stage.setResizable(false);
//            stage.show();
//            UserController userController=fxmlLoader.getController();
//            userController.setUserCurent(userCurent);
//            userController.setUserService(service);
//            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

