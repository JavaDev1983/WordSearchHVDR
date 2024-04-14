package com.wordsearch.data

class WordRepository {
    // Avoid selecting duplicate words
    val wordSet = setOf(
        "Aggregate",
        "Altruistic",
        "Astute",
        "Avail",
        "Benevolence",
        "Cognizant",
        "Covert",
        "Deprecate",
        "Dilatory",
        "Discern",
        "Docile",
        "Efficacious",
        "Gratitude",
        "Heterogeneous",
        "Humility",
        "Illicit",
        "Imperceptible",
        "Inclination",
        "Inevitable",
        "Linear",
        "Manifestation",
        "Meritocracy",
        "Mitigate",
        "Nebulous",
        "Nomenclature",
        "Perpetual",
        "Pivotal",
        "Placid",
        "Precipitate",
        "Profound",
        "Quintessence",
        "Reverence",
        "Solace",
        "Solicitude",
        "Tact",
        "Tenacious",
        "Trepidation",
        "Ubiquitous",
        "Vigilant",
        "Volatile"
    )

    fun getWord(): String {
        val randomWord = (0 until wordSet.size).random()
        return wordSet.elementAt(randomWord)
    }
}
