package com.example.ievent.activity;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.ievent.R;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import org.json.JSONObject;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ievent.R;
import fi.iki.elonen.NanoHTTPD;
import java.io.IOException;

public class P2PloginActivity extends BaseActivity {
    private EditText etIpAddress;
    private EditText etPort;
    private MyHttpServer server;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_p2_plogin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etIpAddress = findViewById(R.id.et_ip_address);
        etPort = findViewById(R.id.et_port);

        findViewById(R.id.btn_login).setOnClickListener(v -> performLogin());
        
        findViewById(R.id.btn_show_ip_port).setOnClickListener(v -> showIpPortDialog());
    }

    private void showIpPortDialog() {
        // 获取IP地址
        String ipAddress = getIPAddress();
        int port = 8800; // 假定端口号

        // 创建弹窗
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.ip_port, null);
        builder.setView(dialogView);

        EditText etIpAddress = dialogView.findViewById(R.id.et_ip_address);
        EditText etPort = dialogView.findViewById(R.id.et_port);

        // 设置IP地址和端口号
        etIpAddress.setText(ipAddress);
        etPort.setText(String.valueOf(port));

        builder.setPositiveButton("OK", (dialog, which) -> {
            // 处理用户按下 OK 后的操作
            Toast.makeText(this, "IP: " + ipAddress + " Port: " + port, Toast.LENGTH_SHORT).show();
            startServer(port);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
    private void startServer(int port) {
        if (server == null) {
            server = new MyHttpServer(port);
            try {
                server.start();
                Toast.makeText(this, "Server started on port " + port, Toast.LENGTH_SHORT).show();

                // 设置定时器，30秒后关闭服务器
                new Handler().postDelayed(() -> {
                    server.stop();
                    server = null;
                    Toast.makeText(this, "Server stopped after timeout", Toast.LENGTH_SHORT).show();
                }, 30000);

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error starting server: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (server != null) {
            server.stop();
        }
    }
    private String getIPAddress() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        boolean isIPv4 = sAddr.indexOf(':') < 0;
                        if (isIPv4) return sAddr;
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return "Unknown IP";
    }
    private static class MyHttpServer extends NanoHTTPD {
        public MyHttpServer(int port) {
            super(port);
        }
        @Override
        public Response serve(IHTTPSession session) {
            String message = "Hello, client!";
            return newFixedLengthResponse(message);
        }
    }


    //login 操作
    private void performLogin() {
        String ipAddress = etIpAddress.getText().toString().trim();
        String port = etPort.getText().toString().trim();

        // 检查 IP 地址和端口是否为空
        if (ipAddress.isEmpty() || port.isEmpty()) {
            Toast.makeText(this, "IP Address and Port cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            String response = connectToServerUsingHttp(ipAddress, port);
            runOnUiThread(() -> {
                Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
            });
        }).start();
    }
    private String connectToServerUsingHttp(String ipAddress, String port) {
        try {
            // 假设服务器 URL
            String serverAddress = "http://" + ipAddress + ":" + port + "/login";
            URL url = new URL(serverAddress);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setDoOutput(true);

            // JSON 数据
            JSONObject json = new JSONObject();
            json.put("username", "test_user");
            json.put("password", "test_pass");
            String jsonInputString = json.toString();

            // 发送 JSON 数据
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // 接收响应
            int responseCode = conn.getResponseCode();
            Scanner scanner = new Scanner(conn.getInputStream(), "UTF-8").useDelimiter("\\A");
            String response = scanner.hasNext() ? scanner.next() : "";
            return responseCode == 200 ? "Login successful: " + response : "Login failed: " + responseCode;
        } catch (Exception e) {
            e.printStackTrace();
            return "Connection failed";
        }
    }
}
