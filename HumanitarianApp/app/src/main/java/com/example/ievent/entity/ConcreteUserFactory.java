package com.example.ievent.entity;

import com.example.ievent.interfaces.UserFactory;


/**
 * ConcreteUserFactory is a class that implements the UserFactory interface. It is responsible for creating
 * instances of the User class based on the type of user specified.
 * @author : Tengkai Wang
 */
public class ConcreteUserFactory implements UserFactory {


    private static ConcreteUserFactory instance = null;


    private ConcreteUserFactory() {
    }

    public static ConcreteUserFactory getInstance() {
        if (instance == null) {
            instance = new ConcreteUserFactory();
        }
        return instance;
    }

    /**
     * createUser is a method that creates an instance of the User class based on the type of user specified.
     * @param type type of user (Organizer or Participant)
     * @param uid user id
     * @param email user email
     * @param userName user name
     * @return an instance of the Organizer or Participant class based on the type specified
     */
    @Override
    public User createUser(String type, String uid, String email, String userName) {
        if (type.equalsIgnoreCase("Organizer")) {
            return new Organizer(uid, email, userName);
        } else if (type.equalsIgnoreCase("Participant")) {
            return new Participant(uid, email, userName);
        } else {
            throw new IllegalArgumentException("Unknown user type " + type);
        }
    }
}
