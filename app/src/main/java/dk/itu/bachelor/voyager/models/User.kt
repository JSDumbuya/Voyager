package dk.itu.bachelor.voyager.models

data class User(
    var userId: String? = null,
    var itineraries: List<String>? = null
)
