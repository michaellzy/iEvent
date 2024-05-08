package com.example.ievent.activity;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.ievent.R;
import com.example.ievent.databinding.ActivityP2PloginBinding;
import com.example.ievent.entity.ChatMessage;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class P2PloginActivity extends BaseActivity {

    ActivityP2PloginBinding binding;

    FirebaseDatabase database = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityP2PloginBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        binding.btnLogin.setOnClickListener(v -> {
            // Write a message list to the database
            ArrayList<ChatMessage> chatMessages = new ArrayList<>();
            // 假设已经有一个 List<ChatMessage> chatMessages 用来存储消息
            chatMessages.add(new ChatMessage("Hello", "3", 231));
            chatMessages.add(new ChatMessage("Hi", "4", 232));
            chatMessages.add(new ChatMessage("How's your day going?", "3", 233));
            chatMessages.add(new ChatMessage("Pretty good, just got back from hiking.", "3", 234));
            chatMessages.add(new ChatMessage("Wow, that sounds exciting!", "3", 235));
            chatMessages.add(new ChatMessage("It was! Have you been outdoors much lately?", "3", 236));
            chatMessages.add(new ChatMessage("Not really, been caught up with work. But I plan to go for a walk in the park this weekend.", "4", 237));
            chatMessages.add(new ChatMessage("That's a good idea. It's supposed to be sunny this weekend.", "3", 238));
            chatMessages.add(new ChatMessage("Yes, looking forward to it. How about you, any plans?", "4", 239));
            chatMessages.add(new ChatMessage("Might catch a movie. Haven't been to the cinema in ages.", "3", 240));
            chatMessages.add(new ChatMessage("Same here! Maybe I should join you.", "4", 241));
            chatMessages.add(new ChatMessage("Sure, let's plan for Saturday evening.", "3", 242));
            chatMessages.add(new ChatMessage("Perfect! See you then.", "4", 243));
            chatMessages.add(new ChatMessage("Definitely! Any idea what's playing?", "4", 244));
            chatMessages.add(new ChatMessage("I think the new Marvel movie is out. Heard it’s great!", "3", 245));
            chatMessages.add(new ChatMessage("Oh, I love superhero movies! It’s a plan then.", "4", 246));
            chatMessages.add(new ChatMessage("Great! I’ll check the showtimes and let you know.", "3", 247));
            chatMessages.add(new ChatMessage("Thanks! I’ll be free after 6 PM.", "4", 248));
            chatMessages.add(new ChatMessage("Noted. I’ll book the evening show.", "3", 249));
            chatMessages.add(new ChatMessage("Awesome! Should we grab dinner first?", "4", 250));
            chatMessages.add(new ChatMessage("Sounds good. There’s that new Italian place we could try out.", "3", 251));
            chatMessages.add(new ChatMessage("Heard they have the best pasta.", "4", 252));
            chatMessages.add(new ChatMessage("Yes, let’s meet there at 5?", "3", 253));
            chatMessages.add(new ChatMessage("Perfect! It’s a date.", "4", 254));
            chatMessages.add(new ChatMessage("Looking forward to it. See you Saturday!", "3", 255));
            chatMessages.add(new ChatMessage("Have a good rest of the week!", "4", 256));
            chatMessages.add(new ChatMessage("You too. Take care!", "3", 257));
            chatMessages.add(new ChatMessage("By the way, did you finish the project you were working on?", "4", 258));
            chatMessages.add(new ChatMessage("Just wrapped it up today. Feels good to be done.", "3", 259));
            chatMessages.add(new ChatMessage("Congrats! That’s got to be a relief.", "4", 260));
            chatMessages.add(new ChatMessage("It is. Now looking forward to the weekend even more.", "3", 261));
            chatMessages.add(new ChatMessage("Same here. Alright, I’ll leave you to your evening. Catch up soon!", "4", 262));
            chatMessages.add(new ChatMessage("Sure thing. Good night!", "3", 263));


            for (ChatMessage chatMessage : chatMessages) {
                database.getReference("chats").child("3-4").push().setValue(chatMessage).addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        // If the message is sent successfully, display a success message.
                        Toast.makeText(P2PloginActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                    }else{
                        // If the message is not sent successfully, display an error message.
                        Toast.makeText(P2PloginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
    }


}
