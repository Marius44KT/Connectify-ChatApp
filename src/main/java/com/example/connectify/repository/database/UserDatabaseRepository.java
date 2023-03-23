package com.example.connectify.repository.database;


import com.example.connectify.domain.User;
import com.example.connectify.domain.validators.Validator;
import com.example.connectify.repository.Repository;

import java.sql.*;
import java.util.*;

public class UserDatabaseRepository implements Repository<Long, User> {
    private String url;
    private String username;
    private String password;
    private Validator<User> validator;

    public UserDatabaseRepository(String url, String username, String password, Validator<User> validator) {
        this.url=url;
        this.username=username;
        this.password=password;
        this.validator=validator;
    }



    @Override
    public User findOne(Long idUser) {
        String sql="SELECT * from users where id=?";
        try (Connection connection = DriverManager.getConnection(url,username,password);
             PreparedStatement ps=connection.prepareStatement(sql))
        {
            ps.setLong(1,idUser);
            ResultSet resultSet=ps.executeQuery();
            if(resultSet.next())
            {
                String firstname=resultSet.getString("firstname");
                String lastname=resultSet.getString("lastname");
                String email=resultSet.getString("email");
                String password=resultSet.getString("password");
                User u=new User(firstname,lastname,email,password);
                u.setId(idUser);
                return u;
            }
            else
                return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }



    @Override
    public Map<Long,User> getEntities()
    {
        Map<Long,User> users=new HashMap<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement("SELECT * from users");
             ResultSet resultSet = ps.executeQuery())
        {
            while (resultSet.next())
            {
                Long user_ID=resultSet.getLong("id");
                String firstName=resultSet.getString("firstname");
                String lastName=resultSet.getString("lastname");
                String email=resultSet.getString("email");
                String password=resultSet.getString("password");

                User User=new User(firstName, lastName,email,password);
                User.setId(user_ID);
                users.put(user_ID,User);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return users;
    }



    @Override
    public User save(User entity) {

        String sql="insert into users (id,firstname, lastname,email,password) values (?,?,?,?,?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, entity.getId());
            ps.setString(2, entity.getFirstName());
            ps.setString(3, entity.getLastName());
            ps.setString(4,entity.getEmail());
            ps.setString(5,entity.getPassword());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    @Override
    public User delete(Long aLong) {
        String sql="delete from \"users\" where id=?";
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
    public User update(User entity) {
        return null;
    }



    public List<User> getTheOtherUsers(Long idUser)
    {
        List<User> users=new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement("SELECT * from users where id!=?"))
        {
            ps.setLong(1,idUser);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next())
            {
                Long user_ID=resultSet.getLong("id");
                String firstName=resultSet.getString("firstname");
                String lastName=resultSet.getString("lastname");
                String email=resultSet.getString("email");
                String password=resultSet.getString("password");

                User user=new User(firstName, lastName,email,password);
                user.setId(user_ID);
                users.add(user);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return users;
    }



    public List<User> getSearchedUsers(String str,Long idUserCurent)
    {
        List<User> users=new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement("SELECT * from users where (firstname like concat('%', ?, '%') or lastname like concat('%', ?, '%')) and id!=?"))
        {
            ps.setString(1,str);
            ps.setString(2,str);
            ps.setLong(3,idUserCurent);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next())
            {
                Long user_ID=resultSet.getLong("id");
                String firstName=resultSet.getString("firstname");
                String lastName=resultSet.getString("lastname");
                String email=resultSet.getString("email");
                String password=resultSet.getString("password");

                User user=new User(firstName, lastName,email,password);
                user.setId(user_ID);
                users.add(user);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return users;
    }



    public User findOneByEmailAndPassword(String email,String pass) {
        String sql="SELECT * from users where email=? and password=?";
        try (Connection connection = DriverManager.getConnection(url,username,password);
             PreparedStatement ps=connection.prepareStatement(sql))
        {
            ps.setString(1,email);
            ps.setString(2,pass);
            ResultSet resultSet=ps.executeQuery();
            if(resultSet.next())
            {
                Long id=resultSet.getLong("id");
                String firstname=resultSet.getString("firstname");
                String lastname=resultSet.getString("lastname");
                String password=resultSet.getString("password");
                User u=new User(firstname,lastname,email,password);
                u.setId(id);
                return u;
            }
            else
                return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }



    public User findOneByEmail(String email) {
        String sql="SELECT * from users where email=?";
        try (Connection connection = DriverManager.getConnection(url,username,password);
             PreparedStatement ps=connection.prepareStatement(sql))
        {
            ps.setString(1,email);
            ResultSet resultSet=ps.executeQuery();
            if(resultSet.next())
            {
                Long id=resultSet.getLong("id");
                String firstname=resultSet.getString("firstname");
                String lastname=resultSet.getString("lastname");
                String password=resultSet.getString("password");
                User u=new User(firstname,lastname,email,password);
                u.setId(id);
                return u;
            }
            else
                return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }



    public Long getNewId()
    {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps= connection.prepareStatement("SELECT max(id) from users");
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