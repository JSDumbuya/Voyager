package dk.itu.bachelor.voyager.models

data class Experience(
    var lon: Double? = null,
    var lat: Double? = null,
    var name: String? = null,
    var description: String? = null,
    var priceRange: PriceRange? = null,
    var labels: List<Labels>? = null,
    var rating: Double? = null,
    var pictureUrls: List<String>? = null,
    var openTime: Long? = null,
    var closingTime: Long? = null,
    var specialOffers: List<String>? = null
)
