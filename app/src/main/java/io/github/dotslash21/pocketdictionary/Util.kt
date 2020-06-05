package io.github.dotslash21.pocketdictionary

import android.content.res.Resources

/*
* Function to format the word number in the SearchResultFragment.
*/
fun wordNumberTextFormatter(number: Int, res: Resources): String {
    return res.getString(R.string.word_number_text, number)
}

/*
* Function to format the origin text in the SearchResultFragment.
*/
fun originTextFormatter(origin: String, res: Resources): String {
    // Trim the string
    origin.trim()

    return res.getString(R.string.origin_text, origin.trim())
}

/*
* Function to format the definition string in the SearchResultFragment.
*/
fun definitionTextFormatter(definition: String, res: Resources): String {
    // Trim the string
    definition.trim()

    return res.getString(R.string.definition_text, definition.trim())
}

/*
* Function to format the example string in the SearchResultFragment.
*/
fun exampleTextFormatter(example: String, res: Resources): String {
    // Trim the string
    example.trim()

    return res.getString(R.string.example_text, example)
}