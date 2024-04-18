package com.wordsearch.wordsearch.common

class WordSearchUtil {
    fun isFinished(): Boolean {
        for (wordSearchDto in WordSearchValues.wordSearchDtoArray) {
            if (!wordSearchDto.found) {
                return false
            }
        }
        return true
    }

    fun getRandomCellColor(): Int {
        val randomColor = (0 until CellColorEnum.entries.size).random()
        WordSearchValues.currentSelectedCellColorEnum = when (randomColor) {
            0 -> CellColorEnum.Random00
            1 -> CellColorEnum.Random01
            2 -> CellColorEnum.Random02
            3 -> CellColorEnum.Random03
            4 -> CellColorEnum.Random04
            5 -> CellColorEnum.Random05
            6 -> CellColorEnum.Random06
            7 -> CellColorEnum.Random07
            8 -> CellColorEnum.Random08
            9 -> CellColorEnum.Random09
            10 -> CellColorEnum.Random10
            11 -> CellColorEnum.Random11
            12 -> CellColorEnum.Random12
            13 -> CellColorEnum.Random13
            14 -> CellColorEnum.Random14
            else -> CellColorEnum.Random14 // Should never select else
        }
        return WordSearchValues.currentSelectedCellColorEnum.color
    }
}
