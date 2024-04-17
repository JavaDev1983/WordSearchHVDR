package com.wordsearch.data

class WordRepository {
    // Avoid selecting duplicate words
    val wordSet = setOf(
        "AGGREGATE",
        "ALTRUISTIC",
        "ASTUTE",
        "AVAIL",
        "BENEVOLENCE",
        "COGNIZANT",
        "COVERT",
        "DEPRECATE",
        "DILATORY",
        "DISCERN",
        "DOCILE",
        "EFFICACIOUS",
        "GRATITUDE",
        "HUMILITY",
        "ILLICIT",
        "IMPERCEPTIBLE",
        "INCLINATION",
        "INEVITABLE",
        "INTRINSIC",
        "LINEAR",
        "MUTABLE",
        "MERITOCRACY",
        "MITIGATE",
        "NEBULOUS",
        "NOMENCLATURE",
        "PERPETUAL",
        "PIVOTAL",
        "PLACID",
        "PRECIPITATE",
        "PROFOUND",
        "QUINTESSENCE",
        "REVERENCE",
        "SOLACE",
        "SOLICITUDE",
        "TACT",
        "TENACIOUS",
        "TREPIDATION",
        "UBIQUITOUS",
        "VIGILANT",
        "VOLATILE"
    )

    fun getWord(): String {
        val randomWord = (0 until wordSet.size).random()
        return wordSet.elementAt(randomWord)
    }
}
