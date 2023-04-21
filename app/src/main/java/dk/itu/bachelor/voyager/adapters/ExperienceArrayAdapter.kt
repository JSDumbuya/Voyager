package dk.itu.bachelor.voyager.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import dk.itu.bachelor.voyager.ExperiencesByListFragment
import dk.itu.bachelor.voyager.R
import dk.itu.bachelor.voyager.models.Experience

class ExperienceArrayAdapter(
    private val itemClickListener: ExperiencesByListFragment,
    options: FirebaseRecyclerOptions<Experience>
    ) :
    FirebaseRecyclerAdapter<Experience, ExperienceArrayAdapter.ViewHolder>(options) {


        //A set of private constants used in this class
        companion object {
            private val TAG = ExperienceArrayAdapter::class.qualifiedName
        }

        /**
         * An internal view holder class used to represent the layout that shows a single `VoyagerDB` instance
         */
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val name: TextView = view.findViewById(R.id.title)
            val rating: TextView = view.findViewById(R.id.rating)
            val description: TextView = view.findViewById(R.id.description)
            val photo: ImageView = view.findViewById(R.id.exphoto)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            // Create a new view, which defines the UI of the list item
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_experiences_by_list_element, parent, false)
            return ViewHolder(view)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int, experience: Experience) {
            Log.i(TAG, "Populate an item at position: $position")

            holder.apply {
                name.text =  experience.name
                rating.text = "Rating: ${experience.rating.toString()}"
                description.text = experience.description?.take(170) + "..."

                if (experience.pictureUrls?.isNotEmpty() == true) {
                    Glide.with(holder.itemView.context)
                        .load(experience.pictureUrls?.get(0))
                        .into(holder.photo)
                }


                itemView.setOnLongClickListener {
                    itemClickListener.onItemClickListener(experience, position)
                    true
                }
            }
        }
    }