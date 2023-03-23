package com.example.connectify.repository.file;


import com.example.connectify.domain.Friendship;
import com.example.connectify.domain.User;
import com.example.connectify.domain.validators.Validator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;


public class FriendFileRepository extends AbstractFileRepository<Long, Friendship> {
    Map<Long, User> users;
    public FriendFileRepository(String fileName, UserFileRepository ufr, Validator<Friendship> validator) {
        super(fileName,validator);
        users=ufr.getEntities();
        super.loadData();
    }


    public Friendship save(Friendship f1)
    {
        return super.save(f1);
    }

    public Friendship delete(Long ID)
    {
        return super.delete(ID);
    }



    @Override
    public Friendship extractEntity(List<String> attributes) {
        Long user_id1=Long.parseLong(attributes.get(1));
        Long user_id2=Long.parseLong(attributes.get(2));
        User user1=users.get(user_id1);
        User user2=users.get(user_id2);
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime datetime=LocalDateTime.parse(attributes.get(3),formatter);
        Friendship friendship=new Friendship(user1,user2,datetime);
        friendship.setId(Long.parseLong(attributes.get(0)));
        return friendship;
    }



    @Override
    protected String createEntityAsString(Friendship entity) {
        DateTimeFormatter format=DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String data=entity.getDateTime().format(format);
        return entity.getId()+";"+entity.getUser1().getId()+";"+entity.getUser2().getId()+";"+data;
    }
}