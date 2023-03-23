package com.example.connectify.ui;
import com.example.connectify.domain.Message;
import com.example.connectify.domain.User;
import com.example.connectify.domain.validators.ValidationException;
import com.example.connectify.repository.database.FriendRequestDatabaseRepository;
import com.example.connectify.repository.database.FriendshipDatabaseRepository;
import com.example.connectify.repository.database.MessageDatabaseRepository;
import com.example.connectify.repository.database.UserDatabaseRepository;
import com.example.connectify.service.Service;


import java.time.LocalDateTime;
import java.util.*;

public class UserInterface {
    //private static UserFileRepository repo_useri;
    //private static FriendFileRepository repo_prieteni;

    private static UserDatabaseRepository repo_useri;
    private static FriendshipDatabaseRepository repo_prieteni;
    private static FriendRequestDatabaseRepository repo_cereri_prietenie;
    private static MessageDatabaseRepository repo_mesaje;
    private static Service service;
    private static Scanner read;
    //public UserInterface(UserFileRepository repo_useri,FriendFileRepository repo_prieteni,Service service)
    public UserInterface(UserDatabaseRepository repo_useri, FriendshipDatabaseRepository repo_prieteni,
                         FriendRequestDatabaseRepository repo_cereri_prietenie,MessageDatabaseRepository repo_mesaje,
                         Service service)
    {
        this.repo_useri=repo_useri;
        this.repo_prieteni=repo_prieteni;
        this.repo_cereri_prietenie=repo_cereri_prietenie;
        this.repo_mesaje=repo_mesaje;
        this.service=service;
        this.read=new Scanner(System.in);
    }





    public static void appStart() {
        boolean ok=true; String command; int cmd;
        while (ok) {
            System.out.println("Social network menu, please type the ID of the command you wish to execute.");
            System.out.println("0.Print all users registered on the platform");
            System.out.println("1.Print a conversation between two users");
            System.out.println("2.Add a new user on the social platform");
            System.out.println("3.Delete an existing user from the platform");
            System.out.println("4.Send a friend request to someone");
            System.out.println("5.Withdraw a sent friend request");
            System.out.println("6.Accept a friend request");
            System.out.println("7.Reject a friend request");
            System.out.println("8.Remove a friend from a valid user");
            System.out.println("9.Send a message to another user");
            System.out.println("10.Delete a sent message");
            System.out.println("11.Search for a message in conversation");
            System.out.println("12.Show the number of individual communities");
            System.out.println("13.Show the most sociable network(the most widespread)");
            System.out.println("14.Close the application");
            command = read.next();
            try {
                cmd = Integer.parseInt(command);
            } catch (NumberFormatException ex) {
                System.out.println("Error: command key must be a valid number.");
                break;
            }
            switch (cmd) {
                case 0:
                    printAllUi();
                    break;
                case 1:
                    printConversationUi();
                    break;
                case 2:
                    addUserUi();
                    break;
                case 3:
                    deleteUserUi();
                    break;
                case 4:
                    sendFriendRequestUi();
                    break;
                case 5:
                    withdrawFriendRequest();
                    break;
                case 6:
                    addFriendUi();
                    break;
                case 7:
                    rejectFriendRequest();
                    break;
                case 8:
                    deleteFriendUi();
                    break;
                case 9:
                    sendMessage();
                    break;
                case 11:
                    printSearchedMessages();
                    break;
                case 12:
                    printNumberOfCommunities();
                    break;
                case 13:
                    printLargestSocialCommunity();
                    break;
                case 14:
                    System.out.println("Closing application...");
                    ok=false;
                    break;
                default:
                    System.out.println("Invalid command");
                    break;
            }
        }
    }



    public static void printAllUi()
    {
        service.printAll();
    }


    public static void printConversationUi()
    {
        String str_ID; Long ID;
        System.out.println("Introduce user ID:");
        //check if the introduced user ID is a long variable
        try
        {
            str_ID=read.next();
            ID=Long.parseLong(str_ID);
        }
        catch(NumberFormatException ex)
        {
            System.out.println("Error: ID must be a valid long number.");
            return;
        }


        String str_friend_ID; Long friend_ID;
        System.out.println("Introduce friend ID:");
        //check if introduced friend ID is a long variable
        try
        {
            str_friend_ID=read.next();
            friend_ID=Long.parseLong(str_friend_ID);
        }
        catch(NumberFormatException ex)
        {
            System.out.println("Error: ID must be a valid long number.");
            return;
        }
        service.printConversation(ID,friend_ID);
    }


