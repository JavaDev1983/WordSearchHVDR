package com.wordsearch.main.activity

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.wordsearch.R
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @Test
    fun testMainActivity() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val startMessage = context.resources.getString(R.string.start_message)

        launchActivity<MainActivity>().use { _ ->
            onView(withId(R.id.startButton)).perform(click())
            onView(withId(R.id.messageTextview)).check(matches(withText(startMessage)))
        }
    }
}
