package com.example.connectify.service;

import com.example.connectify.domain.Friendship;
import com.example.connectify.domain.User;
import com.example.connectify.repository.database.FriendshipDatabaseRepository;
import com.example.connectify.repository.database.UserDatabaseRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphAlgorithms {
    private static UserDatabaseRepository repo_useri;
    private static FriendshipDatabaseRepository repo_prieteni;
    static Map<Long,Integer> viz=new HashMap<Long,Integer>();
    static int maxlen;
    static String users="";
    public GraphAlgorithms(UserDatabaseRepository repo_useri, FriendshipDatabaseRepository repo_prieteni)
    {
        this.repo_useri=repo_useri;
        this.repo_prieteni=repo_prieteni;
    }






    public static void DFS(Long x)
    {
        viz.put(x,1);
        User user=repo_useri.getEntities().get(x);
        List<User> lista_prieteni=new ArrayList<User>();
        Map<Long,Friendship> friendships=repo_prieteni.getEntities();
        if(friendships!=null)
        {
            for (Map.Entry<Long, Friendship> friendship : friendships.entrySet())
                if (friendship.getValue().getUser1().getId() == x)
                    lista_prieteni.add(friendship.getValue().getUser2());
                else if (friendship.getValue().getUser2().getId() == x)
                    lista_prieteni.add(friendship.getValue().getUser1());


            for (User prieten : lista_prieteni)
                if (viz.get(prieten.getId()) != 1)
                    DFS(prieten.getId());
        }
    }


    public static int returnNumberOfCommunities()
    {
        int nrc=0;
        for (Map.Entry<Long, User> user: repo_useri.getEntities().entrySet())
            viz.put(user.getKey(),0);
        for (Map.Entry<Long,User> user: repo_useri.getEntities().entrySet())
            if(viz.get(user.getKey())!=1)
            {
                DFS(user.getKey());
                nrc++;
            }
        return nrc;
    }




    public static void DFS_updated(Long x,int len,String names)
    {
        viz.put(x,1);
        User user=repo_useri.getEntities().get(x);
        len=len+1;
        names=names+user.getFirstName()+" "+user.getLastName()+",";
        if(len>maxlen)
        {
            maxlen=len;
            users=names;
        }
        List<User> lista_prieteni=new ArrayList<User>();
        Map<Long, Friendship> friendships=repo_prieteni.getEntities();
        if(friendships!=null)
        {
            for (Map.Entry<Long, Friendship> friendship : friendships.entrySet())
                if (friendship.getValue().getUser1().getId() == x)
                    lista_prieteni.add(friendship.getValue().getUser2());
                else if (friendship.getValue().getUser2().getId() == x)
                    lista_prieteni.add(friendship.getValue().getUser1());

            for (User prieten : lista_prieteni)
                if (viz.get(prieten.getId()) != 1)
                    DFS_updated(prieten.getId(), len, names);
        }
    }



    public static String returnLargestSocialCommunity()
    {
        for (Map.Entry<Long,User> user: repo_useri.getEntities().entrySet())
        {
            for (Map.Entry<Long,User> u: repo_useri.getEntities().entrySet())
                viz.put(u.getKey(),0);
            DFS_updated(user.getKey(),0,"");
        }
        return users;
    }
}
