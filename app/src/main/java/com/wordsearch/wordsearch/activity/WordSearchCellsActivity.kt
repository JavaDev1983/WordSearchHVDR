package com.wordsearch.wordsearch.activity

import android.graphics.Paint
import android.graphics.Point
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.wordsearch.R
import com.wordsearch.wordsearch.common.CellColorEnum
import com.wordsearch.wordsearch.common.WordDirectionEnum
import com.wordsearch.wordsearch.common.WordSearchUtil
import com.wordsearch.wordsearch.common.WordSearchValues
import com.wordsearch.wordsearch.common.WordSearchValues.Companion.cellColorEnumArray
import com.wordsearch.wordsearch.common.WordSearchValues.Companion.currentSelectedCellColorEnum
import com.wordsearch.wordsearch.common.WordSearchValues.Companion.gameboardDimensions
import com.wordsearch.wordsearch.common.WordSearchValues.Companion.letterFoundArray
import com.wordsearch.wordsearch.common.WordSearchValues.Companion.letterPlacedArray
import com.wordsearch.wordsearch.common.WordSearchValues.Companion.resultsActive
import com.wordsearch.wordsearch.common.WordSearchValues.Companion.wordSearchDtoArray

abstract class WordSearchCellsActivity : WordSearchBaseActivity() {
    private val TAG = javaClass.simpleName

    fun cellClicked(view: View, row: Int, col: Int) {
        if (WordSearchUtil().isFinished()) {
            // All words were found, don't allow any further selections
            return
        }
        if (WordSearchValues.point1 == WordSearchValues.pointUnselected) {
            // First letter selected (point1)
            WordSearchValues.point1 = Point(row, col)
            view.setBackgroundResource(R.drawable.bg_selected_cell_red)
        } else if (WordSearchValues.point1 == Point(row, col)) {
            // First letter was unselected (point1)
            WordSearchValues.point1 = WordSearchValues.pointUnselected
            view.setBackgroundResource(0)
        } else {
            // Second letter selected (point2)
            WordSearchValues.point2 = Point(row, col)
            Log.d(TAG, ">>Points selected: ${WordSearchValues.point1} - ${WordSearchValues.point2}")

            if (WordSearchValues.point1.x == WordSearchValues.point2.x) {
                onSelectHorizontal()
            } else if (WordSearchValues.point1.y == WordSearchValues.point2.y) {
                onSelectVertical()
            } else if ((WordSearchValues.point1.x < WordSearchValues.point2.x && WordSearchValues.point1.y < WordSearchValues.point2.y) || (WordSearchValues.point1.x > WordSearchValues.point2.x && WordSearchValues.point1.y > WordSearchValues.point2.y)) {
                onSelectDiagonalDown()
            } else if ((WordSearchValues.point1.x > WordSearchValues.point2.x && WordSearchValues.point1.y < WordSearchValues.point2.y) || (WordSearchValues.point1.x < WordSearchValues.point2.x && WordSearchValues.point1.y > WordSearchValues.point2.y)) {
                onSelectDiagonalUp()
            } else {
                Log.d(TAG, ">>Undefined")
            }
            Log.d(TAG, ">>Reset point1 and point2")
            WordSearchValues.point1 = Point(WordSearchValues.pointUnselected)
            WordSearchValues.point2 = Point(WordSearchValues.pointUnselected)
        }
    }

    private fun onSelectHorizontal() {
        Log.d(TAG, ">>Horizontal selection")
        val selectedColor = WordSearchUtil().getRandomCellColor()
        val gameboardGridLayout: GridLayout = findViewById(R.id.gameboardGridLayout)
        for (i in 0 until gameboardGridLayout.childCount) {
            if (i != WordSearchValues.point1.x) {
                continue
            }
            val gameboardLinearLayout = gameboardGridLayout.getChildAt(i) as LinearLayout
            if (WordSearchValues.point1.y > WordSearchValues.point2.y) {
                for (j in gameboardLinearLayout.childCount downTo 0) {
                    if (j >= WordSearchValues.point2.y && j <= WordSearchValues.point1.y) {
                        gameboardLinearLayout.getChildAt(j).setBackgroundResource(selectedColor)
                    }
                }
            } else {
                for (j in 0 until gameboardLinearLayout.childCount) {
                    if (j >= WordSearchValues.point1.y && j <= WordSearchValues.point2.y) {
                        gameboardLinearLayout.getChildAt(j).setBackgroundResource(selectedColor)
                    }
                }
            }
            break
        }

        val startCell = WordSearchValues.point1.x * gameboardDimensions + WordSearchValues.point1.y
        val endCell = WordSearchValues.point2.x * gameboardDimensions + WordSearchValues.point2.y
        checkSelected(startCell, endCell, WordDirectionEnum.Horizontal)
    }

