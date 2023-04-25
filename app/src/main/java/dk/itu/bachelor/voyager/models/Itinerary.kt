package dk.itu.bachelor.voyager.models

data class Itinerary(
    var id: Int? = null,
    var experiences: HashMap<String, List<Int>>? = null,
    var name: String? = null,
    val userId: String? = null,
    var description: String? = null,
    var notes: String? = null
)
