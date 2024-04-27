package com.wordsearch.wordsearch.activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.wordsearch.R
import com.wordsearch.wordsearch.common.CellColorEnum
import com.wordsearch.wordsearch.common.WordDirectionEnum
import com.wordsearch.wordsearch.common.WordSearchValues
import com.wordsearch.wordsearch.common.WordSearchValues.Companion.cellColorEnumArray
import com.wordsearch.wordsearch.common.WordSearchValues.Companion.gameboardDimensions
import com.wordsearch.wordsearch.common.WordSearchValues.Companion.gameboardLetterArray
import com.wordsearch.wordsearch.common.WordSearchValues.Companion.letterFoundArray
import com.wordsearch.wordsearch.common.WordSearchValues.Companion.letterPlacedArray
import com.wordsearch.wordsearch.common.WordSearchValues.Companion.letters
import com.wordsearch.wordsearch.common.WordSearchValues.Companion.maxPlacementAttempts
import com.wordsearch.wordsearch.common.WordSearchValues.Companion.maxWords
import com.wordsearch.wordsearch.common.WordSearchValues.Companion.wordCellStartEndTextViewId
import com.wordsearch.wordsearch.common.WordSearchValues.Companion.wordSearchDtoArray
import com.wordsearch.wordsearch.common.WordSearchValues.Companion.wordStartTextViewId
import com.wordsearch.wordsearch.common.WordSearchValues.Companion.wordTextViewIdList
import com.wordsearch.wordsearch.dto.WordSearchDto

