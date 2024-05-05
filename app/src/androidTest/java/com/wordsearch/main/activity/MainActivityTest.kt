package com.wordsearch.main.activity

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.wordsearch.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matcher
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

            val viewInteraction: ViewInteraction = onView(withId(R.id.showResultsButton))
            if (isButtonVisibility(viewInteraction)) {
                onView(withId(R.id.showResultsButton)).perform(click())
            }
        }
    }

    private fun isButtonVisibility(matcher: ViewInteraction): Boolean {
        var visible = true
        matcher.perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isAssignableFrom(TextView::class.java)
            }

            override fun getDescription(): String {
                return "visibility of the view"
            }

            override fun perform(uiController: UiController, view: View) {
                val button = view as Button
                visible = button.visibility == View.VISIBLE
            }
        })
        return visible
    }
}
