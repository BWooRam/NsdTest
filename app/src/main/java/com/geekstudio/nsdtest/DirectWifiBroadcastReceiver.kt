package com.geekstudio.nsdtest

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import android.util.Log
import androidx.core.app.ActivityCompat

class DirectWifiBroadcastReceiver(
    private val manager: WifiP2pManager,
    private val channel: WifiP2pManager.Channel
) : BroadcastReceiver() {
    init {
        Log.d(DirectWifiActivity.TAG, "DirectWifiBroadcastReceiver init")
    }

    override fun onReceive(context: Context, intent: Intent) {
        when(intent.action) {
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)
                Log.d(DirectWifiActivity.TAG, "DirectWifiBroadcastReceiver WIFI_P2P_STATE_CHANGED_ACTION state = $state")
            }
            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                Log.d(DirectWifiActivity.TAG, "DirectWifiBroadcastReceiver WIFI_P2P_PEERS_CHANGED_ACTION")
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Log.d(DirectWifiActivity.TAG, "DirectWifiActivity NOT_PERMISSION_GRANTED")
                    return
                }
                manager.requestPeers(channel, peerListListener)
                Log.d(DirectWifiActivity.TAG, "DirectWifiActivity P2P peers changed")
            }
            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {
                Log.d(DirectWifiActivity.TAG, "DirectWifiBroadcastReceiver WIFI_P2P_CONNECTION_CHANGED_ACTION")
            }
            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {
                val device = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE) as? WifiP2pDevice
                Log.d(DirectWifiActivity.TAG, "DirectWifiBroadcastReceiver WIFI_P2P_THIS_DEVICE_CHANGED_ACTION device = $device")
            }
        }
    }

    private val peerListListener = WifiP2pManager.PeerListListener { peerList ->
        val refreshedPeers = peerList.deviceList.forEach { device ->
            Log.d(DirectWifiActivity.TAG, "DirectWifiBroadcastReceiver PeerListListener device = $device")
        }
    }
}