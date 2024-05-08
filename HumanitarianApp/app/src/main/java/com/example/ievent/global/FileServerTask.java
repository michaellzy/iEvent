package com.example.ievent.global;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class FileServerTask {
    private Context context;
    private TextView statusText;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Handler handler = new Handler(Looper.getMainLooper());

    public FileServerTask(Context context, View statusText) {
        this.context = context;
        this.statusText = (TextView) statusText;
    }

    public void startServer() {
        executor.execute(() -> {
            String result = runServer();
            handler.post(() -> updateUI(result));
        });
    }

    private String runServer() {
        try (ServerSocket serverSocket = new ServerSocket(8888)) {
            Log.d("Server", "Server: Socket opened");
            Socket client = serverSocket.accept();
            Log.d("Server", "Server: connection done");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    total.append(line).append('\n');
                }
                return total.toString();
            }
        } catch (IOException e) {
            Log.e("Server", e.getMessage());
            return null;
        }
    }

    private void updateUI(String result) {
        if (result != null) {
            statusText.setText("Text received: " + result);
        } else {
            statusText.setText("Failed to receive text");
        }
    }
}

