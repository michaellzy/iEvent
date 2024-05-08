package com.example.ievent.global;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.example.ievent.activity.P2PchatActivity2;

/**
 * A BroadcastReceiver that notifies of important Wi-Fi p2p events.
 */
public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager manager;

    private WifiP2pManager.Channel channel;

    private P2PchatActivity2 activity;


    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel,
                                       P2PchatActivity2 activity) {
        super();
        this.manager = manager;
        this.channel = channel;
        this.activity = activity;
    }


    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {

            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                // Wifi P2P is enabled
                Toast.makeText(context, "Wi-Fi P2P is enabled", Toast.LENGTH_SHORT).show();
            } else {
                // Wi-Fi P2P is not enabled
                Toast.makeText(context, "Wi-Fi P2P is not enabled", Toast.LENGTH_SHORT).show();
            }

        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

            Toast.makeText(context, "Peers changed", Toast.LENGTH_SHORT).show();
            if (manager != null) {
                if (ActivityCompat.checkSelfPermission(
                        context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.NEARBY_WIFI_DEVICES) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.NEARBY_WIFI_DEVICES}, 1);
                    return;
                }
                manager.requestPeers(channel, activity.peerListListener);
            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
//            Toast.makeText(context, "Connection changed", Toast.LENGTH_SHORT).show();
//            // Respond to new connection or disconnections
//            if (manager == null) {
//                return;
//            }
//            WifiP2pInfo p2pInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_INFO);
//            if (p2pInfo != null) {
//                if (p2pInfo.isGroupOwner) {
//                    // We are the group owner, request connection info to find our IP address
//                    manager.requestConnectionInfo(channel, info -> {
//                        if(info == null) {
//                            return;
//                        }
//                        String goIpAddress = info.groupOwnerAddress.getHostAddress();
//                        Toast.makeText(context, "Group owner IP address: " + goIpAddress, Toast.LENGTH_SHORT).show();
//
//                        // Start a new activity to communicate with the peer
//                    });
//                } else {
//                    // We are a group client, request group owner info to get its IP address
//                    manager.requestGroupInfo(channel, groupInfo -> {
//                        if(groupInfo == null) {
//                            return;
//                        }
//                        String goIpAddress = groupInfo.getOwner().deviceAddress;
//                        Toast.makeText(context, "Group owner IP address: " + goIpAddress, Toast.LENGTH_SHORT).show();
//
//                        // Start a new activity to communicate with the peer
//                    });
//                }
//
//            }
        }else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
        }
    }
}
