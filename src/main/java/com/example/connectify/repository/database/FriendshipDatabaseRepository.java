package com.example.connectify.repository.database;


import com.example.connectify.domain.Friendship;
import com.example.connectify.domain.User;
import com.example.connectify.domain.validators.Validator;
import com.example.connectify.repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FriendshipDatabaseRepository implements Repository<Long, Friendship> {
    private String url;
    private String username;
    private String password;
    UserDatabaseRepository ufr;
    private Validator<Friendship> validator;

    public FriendshipDatabaseRepository(String url, String username, String password, UserDatabaseRepository ufr, Validator<Friendship> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.ufr=ufr;
        this.validator = validator;
    }



    @Override
    public Friendship findOne(Long aLong) {
        return null;
    }



    @Override
    public Map<Long,Friendship> getEntities()
    {
        Map<Long,Friendship> friendships=new HashMap<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement("SELECT * from friendships");
             ResultSet resultSet = ps.executeQuery()) {

            while (resultSet.next()) {
                Long friendship_ID=resultSet.getLong("id");
                Long friend1_ID=resultSet.getLong("friend1id");
                Long friend2_ID=resultSet.getLong("friend2id");
                String str_friends_since=resultSet.getString("datetime");


                Map<Long, User> users=ufr.getEntities();
                User u1=users.get(friend1_ID);
                User u2=users.get(friend2_ID);
                DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                LocalDateTime datetime=LocalDateTime.parse(str_friends_since,formatter);
                Friendship friendship=new Friendship(u1,u2,datetime);
                friendship.setId(friendship_ID);
                friendships.put(friendship_ID,friendship);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }



    @Override
    public Friendship save(Friendship entity) {

        String sql="insert into friendships (id,friend1id,friend2id,datetime) values (?,?,?,?)";
        DateTimeFormatter format=DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setLong(1,entity.getId());
            ps.setLong(2,entity.getUser1().getId());
            ps.setLong(3,entity.getUser2().getId());
            ps.setString(4,entity.getDateTime().format(format));

            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }



    @Override
    public Friendship delete(Long aLong) {
        String sql="delete from friendships where id=?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1,aLong);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    @Override
    public Friendship update(Friendship entity) {
        return null;
    }



    public void deleteAllFriendshipsInvolvingUser(Long idUser) {
        String sql="delete from friendships where friend1id=? or friend2id=?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1,idUser);
            ps.setLong(2,idUser);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public boolean deleteSpecificFriendship(Long ID1,Long ID2)
    {
        String sql="delete from friendships where friend1id=? and friend2id=? or friend1id=? and friend2id=?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1,ID1);
            ps.setLong(2,ID2);
            ps.setLong(3,ID2);
            ps.setLong(4,ID1);
            if(ps.executeUpdate()>0)
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



    public boolean checkIfFriendshipExists(Long ID1,Long ID2)
    {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps= connection.prepareStatement("SELECT * from friendships where friend1id=? and friend2id=? or friend1id=? and friend2id=?"))
             {
                 ps.setLong(1,ID1);
                 ps.setLong(2,ID2);
                 ps.setLong(3,ID2);
                 ps.setLong(4,ID1);
                 ResultSet resultSet=ps.executeQuery();
                 return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



    public Long getNewId()
    {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement("SELECT max(id) from friendships");
             ResultSet resultSet = ps.executeQuery()) {
            if(resultSet.next()) {
                return resultSet.getLong(1)+1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}