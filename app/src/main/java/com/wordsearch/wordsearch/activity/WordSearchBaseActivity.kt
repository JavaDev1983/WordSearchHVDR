package com.wordsearch.wordsearch.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.wordsearch.R

abstract class WordSearchBaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_search)

        // Add back arrow to ActionBar
        supportActionBar?.run {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                super.onBackPressedDispatcher.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
