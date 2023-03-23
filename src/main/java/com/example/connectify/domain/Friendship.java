package com.example.connectify.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Friendship extends Entity<Long>{
    User user1,user2;
    LocalDateTime dateTime;

    public Friendship(User u1,User u2,LocalDateTime date)
    {
        this.user1=u1;
        this.user2=u2;
        this.dateTime=date;
    }


    public User getUser1()
    {
        return user1;
    }


    public User getUser2()
    {
        return user2;
    }


    public void setUser1(User user1)
    {
        this.user1 = user1;
    }

    public void setUser2(User user2)
    {
        this.user2 = user2;
    }


    public LocalDateTime getDateTime()
    {
        return dateTime;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Friendship that = (Friendship) o;
        return Objects.equals(user1, that.user1) && Objects.equals(user2, that.user2);
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(user1, user2);
    }
}
