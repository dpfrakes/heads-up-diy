package com.dpfrakes.headsupdiy

object AppData {
    var selectedCategory = ""

    var isWaitingForRoundStart = true
    var numPassed = 0
    var numCorrect = 0

    var cluesChallenged = mutableListOf<Clue>()

    val DEBUG = false
}
