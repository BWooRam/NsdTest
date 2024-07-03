package com.geekstudio.nsdtest

import android.Manifest
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.p2p.WifiP2pManager
import android.net.wifi.p2p.WifiP2pManager.DnsSdServiceResponseListener
import android.net.wifi.p2p.WifiP2pManager.DnsSdTxtRecordListener
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.geekstudio.nsdtest.ui.theme.NsdTestTheme

class DirectWifiActivity : ComponentActivity() {
    companion object {
        const val TAG = "DirectWifiActivity"
    }

    private val intentFilter = IntentFilter().apply {
        addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
    }
    private lateinit var channel: WifiP2pManager.Channel
    private lateinit var manager: WifiP2pManager
    private lateinit var serviceRequest: WifiP2pDnsSdServiceRequest
    private var receiver: DirectWifiBroadcastReceiver? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        manager = getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
        channel = manager.initialize(this, mainLooper, null)
//        discoverService()

        serviceRequest = WifiP2pDnsSdServiceRequest.newInstance()
        manager.addServiceRequest(
            channel,
            serviceRequest,
            object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    // Success!
                    Log.d(TAG, "addServiceRequest ActionListener onSuccess")
                }

                override fun onFailure(code: Int) {
                    // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
                    Log.d(TAG, "addServiceRequest ActionListener onFailure code = $code")
                }
            }
        )

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(TAG, "addServiceRequest permission NOT_PERMISSION_GRANTED")
            return
        }

        manager.discoverServices(
            channel,
            object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    // Success!
                    Log.d(TAG, "discoverServices ActionListener onSuccess")
                }

                override fun onFailure(code: Int) {
                    Log.d(TAG, "discoverServices ActionListener onFailure code = $code")
                    when (code) {
                        WifiP2pManager.P2P_UNSUPPORTED -> {
                            Log.d(TAG, "Wi-Fi Direct isn't supported on this device.")
                        }
                    }
                }
            }
        )

        enableEdgeToEdge()
        setContent {
            NsdTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = javaClass.simpleName,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private val buddies = mutableMapOf<String, String>()

    private fun discoverService() {
        val txtListener = DnsSdTxtRecordListener { fullDomain, record, device ->
            Log.d(TAG, "DnsSdTxtRecord available -$record")
            record["buddyname"]?.also {
                buddies[device.deviceAddress] = it
            }
        }
        val servListener =
            DnsSdServiceResponseListener { instanceName, registrationType, resourceType ->
                resourceType.deviceName =
                    buddies[resourceType.deviceAddress] ?: resourceType.deviceName
                Log.d(
                    TAG,
                    "onBonjourServiceAvailable registrationType = $registrationType, resourceType = $resourceType, instanceName = $instanceName"
                )
            }

        manager.setDnsSdResponseListeners(channel, servListener, txtListener)
    }

    public override fun onResume() {
        super.onResume()
        receiver = DirectWifiBroadcastReceiver(manager, channel)
        registerReceiver(receiver, intentFilter)
    }

    public override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
    }
}