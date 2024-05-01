package com.example.ievent.entity;

import com.google.firebase.firestore.PropertyName;
import java.util.ArrayList;

public class Event implements java.io.Serializable{
    private String type;
    private String title;
    private String description;

    private String organizer;
    private String location;

    @PropertyName("date-time")
    private String dateTime;
    private double price;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
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

    public static void preprocessData(Event e){
        if(e.getTitle() == null || e.getTitle().isEmpty()) {
            e.setTitle("No title");
        }

        if(e.getDateTime() == null || e.getDateTime().isEmpty()) {
            e.setDateTime("No date");
        }

        if(e.getLocation() == null || e.getLocation().isEmpty()) {
            e.setLocation("Online");
        }

        if(e.getDescription() == null || e.getDescription().isEmpty()) {
            e.setDescription("This organizer is too lazy to write a description.");
        }

        if(e.getOrganizer() == null || e.getOrganizer().isEmpty()) {
            e.setOrganizer("Unknown");
        }

        if(e.getImg() == null || e.getImg().isEmpty()) {
            e.setImg("https://t.mwm.moe/fj");
        }


    }

}
