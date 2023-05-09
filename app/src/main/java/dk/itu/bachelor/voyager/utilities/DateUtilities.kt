package dk.itu.bachelor.voyager.utilities

import java.time.LocalTime
import kotlin.random.Random

object DateUtilities {

    fun generateRandomTimestamp(openingTime: String, closingTime: String) : String {
        val openingMinutes = openingTime.split(":")[0].toInt() * 60 + openingTime.split(":")[1].toInt()
        val closingMinutes = closingTime.split(":")[0].toInt() * 60 + closingTime.split(":")[1].toInt()

        val randomMinutes = Random.nextInt(openingMinutes, closingMinutes + 1)

        val randomTime = "%02d:%02d".format(randomMinutes / 60, randomMinutes % 60)

        val time1 = LocalTime.parse(randomTime)
        val time2 = LocalTime.parse("12:00")

        val amOrpm = if (time1.isBefore(time2)) "AM" else "PM"

        return randomTime + " " + amOrpm

    }
}