    public static void addUserUi()
    {
        System.out.println("Introduce user firstname:");
        String firstname=read.next();
        System.out.println("Introduce user lastname:");
        String lastname=read.next();
        System.out.println("Introduce user email:");
        String email=read.next();
        System.out.println("Introduce user password:");
        String password=read.next();
        try
        {
            service.addUser(firstname,lastname,email,password);
            System.out.println("User "+ firstname+" "+lastname+" added successfully!");
        }
        catch(ValidationException ex)
        {
            System.out.println(ex.getMessage());
        }
    }








    public static void deleteUserUi()
    {
        String str_ID; Long ID;
        System.out.println("Introduce user ID:");
        //check if the introduced user ID is a long variable
        try
        {
            str_ID=read.next();
            ID=Long.parseLong(str_ID);
        }
        catch(NumberFormatException ex)
        {
            System.out.println("Error: ID must be a valid long number.");
            return;
        }
        try
        {
            service.deleteUser(ID);
            System.out.println("User with ID " + String.valueOf(ID) + " deleted successfully!");
        }
        catch(ValidationException ex)
        {
            System.out.println(ex.getMessage());
        }
    }








    public static void sendFriendRequestUi()
    {
        String str_ID; Long ID;
        System.out.println("Introduce user ID:");
        //check if the introduced user ID is a long variable
        try
        {
            str_ID=read.next();
            ID=Long.parseLong(str_ID);
        }
        catch(NumberFormatException ex)
        {
            System.out.println("Error: ID must be a valid long number.");
            return;
        }


        String str_friend_ID; Long friend_ID;
        System.out.println("Introduce friend ID:");
        //check if introduced friend ID is a long variable
        try
        {
            str_friend_ID=read.next();
            friend_ID=Long.parseLong(str_friend_ID);
        }
        catch(NumberFormatException ex)
        {
            System.out.println("Error: ID must be a valid long number.");
            return;
        }
        try
        {
            LocalDateTime datetime=LocalDateTime.now();
            service.sendFriendRequest(ID,friend_ID,datetime);
            System.out.println("Friend request succesfully sent!");
        }
        catch(ValidationException ex)
        {
            System.out.println(ex.getMessage());
        }
    }








    public static void rejectFriendRequest()
    {
        String str_ID; Long ID1; Long ID2;
        System.out.println("Introduce user ID:");
        //check if the introduced user ID is a long variable
        try
        {
            str_ID=read.next();
            ID1=Long.parseLong(str_ID);
        }
        catch(NumberFormatException ex)
        {
            System.out.println("Error: ID must be a valid long number.");
            return;
        }


        System.out.println("Introduce friend ID:");
        //check if introduced friend ID is a long variable
        try
        {
            str_ID=read.next();
            ID2=Long.parseLong(str_ID);
        }
        catch(NumberFormatException ex)
        {
            System.out.println("Error: ID must be a valid long number.");
            return;
        }
        try
        {
            service.rejectFriendRequest(ID1,ID2);
            System.out.println("Friend request successfully rejected!");
        }
        catch(ValidationException ex)
        {
            System.out.println(ex.getMessage());
        }
    }








    public static void withdrawFriendRequest()
    {
        String str_ID; Long ID1; Long ID2;
        System.out.println("Introduce user ID:");
        //check if the introduced user ID is a long variable
        try
        {
            str_ID=read.next();
            ID1=Long.parseLong(str_ID);
        }
        catch(NumberFormatException ex)
        {
            System.out.println("Error: ID must be a valid long number.");
            return;
        }


        System.out.println("Introduce friend ID:");
        //check if introduced friend ID is a long variable
        try
        {
            str_ID=read.next();
            ID2=Long.parseLong(str_ID);
        }
        catch(NumberFormatException ex)
        {
            System.out.println("Error: ID must be a valid long number.");
            return;
        }
        try
        {
            service.withdrawFriendRequest(ID1,ID2);
            System.out.println("Friend request successfully withdrawn!");
        }
        catch(ValidationException ex)
        {
            System.out.println(ex.getMessage());
        }
    }








