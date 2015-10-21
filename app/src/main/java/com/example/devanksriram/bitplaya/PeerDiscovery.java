package com.example.devanksriram.bitplaya;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class PeerDiscovery extends Activity {

    WifiP2pManager mManager;
    Channel mChannel;
    BroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;

    private final String TAG="Wifi Direct Discovery";
    private WifiP2pManager.ActionListener actionListener = new WifiP2pManager.ActionListener() {
        public void onFailure(int reason) {
            String errorMessage = "WiFi Direct Failed: ";
            switch (reason) {
                case WifiP2pManager.BUSY :
                    errorMessage += "Framework busy."; break;
                case WifiP2pManager.ERROR :
                    errorMessage += "Internal error."; break;
                case WifiP2pManager.P2P_UNSUPPORTED :
                    errorMessage += "Unsupported."; break;
                default:
                    errorMessage += "Unknown error."; break;
            }
            Log.d(TAG, errorMessage);
        }

        public void onSuccess() {
            // Success!
            // Return values will be returned using a Broadcast Intent
        }
    };

    List<WifiP2pDevice> devicelist=new ArrayList<WifiP2pDevice>();
    //declaring ui elements
    Button discoverpeersbutton;
    ListView peerlistview;
    ArrayAdapter aa;
    IntentFilter peerFilter;
    IntentFilter p2penabled;
    IntentFilter connectionFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peer_discovery);

        p2penabled=new IntentFilter(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        peerFilter=new IntentFilter(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        connectionFilter=new IntentFilter(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        mManager=(WifiP2pManager)getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel=mManager.initialize(this,getMainLooper(),null);
        //mReceiver=new WifiDirectBroadcastReceiver(mManager,mChannel,this);
        discoverpeersbutton=(Button)findViewById(R.id.discoverpeers);

        Button enablewifi=(Button)findViewById(R.id.enablewifibutton);
        enablewifi.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(intent);
            }
        });
        peerlistview=(ListView)findViewById(R.id.peerlist);
        aa=new ArrayAdapter<WifiP2pDevice>(this,android.R.layout.simple_expandable_list_item_1,devicelist);
        peerlistview.setAdapter(aa);

        discoverpeersbutton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                discoverPeers();
            }
        });

    }
    BroadcastReceiver p2pstatusreceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int state=intent.getIntExtra(
                    WifiP2pManager.EXTRA_WIFI_STATE,
                    WifiP2pManager.WIFI_P2P_STATE_DISABLED
            );

            switch (state){
                case(WifiP2pManager.WIFI_P2P_STATE_ENABLED):
                    discoverpeersbutton.setEnabled(true);
                    break;
                default:
                    discoverpeersbutton.setEnabled(false);
            }
        }
    };
    BroadcastReceiver peerDiscoveryReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mManager.requestPeers(mChannel, new WifiP2pManager.PeerListListener() {
                @Override
                public void onPeersAvailable(WifiP2pDeviceList peers) {
                    devicelist.clear();
                    devicelist.addAll(peers.getDeviceList());
                    aa.notifyDataSetChanged();
                }
            });
        }
    };


    private void discoverPeers(){
        mManager.discoverPeers(mChannel,actionListener);
    }

    @Override
    protected void onResume(){
        super.onResume();
        registerReceiver(peerDiscoveryReceiver, peerFilter);
        registerReceiver(p2pstatusreceiver,p2penabled );
    }

    @Override
    protected void onPause(){
        super.onPause();
        unregisterReceiver(peerDiscoveryReceiver);
        unregisterReceiver(p2pstatusreceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_peer_discovery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
