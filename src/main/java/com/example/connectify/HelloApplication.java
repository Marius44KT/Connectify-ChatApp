package com.example.connectify;

import com.example.connectify.controller.LoginController;
import com.example.connectify.domain.FriendRequest;
import com.example.connectify.domain.Friendship;
import com.example.connectify.domain.Message;
import com.example.connectify.domain.User;
import com.example.connectify.domain.validators.*;
import com.example.connectify.repository.database.FriendRequestDatabaseRepository;
import com.example.connectify.repository.database.FriendshipDatabaseRepository;
import com.example.connectify.repository.database.MessageDatabaseRepository;
import com.example.connectify.repository.database.UserDatabaseRepository;
import com.example.connectify.service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    Validator<User> validator=new UtilizatorValidator();
    Validator<Friendship> friendship_validator=new FriendshipValidator();
    Validator<FriendRequest> friend_request_validator=new FriendRequestValidator();
    Validator<Message> message_validator=new MessageValidator();
    UserDatabaseRepository repo_useri=new UserDatabaseRepository("jdbc:postgresql://localhost:5432/social_network","postgres","aezakmi",validator);
    FriendshipDatabaseRepository repo_prieteni=new FriendshipDatabaseRepository("jdbc:postgresql://localhost:5432/social_network","postgres","aezakmi",repo_useri,friendship_validator);
    FriendRequestDatabaseRepository repo_cereri_prietenie=new FriendRequestDatabaseRepository("jdbc:postgresql://localhost:5432/social_network","postgres","aezakmi",repo_useri,friend_request_validator);
    MessageDatabaseRepository repo_mesaje=new MessageDatabaseRepository("jdbc:postgresql://localhost:5432/social_network","postgres","aezakmi",repo_useri,message_validator);
    Service service=new Service(validator,repo_useri,repo_prieteni,repo_cereri_prietenie,repo_mesaje);


    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException
    {
        initView(stage);
        stage.setResizable(false);
        stage.setTitle("Login");
        stage.show();
    }

    private void initView(Stage primaryStage) throws IOException
    {
        FXMLLoader fxmllLoader=new FXMLLoader(HelloApplication.class.getResource("views/LogInView.fxml"));
        Pane userLayout=(Pane) fxmllLoader.load();
        primaryStage.setScene(new Scene(userLayout,610,400));

        LoginController loginController= fxmllLoader.getController();
        loginController.setUserService(service);
    }
}