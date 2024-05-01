package com.example.ievent.adapter;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Boolean> eventUploaded = new MutableLiveData<>();

    // Call this method when the event upload is complete
    public void notifyEventUploaded() {
        Log.d("SharedViewModel", "Event upload notified.");
        eventUploaded.postValue(true);
    }

    public void resetEventUploaded() {
        Log.d("SharedViewModel", "Event upload reset.");
        eventUploaded.postValue(false);
    }

    // Observe this in MainActivity
    public LiveData<Boolean> getEventUploaded() {
        return eventUploaded;
    }
}
