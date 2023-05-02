package dk.itu.bachelor.voyager.models

data class ViewedItinerary(
    var address: String? = null,
    var lat: Double? = null,
    var lon: Double? = null,
    var day: String? = null,
    var open: String? = null,
    var closed: String? = null,
    var time: String? = null,
    var experienceTitle: String? = null
)
