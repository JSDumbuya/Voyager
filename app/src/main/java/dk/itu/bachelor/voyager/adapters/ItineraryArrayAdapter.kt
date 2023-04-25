package dk.itu.bachelor.voyager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import dk.itu.bachelor.voyager.databinding.ItineraryItemBinding
import dk.itu.bachelor.voyager.models.Itinerary
import dk.itu.bachelor.voyager.interfaces.ItineraryItemClickListener

class ItineraryArrayAdapter (private val itemClickListener: ItineraryItemClickListener, options: FirebaseRecyclerOptions<Itinerary>):
    FirebaseRecyclerAdapter<Itinerary, ItineraryArrayAdapter.ViewHolder>(options) {

    inner class ViewHolder(private val binding: ItineraryItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(itinerary: Itinerary) {
            binding.itineraryName.text = itinerary.name
            binding.itineraryDescription.text = itinerary.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItineraryItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, itinerary: Itinerary) {

        holder.apply {
            bind(itinerary)

            itemView.setOnClickListener() {
                itemClickListener.onItemClickListener(itinerary, position)
                true
            }
        }
    }



}