    private fun onSelectVertical() {
        Log.d(TAG, ">>Vertical selection")
        val selectedColor = WordSearchUtil().getRandomCellColor()
        val gameboardGridLayout: GridLayout = findViewById(R.id.gameboardGridLayout)
        if (WordSearchValues.point1.x > WordSearchValues.point2.x) {
            for (i in gameboardGridLayout.childCount downTo 0) {
                if (i >= WordSearchValues.point2.x && i <= WordSearchValues.point1.x) {
                    val gameboardLinearLayout = gameboardGridLayout.getChildAt(i) as LinearLayout
                    gameboardLinearLayout.getChildAt(WordSearchValues.point1.y).setBackgroundResource(selectedColor)
                }
            }
        } else {
            for (i in 0 until gameboardGridLayout.childCount) {
                if (i >= WordSearchValues.point1.x && i <= WordSearchValues.point2.x) {
                    val gameboardLinearLayout = gameboardGridLayout.getChildAt(i) as LinearLayout
                    gameboardLinearLayout.getChildAt(WordSearchValues.point1.y).setBackgroundResource(selectedColor)
                }
            }
        }

        val startCell = WordSearchValues.point1.x * gameboardDimensions + WordSearchValues.point1.y
        val endCell = WordSearchValues.point2.x * gameboardDimensions + WordSearchValues.point2.y
        checkSelected(startCell, endCell, WordDirectionEnum.Vertical)
    }

    private fun onSelectDiagonalDown() {
        Log.d(TAG, ">>DiagonalDown selection")
        val selectedColor = WordSearchUtil().getRandomCellColor()
        val gameboardGridLayout: GridLayout = findViewById(R.id.gameboardGridLayout)
        var row = WordSearchValues.point1.x
        var col = WordSearchValues.point1.y
        if (WordSearchValues.point1.x > WordSearchValues.point2.x && WordSearchValues.point1.y > WordSearchValues.point2.y) {
            for (i in gameboardGridLayout.childCount - 1 downTo 0) {
                val gameboardLinearLayout = gameboardGridLayout.getChildAt(i) as LinearLayout
                for (j in 0 until gameboardLinearLayout.childCount) {
                    if ((i == row && j == col)) {
                        gameboardLinearLayout.getChildAt(j).setBackgroundResource(selectedColor)
                        row--
                        col--
                    }
                }
                if ((row < WordSearchValues.point2.x && col < WordSearchValues.point2.y)) {
                    break
                }
            }
        } else {
            for (i in 0 until gameboardGridLayout.childCount) {
                val gameboardLinearLayout = gameboardGridLayout.getChildAt(i) as LinearLayout
                for (j in 0 until gameboardLinearLayout.childCount) {
                    if ((i == row && j == col)) {
                        gameboardLinearLayout.getChildAt(j).setBackgroundResource(selectedColor)
                        row++
                        col++
                    }
                }
                if ((row > WordSearchValues.point2.x && col > WordSearchValues.point2.y)) {
                    break
                }
            }
        }

        val startCell = WordSearchValues.point1.x * gameboardDimensions + WordSearchValues.point1.y
        val endCell = WordSearchValues.point2.x * gameboardDimensions + WordSearchValues.point2.y
        checkSelected(startCell, endCell, WordDirectionEnum.DiagonalDown)
    }

    private fun onSelectDiagonalUp() {
        Log.d(TAG, ">>DiagonalUp selection")
        val selectedColor = WordSearchUtil().getRandomCellColor()
        val gameboardGridLayout: GridLayout = findViewById(R.id.gameboardGridLayout)
        var row = WordSearchValues.point1.x
        var col = WordSearchValues.point1.y
        if (WordSearchValues.point1.x > WordSearchValues.point2.x && WordSearchValues.point1.y < WordSearchValues.point2.y) {
            for (i in (gameboardGridLayout.childCount - 1) downTo 0) {
                val gameboardLinearLayout = gameboardGridLayout.getChildAt(i) as LinearLayout
                for (j in 0 until gameboardLinearLayout.childCount) {
                    if ((i == row && j == col)) {
                        gameboardLinearLayout.getChildAt(j).setBackgroundResource(selectedColor)
                        row--
                        col++
                    }
                }
                if ((row < WordSearchValues.point2.x && col > WordSearchValues.point2.y)) {
                    break
                }
            }
        } else {
            for (i in 0 until gameboardGridLayout.childCount) {
                val gameboardLinearLayout = gameboardGridLayout.getChildAt(i) as LinearLayout
                for (j in 0 until gameboardLinearLayout.childCount) {
                    if ((i == row && j == col)) {
                        gameboardLinearLayout.getChildAt(j).setBackgroundResource(selectedColor)
                        row++
                        col--
                    }
                }
                if ((row > WordSearchValues.point2.x && col < WordSearchValues.point2.y)) {
                    break
                }
            }
        }

        val startCell = WordSearchValues.point1.x * gameboardDimensions + WordSearchValues.point1.y
        val endCell = WordSearchValues.point2.x * gameboardDimensions + WordSearchValues.point2.y
        checkSelected(startCell, endCell, WordDirectionEnum.DiagonalUp)
    }

