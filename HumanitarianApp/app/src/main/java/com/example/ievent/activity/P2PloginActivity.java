package com.example.ievent.activity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.ievent.entity.ChatMessage;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class P2PloginActivity extends BaseActivity {


    FirebaseDatabase database = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        chatMessages.add(new ChatMessage("Good night!", "4", 264));
        chatMessages.add(new ChatMessage("Morning! How's the weekend going?", "4", 265));
        chatMessages.add(new ChatMessage("Hey! Just getting some rest. How about you?", "3", 266));
        chatMessages.add(new ChatMessage("Pretty much the same. Needed this break badly.", "4", 267));
        chatMessages.add(new ChatMessage("Yeah, these days rest feels like a luxury.", "3", 268));
        chatMessages.add(new ChatMessage("True that. Any plans for today?", "4", 269));
        chatMessages.add(new ChatMessage("Not really. Maybe just a walk later in the evening. You?", "3", 270));
        chatMessages.add(new ChatMessage("Thinking of catching a movie. You interested?", "4", 271));
        chatMessages.add(new ChatMessage("Sounds like a plan. What time?", "3", 272));
        chatMessages.add(new ChatMessage("How about 7 PM?", "4", 273));
        chatMessages.add(new ChatMessage("Perfect. See you there!", "3", 274));
        chatMessages.add(new ChatMessage("Great! It's a date then. Later!", "4", 275));
        chatMessages.add(new ChatMessage("Looking forward to it. Bye!", "3", 276));
        chatMessages.add(new ChatMessage("Hey, running a bit late. Start without me!", "3", 277));
        chatMessages.add(new ChatMessage("No worries, I'll grab us some snacks meanwhile.", "4", 278));
        chatMessages.add(new ChatMessage("Thanks! Be there in 5.", "3", 279));
        chatMessages.add(new ChatMessage("Got our snacks. See you soon!", "4", 280));
        chatMessages.add(new ChatMessage("Just arrived. Where are you at?", "3", 281));
        chatMessages.add(new ChatMessage("Front row, right side. See you!", "4", 282));
        chatMessages.add(new ChatMessage("Found you. This movie better be good.", "3", 283));
        chatMessages.add(new ChatMessage("It's got great reviews. Fingers crossed.", "4", 284));
        chatMessages.add(new ChatMessage("That was hilarious! Good call on the movie.", "3", 285));
        chatMessages.add(new ChatMessage("Told ya! Glad you enjoyed it.", "4", 286));
        chatMessages.add(new ChatMessage("Definitely needed that laugh. Thanks for the company.", "3", 287));
        chatMessages.add(new ChatMessage("Anytime! Should do this more often.", "4", 288));
        chatMessages.add(new ChatMessage("Agree. Let’s not wait another year next time.", "3", 289));
        chatMessages.add(new ChatMessage("Deal! Have a good night!", "4", 290));
        chatMessages.add(new ChatMessage("You too. Bye!", "3", 291));
        chatMessages.add(new ChatMessage("Morning! How's the start to your week?", "4", 292));
        chatMessages.add(new ChatMessage("Busy as ever. How about you?", "3", 293));
        chatMessages.add(new ChatMessage("Same here, just diving into work. Coffee hasn't kicked in yet.", "4", 294));
        chatMessages.add(new ChatMessage("I can relate. Second cup might do the trick.", "3", 295));
        chatMessages.add(new ChatMessage("Worth a shot. Let's survive today!", "4", 296));
        chatMessages.add(new ChatMessage("Survival mode: ON. Talk later?", "3", 297));
        chatMessages.add(new ChatMessage("Sure, catch up after work. Hang in there!", "3", 298));
        chatMessages.add(new ChatMessage("Will do. Later!", "3", 299));
        chatMessages.add(new ChatMessage("How's the day winding down?", "3", 300));
        chatMessages.add(new ChatMessage("Finally over. Ready to crash.", "3", 301));
        chatMessages.add(new ChatMessage("Same. Long day. Dinner then sleep.", "4", 302));
        chatMessages.add(new ChatMessage("Sounds like a plan. Enjoy your meal!", "3", 303));
        chatMessages.add(new ChatMessage("Thanks, you too. Rest well!", "4", 304));
        chatMessages.add(new ChatMessage("Will try. Good night!", "3", 305));
        chatMessages.add(new ChatMessage("Good morning! Ready for another day?", "4", 306));
        chatMessages.add(new ChatMessage("As ready as I'll ever be. How's it going?", "4", 307));
        chatMessages.add(new ChatMessage("Taking it one task at a time. Let’s do this!", "4", 308));
        chatMessages.add(new ChatMessage("That’s the spirit. Let's conquer the day!", "3", 309));
        chatMessages.add(new ChatMessage("Absolutely. Check in later?", "4", 310));
        chatMessages.add(new ChatMessage("Definitely. See you!", "3", 311));
        chatMessages.add(new ChatMessage("Take care. Bye for now!", "4", 312));
        chatMessages.add(new ChatMessage("Bye!", "3", 313));

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
    }


}
