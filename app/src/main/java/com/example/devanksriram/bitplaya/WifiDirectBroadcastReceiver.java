package com.example.devanksriram.bitplaya;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;

import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.widget.ListView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


public class WifiDirectBroadcastReceiver extends BroadcastReceiver {
    private WifiP2pManager manager;
    private Channel channel;
    private PeerDiscovery activity;
    private List<WifiP2pDevice> deviceList=new ArrayList<WifiP2pDevice>();
    //private ListView peerlistview=(ListView)findViewById(R.id.peerlistview);

    public WifiDirectBroadcastReceiver(WifiP2pManager manager,Channel channel,PeerDiscovery activity) {
        this.manager=manager;
        this.channel=channel;
        this.activity=activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        this.manager.requestPeers(this.channel, new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList peers) {
                deviceList.clear();
                deviceList.addAll(peers.getDeviceList());


            }
        });

    }
}
