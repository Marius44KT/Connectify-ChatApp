package com.example.connectify;

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
import com.example.connectify.ui.UserInterface;

public class Main {
    //static Scanner read=new Scanner(System.in);
    //static Validator<Utilizator> validator=new UtilizatorValidator();
    //static InMemoryRepository<Long,Utilizator> repo_in_memorie=new InMemoryRepository<>(validator);
    public static void main(String[] args)
    {
        Validator<User> validator=new UtilizatorValidator();
        Validator<Friendship> friendship_validator=new FriendshipValidator();
        Validator<FriendRequest> friend_request_validator=new FriendRequestValidator();
        Validator<Message> message_validator=new MessageValidator();
        //static InMemoryUserRepository<Long,User> repo_useri=new InMemoryUserRepository<>(validator);
        //static InMemoryFriendRepository<Long, Friendship> repo_prieteni=new InMemoryFriendRepository<>();


        //UserFileRepository repo_useri=new UserFileRepository("useri.txt",validator);
        //FriendFileRepository repo_prieteni=new FriendFileRepository("prietenii.txt",repo_useri,friendship_validator);


        UserDatabaseRepository repo_useri=new UserDatabaseRepository("jdbc:postgresql://localhost:5432/social_network","postgres","aezakmi",validator);
        FriendshipDatabaseRepository repo_prieteni=new FriendshipDatabaseRepository("jdbc:postgresql://localhost:5432/social_network","postgres","aezakmi",repo_useri,friendship_validator);
        FriendRequestDatabaseRepository repo_cereri_prietenie=new FriendRequestDatabaseRepository("jdbc:postgresql://localhost:5432/social_network","postgres","aezakmi",repo_useri,friend_request_validator);
        MessageDatabaseRepository repo_mesaje=new MessageDatabaseRepository("jdbc:postgresql://localhost:5432/social_network","postgres","aezakmi",repo_useri,message_validator);

        Service service=new Service(validator,repo_useri,repo_prieteni,repo_cereri_prietenie,repo_mesaje);
        UserInterface ui=new UserInterface(repo_useri,repo_prieteni,repo_cereri_prietenie,repo_mesaje,service);
//        User u1 = new User("Marius", "Andreiasi","marius.andreiasi2002@gmail.com","1Aa");
//        u1.setId(1L);  //
        ui.appStart();
    }
}
