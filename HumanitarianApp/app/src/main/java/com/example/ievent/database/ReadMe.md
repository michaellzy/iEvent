# How to Call the Database API

In every activity, the database API is the only way to operate data, you shouldn't directly call the database operation in the activity.




## General Call



```java
db.getAllEventsByType("boat party", new EventDataListener() {
            @Override
            public void onSuccess(ArrayList<T> data) {
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
```

In success, the data will be returned in the form of an ArrayList( Even only one object. it should be accessed by list.get(0) ) and you can do operations.

In failure, an error message will be returned.

## User Data Operation

### Get User Data

```java
db.addNewUser(String uid, User user);


// get current user
db.getLoggedInUser(String uid, UserDataListener listener);
```


### Event Data Operation

```java
db.addNewEvent(Event e);

db.getAllEventsByType(String type, EventDataListener listener);
```
