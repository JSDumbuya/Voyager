package dk.itu.bachelor.voyager.models

data class Experience(
    var id: Int? = null,
    var lon: Double? = null,
    var lat: Double? = null,
    var name: String? = null,
    var description: String? = null,
    var priceRange: PriceRange? = null,
    var labels: List<Labels>? = null,
    var rating: Double? = null,
    var pictureUrls: List<String>? = null,
    var openTime: String? = null,
    var closingTime: String? = null
)
