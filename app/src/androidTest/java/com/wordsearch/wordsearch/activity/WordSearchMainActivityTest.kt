package com.wordsearch.wordsearch.activity

import android.graphics.Paint
import android.view.View
import android.widget.TextView
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.wordsearch.R
import com.wordsearch.wordsearch.common.WordSearchValues.Companion.maxWords
import com.wordsearch.wordsearch.common.WordSearchValues.Companion.wordCellStartEndTextViewId
import com.wordsearch.wordsearch.common.WordSearchValues.Companion.wordStartTextViewId
import org.hamcrest.Matcher
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class WordSearchMainActivityTest {
    private val TAG = javaClass.simpleName
    private val textViewId = wordStartTextViewId + 1

    @Test
    fun testWordSearchMainActivity() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val finishMessage = context.resources.getString(R.string.finish_message)

        launchActivity<WordSearchMainActivity>().use { _ ->
            // Get each gameboard words
            var wordDtoList = getWords()
            assertFalse(wordDtoList.isEmpty())

            // Select each word on the gameboard
            wordDtoList.forEach { wordDto ->
                onView(withId(wordDto.startCell)).perform(ViewActions.click())
                onView(withId(wordDto.endCell)).perform(ViewActions.click())
            }
            // Confirm the game was finished successfully
            onView(withId(R.id.messageTextview)).check(matches(ViewMatchers.withText(finishMessage)))

            // Confirm each word has a strikethrough
            wordDtoList = getWordPaintFlags()
            wordDtoList.forEach { wordDto ->
                assertEquals(wordDto.paintFlags, Paint.STRIKE_THRU_TEXT_FLAG)
            }
//            runBlocking(Dispatchers.IO) {
//                delay(2000)
//            }
        }
    }

    private fun getWords(): List<WordDto> {
        val wordDtoList: MutableList<WordDto> = mutableListOf()

        var wordSide = 0
        for (i in textViewId until maxWords + textViewId) {
            var viewInteraction: ViewInteraction = onView(withId(i))
            val word = getText(viewInteraction)

            if (word.isNotEmpty()) {
                val wordDto = WordDto()
                wordDto.word = word
                if ((wordSide % 2) == 0) {
                    viewInteraction = onView(withId(i + wordCellStartEndTextViewId))
                    val startCell = getText(viewInteraction)
                    wordDto.startCell = startCell.toInt()

                    viewInteraction = onView(withId(i + (wordCellStartEndTextViewId * 2)))
                    val endCell = getText(viewInteraction)
                    wordDto.endCell = endCell.toInt()
                } else {
                    viewInteraction = onView(withId(i + (wordCellStartEndTextViewId * 3)))
                    val startCell = getText(viewInteraction)
                    wordDto.startCell = startCell.toInt()

                    viewInteraction = onView(withId(i + (wordCellStartEndTextViewId * 4)))
                    val endCell = getText(viewInteraction)
                    wordDto.endCell = endCell.toInt()
                }
                wordDtoList.add(wordDto)
            }
            wordSide++
        }
        return wordDtoList
    }

    private fun getText(matcher: ViewInteraction): String {
        var text = ""
        matcher.perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(TextView::class.java)
            }

            override fun getDescription(): String {
                return "Text of the view"
            }

            override fun perform(uiController: UiController, view: View) {
                val textView = view as TextView
                text = textView.text.toString()
            }
        })
        return text
    }

    private fun getWordPaintFlags(): List<WordDto> {
        val wordDtoList: MutableList<WordDto> = mutableListOf()

        for (i in textViewId until maxWords + textViewId) {
            val viewInteraction: ViewInteraction = onView(withId(i))
            val word = getText(viewInteraction)

            if (word.isNotEmpty()) {
                val wordDto = WordDto()
                wordDto.word = word
                wordDto.paintFlags = getPaintFlag(viewInteraction)
                wordDtoList.add(wordDto)
            }
        }
        return wordDtoList
    }

    private fun getPaintFlag(matcher: ViewInteraction): Int {
        var paintFlag = 0
        matcher.perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(TextView::class.java)
            }

            override fun getDescription(): String {
                return "paintFlag of the view"
            }

            override fun perform(uiController: UiController, view: View) {
                val textView = view as TextView
                paintFlag = textView.paintFlags
            }
        })
        return paintFlag
    }
}

class WordDto() {
    var word = ""
    var startCell = 0
    var endCell = 0
    var paintFlags = 0
}
