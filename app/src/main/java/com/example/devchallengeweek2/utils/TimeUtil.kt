package com.example.devchallengeweek2.utils

import java.util.concurrent.TimeUnit

fun Long.toFormattedTime(): String = String.format(
    "%02d : %02d",
    TimeUnit.MILLISECONDS.toMinutes(this),
    TimeUnit.MILLISECONDS.toSeconds(this) % 60
)

val GreenProgress = 0xFF54E259
val YellowProgress = 0xAAF7DB5A
val RedProgress = 0xFFF7847C
