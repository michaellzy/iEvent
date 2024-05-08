package com.example.ievent.entity;

import com.example.ievent.database.data_manager.OrganizerDataManager;

import java.util.ArrayList;
import java.util.List;

public class Organizer extends User implements java.io.Serializable,Observer {

    private ArrayList<String> organizedEventList = new ArrayList<>();
    private ArrayList<String> followersList = new ArrayList<>();
    private PostSubject postSubject;

    public Organizer() {
        super();
        postSubject = new PostSubject();
    }

    public Organizer(String uid, String email, String userName) {
        super(uid, email, userName);
    }

    public void organizeEvent(String eventId) {
        // Adds an event to the list of events the participant has joined
        // organizedEventList.add(event);
        OrganizerDataManager.getInstance().addOrganizedEvent(super.getUid(), eventId);
        // 发布新活动，通知所有关注者

    }
    // 注册观察者
    public void registerFollowers(List<User> followers) {
        for (User follower : followers) {
            postSubject.registerObserver(follower);
        }
    }

    // 发布新活动
    public void postEvent(Event event) {
        // 发布新活动的逻辑...

        // 通知所有观察者（followers）
        postSubject.notifyObservers(event);
    }

    public ArrayList<String> getOrganizedEventList() {
        // Overridden to return the list of events this participant has joined
        return organizedEventList;
    }

    public ArrayList<String> getFollowersList() {
        // Returns the list of followers for this organizer
        return followersList;
    }


    public boolean isFollower(String userId) {
        // Checks if a specific user is following this organizer
        return followersList.contains(userId);
    }


    @Override
    public void update(Event event) {
        // 当接收到通知时执行的操作，例如在NotificationActivity中显示通知

    }
}


