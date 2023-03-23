package com.example.connectify.observer;

import com.example.connectify.events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}

