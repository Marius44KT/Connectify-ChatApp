package com.example.connectify.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class FriendRequest extends Entity<Long>{
    User user1,user2;
    LocalDateTime dateTime;
    String status;
    public FriendRequest(User u1,User u2,LocalDateTime date,String status)
    {
        this.user1=u1;
        this.user2=u2;
        this.dateTime=date;
        this.status=status;
    }

    public User getUser1()
    {
        return user1;
    }

    public void setUser1(User user1)
    {
        this.user1 = user1;
    }

    public User getUser2()
    {
        return user2;
    }

    public void setUser2(User user2)
    {
        this.user2 = user2;
    }

    public LocalDateTime getDateTime()
    {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime)
    {
        this.dateTime = dateTime;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        FriendRequest that = (FriendRequest) o;
        return Objects.equals(user1, that.user1) && Objects.equals(user2, that.user2) && Objects.equals(dateTime, that.dateTime) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(user1, user2, dateTime, status);
    }
}
