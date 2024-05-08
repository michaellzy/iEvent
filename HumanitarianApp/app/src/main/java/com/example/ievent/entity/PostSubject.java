package com.example.ievent.entity;
import java.util.ArrayList;
import java.util.List;

public class PostSubject {
    private List<Observer> observers = new ArrayList<>();

    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers(Event event) {
        for (Observer observer : observers) {
            observer.update(event);
        }
    }
}