    private fun checkSelected(startCell: Int, endCell: Int, wordDirectionEnum: WordDirectionEnum) {
        var isFound = false
        var textViewId = 0

        for (wordSearchDto in wordSearchDtoArray) {
            if (wordSearchDto.compareCells(startCell, endCell, wordDirectionEnum)) {
                if (wordSearchDto.found) {
                    return
                }
                markCellsAsFound(startCell, endCell, wordDirectionEnum)
                wordSearchDto.found = true
                isFound = true
                textViewId = wordSearchDto.textViewId
                break
            }
        }

        if (isFound) {
            val wordTextView: TextView = findViewById(textViewId)
            wordTextView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            unselectCells()
        }

        val finished = WordSearchUtil().isFinished()
        Log.d(TAG, ">>isFinished: $finished")
        if (finished) {
            val messageTextView: TextView = findViewById(R.id.messageTextview)
            messageTextView.text = getString(R.string.finish_message)
            val showResultButton: Button = findViewById(R.id.showResultsButton)
            showResultButton.visibility = View.INVISIBLE
        }
    }

    private fun markCellsAsFound(startCell: Int, endCell: Int, wordDirectionEnum: WordDirectionEnum) {
        var start = startCell
        var end = endCell

        if (endCell < startCell) {
            start = endCell
            end = startCell
        }

        if (wordDirectionEnum == WordDirectionEnum.Horizontal) {
            for (i in start..end) {
                letterFoundArray[i / gameboardDimensions][i % gameboardDimensions] = true
                cellColorEnumArray[i / gameboardDimensions][i % gameboardDimensions] = currentSelectedCellColorEnum
            }
        } else if (wordDirectionEnum == WordDirectionEnum.Vertical) {
            // step dim to increment dim cells horizontal since traversing vertically
            for (i in start..end step gameboardDimensions) {
                letterFoundArray[i / gameboardDimensions][i % gameboardDimensions] = true
                cellColorEnumArray[i / gameboardDimensions][i % gameboardDimensions] = currentSelectedCellColorEnum
            }
        } else if (wordDirectionEnum == WordDirectionEnum.DiagonalDown) {
            var row = start / gameboardDimensions
            var col = start % gameboardDimensions
            val wordLength = (end % gameboardDimensions) - (start % gameboardDimensions)
            for (k in 0..wordLength) {
                letterFoundArray[row][col] = true
                cellColorEnumArray[row][col] = currentSelectedCellColorEnum
                row++
                col++
            }
        } else {
            var row = start / gameboardDimensions
            var col = start % gameboardDimensions
            val wordLength = (start % gameboardDimensions) - (end % gameboardDimensions)
            for (k in 0..wordLength) {
                letterFoundArray[row][col] = true
                cellColorEnumArray[row][col] = currentSelectedCellColorEnum
                row++
                col--
            }
        }
    }

    // Word selected was not found so reset the background cells
    private fun unselectCells() {
        Toast.makeText(this, getString(R.string.try_again), Toast.LENGTH_LONG).show()
        val gameboardGridLayout: GridLayout = findViewById(R.id.gameboardGridLayout)
        for (i in 0 until gameboardGridLayout.childCount) {
            val gameboardLinearLayout = gameboardGridLayout.getChildAt(i) as LinearLayout
            for (j in 0 until gameboardLinearLayout.childCount) {
                if (letterFoundArray[i][j]) {
                    val cellColor = cellColorEnumArray[i][j].color
                    gameboardLinearLayout.getChildAt(j).setBackgroundResource(cellColor)
                } else {
                    if (resultsActive && letterPlacedArray[i][j]) {
                        gameboardLinearLayout.getChildAt(j).setBackgroundResource(R.drawable.bg_result_cell_border)
                        continue
                    }
                    gameboardLinearLayout.getChildAt(j).setBackgroundResource(0)
                    cellColorEnumArray[i][j] = CellColorEnum.Random11
                }
            }
        }
    }
}
