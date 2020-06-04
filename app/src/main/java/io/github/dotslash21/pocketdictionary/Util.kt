package io.github.dotslash21.pocketdictionary

import android.content.res.Resources

fun wordNumberTextFormatter(number: Int, res: Resources): String {
    return res.getString(R.string.word_number_text, number)
}

fun originTextFormatter(origin: String, res: Resources): String {
    // Trim the string
    origin.trim()

    return res.getString(R.string.origin_text, origin.trim())
}

fun definitionTextFormatter(definition: String, res: Resources): String {
    // Trim the string
    definition.trim()

    return res.getString(R.string.definition_text, definition.trim())
}

fun exampleTextFormatter(example: String, res: Resources): String {
    // Trim the string
    example.trim()

    return res.getString(R.string.example_text, example)
}