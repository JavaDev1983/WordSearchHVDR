package com.wordsearch.wordsearch.dto

import android.util.Log
import com.wordsearch.wordsearch.common.WordDirectionEnum
import com.wordsearch.wordsearch.common.WordSearchValues.Companion.gameboardDimensions

class WordSearchDto(_word: String, _textViewId: Int, val _reversed: Boolean = false) {
    private val TAG = javaClass.simpleName

    var word = _word
    var startCell = 0
    var endCell = 0
    var textViewId = _textViewId
    var textViewWord = _word
    var reversed = _reversed
    var found = false
    var wordSkipped = false

    fun setStartEndCells(_startCell: Int, wordDirectionEnum: WordDirectionEnum) {
        startCell = _startCell
        if (wordDirectionEnum == WordDirectionEnum.Horizontal) {
            endCell = _startCell + word.length - 1
        } else if (wordDirectionEnum == WordDirectionEnum.Vertical) {
            endCell = _startCell + (word.length - 1) * gameboardDimensions
        } else if (wordDirectionEnum == WordDirectionEnum.DiagonalDown) {
            endCell = _startCell + (word.length - 1) * gameboardDimensions + (word.length - 1)
        } else {
            endCell = _startCell + (word.length - 1) * gameboardDimensions - (word.length - 1)
        }
        Log.d(TAG, ">>setStartEndCells $word $startCell $endCell")
    }

    fun compareCells(start: Int, end: Int, wordDirectionEnum: WordDirectionEnum): Boolean {
        if (end > start) {
            return checkCells(start, end, wordDirectionEnum)
        }
        return checkCells(end, start, wordDirectionEnum)
    }

    private fun checkCells(start: Int, end: Int, wordDirectionEnum: WordDirectionEnum): Boolean {
        if (wordDirectionEnum == WordDirectionEnum.Horizontal) {
            // horizontal case: check if word length and selected equal
            if (end - start != word.length - 1) {
                return false
            }
        } else if (wordDirectionEnum == WordDirectionEnum.Vertical) {
            // vertical case: check if word length and selected equal
            if ((end - start) / gameboardDimensions != word.length - 1) {
                return false
            }
        } else if (wordDirectionEnum == WordDirectionEnum.DiagonalDown) {
            val e = end % gameboardDimensions
            val s = start % gameboardDimensions
            if (e - s != word.length - 1) {
                return false
            }
        } else {
            val e = end % gameboardDimensions
            val s = start % gameboardDimensions
            if (s - e != word.length - 1) {
                return false
            }
        }
        if (startCell == start && endCell == end) {
            Log.d(TAG, ">>Word found: $this")
            Log.d(TAG, ">>Cells: start = $start - end = $end")
            return true
        }
        return false
    }

    override fun toString(): String {
        return "WordSearchWordDto(word='$word', startCell=$startCell, endCell=$endCell, textViewId=$textViewId, textViewWord='$textViewWord', isReverse=$reversed, isFound=$found, wordSkipped=$wordSkipped)"
    }
}
