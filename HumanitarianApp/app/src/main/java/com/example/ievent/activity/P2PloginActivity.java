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

//        // chat messages
//        ArrayList<ChatMessage> chatMessages = new ArrayList<>();
//        chatMessages.add(new ChatMessage("Hello", "3", 231));
//        chatMessages.add(new ChatMessage("Bye!", "3", 313));
//
//        for (ChatMessage chatMessage : chatMessages) {
//            database.getReference("chats").child("5-6").push().setValue(chatMessage).addOnCompleteListener(task -> {
//                if(task.isSuccessful()) {
//                    // If the message is sent successfully, display a success message.
//                    Toast.makeText(P2PloginActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
//                }else{
//                    // If the message is not sent successfully, display an error message.
//                    Toast.makeText(P2PloginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        }

        //block massages
        ArrayList<String> BlockMessages = new ArrayList<>();
        BlockMessages.add("owDAoIqVfMAT3m40Dgsf9ETk1T72-Kb84rvbkTiYDppEPZPT7NuKXd5Pd2");
        BlockMessages.add("g9NVMbuXKngNXQTYcuRoULbhPsf1-6nD4n8BkLUb2xkrBQS3n0d6nRKA3");
        BlockMessages.add("3dmf0gmzhyVcpSXEs35Q2lPW8M23-g9NVMbuXKngNXQTYcuRoULbhPsf1");
        BlockMessages.add("owDAoIqVfMAT3m40Dgsf9ETk1T72-V5DZOTNohYTtgsh9iU3IsvbGIwf2");
        BlockMessages.add("nnngdJFR3RaW1vS2cg0UX9cpjNw2-fwhZCXkyduWtf2urV0FUCCfKYS33");
        BlockMessages.add("INFgd1oC8JeYFVQyCwfYFXQOXk92-aPATXqHqdzaM0czngj8FYVkhcAh2");
        BlockMessages.add("aPATXqHqdzaM0czngj8FYVkhcAh2-6nD4n8BkLUb2xkrBQS3n0d6nRKA3");
        BlockMessages.add("6nD4n8BkLUb2xkrBQS3n0d6nRKA3-Kb84rvbkTiYDppEPZPT7NuKXd5Pd2");
        BlockMessages.add("bikdh4LjE2Qpu1EXMFrSv1c8HUG3-ZyKnS8CTIUV0XTHqEknM2tGnvKT43");
        BlockMessages.add("Jc3x2WGFgK0tQWxYxEstcEiJH6d2-INFgd1oC8JeYFVQyCwfYFXQOXk92");
        BlockMessages.add("6nD4n8BkLUb2xkrBQS3n0d6nRKA3-nnngdJF3R3aW1vS2cg0UX9cpjNw2");

        //overturn
        BlockMessages.add("Kb84rvbkTiYDppEPZPT7NuKXd5Pd2-owDAoIqVfMAT3m40Dgsf9ETk1T72");
        BlockMessages.add("6nD4n8BkLUb2xkrBQS3n0d6nRKA3-g9NVMbuXKngNXQTYcuRoULbhPsf1");
        BlockMessages.add("g9NVMbuXKngNXQTYcuRoULbhPsf1-3dmf0gmzhyVcpSXEs35Q2lPW8M23");
        BlockMessages.add("V5DZOTNohYTtgsh9iU3IsvbGIwf2-owDAoIqVfMAT3m40Dgsf9ETk1T72");
        BlockMessages.add("fwhZCXkyduWtf2urV0FUCCfKYS33-nnngdJFR3RaW1vS2cg0UX9cpjNw2");

        for (String BlockMessage : BlockMessages) {
            // 直接使用 "chats" 作为第一级，BlockMessage 作为第二级的key
            database.getReference("block").child(BlockMessage).setValue(true) // 假设您只需要标记存在即可
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // 如果消息发送成功，显示成功消息
                            Toast.makeText(P2PloginActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                        } else {
                            // 如果消息未成功发送，显示错误消息
                            Toast.makeText(P2PloginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


}
