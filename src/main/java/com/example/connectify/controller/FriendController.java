package com.example.connectify.controller;

import com.example.connectify.HelloApplication;
import com.example.connectify.domain.Friendship;
import com.example.connectify.domain.User;
import com.example.connectify.events.ChangeEventType;
import com.example.connectify.events.UserChangeEvent;
import com.example.connectify.observer.Observer;
import com.example.connectify.service.Service;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FriendController implements Observer<UserChangeEvent> {
    @FXML
    private Button removeFriendButton;
    @FXML
    private Button sendMessageButton;
    @FXML
    private TextField searchField;
    @FXML
    private Label nameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private TableView<Friendship> tableFriendView;
    @FXML
    private TableColumn<Friendship,String> tableColumnFirstName;
    @FXML
    private TableColumn<Friendship,String> tableColumnLastName;
    @FXML
    private TableColumn<Friendship, String> tableColumnFriendsSince;


    private UserController controller;
    private Service service;
    private User userCurent;
    private ObservableList<Friendship> modelFriend=FXCollections.observableArrayList();



    public void setController(UserController controller) {
        this.controller=controller;
    }
    //@FXML
    public void setUserCurent(User userCurent)
    {
        this.userCurent=userCurent;
    }

    public void setFriendshipService(Service service)
    {
        this.service=service;
        service.addObserver(this);
        initModel();
    }



    @FXML
    public void initialize()
    {
        DateTimeFormatter format=DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        tableColumnFirstName.setCellValueFactory(data->new SimpleStringProperty(data.getValue().getUser2().getFirstName()));
        tableColumnLastName.setCellValueFactory(data->new SimpleStringProperty(data.getValue().getUser2().getLastName()));
        tableColumnFriendsSince.setCellValueFactory(data->new SimpleStringProperty(data.getValue().getDateTime().format(format)));
        tableFriendView.setItems(modelFriend);
    }


//---
    public void initModel()
    {
        sendMessageButton.setDisable(true);
        removeFriendButton.setDisable(true);
        nameLabel.setText(userCurent.getFirstName()+" "+userCurent.getLastName());
        emailLabel.setText(userCurent.getEmail());

        List<Friendship> friendships=new ArrayList<Friendship>();
        Map<Long,Friendship> friendships1=service.getFriendships();
        if(friendships1!=null)
            for(Map.Entry<Long,Friendship> friendship:friendships1.entrySet())
            {
                if(Objects.equals(friendship.getValue().getUser2().getId(), userCurent.getId()))
                {
                    friendship.getValue().setUser2(friendship.getValue().getUser1());
                    friendship.getValue().setUser1(userCurent);
                }
                if(Objects.equals(friendship.getValue().getUser1().getId(), userCurent.getId()))
                    friendships.add(friendship.getValue());
            }
        modelFriend.setAll(friendships);
    }

    @Override
    public void update(UserChangeEvent userChangeEvent)
    {
        initModel();
        initialize();
    }


//---
    public void handleSearchFriendTextField()
    {
        List<Friendship> friendshipsToShow=new ArrayList<Friendship>();
        String nameStr=searchField.getText();
        List<Friendship> friendships=modelFriend;
        //System.out.println(friendships.size());
        if(nameStr.equals(""))
            initModel();
        else
        {
            for(Friendship f:friendships)
            {
                if(f.getUser2().getFirstName().contains(nameStr) || f.getUser2().getLastName().contains(nameStr))
                    friendshipsToShow.add(f);
            }
            modelFriend.setAll(friendshipsToShow);
        }
    }


    public void handleActivateButtons(MouseEvent actionEvent)
    {
        if(tableFriendView.getSelectionModel().getSelectedItem()!=null)
        {
            sendMessageButton.setDisable(false);
            removeFriendButton.setDisable(false);
        }

    }


    public void handleDeleteFriendButton()
    {
        User user=tableFriendView.getSelectionModel().getSelectedItem().getUser2();
        service.deleteFriendship(user.getId(),userCurent.getId());
        UserChangeEvent event=new UserChangeEvent(ChangeEventType.DELETE,user);
        service.notifyObservers(event);
    }


    public void handleSendMessageButton(ActionEvent actionEvent) throws IOException
    {
        User friend=tableFriendView.getSelectionModel().getSelectedItem().getUser2();
        FXMLLoader fxmlLoader=new FXMLLoader(HelloApplication.class.getResource("views/MessagesView.fxml"));
        Scene scene=new Scene(fxmlLoader.load(),700,450);
        Stage stage=new Stage();
        stage.setTitle("Messages");
        stage.setScene(scene);

        MessageController messageController=fxmlLoader.getController();
        messageController.setUsers(userCurent,friend);
        messageController.setMessageService(service);
        stage.show();
    }


    public void handleHomeButton(ActionEvent actionEvent) throws IOException
    {
        FXMLLoader fxmlLoader=new FXMLLoader(HelloApplication.class.getResource("views/UsersView.fxml"));
        Parent root=fxmlLoader.load();
        UserController userController=fxmlLoader.getController();
        userController.setUserCurent(userCurent);
        userController.setUserService(service);
        service.setScene(root,actionEvent,800,350,"Users");
    }


    
    public void handleLogoutButton(ActionEvent actionEvent) throws IOException
    {
        FXMLLoader fxmlLoader=new FXMLLoader(HelloApplication.class.getResource("views/LogInView.fxml"));
        Parent root=fxmlLoader.load();
        LoginController loginController=fxmlLoader.getController();
        loginController.setUserService(service);
        service.setScene(root,actionEvent,610,400,"Login");
    }
}
