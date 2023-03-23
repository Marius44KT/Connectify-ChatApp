package com.example.connectify.controller;

import com.example.connectify.HelloApplication;
import com.example.connectify.domain.FriendRequest;
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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FriendRequestController implements Observer<UserChangeEvent> {
    @FXML
    private Button acceptFriendRequestButton;
    @FXML
    private Button rejectFriendRequestButton;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<FriendRequest> tableFriendRequestView;
    @FXML
    private TableColumn<FriendRequest,String> tableColumnFirstName;
    @FXML
    private TableColumn<FriendRequest,String> tableColumnLastName;
    @FXML
    private TableColumn<FriendRequest, String> tableColumnDateSent;


    private UserController controller;
    private Service service;
    private User userCurent;
    private ObservableList<FriendRequest> modelFriendRequest=FXCollections.observableArrayList();
    
    public void setController(UserController controller){
        this.controller=controller;
    }
    
    //@FXML
    public void setUserCurent(User userCurent){
        this.userCurent=userCurent;
    }

    public void setFriendRequestService(Service service)
    {
        this.service=service;
        service.addObserver(this);
        initModel();
    }


    public void initialize()
    {
        DateTimeFormatter format=DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        tableColumnFirstName.setCellValueFactory(data->new SimpleStringProperty(data.getValue().getUser1().getFirstName()));
        tableColumnLastName.setCellValueFactory(data->new SimpleStringProperty(data.getValue().getUser1().getLastName()));
        tableColumnDateSent.setCellValueFactory(data->new SimpleStringProperty(data.getValue().getDateTime().format(format)));
        tableFriendRequestView.setItems(modelFriendRequest);
    }


    public void initModel()
    {
        acceptFriendRequestButton.setDisable(true);
        rejectFriendRequestButton.setDisable(true);
        modelFriendRequest.setAll(service.getFriendRequestsReceivedByUser(userCurent.getId()));
    }


    @Override
    public void update(UserChangeEvent userChangeEvent)
    {
        initModel();
        initialize();
    }



    public void handleActivateButtons(MouseEvent actionEvent)
    {
        if(tableFriendRequestView.getSelectionModel().getSelectedItem()!=null)
        {
            acceptFriendRequestButton.setDisable(false);
            rejectFriendRequestButton.setDisable(false);
        }
    }


    public void handleAcceptFriendRequest()
    {
        User user=tableFriendRequestView.getSelectionModel().getSelectedItem().getUser1();
        Long IdUserCurent= userCurent.getId(), IdUser=user.getId();
        service.addFriendship(IdUser,IdUserCurent,LocalDateTime.now());
        UserChangeEvent event=new UserChangeEvent(ChangeEventType.DELETE,user);
        service.notifyObservers(event);
        //update(null);
    }


    public void handleRejectFriendRequest()
    {
        User user=tableFriendRequestView.getSelectionModel().getSelectedItem().getUser1();
        Long IdUserCurent= userCurent.getId(), IdUser=user.getId();
        service.rejectFriendRequest(IdUser,IdUserCurent);
        UserChangeEvent event=new UserChangeEvent(ChangeEventType.DELETE,user);
        service.notifyObservers(event);
    }


//---
    public void handleSearchFriendRequestTextField()
    {
        List<FriendRequest> friendRequestsToShow=new ArrayList<FriendRequest>();
        String nameStr=searchField.getText();
        List<FriendRequest> friendRequests=modelFriendRequest;
        if(nameStr.equals(""))
            initModel();
        else
        {
            for(FriendRequest f:friendRequests)
            {
                if((f.getUser1().getFirstName().contains(nameStr) && !f.getUser1().equals(userCurent)) ||
                        (f.getUser1().getLastName().contains(nameStr) && !f.getUser1().equals(userCurent)))
                    friendRequestsToShow.add(f);
            }
            modelFriendRequest.setAll(friendRequestsToShow);
        }
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
}
