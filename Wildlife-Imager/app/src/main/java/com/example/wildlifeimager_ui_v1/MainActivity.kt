package com.example.wildlifeimager_ui_v1

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.bluetooth.*
import android.bluetooth.BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
import android.bluetooth.BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.*
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ScrollView
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.*
import java.util.*
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

/**
 * Wildlife Dimensional Probe App
 *
 * Description: This Android Kotlin application is designed to communicate with a Wildlife Dimensional Probe
 *              using Bluetooth Low Energy (BLE) technology. It allows users to scan for and connect to the
 *              probe, send commands, receive data, and process the collected coordinates.
 *
 * Author: Gage Elenbaas
 * Version: 1.0
 * Last Updated: 7/26/2023
 *
 */

private const val RUNTIME_PERMISSION_REQUEST_CODE = 2


class MainActivity : AppCompatActivity() {

//    private lateinit var pointCloud: FloatArray
//    private var coordinateCount by Delegates.notNull<Int>()
//    private lateinit var connectionButton: Button
//    private lateinit var clearButton: Button
//    private lateinit var captureButton: Button
//    private lateinit var viewButton: Button
//    private lateinit var connectionState: TextView
//    private lateinit var deviceAddressText: TextView
//    private lateinit var scrollingTextView: TextView
//    private lateinit var textScroller: ScrollView
//    private lateinit var sliderVal: TextView
//    private lateinit var slider: SeekBar
//    private lateinit var fileKeyword: TextView
//
//    private var readDataIn = false
//    private val communicationServiceUUID: UUID =
//        UUID.fromString("0783b03e-8535-b5a0-7140-a304d2495cb7")
//    private val mobileToImagerCharacteristicUUID: UUID =
//        UUID.fromString("0783b03e-8535-b5a0-7140-a304d2495cba")
//    private var gatt: BluetoothGatt? = null
//    private var initialized = false
//    private val CccdUuid: UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
//    private val init1UUIDServ: UUID = UUID.fromString("866d3b04-e674-40dc-9c05-b7f91bec6e83")
//    private val init2UUIDServ: UUID = UUID.fromString("0783b03e-8535-b5a0-7140-a304d2495cb7")
//    private val init3UUIDServ: UUID = UUID.fromString("0783b03e-8535-b5a0-7140-a304d2495cb7")
//    private val init4UUIDServ: UUID = UUID.fromString("0783b03e-8535-b5a0-7140-a304d2495cb7")
//    private val init59UUIDServ: UUID = UUID.fromString("866d3b04-e674-40dc-9c05-b7f91bec6e83")
//    private val init1UUID: UUID = UUID.fromString("e2048b39-d4f9-4a45-9f25-1856c10d5639")
//    private val init2UUID: UUID = UUID.fromString("0783b03e-8535-b5a0-7140-a304d2495cb8")
//    private val init3UUID: UUID = UUID.fromString("0783b03e-8535-b5a0-7140-a304d2495cb9")
//    private val init4UUID: UUID = UUID.fromString("0783b03e-8535-b5a0-7140-a304d2495cb9")
//    private val init59UUID: UUID = UUID.fromString("914f8fb9-e8cd-411d-b7d1-14594de45425")
//    private val init14Data = byteArrayOf(0x01)
//    private val init5Data = byteArrayOf(0X41, 0X54, 0X2B, 0X42, 0X49, 0X4E, 0X52, 0X45, 0X51)
//    private val init9Data = byteArrayOf(0X0d, 0X0a, 0X4f, 0X4b, 0X0d, 0X0a, 0X00, 0X0A)
//    private val imagerName = "CLv2-CodeLess"//Change this name to the name of the Bluetooth device.
//    private lateinit var imagerAddress: String
//    private var foundDevice = false
//    private var connected = false
//    private var dataBuffer: StringBuilder = StringBuilder()
//    private var buffer: String = ""
//    private var isScanning: Boolean = false
//    private val handler = Handler(Looper.getMainLooper())
//    private var captureCount: String = "1" //initialize to 1


    private lateinit var pointCloud: FloatArray
    private var coordinateCount by Delegates.notNull<Int>()

    private lateinit var connectionButton: Button
    private lateinit var clearButton: Button
    private lateinit var captureButton: Button
    private lateinit var viewButton: Button
    private lateinit var connectionState: TextView
    private lateinit var deviceAddressText: TextView
    private lateinit var scrollingTextView: TextView
    private lateinit var textScroller: ScrollView
    private lateinit var sliderVal: TextView
    private lateinit var slider: SeekBar
    private lateinit var fileKeyword: TextView
    private lateinit var loadButton: Button
    private lateinit var filePickerLauncher: ActivityResultLauncher<Intent>

    private var readDataIn = false
    private val communicationServiceUUID: UUID =
        UUID.fromString("0783b03e-8535-b5a0-7140-a304d2495cb7")
    private val mobileToImagerCharacteristicUUID: UUID =
        UUID.fromString("0783b03e-8535-b5a0-7140-a304d2495cba")
    private var gatt: BluetoothGatt? = null
    private var initialized = false
    private val CccdUuid: UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
    private val init1UUIDServ: UUID = UUID.fromString("866d3b04-e674-40dc-9c05-b7f91bec6e83")
    private val init2UUIDServ: UUID = UUID.fromString("0783b03e-8535-b5a0-7140-a304d2495cb7")
    private val init3UUIDServ: UUID = UUID.fromString("0783b03e-8535-b5a0-7140-a304d2495cb7")
    private val init4UUIDServ: UUID = UUID.fromString("0783b03e-8535-b5a0-7140-a304d2495cb7")
    private val init59UUIDServ: UUID = UUID.fromString("866d3b04-e674-40dc-9c05-b7f91bec6e83")
    private val init1UUID: UUID = UUID.fromString("e2048b39-d4f9-4a45-9f25-1856c10d5639")
    private val init2UUID: UUID = UUID.fromString("0783b03e-8535-b5a0-7140-a304d2495cb8")
    private val init3UUID: UUID = UUID.fromString("0783b03e-8535-b5a0-7140-a304d2495cb9")
    private val init4UUID: UUID = UUID.fromString("0783b03e-8535-b5a0-7140-a304d2495cb9")
    private val init59UUID: UUID = UUID.fromString("914f8fb9-e8cd-411d-b7d1-14594de45425")
    private val init14Data = byteArrayOf(0x01)
    private val init5Data = byteArrayOf(0X41, 0X54, 0X2B, 0X42, 0X49, 0X4E, 0X52, 0X45, 0X51)
    private val init9Data = byteArrayOf(0X0d, 0X0a, 0X4f, 0X4b, 0X0d, 0X0a, 0X00, 0X0A)
    private val imagerName = "CLv2-CodeLess"
    private lateinit var imagerAddress: String

