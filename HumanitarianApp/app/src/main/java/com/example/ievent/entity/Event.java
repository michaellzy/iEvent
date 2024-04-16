package com.example.ievent.entity;

import com.google.firebase.firestore.PropertyName;

import java.util.ArrayList;

public class Event {
    private String type;
    private String title;
    private String description;

    private String organizer;
    private String location;

    @PropertyName("date-time")
    private String dateTime;
    private int price;
    private String img;
    private ArrayList<User> participants;

    public Event() {}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @PropertyName("date-time")
    public String getDateTime() {
        return dateTime;
    }

    @PropertyName("date-time")
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }


    public ArrayList<User> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<User> participants) {
        this.participants = participants;
    }


    @Override
    public String toString() {
        return "Event{" +
                "type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", organizer='" + organizer + '\'' +
                ", location='" + location + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", price=" + price +
                ", img='" + img + '\'' +
                ", participants=" + participants +
                '}';
    }

}
