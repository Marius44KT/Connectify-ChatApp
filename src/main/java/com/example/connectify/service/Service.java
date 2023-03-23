package com.example.connectify.service;

import com.example.connectify.domain.FriendRequest;
import com.example.connectify.domain.Friendship;
import com.example.connectify.domain.Message;
import com.example.connectify.domain.User;
import com.example.connectify.domain.validators.ValidationException;
import com.example.connectify.domain.validators.Validator;
import com.example.connectify.events.UserChangeEvent;
import com.example.connectify.observer.Observable;
import com.example.connectify.observer.Observer;
import com.example.connectify.repository.database.FriendRequestDatabaseRepository;
import com.example.connectify.repository.database.FriendshipDatabaseRepository;
import com.example.connectify.repository.database.MessageDatabaseRepository;
import com.example.connectify.repository.database.UserDatabaseRepository;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Service implements Observable<UserChangeEvent>
{
    private static Validator<User> validator;
    private static GraphAlgorithms graphs;
    //private static InMemoryUserRepository<Long,User> repo_useri;
    //private static InMemoryFriendRepository<Long,Friendship> repo_prieteni;


    //private static UserFileRepository repo_useri;
    //private static FriendFileRepository repo_prieteni;


    private static UserDatabaseRepository repo_useri;
    private static FriendshipDatabaseRepository repo_prieteni;
    private static FriendRequestDatabaseRepository repo_cereri_prietenie;
    private static MessageDatabaseRepository repo_mesaje;
    private List<Observer<UserChangeEvent>> obs=new ArrayList<>();


    //public Service(Validator<User> validator,InMemoryUserRepository repo_useri,InMemoryFriendRepository<Long,Friendship> repo_prieteni)
    //public Service(Validator<User> validator,UserFileRepository repo_useri, FriendFileRepository repo_prieteni)
    public Service(Validator<User> validator, UserDatabaseRepository repo_useri, FriendshipDatabaseRepository repo_prieteni,
                   FriendRequestDatabaseRepository repo_cereri_prietenie,MessageDatabaseRepository repo_mesaje){
        this.validator=validator;
        this.repo_useri=repo_useri;
        this.repo_prieteni=repo_prieteni;
        this.repo_cereri_prietenie=repo_cereri_prietenie;
        this.repo_mesaje=repo_mesaje;
        this.graphs=new GraphAlgorithms(repo_useri,repo_prieteni);
    }





    public static void printUsers()
    {
        Map<Long,User> utilizatori=repo_useri.getEntities();
        if(repo_useri.getEntities()!=null)
        {
            for (Map.Entry<Long,User> user: utilizatori.entrySet())
                System.out.println(user.getKey()+"; "+user.getValue());
            System.out.println();
        }
        else
            System.out.println("There are no registered users.");
    }

    public static void printFriendships()
    {
        if(repo_prieteni.getEntities()!=null)
        {
            System.out.println("Friendships are:");
            Map<Long,Friendship> prietenii=repo_prieteni.getEntities();
            DateTimeFormatter format=DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            for (Map.Entry<Long, Friendship> friendship : prietenii.entrySet())
            {
                String name1=friendship.getValue().getUser1().getFirstName()+" "+friendship.getValue().getUser1().getLastName();
                String name2=friendship.getValue().getUser2().getFirstName()+" "+friendship.getValue().getUser2().getLastName();
                String data=friendship.getValue().getDateTime().format(format);
                System.out.println(name1+"---"+name2+" ; "+data);
            }
            System.out.println();
        }
        else
            System.out.println("There is no friendship registered.");
    }

    public static void printFriendRequests()
    {
        if(repo_cereri_prietenie.getEntities()!=null)
        {
            System.out.println("Friend requests are:");
            Map<Long,FriendRequest> cereriPrietenie=repo_cereri_prietenie.getEntities();
            DateTimeFormatter format=DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            for (Map.Entry<Long, FriendRequest> friendRequest:cereriPrietenie.entrySet())
            {
                String name1=friendRequest.getValue().getUser1().getFirstName()+" "+friendRequest.getValue().getUser1().getLastName();
                String name2=friendRequest.getValue().getUser2().getFirstName()+" "+friendRequest.getValue().getUser2().getLastName();
                String data=friendRequest.getValue().getDateTime().format(format);
                String status=friendRequest.getValue().getStatus();
                System.out.println(name1+"---"+name2+"---"+data+"; "+status);
            }
            System.out.println();
        }
        else
            System.out.println("There are no friend requests registered.");
    }

    public static void printConversation(long id1,long id2)
    {
        if(repo_mesaje.getEntities()!=null)
        {
            System.out.println("The conversation between the two users is:");
            Map<Long,Message> mesaje=repo_mesaje.getEntities();
            DateTimeFormatter format=DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            for (Map.Entry<Long, Message> mesaj:mesaje.entrySet())
            {
                if((mesaj.getValue().getUser1().getId()==id1 && mesaj.getValue().getUser2().getId()==id2)
                ||(mesaj.getValue().getUser2().getId()==id1 && mesaj.getValue().getUser1().getId()==id2))
                {
                    String message=mesaj.getValue().getMessage();
                    String name1=mesaj.getValue().getUser1().toString();
                    String name2=mesaj.getValue().getUser2().toString();
                    System.out.println(name1+"---"+name2+"---"+message);
                }
            }
        }
        else
        {
            System.out.println("This conversation holds no messages");
        }
    }

    public static void printAll()
    {
        printUsers();
        printFriendships();
        printFriendRequests();
    }








    public static void addUser(String firstname, String lastname,String email,String password)
    {
        User u=new User(firstname,lastname,email,password);
        u.setId(repo_useri.getNewId());
        repo_useri.save(u);
    }


    public static void deleteUselessFriendships(Long ID)
    {
        repo_prieteni.deleteAllFriendshipsInvolvingUser(ID);
    }


    public static void deleteUselessFriendRequests(Long ID)
    {
        repo_cereri_prietenie.deleteAllFriendRequestesInvolvingUser(ID);
    }


    public static void deleteUser(Long ID)
    {
        User deleted_user=repo_useri.findOne(ID);
        if (deleted_user==null)
            throw new ValidationException("User ID is non-existent.");
        deleteUselessFriendships(ID);
        deleteUselessFriendRequests(ID);
        repo_useri.delete(ID);
    }









    public static void checkIdOfEveryUser(Long ID1,Long ID2)
    {
        if(repo_useri.findOne(ID1)==null)
            throw new ValidationException("First user ID is not registered in the database.");
        if(repo_useri.findOne(ID2)==null)
            throw new ValidationException("Second user ID is not registered in the database.");
    }


    public static boolean existsFriendship(Long ID1,Long ID2)
    {
        return repo_prieteni.checkIfFriendshipExists(ID1,ID2);
    }


    public static boolean existsFriendRequest(Long ID1,Long ID2)
    {
        return repo_cereri_prietenie.checkIfFriendshipRequestExists(ID1,ID2);
    }


    public static void sendFriendRequest(Long ID1, Long ID2, LocalDateTime datetime)
    {
        checkIdOfEveryUser(ID1,ID2);
        if(existsFriendRequest(ID1,ID2))
                    throw new ValidationException("This friend request already exists.");
        if(existsFriendship(ID1, ID2))
            throw new ValidationException("These people are friends!");
        Map<Long,User> utilizatori=repo_useri.getEntities();
        User user=utilizatori.get(ID1);
        User friend=utilizatori.get(ID2);
        FriendRequest fr1=new FriendRequest(user,friend,datetime,"pending");
        fr1.setId(repo_cereri_prietenie.getNewId());
        repo_cereri_prietenie.save(fr1);
    }


    public static void deleteFriendRequest(Long ID1,Long ID2)
    {
        boolean found=repo_cereri_prietenie.deleteSpecificFriendRequest(ID1,ID2);
        if(!found)
            throw new ValidationException("Friend request not found!");
    }


    public static void withdrawFriendRequest(Long ID1,Long ID2)
    {
        boolean found=repo_cereri_prietenie.deleteSpecificFriendRequest(ID1,ID2);
        if(!found)
            throw new ValidationException("Friend request was not found!");
    }


    public static void rejectFriendRequest(Long ID1,Long ID2)
    {
        checkIdOfEveryUser(ID1,ID2);
        deleteFriendRequest(ID1,ID2);
    }









    public static void addFriendship(Long ID, Long friendId, LocalDateTime datetime)
    {
        checkIdOfEveryUser(ID,friendId);
        if(!existsFriendRequest(ID,friendId))
            throw new ValidationException("This friend request doesn't exist.");
        deleteFriendRequest(ID,friendId);
        if(existsFriendship(ID,friendId))
            throw new ValidationException("This friendship already exists!");
        Map<Long,User> utilizatori=repo_useri.getEntities();
        User user=utilizatori.get(ID);
        User friend=utilizatori.get(friendId);
        Friendship f1=new Friendship(user,friend,datetime);
        f1.setId(repo_prieteni.getNewId());
        repo_prieteni.save(f1);
    }


    public static void deleteFriendship(Long ID, Long friend_ID)
    {
        boolean found=repo_prieteni.deleteSpecificFriendship(ID,friend_ID);
        if(!found)
            throw new ValidationException("Friendship doesn't exist.");
    }


    public void sendMessage(long userID1,long userID2,String messageText,LocalDateTime datetime)
    {
        Map<Long,User> users=repo_useri.getEntities();
        User u1=users.get(userID1);
        User u2=users.get(userID2);
        Message message=new Message(u1,u2,messageText,datetime);
        message.setId(repo_mesaje.getNewId());
        repo_mesaje.save(message);
    }









    public User getUserByEmail(String email)
    {
        return repo_useri.findOneByEmail(email);
    }

    public User getUserByEmailAndPassword(String email,String password)
    {
        return repo_useri.findOneByEmailAndPassword(email,password);
    }


    public List<User> getTheOtherUsers(Long idUser)
    {
        return repo_useri.getTheOtherUsers(idUser);
    }


    public List<User> getSearchedUsers(String str,Long idUserCurent)
    {
        return repo_useri.getSearchedUsers(str,idUserCurent);
    }

    public Map<Long,User> getUsers()
    {
        return repo_useri.getEntities();
    }



    public Map<Long,Friendship> getFriendships()
    {
        return repo_prieteni.getEntities();
    }


    public Map<Long,FriendRequest> getFriendRequests()
    {
        return repo_cereri_prietenie.getEntities();
    }


    public List<FriendRequest> getFriendRequestsReceivedByUser(Long idUser)
    {
        return repo_cereri_prietenie.getFriendRequestsReceivedByUser(idUser);
    }


    public Map<Long,Message> getMessages(Long userID1,Long userID2)
    {
        return repo_mesaje.getConversation(userID1,userID2);
    }

    public Map<Long,Message> searchMessages(long userID1,long userID2,String messageText)
    {
        return repo_mesaje.getSearchedMessages(userID1,userID2,messageText);
    }










    public static int getNumberOfCommunities()
    {
        return GraphAlgorithms.returnNumberOfCommunities();
    }

    public static String getLargestSocialCommunity()
    {
        return GraphAlgorithms.returnLargestSocialCommunity();
    }


    public void setScene(Parent root, ActionEvent actionEvent, int width, int height, String title) throws IOException
    {
        Stage stage=(Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene=new Scene(root,width,height);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void addObserver(Observer<UserChangeEvent> e)
    {
        obs.add(e);
    }

    @Override
    public void removeObserver(Observer<UserChangeEvent> e)
    {
        obs.remove(e);
    }

    @Override
    public void notifyObservers(UserChangeEvent entity)
    {
        obs.stream().forEach(x->x.update(entity));
    }
}
