package com.example.step_counter.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.step_counter.R
import com.example.step_counter.callback.stepsCallback
import com.example.step_counter.helper.GeneralHelper
import com.example.step_counter.service.StepDetectorService
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.ImageButton;


class MainActivity : AppCompatActivity(), stepsCallback {


    var rhrLinearLayout: ImageButton? = null

    private val TAG = "PermissionDemo"
    private var progressBar: ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.progress_bar);
        val intent = Intent(this, StepDetectorService::class.java)
        startService(intent)

        StepDetectorService.subscribe.register(this)


        setUpPermissions()

        rhrLinearLayout = findViewById(R.id.ll_restingHeartRate) as ImageButton

        rhrLinearLayout!!.setOnClickListener(View.OnClickListener { getRestingHeartRate() })
    }


    override fun subscribeSteps(steps: Int) {
        TV_STEPS.setText(steps.toString()+" steps")
        progressBar?.progress = steps;
        TV_CALORIES.setText(GeneralHelper.getCalories(steps))
        TV_DISTANCE.setText(GeneralHelper.getDistanceCovered(steps))
    }

    private fun setUpPermissions() {

        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Permission to record denied");
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.CAMERA),
            101)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            101 -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    System.out.println(TAG+ "Permission has been denied by user")
                } else {
                    System.out.println(TAG+ "Permission has been granted by user")
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }


    fun getRestingHeartRate() {
        val intentToGetRHR = Intent(this@MainActivity, HeartBeatActivity::class.java)
        startActivityForResult(intentToGetRHR, 1)
    }

    fun sendMessage(view: View) {
        val intent = Intent(this@MainActivity, HeartBeatActivity::class.java)
        startActivity(intent)
    }

}


