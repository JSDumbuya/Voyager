package dk.itu.bachelor.voyager.utilities

import dk.itu.bachelor.voyager.models.Itinerary

interface ItineraryItemClickListener {

    fun onItemClickListener(itenerary: Itinerary, position: Int)
}