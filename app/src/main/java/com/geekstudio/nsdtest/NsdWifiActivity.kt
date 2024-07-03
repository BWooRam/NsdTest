package com.geekstudio.nsdtest

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.geekstudio.nsdtest.ui.theme.NsdTestTheme
import java.net.InetAddress
import java.net.ServerSocket

class NsdWifiActivity : ComponentActivity() {
    private var TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NsdTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    /*private fun startRegistration() {
        //  Create a string map containing information about your service.
        val record: Map<String, String> = mapOf(
            "listenport" to SERVER_PORT.toString(),
            "buddyname" to "John Doe${(Math.random() * 1000).toInt()}",
            "available" to "visible"
        )

        // Service information.  Pass it an instance name, service type
        // _protocol._transportlayer , and the map containing
        // information other devices will want once they connect to this one.
        val serviceInfo =
            WifiP2pDnsSdServiceInfo.newInstance("_test", "_presence._tcp", record)

        // Add the local service, sending the service info, network channel,
        // and listener that will be used to indicate success or failure of
        // the request.
        manager.addLocalService(channel, serviceInfo, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                // Command successful! Code isn't necessarily needed here,
                // Unless you want to update the UI or add logging statements.
            }

            override fun onFailure(arg0: Int) {
                // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
            }
        })
    }

    private val buddies = mutableMapOf<String, String>()

    private fun discoverService() {
        *//* Callback includes:
         * fullDomain: full domain name: e.g. "printer._ipp._tcp.local."
         * record: TXT record dta as a map of key/value pairs.
         * device: The device running the advertised service.
         *//*
        val txtListener = DnsSdTxtRecordListener { fullDomain, record, device ->
            Log.d(TAG, "DnsSdTxtRecord available -$record")
            record["buddyname"]?.also {
                buddies[device.deviceAddress] = it
            }
        }

        val servListener = DnsSdServiceResponseListener { instanceName, registrationType, resourceType ->
            // Update the device name with the human-friendly version from
            // the DnsTxtRecord, assuming one arrived.
            resourceType.deviceName = buddies[resourceType.deviceAddress] ?: resourceType.deviceName

            // Add to the custom adapter defined specifically for showing
            // wifi devices.
            val fragment = fragmentManager
                .findFragmentById(R.id.frag_peerlist) as WiFiDirectServicesList
            (fragment.listAdapter as WiFiDevicesAdapter).apply {
                add(resourceType)
                notifyDataSetChanged()
            }

            Log.d(TAG, "onBonjourServiceAvailable $instanceName")
        }

        manager.setDnsSdResponseListeners(channel, servListener, txtListener)
    }*/

}