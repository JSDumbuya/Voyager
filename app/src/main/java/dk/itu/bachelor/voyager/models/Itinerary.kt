package dk.itu.bachelor.voyager.models

data class Itinerary(
    var id: String? = null,
    var experiences: HashMap<String, String>? = null,
    var name: String? = null,
    val userId: String? = null,
    var description: String? = null,
    var notes: String? = null,
    var duration: Long? = null
)
