package dk.itu.bachelor.voyager.utilities

import kotlin.random.Random

object DateUtilities {

    fun generateRandomTimestamp(openingTime: String, closingTime: String) : String {
        val openingMinutes = openingTime.split(":")[0].toInt() * 60 + openingTime.split(":")[1].toInt()
        val closingMinutes = closingTime.split(":")[0].toInt() * 60 + closingTime.split(":")[1].toInt()

        val randomMinutes = Random.nextInt(openingMinutes, closingMinutes + 1)

        val randomTime = "%02d:%02d".format(randomMinutes / 60, randomMinutes % 60)

        return randomTime

    }
}