    public static void addFriendUi()
    {
        String str_ID; Long ID;
        System.out.println("Introduce user ID:");
        //check if the introduced user ID is a long variable
        try
        {
            str_ID=read.next();
            ID=Long.parseLong(str_ID);
        }
        catch(NumberFormatException ex)
        {
            System.out.println("Error: ID must be a valid long number.");
            return;
        }


        String str_friend_ID; Long friend_ID;
        System.out.println("Introduce friend ID:");
        //check if introduced friend ID is a long variable
        try
        {
            str_friend_ID=read.next();
            friend_ID=Long.parseLong(str_friend_ID);
        }
        catch(NumberFormatException ex)
        {
            System.out.println("Error: ID must be a valid long number.");
            return;
        }
        try
        {
            LocalDateTime datetime=LocalDateTime.now();
            service.addFriendship(ID,friend_ID,datetime);
            System.out.println("Friendship succesfully added!");
        }
        catch(ValidationException ex)
        {
            System.out.println(ex.getMessage());
        }
    }








    public static void deleteFriendUi()
    {
        Map<Long,User> utilizatori=repo_useri.getEntities();
        String str_ID; Long ID;
        System.out.println("Introduce user ID:");
        //check if the introduced user ID is a long variable
        try
        {
            str_ID=read.next();
            ID=Long.parseLong(str_ID);
        }
        catch(NumberFormatException ex)
        {
            System.out.println("Error: ID must be a valid long number.");
            return;
        }

        String str_friend_ID; Long friend_ID;
        System.out.println("Introduce friend ID:");
        //check if the introduced friend ID is a long variable
        try
        {
            str_friend_ID=read.next();
            friend_ID=Long.parseLong(str_friend_ID);
        }
        catch(NumberFormatException ex)
        {
            System.out.println("Error: ID must be a valid long number.");
            return;
        }
        try
        {
            service.deleteFriendship(ID,friend_ID);
            System.out.println("Friendship successfully deleted!");
        }
        catch(ValidationException ex)
        {
            System.out.println(ex.getMessage());
        }
    }



    public static void sendMessage()
    {
        String str_ID; Long ID;
        System.out.println("Introduce user ID:");
        //check if the introduced user ID is a long variable
        try
        {
            str_ID=read.next();
            ID=Long.parseLong(str_ID);
        }
        catch(NumberFormatException ex)
        {
            System.out.println("Error: ID must be a valid long number.");
            return;
        }


        String str_friend_ID; Long friend_ID;
        System.out.println("Introduce friend ID:");
        //check if introduced friend ID is a long variable
        try
        {
            str_friend_ID=read.next();
            friend_ID=Long.parseLong(str_friend_ID);
        }
        catch(NumberFormatException ex)
        {
            System.out.println("Error: ID must be a valid long number.");
            return;
        }
        String message="";
        System.out.println("Enter the message");
        message=read.nextLine();
        try
        {
            LocalDateTime datetime=LocalDateTime.now();
            service.sendMessage(ID,friend_ID,message,datetime);
            System.out.println("Message succesfully sent!");
        }
        catch(ValidationException ex)
        {
            System.out.println(ex.getMessage());
        }
    }


    public static void printSearchedMessages()
    {
        String str_ID; Long ID;
        System.out.println("Introduce user ID:");
        //check if the introduced user ID is a long variable
        try
        {
            str_ID=read.next();
            ID=Long.parseLong(str_ID);
        }
        catch(NumberFormatException ex)
        {
            System.out.println("Error: ID must be a valid long number.");
            return;
        }


        String str_friend_ID; Long friend_ID;
        System.out.println("Introduce friend ID:");
        //check if introduced friend ID is a long variable
        try
        {
            str_friend_ID=read.next();
            friend_ID=Long.parseLong(str_friend_ID);
        }
        catch(NumberFormatException ex)
        {
            System.out.println("Error: ID must be a valid long number.");
            return;
        }
        String message;
        System.out.println("Enter the message:");
        read.nextLine();
        message=read.nextLine();
        Map<Long,Message> messages;
        messages=service.searchMessages(ID,friend_ID,message);
//        for(Message m:messages)
//            System.out.println(m.getMessage());
    }




    public static void printNumberOfCommunities()
    {
        int nrc=service.getNumberOfCommunities();
        System.out.println("There are "+Integer.toString(nrc)+" communities.");
    }



    public static void printLargestSocialCommunity()
    {
        String social_network=service.getLargestSocialCommunity();
        System.out.println("Here is a list of the largest social community members:");
        System.out.println(social_network);
    }
}