abstract class WordSearchGameboardActivity : WordSearchCellsActivity() {
    private val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        defineGameboard()
        defineWordLayout()
        defineGameboardOnclick()
    }

    // The gameboard only needs to be created one time
    private fun defineGameboard() {
        val gridLayout: GridLayout = findViewById(R.id.gameboardGridLayout)

        var textViewId = 0
        for (i in 0 until gameboardDimensions) {
            val linearLayout = LinearLayout(this)

            val gridLayoutParams: ViewGroup.LayoutParams = GridLayout.LayoutParams(
                GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f),
                GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
            )
            gridLayoutParams.height = 0;
            gridLayoutParams.width = 0;
            linearLayout.setLayoutParams(gridLayoutParams)

            linearLayout.orientation = LinearLayout.HORIZONTAL
            gridLayout.addView(linearLayout)

            for (j in 0 until gameboardDimensions) {
                val cellTextView = TextView(this)
                val linearLayoutParams: ViewGroup.LayoutParams =
                    LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f)
                cellTextView.setLayoutParams(linearLayoutParams)
                cellTextView.setTextColor(ContextCompat.getColor(this, R.color.black))  // black even on night theme
                cellTextView.gravity = Gravity.CENTER
                cellTextView.text = ""
                cellTextView.id = textViewId++
                linearLayout.addView(cellTextView)
            }
        }
    }

    // The word layout only needs to be created one time
    private fun defineWordLayout() {
        wordTextViewIdList = mutableListOf()

        val layoutCnt = maxWords / 2
        val wordLinearLayout: LinearLayout = findViewById(R.id.wordLinearLayout)

        var textViewId = wordStartTextViewId
        for (i in 0 until layoutCnt) {
            val linearLayout = LinearLayout(this)
            val linearLayoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            linearLayout.setLayoutParams(linearLayoutParams)
            linearLayout.orientation = LinearLayout.HORIZONTAL
            linearLayout.gravity = Gravity.CENTER_HORIZONTAL
            wordLinearLayout.addView(linearLayout)

            val leftTextViewParams: ViewGroup.LayoutParams =
                LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)

            val leftWordTextView = TextView(this)
            leftWordTextView.setLayoutParams(leftTextViewParams)
            leftWordTextView.id = ++textViewId
            leftWordTextView.gravity = Gravity.CENTER
            leftWordTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 19f)
            leftWordTextView.text = ""
            linearLayout.addView(leftWordTextView)
            wordTextViewIdList.add(leftWordTextView.id)

            val leftStartCellTextView = TextView(this)
            leftStartCellTextView.visibility = View.INVISIBLE
            leftStartCellTextView.id = textViewId + wordCellStartEndTextViewId
            leftStartCellTextView.text = ""
            linearLayout.addView(leftStartCellTextView)

            val leftEndCellTextView = TextView(this)
            leftEndCellTextView.visibility = View.INVISIBLE
            leftEndCellTextView.id = textViewId + (wordCellStartEndTextViewId * 2)
            leftEndCellTextView.text = ""
            linearLayout.addView(leftEndCellTextView)

            val rightLTextViewParams: ViewGroup.LayoutParams =
                LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)

            val rightWordTextView = TextView(this)
            rightWordTextView.setLayoutParams(rightLTextViewParams)
            rightWordTextView.id = ++textViewId
            rightWordTextView.gravity = Gravity.CENTER
            rightWordTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 19f)
            rightWordTextView.text = ""
            linearLayout.addView(rightWordTextView)
            wordTextViewIdList.add(rightWordTextView.id)

            val rightStartCellTextView = TextView(this)
            rightStartCellTextView.visibility = View.INVISIBLE
            rightStartCellTextView.id = textViewId + (wordCellStartEndTextViewId * 3)
            rightStartCellTextView.text = ""
            linearLayout.addView(rightStartCellTextView)

            val rightEndCellTextView = TextView(this)
            rightEndCellTextView.visibility = View.INVISIBLE
            rightEndCellTextView.id = textViewId + (wordCellStartEndTextViewId * 4)
            rightEndCellTextView.text = ""
            linearLayout.addView(rightEndCellTextView)
        }
    }

    private fun defineGameboardOnclick() {
        val gameboardGridLayout: GridLayout = findViewById(R.id.gameboardGridLayout)
        for (i in 0 until gameboardGridLayout.childCount) {
            val gameboardLinearLayout = gameboardGridLayout.getChildAt(i) as LinearLayout
            for (j in 0 until gameboardLinearLayout.childCount) {
                gameboardLinearLayout.getChildAt(j).setOnClickListener { view ->
                    cellClicked(view, i, j)
                }
            }
        }
    }

    fun generateGameboard() {
        // Reset all flags to false
        letterPlacedArray = Array(gameboardDimensions) { Array(gameboardDimensions) { false } }
        letterFoundArray = Array(gameboardDimensions) { Array(gameboardDimensions) { false } }
        cellColorEnumArray = Array(gameboardDimensions) { Array(gameboardDimensions) { CellColorEnum.Random11 } }
        WordSearchValues.resultsActive = false

        val showResultsButton: Button = findViewById(R.id.showResultsButton)
        showResultsButton.visibility = View.VISIBLE

        generateRandomLetters()

        for (index in 0 until wordSearchDtoArray.size) {
            var directionAttempts = 0
            var ordinal = (0 until WordDirectionEnum.entries.size).random()
            while (directionAttempts < WordDirectionEnum.entries.size) {
                val wordDirectionEnum = WordDirectionEnum.entries[ordinal]
                Log.d(
                    TAG,
                    ">>Word placement: word = ${wordSearchDtoArray[index].word}" +
                            " - length = ${wordSearchDtoArray[index].word.length}" +
                            " - direction = ${wordDirectionEnum} - attempt = ${directionAttempts}"
                )
                if (wordDirectionEnum == WordDirectionEnum.Horizontal) {
                    placeWordHorizontal(wordSearchDtoArray[index])
                } else if (wordDirectionEnum == WordDirectionEnum.Vertical) {
                    placeWordVertical(wordSearchDtoArray[index])
                } else if (wordDirectionEnum == WordDirectionEnum.DiagonalDown) {
                    placeWordDiagonalDown(wordSearchDtoArray[index])
                } else {
                    placeWordDiagonalUp(wordSearchDtoArray[index])
                }
                if (!wordSearchDtoArray[index].wordSkipped) {
                    break   // Found word placement
                }
                ordinal++
                if (ordinal == WordDirectionEnum.entries.size) {
                    ordinal = 0
                }
                directionAttempts++
            }
        }

        populateGameboardResults()
    }

    private fun placeWordHorizontal(wordSearchDto: WordSearchDto) {
        var foundLocation = false
        var attempts = 0
        while (!foundLocation) {
            attempts++

            var range = 0
            if (wordSearchDto.word.length < gameboardDimensions) {
                range = gameboardDimensions - wordSearchDto.word.length
            } else if (wordSearchDto.word.length > gameboardDimensions) {
                // Word length exceed gameboard dimensions
                attempts = maxPlacementAttempts
            }

            if (attempts >= maxPlacementAttempts) {
                // Skip word since it cannot fit into the gameboard
                Log.d(TAG, ">>Word skipped: ${wordSearchDto.word}")
                wordSearchDto.wordSkipped = true
                wordSearchDto.found = true
                break
            }

            val row = (0 until gameboardDimensions).random()
            for (col in range until range + wordSearchDto.word.length) {
                if (letterPlacedArray[row][col] && gameboardLetterArray[row][col] != wordSearchDto.word[col - range].toString()) {
                    break
                } else if (col == range + wordSearchDto.word.length - 1) {
                    foundLocation = true
                }
            }

            if (foundLocation) {
                wordSearchDto.wordSkipped = false
                wordSearchDto.found = false
                wordSearchDto.setStartEndCells(row * gameboardDimensions + range, WordDirectionEnum.Horizontal)
                // Mark boardValues with word
                for (col in range until range + wordSearchDto.word.length) {
                    gameboardLetterArray[row][col] = wordSearchDto.word[col - range].toString()
                    letterPlacedArray[row][col] = true
                }
            }
        }
    }

    private fun placeWordVertical(wordSearchDto: WordSearchDto) {
        var foundLocation = false
        var attempts = 0
        while (!foundLocation) {
            attempts++

            var range = 0
            if (wordSearchDto.word.length < gameboardDimensions) {
                range = gameboardDimensions - wordSearchDto.word.length
            } else if (wordSearchDto.word.length > gameboardDimensions) {
                // Word length exceed gameboard dimensions
                attempts = maxPlacementAttempts
            }

            if (attempts >= maxPlacementAttempts) {
                // Skip word since it cannot fit into the gameboard
                Log.d(TAG, ">>Word skipped: ${wordSearchDto.word}")
                wordSearchDto.wordSkipped = true
                wordSearchDto.found = true
                break
            }

            val col = (0 until gameboardDimensions).random()
            for (row in range until range + wordSearchDto.word.length) {
                if (letterPlacedArray[row][col] && gameboardLetterArray[row][col] != wordSearchDto.word[row - range].toString()) {
                    break
                } else if (row == range + wordSearchDto.word.length - 1) {
                    foundLocation = true
                }
            }

            if (foundLocation) {
                wordSearchDto.wordSkipped = false
                wordSearchDto.found = false
                wordSearchDto.setStartEndCells(range * gameboardDimensions + col, WordDirectionEnum.Vertical)
                // Mark boardValues with word
                for (row in range until range + wordSearchDto.word.length) {
                    gameboardLetterArray[row][col] = wordSearchDto.word[row - range].toString()
                    letterPlacedArray[row][col] = true
                }
            }
        }
    }

    private fun placeWordDiagonalDown(wordSearchDto: WordSearchDto) {
        var foundLocation = false
        var attempts = 0
        while (!foundLocation) {
            attempts++

            var range = 0
            if (wordSearchDto.word.length < gameboardDimensions) {
                range = gameboardDimensions - wordSearchDto.word.length
            } else if (wordSearchDto.word.length > gameboardDimensions) {
                // Word length exceed gameboard dimensions
                attempts = maxPlacementAttempts
            }

            if (attempts >= maxPlacementAttempts) {
                // Skip word since it cannot fit into the gameboard
                Log.d(TAG, ">>Word skipped: ${wordSearchDto.word}")
                wordSearchDto.wordSkipped = true
                wordSearchDto.found = true
                break
            }

            val rowRange = (0..range).random()
            val colRange = (0..range).random()

            var row = rowRange
            var col = colRange

            for (i in 0 until wordSearchDto.word.length) {
                if (letterPlacedArray[row][col] && gameboardLetterArray[row][col] != wordSearchDto.word[i].toString()) {
                    break
                } else if (row == range + wordSearchDto.word.length - 1) {
                    foundLocation = true
                }
                row++
                col++
            }

            if (foundLocation) {
                wordSearchDto.wordSkipped = false
                wordSearchDto.found = false
                wordSearchDto.setStartEndCells(rowRange * gameboardDimensions + colRange, WordDirectionEnum.DiagonalDown)
                // Mark boardValues with word
                row = rowRange
                col = colRange
                for (i in 0 until wordSearchDto.word.length) {
                    gameboardLetterArray[row][col] = wordSearchDto.word[i].toString()
                    letterPlacedArray[row][col] = true
                    row++
                    col++
                }
            }
        }
    }

    private fun placeWordDiagonalUp(wordSearchDto: WordSearchDto) {
        var foundLocation = false
        var attempts = 0
        while (!foundLocation) {
            attempts++

            var range = 0
            if (wordSearchDto.word.length < gameboardDimensions) {
                range = gameboardDimensions - wordSearchDto.word.length
            } else if (wordSearchDto.word.length > gameboardDimensions) {
                // Word length exceed gameboard dimensions
                attempts = maxPlacementAttempts
            }

            if (attempts >= maxPlacementAttempts) {
                // Skip word since it cannot fit into the gameboard
                Log.d(TAG, ">>Word skipped: ${wordSearchDto.word}")
                wordSearchDto.wordSkipped = true
                wordSearchDto.found = true
                break
            }

            val rowRange = (0..range).random()

            val rowBaseRange = range
            range = wordSearchDto.word.length - 1
            val colRange = (range until gameboardDimensions).random()

            var row = rowRange
            var col = colRange

            for (i in 0 until wordSearchDto.word.length) {
                if (letterPlacedArray[row][col] && gameboardLetterArray[row][col] != wordSearchDto.word[i].toString()) {
                    break
                } else if (row == rowBaseRange + wordSearchDto.word.length - 1) {
                    foundLocation = true
                }
                row++
                col--
            }

            if (foundLocation) {
                wordSearchDto.wordSkipped = false
                wordSearchDto.found = false
                wordSearchDto.setStartEndCells(rowRange * gameboardDimensions + colRange, WordDirectionEnum.DiagonalUp)
                // Mark boardValues with word
                row = rowRange
                col = colRange
                for (i in 0 until wordSearchDto.word.length) {
                    gameboardLetterArray[row][col] = wordSearchDto.word[i].toString()
                    letterPlacedArray[row][col] = true
                    row++
                    col--
                }
            }
        }
    }

    private fun generateRandomLetters() {
        for (i in 0 until gameboardDimensions) {
            for (j in 0 until gameboardDimensions) {
                // set each cell to a random letter
                gameboardLetterArray[i][j] = letters[(letters.indices).random()].toString()
                //boardValues[i][j] = ""    // Used for testing
            }
        }
    }

    // Populate the gameboard with what was generated inside of gameboardLetterArray
    private fun populateGameboardResults() {
        val gameboardGridLayout: GridLayout = findViewById(R.id.gameboardGridLayout)
        for (i in 0 until gameboardGridLayout.childCount) {
            val gameboardLinearLayout = gameboardGridLayout.getChildAt(i) as LinearLayout
            for (j in 0 until gameboardLinearLayout.childCount) {
                (gameboardLinearLayout.getChildAt(j) as TextView).text = gameboardLetterArray[i][j]
            }
        }
    }

    // Show the results by highlighting each word with a red border
    fun showResults() {
        val gameboardGridLayout: GridLayout = findViewById(R.id.gameboardGridLayout)
        for (i in 0 until gameboardGridLayout.childCount) {
            val gameboardLinearLayout = gameboardGridLayout.getChildAt(i) as LinearLayout
            for (j in 0 until gameboardLinearLayout.childCount) {
                (gameboardLinearLayout.getChildAt(j) as TextView).text = gameboardLetterArray[i][j]
                if (letterPlacedArray[i][j] && cellColorEnumArray[i][j] == CellColorEnum.Random11) {
                    val textView = gameboardLinearLayout.getChildAt(j) as TextView
                    textView.setBackgroundResource(R.drawable.bg_result_cell_border)
                }
            }
        }
    }

    fun showErrorDialog(msg: String) {
        Log.e(TAG, ">>Error: $msg")
        AlertDialog.Builder(this, R.style.style_dialog_rounded_corner)
            .setTitle(getString(R.string.failed_init_title))
            .setMessage(msg)
            .setPositiveButton("Back", DialogInterface.OnClickListener { _, _ ->
                super.onBackPressedDispatcher.onBackPressed()
            })
            .setIcon(R.drawable.ic_error)
            .show()
    }
}
