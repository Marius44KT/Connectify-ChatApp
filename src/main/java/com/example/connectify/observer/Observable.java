package com.example.connectify.observer;

import com.example.connectify.events.Event;

public interface Observable<E extends Event> {
    void addObserver(Observer<E> e);
    void removeObserver(Observer<E> e);
    void notifyObservers(E entity);
}
