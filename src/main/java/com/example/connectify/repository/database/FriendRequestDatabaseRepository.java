package com.example.connectify.repository.database;



import com.example.connectify.domain.FriendRequest;
import com.example.connectify.domain.User;
import com.example.connectify.domain.validators.Validator;
import com.example.connectify.repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FriendRequestDatabaseRepository implements Repository<Long, FriendRequest> {
    private String url;
    private String username;
    private String password;
    UserDatabaseRepository ufr;
    private Validator<FriendRequest> validator;

    public FriendRequestDatabaseRepository(String url, String username, String password, UserDatabaseRepository ufr, Validator<FriendRequest> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.ufr=ufr;
        this.validator = validator;
    }



    @Override
    public FriendRequest findOne(Long aLong) {
        return null;
    }



    @Override
    public Map<Long,FriendRequest> getEntities()
    {
        Map<Long,FriendRequest> friendRequests=new HashMap<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement("SELECT * from requests");
             ResultSet resultSet = ps.executeQuery()) {

            while (resultSet.next()) {
                Long friendRequest_ID=resultSet.getLong("id");
                Long friend1_ID=resultSet.getLong("user1id");
                Long friend2_ID=resultSet.getLong("user2id");
                String strSentSince=resultSet.getString("datetime");
                String status=resultSet.getString("status");

                Map<Long, User> users=ufr.getEntities();
                User u1=users.get(friend1_ID);
                User u2=users.get(friend2_ID);
                DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                LocalDateTime datetime=LocalDateTime.parse(strSentSince,formatter);
                FriendRequest friendRequest1=new FriendRequest(u1,u2,datetime,status);
                friendRequest1.setId(friendRequest_ID);
                friendRequests.put(friendRequest_ID,friendRequest1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendRequests;
    }



    @Override
    public FriendRequest save(FriendRequest entity) {

        String sql="insert into requests (id,user1id,user2id,datetime,status) values (?,?,?,?,?)";
        DateTimeFormatter format=DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        try (Connection connection=DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setLong(1,entity.getId());
            ps.setLong(2,entity.getUser1().getId());
            ps.setLong(3,entity.getUser2().getId());
            ps.setString(4,entity.getDateTime().format(format));
            ps.setString(5,entity.getStatus());
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }



    @Override
    public FriendRequest delete(Long aLong) {
        String sql="delete from requests where id=?";
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
    public FriendRequest update(FriendRequest entity) {
        return null;
    }



    public void deleteAllFriendRequestesInvolvingUser(Long idUser) {
        String sql="delete from requests where user1id=? or user2id=?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1,idUser);
            ps.setLong(2,idUser);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public boolean deleteSpecificFriendRequest(Long ID1,Long ID2)
    {
        String sql="delete from requests where user1id=? and user2id=?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1,ID1);
            ps.setLong(2,ID2);
            if(ps.executeUpdate()>0)
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



    public boolean checkIfFriendshipRequestExists(Long ID1,Long ID2)
    {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps= connection.prepareStatement("SELECT * from requests where user1id=? and user2id=?"))
        {
            ps.setLong(1,ID1);
            ps.setLong(2,ID2);
            ResultSet resultSet=ps.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



    public List<FriendRequest> getFriendRequestsReceivedByUser(Long idUserCurent)
    {
        List<FriendRequest> friendRequests=new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement("SELECT * from requests where user2id=?"))
        {
            ps.setLong(1,idUserCurent);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next())
            {
                Long friendRequest_ID=resultSet.getLong("id");
                Long friend1_ID=resultSet.getLong("user1id");
                String strSentSince=resultSet.getString("datetime");
                String status=resultSet.getString("status");

                Map<Long, User> users=ufr.getEntities();
                User u1=users.get(friend1_ID);
                User u2=users.get(idUserCurent);
                DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                LocalDateTime datetime=LocalDateTime.parse(strSentSince,formatter);
                FriendRequest friendRequest1=new FriendRequest(u1,u2,datetime,status);
                friendRequest1.setId(friendRequest_ID);
                friendRequests.add(friendRequest1);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return friendRequests;
    }



    public Long getNewId()
    {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement("SELECT max(id) from requests");
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
