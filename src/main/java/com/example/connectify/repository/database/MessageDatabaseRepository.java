package com.example.connectify.repository.database;



import com.example.connectify.domain.Message;
import com.example.connectify.domain.User;
import com.example.connectify.domain.validators.Validator;
import com.example.connectify.repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MessageDatabaseRepository implements Repository<Long, Message> {
    private String url;
    private String username;
    private String password;
    UserDatabaseRepository ufr;
    private Validator<Message> validator;

    public MessageDatabaseRepository(String url, String username, String password, UserDatabaseRepository ufr, Validator<Message> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.ufr=ufr;
        this.validator = validator;
    }



    @Override
    public Message findOne(Long aLong) {
        return null;
    }



    @Override
    public Map<Long,Message> getEntities()
    {
        Map<Long,Message> messages=new HashMap<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement("SELECT * from messages");
             ResultSet resultSet = ps.executeQuery()) {

            while (resultSet.next()) {
                Long message_ID=resultSet.getLong("id");
                Long friend1_ID=resultSet.getLong("user1id");
                Long friend2_ID=resultSet.getLong("user2id");
                String messageText=resultSet.getString("message");
                String strDateTimeSent=resultSet.getString("datetime");

                Map<Long, User> users=ufr.getEntities();
                User u1=users.get(friend1_ID);
                User u2=users.get(friend2_ID);
                DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                LocalDateTime datetime=LocalDateTime.parse(strDateTimeSent,formatter);
                Message message1=new Message(u1,u2,messageText,datetime);
                message1.setId(message_ID);
                messages.put(message_ID,message1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }



    @Override
    public Message save(Message entity) {

        String sql="insert into messages (id,user1id,user2id,message,datetime) values (?,?,?,?,?)";
        DateTimeFormatter format=DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        try (Connection connection=DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setLong(1,entity.getId());
            ps.setLong(2,entity.getUser1().getId());
            ps.setLong(3,entity.getUser2().getId());
            ps.setString(4,entity.getMessage());
            ps.setString(5,entity.getDateTime().format(format));
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }



    public Map<Long,Message> getConversation(Long userID1,Long userID2)
    {
        Map<Long,Message> messages=new HashMap<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement("SELECT * from messages where (user1id=? and user2id=?) or (user2id=? and user1id=?)"))
        {
            ps.setLong(1,userID1);
            ps.setLong(2,userID2);
            ps.setLong(3,userID1);
            ps.setLong(4,userID2);
            ResultSet resultSet=ps.executeQuery();
            {
                Long message_ID,friend1_ID,friend2_ID;
                String messageText,strDateTimeSent;
                while (resultSet.next())
                {
                    message_ID=resultSet.getLong("id");
                    friend1_ID=resultSet.getLong("user1id");
                    friend2_ID=resultSet.getLong("user2id");
                    messageText=resultSet.getString("message");
                    strDateTimeSent=resultSet.getString("datetime");

                    Map<Long, User> users=ufr.getEntities();
                    User u1=users.get(friend1_ID);
                    User u2=users.get(friend2_ID);
                    DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                    LocalDateTime datetime=LocalDateTime.parse(strDateTimeSent,formatter);
                    Message message1=new Message(u1,u2,messageText,datetime);
                    message1.setId(message_ID);
                    messages.put(message_ID,message1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }



    public Map<Long,Message> getSearchedMessages(Long userID1,Long userID2,String text)
    {
        Map<Long,Message> messages=new HashMap<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps=connection.prepareStatement("SELECT * from messages where (user1id=? and user2id=? and message like concat('%', ?, '%')) or (user2id=? and user1id=? and message like concat('%', ?, '%'))"))
        {
            ps.setLong(1,userID1);
            ps.setLong(2,userID2);
            ps.setString(3,text);
            ps.setLong(4,userID1);
            ps.setLong(5,userID2);
            ps.setString(6,text);
            ResultSet resultSet=ps.executeQuery();
            {
                Long message_ID,friend1_ID,friend2_ID;
                String messageText,strDateTimeSent;
                while (resultSet.next())
                {
                    message_ID=resultSet.getLong("id");
                    friend1_ID=resultSet.getLong("user1id");
                    friend2_ID=resultSet.getLong("user2id");
                    messageText=resultSet.getString("message");
                    strDateTimeSent=resultSet.getString("datetime");

                    Map<Long, User> users=ufr.getEntities();
                    User u1=users.get(friend1_ID);
                    User u2=users.get(friend2_ID);
                    DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                    LocalDateTime datetime=LocalDateTime.parse(strDateTimeSent,formatter);
                    Message message1=new Message(u1,u2,messageText,datetime);
                    message1.setId(message_ID);
                    messages.put(message_ID,message1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }



    @Override
    public Message delete(Long aLong) {
        String sql="delete from messages where id=?";
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
    public Message update(Message entity) {
        return null;
    }



    public Long getNewId()
    {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement("SELECT max(id) from messages");
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
