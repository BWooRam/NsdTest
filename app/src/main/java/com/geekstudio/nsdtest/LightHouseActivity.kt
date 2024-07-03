package com.geekstudio.nsdtest

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.ivanempire.lighthouse.LighthouseClient
import com.ivanempire.lighthouse.LighthouseLogger
import com.ivanempire.lighthouse.models.Constants
import com.ivanempire.lighthouse.models.devices.AbridgedMediaDevice
import com.ivanempire.lighthouse.models.search.MulticastSearchRequest
import com.ivanempire.lighthouse.models.search.UnicastSearchRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LightHouseActivity : ComponentActivity() {
    private var TAG = javaClass.simpleName
    private var lighthouseClient: LighthouseClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lighthouseClient = LighthouseClient
            .Builder(this)
            .setLogger(object : LighthouseLogger() {

            }) // Optional: Setup a custom logging system
            .setRetryCount(3) // Optional: Retry sending packets 3 times (4 packets will be sent in total)
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            val multicastSearchRequest = MulticastSearchRequest(
                hostname = Constants.DEFAULT_MEDIA_HOST,
                mx = 2,
                searchTarget = "ssdp:all",
            )
            lighthouseClient?.discoverDevices(multicastSearchRequest)?.collect { deviceList: List<AbridgedMediaDevice> ->
                Log.d(TAG, "MulticastSearchRequest Got an updated device list: $deviceList")
            }
        }
    }

    private suspend fun startDiscovery() {
        lighthouseClient?.discoverDevices()
            ?.collect { deviceList: List<AbridgedMediaDevice> ->
                Log.d(TAG, "Got an updated device list: $deviceList")
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