package com.example.ievent.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import com.example.ievent.databinding.ActivityP2Pchat2Binding;
import com.example.ievent.global.WiFiDirectBroadcastReceiver;
import java.util.ArrayList;
import java.util.List;


public class P2PchatActivity2 extends BaseActivity {
    private final IntentFilter intentFilter = new IntentFilter();

    WifiP2pManager manager;

    WifiP2pManager.Channel channel;

    BroadcastReceiver receiver;

    ActivityP2Pchat2Binding binding;

    List<WifiP2pDevice> peers = new ArrayList<>();

    List<String> deviceNameArray = new ArrayList<>();


    @SuppressLint("SetTextI18n")
    public WifiP2pManager.PeerListListener peerListListener = peers -> {
        if (!peers.getDeviceList().equals(peers)) {
            deviceNameArray.clear();
            P2PchatActivity2.this.peers.clear();
            P2PchatActivity2.this.peers.addAll(peers.getDeviceList());

            for (WifiP2pDevice device : P2PchatActivity2.this.peers) {
                deviceNameArray.add(device.deviceName);
            }
            binding.deviceNumber.setText("Number of devices: " + peers.getDeviceList().size());
            binding.deviceName.setText(deviceNameArray.toString());
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityP2Pchat2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //  Intent filters
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);
        receiver = new WiFiDirectBroadcastReceiver(manager, channel, this);


        binding.bGetPeer.setOnClickListener(v -> {
            Toast.makeText(P2PchatActivity2.this, "Get Peer...", Toast.LENGTH_SHORT).show();


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.NEARBY_WIFI_DEVICES) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.NEARBY_WIFI_DEVICES}, 1);
                return;
            }

            manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    // Success
                    binding.state.setText("Discovery Started");
                }


                @Override
                public void onFailure(int reason) {
                    // Failure
                    // "reason == 2" Indicates that the operation failed because the framework is busy and unable to service the request
                    binding.state.setText("Discovery Failed: " + reason);
                }
            });
        });
    }


    /* register the broadcast receiver with the intent values to be matched */
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, intentFilter);
    }
    /* unregister the broadcast receiver */
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}