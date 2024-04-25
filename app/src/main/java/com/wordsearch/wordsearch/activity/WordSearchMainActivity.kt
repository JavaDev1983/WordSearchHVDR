package com.wordsearch.wordsearch.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.wordsearch.R
import com.wordsearch.data.WordRepository
import com.wordsearch.wordsearch.common.WordSearchValues
import com.wordsearch.wordsearch.common.WordSearchValues.Companion.maxWordAttempts
import com.wordsearch.wordsearch.common.WordSearchValues.Companion.maxWordLength
import com.wordsearch.wordsearch.common.WordSearchValues.Companion.maxWords
import com.wordsearch.wordsearch.common.WordSearchValues.Companion.resultsActive
import com.wordsearch.wordsearch.common.WordSearchValues.Companion.wordSearchDtoArray
import com.wordsearch.wordsearch.common.WordSearchValues.Companion.wordCellStartEndTextViewId
import com.wordsearch.wordsearch.common.WordSearchValues.Companion.wordTextViewIdList
import com.wordsearch.wordsearch.dto.WordSearchDto
import kotlin.random.Random

class WordSearchMainActivity : WordSearchGameboardActivity() {
    private val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Is game configuration validate
        val errorMsg = WordSearchValues().gameConfigValidate(applicationContext)
        if (errorMsg.isEmpty()) {
            defineButtons()
            init()
        } else {
            gameConfigError(errorMsg)
        }
    }

    private fun defineButtons() {
        val restartGameButton: Button = findViewById(R.id.restartGameButton)
        restartGameButton.setOnClickListener {
            init()
        }

        val showResultsButton: Button = findViewById(R.id.showResultsButton)
        showResultsButton.setOnClickListener {
            showResults()
            resultsActive = true
            showResultsButton.visibility = View.INVISIBLE
        }
    }

    private fun init() {
        resetAllBackgroundCells()
        retrieveWords()
        generateGameboard()
        setMessageTextView()
        setWordTextView()
        val showResultButton: Button = findViewById(R.id.showResultsButton)
        showResultButton.visibility = View.VISIBLE
    }

    // Clear all previously selected background cells
    private fun resetAllBackgroundCells() {
        val gameboardGridLayout: GridLayout = findViewById(R.id.gameboardGridLayout)
        val childCount = gameboardGridLayout.childCount
        for (i in 0 until childCount) {
            val gameboardLinearLayout: LinearLayout = gameboardGridLayout.getChildAt(i) as LinearLayout
            for (j in 0 until gameboardLinearLayout.childCount) {
                gameboardLinearLayout.getChildAt(j).setBackgroundResource(0)
            }
        }
    }

    // Retrieve maxWords from the repository
    private fun retrieveWords() {
        val wordSet: MutableSet<String> = mutableSetOf<String>()    // Avoid selecting duplicate words
        val wordSearchDtoList: MutableList<WordSearchDto> = mutableListOf()
        val wordRepository = WordRepository()
        var attempts = 0
        var cnt = 0
        while (cnt < maxWords && attempts < maxWordAttempts) {
            val word = wordRepository.getWord().uppercase()
            if (!wordSet.contains(word) && word.length <= maxWordLength) {
                wordSet.add(word)
                val wordSearchDto = WordSearchDto(word, wordTextViewIdList[cnt], Random.nextBoolean())
                if (wordSearchDto.reversed) {
                    wordSearchDto.word = word.reversed()
                }
                wordSearchDtoList.add(wordSearchDto)
                cnt++
            }
            attempts++
        }

        Log.d(TAG, ">>Word Search: cnt = $cnt - attempts = $attempts")
        if (wordSearchDtoList.isEmpty()) {
            showErrorDialog(getString(R.string.word_search_failed_init_dialog))
            Log.e(TAG, ">>Error: ${getString(R.string.word_search_failed_init_dialog)}")
            return
        }

        wordSearchDtoArray = wordSearchDtoList.toTypedArray()
    }

    private fun setMessageTextView() {
        val messageTextView: TextView = findViewById(R.id.messageTextview)
        messageTextView.text = getString(R.string.word_search_instruction)
    }

    private fun setWordTextView() {
        var wordSide = 0
        for (wordSearchWordDto in wordSearchDtoArray) {
            val wordTextView: TextView = findViewById(wordSearchWordDto.textViewId)
            wordTextView.paintFlags = 0 // remove strikethrough
            wordTextView.text = if (wordSearchWordDto.wordSkipped) "" else wordSearchWordDto.textViewWord

            if ((wordSide % 2) == 0) {
                val wordStartTextView: TextView = findViewById(wordSearchWordDto.textViewId + wordCellStartEndTextViewId)
                wordStartTextView.text = wordSearchWordDto.startCell.toString()
                val wordEndTextView: TextView = findViewById(wordSearchWordDto.textViewId + (wordCellStartEndTextViewId * 2))
                wordEndTextView.text = wordSearchWordDto.endCell.toString()
            } else {
                val wordStartTextView: TextView = findViewById(wordSearchWordDto.textViewId + (wordCellStartEndTextViewId * 3))
                wordStartTextView.text = wordSearchWordDto.startCell.toString()
                val wordEndTextView: TextView = findViewById(wordSearchWordDto.textViewId + (wordCellStartEndTextViewId * 4))
                wordEndTextView.text = wordSearchWordDto.endCell.toString()
            }
            wordSide++
        }
    }

    private fun gameConfigError(errorMsg: String) {
        val restartGameButton: Button = findViewById(R.id.restartGameButton)
        restartGameButton.visibility = View.INVISIBLE
        val showResultsButton: Button = findViewById(R.id.showResultsButton)
        val messageTextView: TextView = findViewById(R.id.messageTextview)
        messageTextView.visibility = View.INVISIBLE
        showResultsButton.visibility = View.INVISIBLE
        showErrorDialog(errorMsg)
        Log.e(TAG, ">>Error: $errorMsg")
    }
}
