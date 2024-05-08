package com.example.ievent.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ievent.databinding.ActivityP2pChatBinding;


public class P2PChatActivity extends AppCompatActivity {

    private ActivityP2pChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initializing the binding
        binding = ActivityP2pChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Now you can access your views directly through binding object
        // For example:
        // binding.textView.setText("Hello, P2P Chat!");
    }
}
