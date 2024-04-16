# How to Call the Database API

## General Call

```java
IEventDatabase.getInstance().getAllEventsByType("boat party", new EventDataListener() {
            @Override
            public void onSuccess(ArrayList<T> data) {
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
}
```

## User Data Operation