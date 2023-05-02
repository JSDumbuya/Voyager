package dk.itu.bachelor.voyager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dk.itu.bachelor.voyager.databinding.ViewItineraryItemBinding
import dk.itu.bachelor.voyager.models.ViewedItinerary


class ViewItineraryArrayAdapter (private val viewItinerariesList: List<ViewedItinerary>):
    RecyclerView.Adapter<ViewItineraryArrayAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ViewItineraryItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(viewedItinerary: ViewedItinerary) {
            binding.dayText.text = viewedItinerary.day
            binding.addressText.text = viewedItinerary.address
            binding.timeText.text = viewedItinerary.time
            binding.experienceTitleText.text = viewedItinerary.experienceTitle
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewItineraryItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return viewItinerariesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val viewItinerary = viewItinerariesList.get(position)

        holder.apply {
            bind(viewItinerary)
        }

    }


}
