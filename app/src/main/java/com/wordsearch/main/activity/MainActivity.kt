package com.wordsearch.main.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.wordsearch.R
import com.wordsearch.wordsearch.activity.WordSearchMainActivity

class MainActivity : AppCompatActivity() {
    private val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, ">>Device SDK Version: ${Build.VERSION.SDK_INT} - Android Release: ${Build.VERSION.RELEASE} - Kotlin Version: ${KotlinVersion.CURRENT}")

        defineButtons()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onContextItemSelected(item)
    }

    private fun defineButtons() {
        val randomButton: Button = findViewById(R.id.startButton)
        randomButton.setOnClickListener {
            val intent = Intent(this, WordSearchMainActivity::class.java)
            startActivity(intent)
        }
    }
}
