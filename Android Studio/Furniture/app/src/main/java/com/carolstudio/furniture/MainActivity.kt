package com.carolstudio.furniture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.*
import androidx.annotation.CheckResult
import androidx.core.app.ActivityCompat
import com.robotemi.sdk.*
import com.robotemi.sdk.Robot.*
import com.robotemi.sdk.Robot.Companion.getInstance
import com.robotemi.sdk.constants.*
import com.robotemi.sdk.listeners.*
import com.robotemi.sdk.map.*
import com.robotemi.sdk.navigation.model.SpeedLevel
import com.robotemi.sdk.permission.Permission

import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var robot: Robot

    private val executorService = Executors.newSingleThreadExecutor()

    private var tts: TextToSpeech? = null
    private var locations = listOf<String>()
    private var loc_selected = toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var btn = findViewById<Button>(R.id.button)
        var go_btn = findViewById<Button>(R.id.go_to_loc)
        //var spinner = findViewById<Spinner>(R.id.location_list)
        //var loc_list = findViewById<TextView>(R.id.name)
        verifyStoragePermissions(this)
        robot = getInstance()
        btn.setOnClickListener(){
            locations = robot.locations.toMutableList()
            println(locations)
        }

        go_btn.setOnClickListener(){
                    robot.goTo(
                        "home base",
                        backwards = false,
                        noBypass = false,
                        speedLevel = SpeedLevel.HIGH
                    )


        }
        /*if (spinner != null) {
            val adap = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, locations)
            spinner.adapter = adap

        }*/
    }

    private fun sendMessage() {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // nothing happens
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        when (view?.id) {
            1 -> println("Spinner 2 Position:${position} and language: ${locations[position]}")
            else -> {
                println("Spinner 1 Position:${position} and language: ${locations[position]}")

            }
        }
    }

    @CheckResult
    private fun requestPermissionIfNeeded(permission: Permission, requestCode: Int): Boolean {
        if (robot.checkSelfPermission(permission) == Permission.GRANTED) {
            return false
        }
        robot.requestPermissions(listOf(permission), requestCode)
        return true
    }


    private var mapList: List<MapModel> = ArrayList()
    private fun getMapList() {
        if (requestPermissionIfNeeded(Permission.MAP, REQUEST_CODE_GET_MAP_LIST)) {
            return
        }
        mapList = robot.getMapList()
    }



    companion object {
        const val ACTION_HOME_WELCOME = "home.welcome"
        const val ACTION_HOME_DANCE = "home.dance"
        const val ACTION_HOME_SLEEP = "home.sleep"
        const val HOME_BASE_LOCATION = "home base"
        const val TAG = "AndroidMqttClient"
        // Storage Permissions
        private const val REQUEST_EXTERNAL_STORAGE = 1
        private const val REQUEST_CODE_NORMAL = 0
        private const val REQUEST_CODE_FACE_START = 1
        private const val REQUEST_CODE_FACE_STOP = 2
        private const val REQUEST_CODE_MAP = 3
        private const val REQUEST_CODE_SEQUENCE_FETCH_ALL = 4
        private const val REQUEST_CODE_SEQUENCE_PLAY = 5
        private const val REQUEST_CODE_START_DETECTION_WITH_DISTANCE = 6
        private const val REQUEST_CODE_SEQUENCE_PLAY_WITHOUT_PLAYER = 7
        private const val REQUEST_CODE_GET_MAP_LIST = 8
        private const val REQUEST_CODE_GET_ALL_FLOORS = 9
        private val PERMISSIONS_STORAGE = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        /**
         * Checks if the app has permission to write to device storage
         * If the app does not has permission then the user will be prompted to grant permissions
         */
        fun verifyStoragePermissions(activity: Activity?) {
            // Check if we have write permission
            val permission = ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
                )
            }
        }
    }

}