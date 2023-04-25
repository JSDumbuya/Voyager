package dk.itu.bachelor.voyager.interfaces

import dk.itu.bachelor.voyager.models.Itinerary

interface ItineraryItemClickListener {

    fun onItemClickListener(itenerary: Itinerary, position: Int)
}