package com.example.connectify.controller;

import com.example.connectify.HelloApplication;
import com.example.connectify.domain.User;
import com.example.connectify.events.UserChangeEvent;
import com.example.connectify.observer.Observer;
import com.example.connectify.service.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.time.LocalDateTime;

public class UserController implements Observer<UserChangeEvent> {
    @FXML
    private Button sendFriendRequestButton;
    @FXML
    private Button withdrawFriendRequestButton;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<User> tableUserView;
    @FXML
    private TableColumn<User,String> tableColumnFirstName;
    @FXML
    private TableColumn<User,String> tableColumnLastName;
    @FXML
    private TableColumn<User,String> tableColumnEmail;



    private Service service;
    private User userCurent;
    private ObservableList<User> modelUser=FXCollections.observableArrayList();



    @FXML
    public void setUserCurent(User userCurent)
    {
        this.userCurent=userCurent;
    }

    public void setUserService(Service service)
    {
        this.service=service;
        service.addObserver(this);
        initModel();
    }

    @FXML
    public void initialize()
    {
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<User,String>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<User,String>("lastName"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<User,String>("email"));
        tableUserView.setItems(modelUser);
    }


    public void initModel()
    {
        sendFriendRequestButton.setDisable(true);
        withdrawFriendRequestButton.setDisable(true);
        modelUser.setAll(service.getTheOtherUsers(userCurent.getId()));
    }



    @Override
    public void update(UserChangeEvent userChangeEvent)
    {
        initModel();
        initialize();
    }



    public void handleActivateButtons(MouseEvent actionEvent)
    {
        if(tableUserView.getSelectionModel().getSelectedItem()!=null)
        {
            sendFriendRequestButton.setDisable(false);
            withdrawFriendRequestButton.setDisable(false);
        }
    }



    public void handleAddFriend(ActionEvent actionEvent)
    {
        User user=(User)tableUserView.getSelectionModel().getSelectedItem();
        if(service.existsFriendRequest(userCurent.getId(),user.getId()))
        {
            MessageAlert.showErrorMessage(null,"Friend request already sent.");
            sendFriendRequestButton.setDisable(true);
            withdrawFriendRequestButton.setDisable(true);
        }
        else if(service.existsFriendRequest(user.getId(),userCurent.getId()))
        {
            MessageAlert.showErrorMessage(null,"This person has already sent you a friend request.");
            sendFriendRequestButton.setDisable(true);
            withdrawFriendRequestButton.setDisable(true);
        }
        else if(service.existsFriendship(userCurent.getId(),user.getId()))
        {
            MessageAlert.showErrorMessage(null,"You are already friends with this user.");
            sendFriendRequestButton.setDisable(true);
            withdrawFriendRequestButton.setDisable(true);
        }
        else
        {
        service.sendFriendRequest(userCurent.getId(),user.getId(),LocalDateTime.now());
        MessageAlert.showMessage(null,"Friendship request successfully sent!");
        update(null);
        }
    }



    public void handleWithdrawFriendRequest(ActionEvent actionEvent)
    {
        User user=(User)tableUserView.getSelectionModel().getSelectedItem();
        if(!service.existsFriendRequest(userCurent.getId(),user.getId()))
        {
            MessageAlert.showErrorMessage(null,"No friendship request was sent to this user.");
            withdrawFriendRequestButton.setDisable(true);
            sendFriendRequestButton.setDisable(true);
        }
        else if(service.existsFriendship(userCurent.getId(),user.getId()))
        {
            MessageAlert.showErrorMessage(null,"You are already friends with this user.");
            withdrawFriendRequestButton.setDisable(true);
            sendFriendRequestButton.setDisable(true);
        }
        else
        {
            service.withdrawFriendRequest(userCurent.getId(),user.getId());
            MessageAlert.showMessage(null,"Friendship request successfully withdrawn!");
            update(null);
        }
    }



    public void handleSearchUserTextField()
    {
        String nameStr=searchField.getText();
        modelUser.setAll(service.getSearchedUsers(nameStr,userCurent.getId()));
    }


    public void handleMyAccountButton(ActionEvent actionEvent) throws IOException
    {
        FXMLLoader fxmlLoader=new FXMLLoader(HelloApplication.class.getResource("views/FriendsView.fxml"));
        Parent root=fxmlLoader.load();
        FriendController friendController=fxmlLoader.getController();
        friendController.setUserCurent(userCurent);
        friendController.setFriendshipService(service);
        service.setScene(root,actionEvent,700,550,"MyAccount");
    }


    public void handleFriendRequestsButton(ActionEvent actionEvent) throws IOException
    {
        FXMLLoader fxmlLoader=new FXMLLoader(HelloApplication.class.getResource("views/FriendRequestsView.fxml"));
        Parent root=fxmlLoader.load();
        FriendRequestController friendRequestController=fxmlLoader.getController();
        friendRequestController.setUserCurent(userCurent);
        friendRequestController.setFriendRequestService(service);
        service.setScene(root,actionEvent,725,325,"Friend requests");
    }
}