    private var foundDevice = false
    private var connected = false
    private var dataBuffer: StringBuilder = StringBuilder()
    private var buffer: String = ""
    private var isScanning: Boolean = false
    private val handler = Handler(Looper.getMainLooper())
    private var captureCount: String = "1"
    private var fileContent: String = ""


    @SuppressLint("MissingPermission")
    val stopScanning: Runnable = Runnable {
        bleScanner.stopScan(scanCallback)
        runOnUiThread {
            scrollingTextView.append(">>The device was not found within the time limit.\n")
            scrollingTextView.append(">>Move closer and try again\n")
        }
        uiDisconnected()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        connectionButton = findViewById(R.id.connectButton)
        clearButton = findViewById(R.id.clearButton)
        captureButton = findViewById(R.id.captureButton)
        scrollingTextView = findViewById(R.id.scrollTextView)
        textScroller = findViewById(R.id.scrollView2)
        viewButton = findViewById(R.id.viewButton)
        sliderVal = findViewById(R.id.scrollBarVal)
        slider = findViewById(R.id.Scrollbar)
        fileKeyword = findViewById(R.id.keywordEditText)
        loadButton = findViewById(R.id.load_button)

        coordinateCount = 0 //initialize to 0
        pointCloud = FloatArray(2688)

        connectionButton.isEnabled = true
        captureButton.isEnabled = false
        viewButton.isEnabled = false

        var keyword: String = ""

        scrollingTextView.isEnabled = true
        sliderVal.text = "Number of captures: 1"

//        runOnUiThread{viewButton.isEnabled = true} //TODO TEMPORARY REMOVE AFTER USE

        // Initialize the filePickerLauncher
        filePickerLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    // Handle the selected file here
                    if (data != null) {
                        val selectedFileUri = data.data
                        // Read the file content using Kotlin Coroutine
                        selectedFileUri?.let { uri ->
                            GlobalScope.launch(Dispatchers.IO) {
                                fileContent = readTextFromUri(uri)
                                // Now you have the file content, you can process it as needed
                                // Example: displayFileContent(fileContent)
                                val status = checkLoadedFile(fileContent)
                                if (status) {
                                    runOnUiThread {
                                        scrollingTextView.append(">>Data successfully loaded in. You may now view the data.\n")
                                        viewButton.isEnabled = true
                                    }
                                }
                                else{
                                    runOnUiThread {
                                        scrollingTextView.append(">>Invalid data. Failed to load.\n")
                                        viewButton.isEnabled = false
                                    }
                                }
                            }
                        }
                    }
                }
            }

        connectionButton.setOnClickListener {
            // Define the action to be taken when the connectionButton is clicked
            if (connected) { //theoretically should never be able to reach this condition with enabling and disabling
                runOnUiThread {
                    scrollingTextView.append(">>The device is already connected!\n")
                    connectionButton.text = "Connected"
                    connectionButton.isEnabled = false
                }
                stopBleScan()
            } else if (!isScanning and !connected) {
                runOnUiThread {
                    scrollingTextView.append(">>Scanning for dimensional probe\n")
                    connectionButton.isEnabled = false //disable so they don't double click
                    connectionButton.text = "Scanning"
                }

                startBleScan() //will start the entire connection sequence
            }
        }

        loadButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type =
                "text/plain" // Set the MIME type here to filter file types, e.g., "image/*" for images
            filePickerLauncher.launch(intent)
        }

        scrollingTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used in this case
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not used in this case
            }

            override fun afterTextChanged(s: Editable?) {
                val appendedText = s?.toString()
                if (appendedText != null && appendedText.isNotEmpty()) {
                    // Code to execute when text gets appended to the scrollingTextView
                    // Place your desired logic here
                    textScroller.post {
                        textScroller.fullScroll(View.FOCUS_DOWN)
                    }
                }
            }
        })

        viewButton.setOnClickListener {
            if (viewButton.isEnabled) {
                val intent = Intent(this, ViewActivity::class.java)
                intent.putExtra("floatArray", pointCloud)
                startActivity(intent)
            } else {
                runOnUiThread { scrollingTextView.append(">>You have not collected data yet!\n") }
            }
        }

        clearButton.setOnClickListener {
            runOnUiThread { scrollingTextView.text = ">>Log Cleared\n" }
        }

        //Set a click listener for the highPowerButton
        captureButton.setOnClickListener {
            keyword = fileKeyword.text.toString()
            if (keyword.isNotEmpty() && (keyword != " ") && !keyword.contains(" ") && keywordChecker()) {
                val imagerToMobileChar = gatt?.getService(communicationServiceUUID)
                    ?.getCharacteristic(mobileToImagerCharacteristicUUID)

                runOnUiThread { scrollingTextView.append(">>Collecting data...\n") }

                val data = asciiToHexByteArray(
                    "CAPTURE-".plus(keyword).plus("-").plus(captureCount).plus("\n")
                )

                if (imagerToMobileChar != null) {
                    writeCharacteristictoGatt(
                        imagerToMobileChar, data //ON /N
                    )
                }
            } else {
                runOnUiThread {
                    scrollingTextView.append(
                        ">>Invalid file keyword" +
                                "\nExample valid keywords:\noak\npine2\ntree4\n" +
                                "Any 10 character string with no spaces, or special characters that " +
                                "cannot be in a regular file name\n"
                    )
                }
            }
        }

        slider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?, progress: Int, fromUser: Boolean
            ) {
                sliderVal.text = "Number of captures: $progress"
                captureCount = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    private fun checkLoadedFile(data: String): Boolean {
        var empty = true
        pointCloud = FloatArray(2688)
        val pattern = Regex("x:(-?\\d+\\.\\d+) y:(-?\\d+\\.\\d+) z:(-?\\d+\\.\\d+)")
        val matches = pattern.findAll(data)

        for (match in matches) {
            val (x, y, z) = match.destructured
            val tripleCoords = Triple(x.toFloat(), y.toFloat(), z.toFloat())
            processCoordinates(tripleCoords)
        }

        for (element in pointCloud) {
            if (element != 0.0f) {
                return true
            }
        }
        return false
    }

    /**
     * Checks if the given input text contains any forbidden characters that are not allowed as keywords.
     *
     * The function uses a regular expression pattern to match forbidden characters in the input text.
     * The pattern includes the following characters: \ / : * ? " < > | { } % $ # @ ! ` ~
     *
     * @return true if the input text is free from forbidden characters, false otherwise.
     */
    private fun keywordChecker(): Boolean {
        val pattern = Regex("[\\\\/:*?\"<>|{}|%$#@!`~]")
        return !pattern.containsMatchIn(fileKeyword.text.toString())
    }

    private suspend fun readTextFromUri(uri: Uri): String = withContext(Dispatchers.IO) {
        val inputStream = contentResolver.openInputStream(uri)
        inputStream?.use { stream ->
            stream.reader().use { reader ->
                return@withContext reader.readText()
            }
        }
        return@withContext ""
    }

    /**
     * Checks if the current Android device has been granted a specific runtime permission.
     *
     * @param permissionType The type of permission to check for. For example, Manifest.permission.BLUETOOTH_SCAN.
     *
     * @return true if the specified permission is granted, false otherwise.
     */
    private fun Context.hasPermission(permissionType: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this, permissionType
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Checks whether the current Android device has the required runtime permissions.
     *
     * On Android 12 (API level 31) and above (Build.VERSION_CODES.S), the required permissions
     * are BLUETOOTH_SCAN and BLUETOOTH_CONNECT.
     *
     * On Android versions prior to Android 12, the required permission is ACCESS_FINE_LOCATION.
     *
     * @return true if the required permissions are granted, false otherwise.
     */
    private fun Context.hasRequiredRuntimePermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            hasPermission(Manifest.permission.BLUETOOTH_SCAN) && hasPermission(Manifest.permission.BLUETOOTH_CONNECT)
        } else {
            hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    /**
     * Updates the UI when a successful connection to a device is established.
     *
     * This function should be called from a background thread using runOnUiThread() to update
     * the UI components safely from a non-UI thread.
     *
     * The function appends a success message to the scrollingTextView, sets the connectionButton
     * text to "Connected" and disables it, disables the captureButton, sets the connectionState
     * text to "Connected" and changes its background color to a shade of green (#6aa84f).
     *
     * The function also sets the foundDevice and connected flags to true to reflect the successful
     * connection state.
     */
    private fun uiConnected() {
        runOnUiThread {
            scrollingTextView.append(">>Connected successfully!\n")
            connectionButton.text = "Connected"
            connectionButton.isEnabled = false
            captureButton.isEnabled = false
            connectionState = findViewById<Button>(R.id.connection_state)
            connectionState.text = "Connected"
            connectionState.setBackgroundColor(Color.parseColor("#6aa84f"))
        }
        foundDevice = true
        connected = true
    }

    /**
     * Updates the UI when the connection to a device is disconnected.
     *
     * This function should be called from a background thread using runOnUiThread() to update
     * the UI components safely from a non-UI thread.
     *
     * The function appends a "Disconnected" message to the scrollingTextView, sets the connectionButton
     * text to "Connect" and enables it, disables the captureButton, sets the connectionState
     * text to "Disconnected" and changes its background color to a shade of red (#f44336).
     *
     * The function also sets the isScanning, foundDevice, and connected flags to false to reflect
     * the disconnection state.
     */
    private fun uiDisconnected() {
        runOnUiThread {
            scrollingTextView.append(">>Disconnected\n")
            connectionButton.text = "Connect"
            connectionButton.isEnabled = true
            captureButton.isEnabled = false
            connectionState = findViewById<Button>(R.id.connection_state)
            connectionState.text = "Disconnected"
            connectionState.setBackgroundColor(Color.parseColor("#f44336"))
        }
        isScanning = false
        foundDevice = false
        connected = false
    }


    /**                          SCANNING                                                      */

    /**
     * Starts Bluetooth Low Energy (BLE) scanning for nearby devices.
     *
     * This function checks if the required runtime permissions for BLE scanning are granted.
     * If the permissions are not granted, it will request them using the requestRelevantRuntimePermissions() function.
     * If the permissions are already granted, it will start the BLE scanning process using the provided bleScanner,
     * scanSettings, and scanCallback objects. The scanning will last for 5 seconds.
     *
     * Note: This function assumes that the necessary permission "ACCESS_FINE_LOCATION" or "BLUETOOTH_SCAN"
     * is already granted since it is indicated by the @SuppressLint("MissingPermission") annotation.
     *
     * The isScanning flag is set to true while scanning is in progress.
     */
    @SuppressLint("MissingPermission") //we will already have this permission
    private fun startBleScan() {
        if (!hasRequiredRuntimePermissions()) {
            requestRelevantRuntimePermissions()
        } else {
            bleScanner.startScan(null, scanSettings, scanCallback)
            isScanning = true
            handler.postDelayed(stopScanning, 5000)
        }
    }


    /**
     * Stops the ongoing Bluetooth Low Energy (BLE) scanning for nearby devices.
     *
     * This function stops the BLE scanning process using the provided bleScanner and scanCallback objects.
     * It also sets the isScanning flag to false to indicate that scanning has been stopped.
     *
     * Note: This function assumes that the necessary permission "ACCESS_FINE_LOCATION" or "BLUETOOTH_SCAN"
     * is already granted since it is indicated by the @SuppressLint("MissingPermission") annotation.
     */
    @SuppressLint("MissingPermission")          //we will already have this permission
    private fun stopBleScan() {
        bleScanner.stopScan(scanCallback)
        isScanning = false
    }

    /**
     * Lazily initializes the BluetoothAdapter instance.
     *
     * The function retrieves the BluetoothManager service using the getSystemService() method,
     * casts it to a BluetoothManager, and then obtains the BluetoothAdapter from it. The obtained
     * BluetoothAdapter is stored in the lazy property to ensure that it is only initialized once
     * when it is first accessed.
     *
     * @return The BluetoothAdapter instance for the current Android device.
     */
    private val bluetoothAdapter: BluetoothAdapter by lazy {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    /**
     * Custom implementation of the getValue operator function for Lazy delegation.
     *
     * This function is used in conjunction with the Lazy delegate to extract the stored value from the
     * Lazy instance when the property is accessed.
     *
     * @param thisRef The instance of the class where the Lazy delegate is used.
     * @param property The metadata for the property being accessed.
     *
     * @return The stored value of the Lazy instance.
     */
    operator fun <T> Lazy<T>.getValue(thisRef: Any?, property: KProperty<*>): T {
        return value
    }

    /**
     * Lazily initializes the Bluetooth Low Energy (BLE) scanner using the BluetoothAdapter instance.
     *
     * The function retrieves the Bluetooth Low Energy (BLE) scanner from the previously initialized
     * BluetoothAdapter instance. It is stored in the lazy property to ensure that it is only
     * initialized once when it is first accessed.
     *
     * @return The Bluetooth Low Energy (BLE) scanner for the current Android device.
     */
    private val bleScanner by lazy {
        bluetoothAdapter.bluetoothLeScanner
    }

    /**

    Lazily initializes and returns the [ScanSettings] instance for BLE scanning by creating a new instance of [ScanSettings.Builder],
    setting the scan mode to [ScanSettings.SCAN_MODE_LOW_LATENCY], and building the settings.
    The initialization of the [ScanSettings] is delayed until the first access to this property.
     *@see ScanSettings.Builder.setScanMode
     *@see ScanSettings.Builder.build
     */
    private val scanSettings =
        ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build()

    /**
     * ScanCallback implementation to handle BLE device scanning results.
     *
     * The callback receives information about nearby BLE devices that are discovered during scanning.
     * When a relevant device (Wildlife Dimensional Probe) is found, the scanning is stopped, and a
     * connection attempt is made using connectGatt1() function.
     *
     * Note: This class assumes that the necessary permission "ACCESS_FINE_LOCATION" or "BLUETOOTH_SCAN"
     * is already granted since it is indicated by the @SuppressLint("MissingPermission") annotation.
     *
     * @see connectGatt1
     */
    @SuppressLint("MissingPermission") // App's role to ensure permissions are available
    private val scanCallback = object : ScanCallback() {
        @SuppressLint("SetTextI18n")
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            with(result.device) {
                Log.i(
                    "ScanCallback", "Found a Device! Name: ${name ?: "Unnamed"}, address: $address"
                )
                val name = name ?: "Unnamed"
                imagerAddress = address
                if ((name == imagerName)) {//) and (address == imagerAddress)) {
                    //end scanning, we have what we need
                    stopBleScan() //stop scanning once we find the device
                    foundDevice = true
                    handler.removeCallbacks(stopScanning)
                    runOnUiThread {
                        deviceAddressText = findViewById<Button>(R.id.deviceAddressText)
                        deviceAddressText.text = imagerAddress
                        deviceAddressText.setBackgroundColor(Color.parseColor("#3d85c6"))
                        scrollingTextView.append(">>A Wildlife Dimensional Probe has been found\n")
                        scrollingTextView.append(">>Attempting Bluetooth connection...\n")
                        connectionButton.text = "Connecting"
                    }
                    connectGatt1()
                }
            }
        }
    }


    /**                          BLUETOOTH CONNECTIVITY                                          */

    /**
     * Initiates a Bluetooth GATT connection with the desired BLE device.
     *
     * This function uses the BluetoothAdapter instance to connect to the BLE device specified by its address,
     * which is stored in the "imagerAddress" property. The connection is established with autoConnect set to false.
     * The GATT connection process will be handled by the gattCallback object.
     *
     * Note: This function assumes that the necessary permission "ACCESS_FINE_LOCATION" or "BLUETOOTH_CONNECT"
     * is already granted as it is required to connect to BLE devices.
     *
     * @see gattCallback
     */
    private fun connectGatt1() {
        connectGatt(this, false, gattCallback)
    }


    /**
     * Initiates a Bluetooth GATT connection with the desired BLE device.
     *
     * This function uses the BluetoothAdapter instance to connect to the BLE device specified by its address,
     * which is passed as the "imagerAddress" parameter. The connection is established with the autoConnect
     * parameter indicating whether the system should try to automatically connect to the device in the future
     * whenever it becomes available.
     *
     * Note: This function assumes that the necessary permission "BLUETOOTH_CONNECT" is already granted,
     * as it is required to initiate a GATT connection.
     *
     * @param context The application context used for the connection.
     * @param autoConnect If true, the system will automatically attempt to reconnect to the device when it becomes available.
     * @param callback The BluetoothGattCallback object that will handle the GATT connection events and operations.
     *
     * @see BluetoothGattCallback
     */
    private fun connectGatt(
        context: Context, autoConnect: Boolean, callback: BluetoothGattCallback
    ) {
        val device =
            bluetoothAdapter.getRemoteDevice(imagerAddress)                // replace with the appropriate address
        gatt = if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        } else {
            device.connectGatt(context, autoConnect, callback)
        }
    }


    /**
     * BluetoothGattCallback implementation to handle Bluetooth GATT events and operations.
     *
     * This callback provides various override methods that handle different GATT events, such as connection state changes,
     * service discovery, characteristic reading, characteristic writing, notifications, etc.
     *
     * Note: The function uses various utility methods (hexToAscii, processReceivedData, etc.) and global variables
     *
     * @see hexToAscii
     * @see processReceivedData
     */
    private val gattCallback = object : BluetoothGattCallback() {
        @SuppressLint("MissingPermission") //I DO NOT CARE

        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            val deviceAddress = gatt.device.address

            if (status == BluetoothGatt.GATT_SUCCESS) {
                val bluetoothGatt = gatt
                bluetoothGatt.discoverServices()
                bluetoothGatt.discoverServices()
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    uiConnected()
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    gatt.close()
                    uiDisconnected()
                }
            } else {
                runOnUiThread {
                    scrollingTextView.append(">>ERROR $status encountered for $deviceAddress... Disconnecting...\n")
                }
                uiDisconnected()
                gatt.close()
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            with(gatt) {
                printGattTable()
            }
            //TODO use a different method of launch to avoid memory leaks
            GlobalScope.launch {
                initBinaryMode()
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic
        ) {
            with(characteristic) {
                val data = hexToAscii(value.toHexString())

                if ((data.uppercase().contains("COMMENCE")) || readDataIn) {
                    runOnUiThread { viewButton.isEnabled = false }

                    runOnUiThread { scrollingTextView.append("...") }
                    readDataIn = true
                    buffer += value.toHexString()
                }

                if (data.uppercase().contains("COMPLETED")) {
                    readDataIn = false
                    runOnUiThread { scrollingTextView.append(">>Starting data processing\n") }
                    try {
                        processReceivedData(hexToAscii(buffer)) // Process the complete data buffer

                        runOnUiThread { viewButton.isEnabled = true }
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }

                if (data.uppercase().contains("ERROR") || data.uppercase().contains("MESSAGE")) {
                    runOnUiThread {
                        scrollingTextView.append("$data")
                    }
                }
            }
        }


        override fun onCharacteristicRead(
            gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int
        ) {
            with(characteristic) {
                when (status) {
                    BluetoothGatt.GATT_SUCCESS -> {
                        val message = hexToAscii(value.toHexString())
                        runOnUiThread { scrollingTextView.append(">>Read: $message\n") }
                    }
                    BluetoothGatt.GATT_READ_NOT_PERMITTED -> {
                        runOnUiThread { scrollingTextView.append(">>Read not permitted for $uuid!\n") }
                    }
                    else -> {
                        runOnUiThread { scrollingTextView.append(">>Characteristic read failed for $uuid, error: $status\n") }
                    }
                }
            }
        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int
        ) {
            with(characteristic) {
                when (status) {
                    BluetoothGatt.GATT_SUCCESS -> {
                        runOnUiThread {
                            scrollingTextView.append(">>Wrote: ${hexToAscii(value.toHexString())}\n")
                        }
                    }
                    BluetoothGatt.GATT_INVALID_ATTRIBUTE_LENGTH -> {
                        runOnUiThread { scrollingTextView.append(">>Write exceeded connection ATT MTU!\n") }
                    }
                    BluetoothGatt.GATT_WRITE_NOT_PERMITTED -> {
                        runOnUiThread { scrollingTextView.append(">>Write not permitted! $uuid, error: $status\n") }
                    }
                    else -> {
                        runOnUiThread { scrollingTextView.append(">>Characteristic write failed for $uuid, error: $status\n") }
                    }
                }
            }
        }

        override fun onMtuChanged(gatt: BluetoothGatt, mtu: Int, status: Int) {
            scrollingTextView.append(">>ATT MTU changed to $mtu, success: ${status == BluetoothGatt.GATT_SUCCESS}\n")
        }

        override fun onDescriptorWrite(
            gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int
        ) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // Descriptor write operation successful
                if (descriptor != null) {
                    if (descriptor.uuid == CccdUuid) {
                        val enabled = Arrays.equals(
                            descriptor.value, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                        )
                        if (enabled) {
                            // Notifications enabled for characteristic
                            runOnUiThread { scrollingTextView.append(">>Notification enabled\n") }
                        } else {
                            // Notifications disabled for characteristic
                            runOnUiThread { scrollingTextView.append(">>Notification disabled\n") }
                        }
                    }
                }
            } else {
                runOnUiThread { scrollingTextView.append(">>Descriptor write failed with status: $status\n") }
            }
        }

    }

    /**
     * Processes received data containing sets of coordinates.
     *
     * This function takes the received data as a String and appends it to the dataBuffer,
     * which is used to accumulate incoming data. The function then searches for a complete set of coordinates
     * within the dataBuffer, identified by ">>" or "#Finished" delimiters. Once a complete set is found,
     * it is extracted from the dataBuffer, and the processed data is removed from the buffer.
     *
     * The extracted complete coordinates are then parsed using the parseCoordinates function,
     * and the resulting coordinates are processed using the processCoordinates function.
     *
     *
     * @param data The received data containing sets of coordinates.
     *
     * @see parseCoordinates
     * @see processCoordinates
     */
    fun processReceivedData(data: String) {
        // Append the data to the buffer
        dataBuffer.append(data)

        // Check if a complete set of coordinates is available in the buffer
        while (true) {
            var completeCoordinateIndex = dataBuffer.indexOf(">>")
            if (completeCoordinateIndex == -1) {
                completeCoordinateIndex = dataBuffer.indexOf("#Finished")
            }
            if (completeCoordinateIndex >= 0) {
                val completeCoordinates = dataBuffer.substring(0, completeCoordinateIndex).trim()
                dataBuffer = dataBuffer.delete(
                    0, completeCoordinateIndex + 2
                ) // Remove the processed data from the buffer

                // Parse and process the complete coordinates
                val coordinates = parseCoordinates(completeCoordinates)
                if (coordinates != null) {
                    processCoordinates(coordinates)
                }
            } else {
                // No complete set of coordinates found, exit the loop
                buffer = ""
                dataBuffer.clear()
                coordinateCount = 0
                break
            }
        }
    }

    /**
     * Parses a string containing coordinates and extracts the x, y, and z values.
     *
     * This function takes a string containing coordinate data and searches for the "x:", "y:", and "z:" markers
     * to identify the start of each coordinate value. It then extracts the corresponding numerical values and returns them
     * as a Triple<Float, Float, Float> representing the x, y, and z coordinates, respectively.
     *
     * Note: The function assumes that the provided coordinate string is well-formed and contains the "x:", "y:", and "z:" markers.
     * If any of these markers are missing, or if the coordinate values cannot be parsed as floats, the function returns null,
     * indicating that the coordinate data is incomplete or invalid.
     *
     * @param coordinatesString The string containing the coordinates in the format "x:<value>\ny:<value>\nz:<value>#".
     *
     * @return A Triple<Float, Float, Float> containing the extracted x, y, and z coordinates, or null if the coordinates are incomplete or invalid.
     */
    private fun parseCoordinates(coordinatesString: String): Triple<Float, Float, Float>? {
        val xIndex = coordinatesString.indexOf("x:")
        val yIndex = coordinatesString.indexOf("y:")
        val zIndex = coordinatesString.indexOf("z:")

        if (xIndex < 0 || yIndex < 0 || zIndex < 0) {
            // Incomplete coordinates
            return null
        }

        val xEndIndex = coordinatesString.indexOf('\n', xIndex)
        val yEndIndex = coordinatesString.indexOf('\n', yIndex)
        val zEndIndex = coordinatesString.indexOf('#', zIndex)

        if (xEndIndex < 0 || yEndIndex < 0 || zEndIndex < 0) {
            // Incomplete coordinates
            return null
        }

        val x = coordinatesString.substring(xIndex + 2, xEndIndex).toFloat()
        val y = coordinatesString.substring(yIndex + 2, yEndIndex).toFloat()
        val z = coordinatesString.substring(zIndex + 2, zEndIndex).toFloat()

        if ((x == null) || (y == null) || (z == null)) {
            // Invalid coordinates
            return null
        }

        return Triple(x, y, z)
    }

    /**
     * Processes a set of coordinates and stores them in the pointCloud array.
     *
     * This function takes a Triple<Float, Float, Float> representing the x, y, and z coordinates,
     * and stores them in the pointCloud array. The array is structured in a sequential manner, where
     * every three consecutive elements represent one set of (x, y, z) coordinates.
     *
     * The function extracts the x, y, and z coordinates from the Triple, and then updates the
     * pointCloud array at the appropriate index positions to store the coordinates. The variable
     * coordinateCount keeps track of the current position in the array where the coordinates are stored.
     * After storing the coordinates, the coordinateCount is increased by 3 to move to the next set of coordinates.
     *
     * @param coordinates The Triple<Float, Float, Float> representing the x, y, and z coordinates to be processed and stored.
     */
    private fun processCoordinates(coordinates: Triple<Float, Float, Float>) {
        val x = coordinates.first
        val y = coordinates.second
        val z = coordinates.third

        pointCloud[coordinateCount] = x
        pointCloud[coordinateCount + 1] = y
        pointCloud[coordinateCount + 2] = z
        coordinateCount += 3 //increase by 3
    }

    /**
     * Converts a hexadecimal string to an ASCII string.
     *
     * This function takes a hexadecimal string as input and converts it into an ASCII string.
     * It processes the input string in pairs of characters (two characters representing one byte),
     * converting each pair to its corresponding ASCII character and appending it to the result.
     *
     * @param hexString The hexadecimal string to be converted to ASCII.
     * @return The corresponding ASCII string representation of the input hexadecimal string.
     */
    fun hexToAscii(hexString: String): String {
        val sb = StringBuilder()
        var i = 0
        while (i < hexString.length) {
            val hex = hexString.substring(i, i + 2)
            val decimal = hex.toInt(16)
            sb.append(decimal.toChar())
            i += 2
        }
        return sb.toString()
    }


    /**
     * Checks if the BluetoothGattCharacteristic supports reading (PROPERTY_READ).
     *
     * @return True if the characteristic is readable, false otherwise.
     */
    private fun BluetoothGattCharacteristic.isReadable(): Boolean =
        containsProperty(BluetoothGattCharacteristic.PROPERTY_READ)


    /**
     * Checks if the BluetoothGattCharacteristic supports notification (PROPERTY_NOTIFY).
     *
     * @return True if the characteristic is notifiable, false otherwise.
     */
    private fun BluetoothGattCharacteristic.isNotifiable(): Boolean =
        containsProperty(BluetoothGattCharacteristic.PROPERTY_NOTIFY)

    /**
     * Checks if the BluetoothGattCharacteristic supports indication (PROPERTY_INDICATE).
     *
     * @return True if the characteristic is indicatable, false otherwise.
     */
    private fun BluetoothGattCharacteristic.isIndicatable(): Boolean =
        containsProperty(BluetoothGattCharacteristic.PROPERTY_INDICATE)

    /**
     * Checks if the BluetoothGattCharacteristic supports writing with a response (PROPERTY_WRITE).
     *
     * @return True if the characteristic is writable with a response, false otherwise.
     */
    private fun BluetoothGattCharacteristic.isWritable(): Boolean =
        containsProperty(BluetoothGattCharacteristic.PROPERTY_WRITE)

    /**
     * Checks if the BluetoothGattCharacteristic supports writing without a response (PROPERTY_WRITE_NO_RESPONSE).
     *
     * @return True if the characteristic is writable without a response, false otherwise.
     */
    private fun BluetoothGattCharacteristic.isWritableWithoutResponse(): Boolean =
        containsProperty(BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)

    /**
     * Checks if the BluetoothGattCharacteristic contains the specified property.
     *
     * @param property The property to check (e.g., PROPERTY_READ, PROPERTY_WRITE, PROPERTY_NOTIFY).
     * @return True if the characteristic contains the specified property, false otherwise.
     */
    private fun BluetoothGattCharacteristic.containsProperty(property: Int): Boolean {
        return properties and property != 0
    }

    /**
     * Writes data to a BluetoothGattCharacteristic and sends it to the GATT server.
     *
     * This function takes a BluetoothGattCharacteristic and a payload (as a ByteArray) and writes the payload to the characteristic.
     * The function determines the appropriate write type (WRITE_TYPE_DEFAULT or WRITE_TYPE_NO_RESPONSE) based on the characteristics
     * writability properties. It then sets the write type and payload to the characteristic and writes the characteristic to the GATT server.
     *
     * If the characteristic is writable with a response (PROPERTY_WRITE), a write request (WRITE_TYPE_DEFAULT) is used.
     * If the characteristic is writable without a response (PROPERTY_WRITE_NO_RESPONSE), a write command (WRITE_TYPE_NO_RESPONSE) is used.
     * If the characteristic is neither writable nor writable without a response, an error is thrown.
     *
     * @param characteristic The BluetoothGattCharacteristic to write data to.
     * @param payload The data to be written to the characteristic as a ByteArray.
     * @throws IllegalArgumentException if the characteristic cannot be written to due to unsupported properties.
     * @throws IllegalStateException if not connected to a BLE device (gatt is null).
     *
     * @see WRITE_TYPE_DEFAULT
     * @see WRITE_TYPE_NO_RESPONSE
     */
    @SuppressLint("MissingPermission")
    fun writeCharacteristictoGatt(characteristic: BluetoothGattCharacteristic, payload: ByteArray) {
        val writeType = when {
            characteristic.isWritable() -> WRITE_TYPE_DEFAULT //WRITE REQUEST
            characteristic.isWritableWithoutResponse() -> {
                WRITE_TYPE_NO_RESPONSE //WRITE COMMAND
            }
            else -> error("Characteristic ${characteristic.uuid} cannot be written to")
        }
        gatt?.let { gatt ->
            characteristic.writeType = writeType
            characteristic.value = payload
            gatt.writeCharacteristic(characteristic)
        } ?: error("Not connected to a BLE device!")
    }

    /**
     * Writes the Client Characteristic Configuration Descriptor (CCCD) for notification or indication.
     *
     * This function writes the Client Characteristic Configuration Descriptor (CCCD) for notification or indication
     * based on the properties of the given BluetoothGattCharacteristic.
     *
     * It retrieves the CCCD descriptor associated with the characteristic and sets its value accordingly:
     * - If the characteristic is indicatable (PROPERTY_INDICATE), it enables indication by setting the descriptor value to ENABLE_INDICATION_VALUE.
     * - If the characteristic is notifiable (PROPERTY_NOTIFY), it enables notification by setting the descriptor value to ENABLE_NOTIFICATION_VALUE.
     *
     * The function then registers for notifications (if the characteristic is notifiable) using setCharacteristicNotification()
     * and writes the descriptor to the GATT server using writeDescriptor().
     *
     * Note: The CCCD descriptor is used to enable notifications and indications for a specific characteristic. The enabled notifications
     * or indications will result in callbacks (e.g., onCharacteristicChanged()) being triggered when the characteristic's value changes.
     *
     * @param characteristic The BluetoothGattCharacteristic for which to write the CCCD descriptor.
     *
     * @see BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
     * @see BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
     */
    @SuppressLint("MissingPermission")
    fun writeCccdDescriptor(
        characteristic: BluetoothGattCharacteristic
    ) {
        val descriptor = characteristic.getDescriptor(CccdUuid)
        if (characteristic.isIndicatable()) {
            descriptor?.value = BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
        } else if (characteristic.isNotifiable()) {
            descriptor?.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            gatt?.setCharacteristicNotification(characteristic, true)
        }
        gatt?.writeDescriptor(descriptor)
    }


    /**
     * Converts a ByteArray to a hexadecimal string representation.
     *
     * This extension function takes a ByteArray and converts it into a hexadecimal string representation.
     * It processes each byte of the ByteArray and formats it as a two-digit hexadecimal value using the "%02x" format specifier.
     * The resulting hexadecimal values are then joined together to form the final hexadecimal string.
     *
     * @return The hexadecimal string representation of the ByteArray.
     */
    private fun ByteArray.toHexString(): String {
        return joinToString("") { "%02x".format(it) }
    }


    /**
     * Initializes binary mode communication with the CodeLess Peripheral.
     *
     * This function is a suspend function that should be called from a coroutine.
     * It initializes the binary mode communication with the CodeLess Peripheral by enabling notifications
     * and writing specific data to the relevant characteristics of the GATT server.
     * It sets up the flow control, reads responses, and sends commands to the CodeLess Peripheral.
     *
     * The function uses delay() to introduce a pause between operations to allow time for responses and processing.
     * The exact sleep duration (in milliseconds) is specified by the 'sleep' variable, which controls the delay time.
     *
     * Note: The function assumes that the necessary permissions are handled appropriately in the surrounding code
     *       and includes '@SuppressLint("MissingPermission")' to suppress lint warnings related to missing permissions
     *       for Bluetooth operations.
     */
    @SuppressLint("MissingPermission")
    private suspend fun initBinaryMode() = withContext(Dispatchers.IO) {
        val char1 = gatt?.getService(init1UUIDServ)?.getCharacteristic(init1UUID)
        val char2 = gatt?.getService(init2UUIDServ)?.getCharacteristic(init2UUID)
        val char3 = gatt?.getService(init3UUIDServ)?.getCharacteristic(init3UUID)
        val char4 = gatt?.getService(init4UUIDServ)?.getCharacteristic(init4UUID)
        val char5 = gatt?.getService(init59UUIDServ)?.getCharacteristic(init59UUID)
        val char6 = gatt?.getService(init59UUIDServ)?.getCharacteristic(init59UUID)
        val char7 = gatt?.getService(init59UUIDServ)?.getCharacteristic(init59UUID)
        val char8 = gatt?.getService(init59UUIDServ)?.getCharacteristic(init59UUID)
        val char9 = gatt?.getService(init59UUIDServ)?.getCharacteristic(init59UUID)
        val sleep = 75
        val currentTime: Calendar = Calendar.getInstance()
        val year: Int = currentTime.get(Calendar.YEAR)
        val day: Int = currentTime.get(Calendar.DAY_OF_MONTH)
        val month: Int = currentTime.get(Calendar.MONTH)
        val hour: Int = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute: Int = currentTime.get(Calendar.MINUTE)
        val second: Int = currentTime.get(Calendar.SECOND)

        /**
         * Enable the CodeLess Flow Control notification. The +COMMAND MODE SUPPORTED string will be printed to the console of the CodeLess
        peripheral as an unsolicited message
         */
        if (char1 != null) { //write to descriptor 0x01
            char1.writeType = WRITE_TYPE_DEFAULT
            writeCccdDescriptor(char1)
            delay(sleep.toLong())
        }
        /**
         * Enable the DSPS Server Tx notification.The +BINARY MODE SUPPORTED string will be printed to the console of the CodeLess peripheral
        as an unsolicited message
         */
        if (char2 != null) {
            char2.writeType = WRITE_TYPE_DEFAULT
            writeCccdDescriptor(char2)
            delay(sleep.toLong())
        }
        /**
         * Enable the DSPS Flow Control notification
         */
        if (char3 != null) {
            char3.writeType = WRITE_TYPE_DEFAULT
            writeCccdDescriptor(char3)
            delay(sleep.toLong())
        }
        /**
         * Enable the DSPS Flow Control notification
         */
        if (char4 != null) {
            char4.writeType = WRITE_TYPE_NO_RESPONSE
            writeCharacteristictoGatt(char4, init14Data)
            delay(sleep.toLong())
        }
        /**
         * Write the CodeLess Inbound Command characteristic with the AT+BINREQ command sent from the mobile phone to the CodeLess Peripheral.The
        +BINREQ unsolicited message will be printed to the console of the CodeLess peripheral. A notification will be sent to the mobile phone application
        at this point.The CodeLess Outbound Command characteristic must be read.
         */
        if (char5 != null) { //write binreq
            char5.writeType = WRITE_TYPE_DEFAULT
            writeCharacteristictoGatt(char5, init5Data)
            delay(sleep.toLong())
        }
        /**
         * Read the CodeLess Outbound Command characteristic. An OK (0x 0d 0a 4f 4b 0d 0a) is expected in the Read Response. This must be read otherwise
        it is not possible to advance to the next stage.
         */
        if (char6 != null) {
            gatt?.readCharacteristic(char6)
            delay(sleep.toLong())
        }

        val testData = //AT + BINREQACK
            byteArrayOf(
                0X41, 0X54, 0X2B, 0X42, 0X49, 0X4E, 0X52, 0X45, 0X51, 0X41, 0X43, 0X4B, 0X0A
            )
        if (char5 != null) { //write binreq
            char5.writeType = WRITE_TYPE_DEFAULT
            writeCharacteristictoGatt(char5, testData)
            delay(sleep.toLong())
        }

        if (char6 != null) {
            gatt?.readCharacteristic(char6)
            delay(sleep.toLong())
        }
        if (char7 != null) {
            gatt?.readCharacteristic(char7) //unsure what to do here
            delay(sleep.toLong())
        }
        if (char8 != null) {
            gatt?.readCharacteristic(char8)
            delay(sleep.toLong())
        }
        if (char9 != null) {
            char9.writeType = WRITE_TYPE_DEFAULT
            writeCharacteristictoGatt(char9, init9Data)
            delay(sleep.toLong())
        }
        initialized = true

        runOnUiThread {
            scrollingTextView.append(">>Sending date and time\n")
            captureButton.isEnabled = true
        }
        delay(sleep.toLong())

        val imagerToMobileChar = gatt?.getService(communicationServiceUUID)
            ?.getCharacteristic(mobileToImagerCharacteristicUUID)

        if (imagerToMobileChar != null) {

            val dateTime = convertToHexAsciiByteArray(year, month, day, hour, minute, second)

            writeCharacteristictoGatt(
                imagerToMobileChar, dateTime
            )
        }
    }

    /**
     * Converts date and time components to a Hex ASCII ByteArray.
     *
     * This function takes date and time components (year, month, day, hour, minute, second) and constructs a Hex ASCII ByteArray
     * containing the formatted date and time information. The date and time components are first converted to strings,
     * and then combined to form the final date-time string representation in Hex ASCII format.
     * The resulting Hex ASCII ByteArray is returned.
     *
     * @param year The year component of the date.
     * @param month The month component of the date.
     * @param day The day component of the date.
     * @param hour The hour component of the time.
     * @param minute The minute component of the time.
     * @param second The second component of the time.
     * @return The Hex ASCII ByteArray containing the formatted date and time information.
     */
    private fun convertToHexAsciiByteArray(
        year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int
    ): ByteArray {

        val yearString = year.toString()
        val monthString = month.toString()
        val dayString = day.toString()
        val hourString = hour.toString()
        val minuteString = minute.toString()
        val secondString = second.toString()

        val totalString = StringBuilder().clear()
        totalString.append("TIME-")
        totalString.append(yearString.substring(2))
        totalString.append("-")
        totalString.append(monthString)
        totalString.append("-")
        totalString.append(dayString)
        totalString.append("-")
        totalString.append(hourString)
        totalString.append("-")
        totalString.append(minuteString)
        totalString.append("-")
        totalString.append(secondString)
        totalString.append("\n")

        val hexString = totalString.toString()

        return asciiToHexByteArray(hexString)
    }

    /**
     * Converts an ASCII string to a Hex ASCII ByteArray.
     *
     * This function takes an ASCII string as input and converts it to a Hex ASCII ByteArray representation.
     * Each character in the ASCII string is converted to its corresponding hexadecimal ASCII representation.
     * The resulting Hex ASCII ByteArray is returned.
     *
     * @param asciiString The ASCII string to be converted to Hex ASCII ByteArray.
     * @return The Hex ASCII ByteArray representation of the input ASCII string.
     */
    private fun asciiToHexByteArray(asciiString: String): ByteArray {
        val hexValues = asciiString.map { char -> char.code.toString(16) }
        return hexValues.map { hexValue -> hexValue.toInt(16).toByte() }.toByteArray()
    }


    /**                           PERMISSIONS CHECKING                                           */

    /**
     * Requests relevant runtime permissions based on the Android SDK version.
     *
     * This function checks if the app has the required runtime permissions. If the required permissions
     * are already granted, the function returns without any action. If the permissions are not granted,
     * the function requests the relevant runtime permissions based on the Android SDK version.
     *
     * For Android versions below Android 12 (SDK_INT < Build.VERSION_CODES.S), the function requests
     * the ACCESS_FINE_LOCATION permission, which is typically used for Bluetooth scanning purposes.
     *
     * For Android versions Android 12 and above (SDK_INT >= Build.VERSION_CODES.S), the function requests
     * the BLUETOOTH_SCAN and BLUETOOTH_CONNECT permissions, as they are required for Bluetooth scanning
     * and connecting to BLE devices.
     */
    private fun Activity.requestRelevantRuntimePermissions() {
        if (hasRequiredRuntimePermissions()) {
            return
        }
        when {
            Build.VERSION.SDK_INT < Build.VERSION_CODES.S -> {
                requestLocationPermission()
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                requestBluetoothPermissions()
            }
        }
    }

    /**
     * Requests the ACCESS_FINE_LOCATION permission.
     *
     * This function displays an AlertDialog on the UI thread to inform the user about the necessity of the
     * location permission for Bluetooth scanning on Android 6.0 (Marshmallow) and above. It provides a
     * brief explanation of the requirement and prompts the user to grant the location permission.
     * Once the user taps the "OK" button, the function requests the ACCESS_FINE_LOCATION permission
     * using ActivityCompat.requestPermissions() method with the appropriate request code.
     */
    private fun requestLocationPermission() {
        runOnUiThread {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Location permission required")
            builder.setMessage(
                "Starting from Android M (6.0), the system requires apps to be granted " + "location access in order to scan for BLE devices."
            )
            builder.setCancelable(false)
            builder.setPositiveButton("OK") { _, _ ->
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    RUNTIME_PERMISSION_REQUEST_CODE
                )
            }
            val dialog = builder.create()
            dialog.show()
        }
    }

    /**
     * Requests the BLUETOOTH_SCAN and BLUETOOTH_CONNECT permissions.
     *
     * This function displays an AlertDialog on the UI thread to inform the user about the necessity of the
     * Bluetooth permissions for scanning and connecting to BLE devices on Android 12 and above. It provides
     * a brief explanation of the requirement and prompts the user to grant the required permissions.
     * Once the user taps the "OK" button, the function requests the BLUETOOTH_SCAN and BLUETOOTH_CONNECT
     * permissions using ActivityCompat.requestPermissions() method with the appropriate request code.
     */
    private fun requestBluetoothPermissions() {
        runOnUiThread {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Bluetooth permissions required")
            builder.setMessage(
                "Starting from Android 12, the system requires apps to be granted " + "Bluetooth access in order to scan for and connect to BLE devices."
            )
            builder.setCancelable(false)
            builder.setPositiveButton("OK") { _, _ ->
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT
                    ), RUNTIME_PERMISSION_REQUEST_CODE
                )
            }
            val dialog = builder.create()
            dialog.show()
        }
    }

    /**
     * Called when the result of a permission request is available.
     *
     * This function handles the result of the permission request made with the request code
     * RUNTIME_PERMISSION_REQUEST_CODE. It checks the granted results for each requested permission
     * and takes appropriate actions based on the outcome.
     *
     * If any permission was permanently denied (containsPermanentDenial), it shows an AlertDialog
     * informing the user that the permissions are required for the application and asks them to
     * manually grant the permissions from the app settings. In this case, the application will be
     * shut down (finish()) as it cannot proceed without the required permissions.
     *
     * If any permission was denied (containsDenial), it requests the relevant runtime permissions
     * using the requestRelevantRuntimePermissions() function.
     *
     * If all requested permissions are granted and the app has the required runtime permissions
     * (allGranted && hasRequiredRuntimePermissions()), it starts the BLE scanning process using the
     * startBleScan() function.
     *
     * If none of the above cases match (e.g., unexpected scenario encountered when handling permissions),
     * it recreates the activity using the recreate() method to handle the unexpected state.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RUNTIME_PERMISSION_REQUEST_CODE -> {
                val containsPermanentDenial = permissions.zip(grantResults.toTypedArray()).any {
                    it.second == PackageManager.PERMISSION_DENIED && !ActivityCompat.shouldShowRequestPermissionRationale(
                        this, it.first
                    )
                }
                val containsDenial = grantResults.any { it == PackageManager.PERMISSION_DENIED }
                val allGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
                when {
                    containsPermanentDenial -> { //HANDLE PERMANENT DENIAL
                        runOnUiThread {
                            val builder = AlertDialog.Builder(this)
                            builder.setTitle("Permission Denied")
                            builder.setMessage(
                                "These permissions are required for this application. Please, " + "navigate to the app settings and manually grant permissions that were " + "permanently denied..."
                            )
                            builder.setCancelable(false)
                            builder.setPositiveButton("OK") { _, _ ->
                                finish() //SHUTDOWN APPLICATION
                            }
                            val dialog = builder.create()
                            dialog.show()
                        }
                    }
                    containsDenial -> {
                        requestRelevantRuntimePermissions()
                    }
                    allGranted && hasRequiredRuntimePermissions() -> {
                        startBleScan()
                    }
                    else -> {
                        // Unexpected scenario encountered when handling permissions
                        recreate()
                    }
                }
            }
        }
    }

    /**
     * Prints the GATT table for the connected BluetoothGatt instance.
     *
     * This function prints the GATT table containing information about the available services and their
     * associated characteristics of the connected BluetoothGatt instance. If no services and characteristics
     * are available, it logs an informational message indicating that `discoverServices()` might need to be
     * called first to retrieve the GATT information.
     *
     * The function iterates through each service and creates a characteristicsTable by joining the UUIDs
     * of the service's characteristics using the specified separator and prefix. It then logs the GATT
     * table information, including the service UUID and its associated characteristics.
     */
    private fun BluetoothGatt.printGattTable() {
        if (services.isEmpty()) {
            Log.i(
                "printGattTable",
                "No service and characteristic available, call discoverServices() first?"
            )
            return
        }
        services.forEach { service ->
            val characteristicsTable = service.characteristics.joinToString(
                separator = "\n|--", prefix = "|--"
            ) { it.uuid.toString() }
            Log.i(
                "printGattTable",
                "\nService ${service.uuid}\nCharacteristics:\n$characteristicsTable"
            )
        }
    }
}

/**END OF CLASS*/

