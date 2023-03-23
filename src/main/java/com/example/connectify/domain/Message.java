package com.example.connectify.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Message extends Entity<Long>{
    User user1;
    User user2;
    LocalDateTime dateTime;
    String message;
    public Message(User u1,User u2,String message,LocalDateTime date)
    {
        this.user1=u1;
        this.user2=u2;
        this.message=message;
        this.dateTime=date;
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

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Message message1 = (Message) o;
        return Objects.equals(user1, message1.user1) && Objects.equals(user2, message1.user2) && Objects.equals(dateTime, message1.dateTime) && Objects.equals(message, message1.message);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(user1, user2, dateTime, message);
    }
}
