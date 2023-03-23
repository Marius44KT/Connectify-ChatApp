package com.example.connectify.repository.file;


import com.example.connectify.domain.User;
import com.example.connectify.domain.validators.Validator;

import java.util.List;
import java.util.Map;


public class UserFileRepository extends AbstractFileRepository<Long, User> {
    Map<Long, User> users;
    public UserFileRepository(String fileName, Validator<User> validator) {
        super(fileName,validator);
        super.loadData();
    }

    public Map<Long,User> getEntities()
    {
        return super.getEntities();
    }


    public User save(User u1)
    {
        return super.save(u1);
    }


    public User delete(Long ID)
    {
        return super.delete(ID);
    }



    @Override
    public User extractEntity(List<String> attributes) {
        User user = new User(attributes.get(1),attributes.get(2),attributes.get(3),attributes.get(4));
        user.setId(Long.parseLong(attributes.get(0)));
        return user;
    }



    @Override
    protected String createEntityAsString(User entity) {
        return entity.getId()+";"+entity.getFirstName()+";"+entity.getLastName();
    }
}


