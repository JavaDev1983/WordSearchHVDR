package com.wordsearch.wordsearch.common

import android.content.Context
import android.graphics.Point
import com.wordsearch.R
import com.wordsearch.wordsearch.dto.WordSearchDto

class WordSearchValues {

    companion object {
        var gameboardDimensions = 12        // Gameboard dimensions 12x12
        val maxWordLength = 10              // Maximum length of the word from the repository to place on the gameboard
        val maxWords = 6                    // Maximum number of words (2 to 10) to search for within the gameboard
        val maxPlacementAttempts = 50       // Maximum attempts to place a word on the gameboard
        val maxWordAttempts = 100           // Maximum attempts to found a word from the repository
        val enableShowResultButton = true   // Ability to show or hide the `Show Result` button

        val wordStartTextViewId = 1000
        val wordCellStartEndTextViewId = 100

        val letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

        var wordTextViewIdList: MutableList<Int> = mutableListOf()

        // Contains random words selected to place on the gameboard
        var wordSearchDtoArray = emptyArray<WordSearchDto>()

        // A dim x dim Array of String, contains the letter on the gameboard
        var gameboardLetterArray = Array(gameboardDimensions) { Array(gameboardDimensions) { "" } }

        // A dim x dim Array of Boolean, cell be changed to true if the cell is used for one of the search words placed on the gameboard
        var letterPlacedArray = Array(gameboardDimensions) { Array(gameboardDimensions) { false } }

        // A dim x dim Array of Boolean, cell be changed to true if the search word is collected selected
        var letterFoundArray = Array(gameboardDimensions) { Array(gameboardDimensions) { false } }

        var currentSelectedCellColorEnum = CellColorEnum.Random11

        // A dim x dim Array of CellColor, ceill be changed to a random color if the search word is collected selected
        var cellColorEnumArray = Array(gameboardDimensions) { Array(gameboardDimensions) { CellColorEnum.Random11 } }

        var pointUnselected = Point(-1, -1)
        var point1 = Point(pointUnselected)
        var point2 = Point(pointUnselected)

        var resultsActive = false
    }

    fun gameConfigValidate(context: Context): String {
        if (gameboardDimensions < 8) {
            return context.getString(R.string.word_search_failed_gameboard_dimensions)
        }
        if (maxWords < 2 || maxWords > 10 || (maxWords % 2) != 0) {
            return context.getString(R.string.word_search_failed_max_words)
        }

        if (maxWordLength > gameboardDimensions) {
            return context.getString(R.string.word_search_failed_max_word_length)
        }

        if (maxPlacementAttempts < 50) {
            return context.getString(R.string.word_search_failed_max_placement_attempts)
        }

        if (maxWordAttempts < 100) {
            return context.getString(R.string.word_search_failed_max_word_Attempts)
        }
        return ""
    }
}
