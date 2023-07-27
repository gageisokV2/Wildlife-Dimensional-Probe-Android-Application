package com.example.wildlifeimager_ui_v1

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlin.reflect.KProperty


//private lateinit var enableBtLauncher: ActivityResultLauncher<Intent>

const val TAG = "launchActivity"

@SuppressLint("CustomSplashScreen")
class launchActivity : AppCompatActivity() {

    private val enableBtLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Bluetooth was enabled
            // Enable the connection button here
            Log.i(TAG, "Bluetooth enabled\n")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        } else {
            // Bluetooth was not enabled
            // Prompt again to enable Bluetooth
            Log.i(TAG, "Bluetooth disabled, prompting from enableBTlaunce\n")
            promptEnableBluetooth()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        val permission = Manifest.permission.BLUETOOTH_CONNECT
        val requestCode = 123 // choose any value

        if (ContextCompat.checkSelfPermission(this, permission)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        } else {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            enableBtLauncher.launch(enableBtIntent)
        }
        promptEnableBluetooth()
    }

    private val bluetoothAdapter: BluetoothAdapter by lazy {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    // Define the getValue method for the bluetoothAdapter delegate
    operator fun <T> Lazy<T>.getValue(thisRef: Any?, property: KProperty<*>): T {
        return value
    }

    override fun onResume() {
        super.onResume()
        if (!bluetoothAdapter.isEnabled) {
            promptEnableBluetooth()
        }
    }

    private fun promptEnableBluetooth() {
        if (!bluetoothAdapter.isEnabled) { //bluetooth not enabled
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE) //we want to enable
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Request Bluetooth permission
                val requestPermissionLauncher =
                    registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                        if (isGranted) {
                            Log.i(TAG, "Bluetooth permission granted\n")
                            enableBtLauncher.launch(enableBtIntent)
                        } else {
                            Log.i(TAG, "Bluetooth permission denied\n")
                        }
                    }
                requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT)
            } else {
                Log.i(TAG, "Bluetooth disabled\n")
                enableBtLauncher.launch(enableBtIntent)
            }
        } else {
            // Bluetooth is already enabled
            Log.i(TAG, "Bluetooth enabled\n")
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE) //we want to enable
            //tod
            enableBtLauncher.launch(enableBtIntent)
            // Enable the connection button here
        }
    }
}