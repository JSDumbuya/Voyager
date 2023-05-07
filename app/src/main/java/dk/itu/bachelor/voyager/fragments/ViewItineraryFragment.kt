package dk.itu.bachelor.voyager.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dk.itu.bachelor.voyager.R
import dk.itu.bachelor.voyager.adapters.ViewItineraryArrayAdapter
import dk.itu.bachelor.voyager.databinding.FragmentViewItineraryBinding
import dk.itu.bachelor.voyager.models.Experience
import dk.itu.bachelor.voyager.models.Itinerary
import dk.itu.bachelor.voyager.models.ViewedItinerary
import dk.itu.bachelor.voyager.utilities.DATABASE_URL
import dk.itu.bachelor.voyager.utilities.DateUtilities.generateRandomTimestamp
import dk.itu.bachelor.voyager.utilities.LocationUtilities
import kotlin.math.log


class ViewItineraryFragment : Fragment() {

    private var _binding: FragmentViewItineraryBinding? = null
    private val binding get() = checkNotNull(_binding) {
        "Binding is null"
    }

    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = Firebase.database(DATABASE_URL).reference
        database.keepSynced(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewItineraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.closeButton.setOnClickListener {
            findNavController().navigate(R.id.show_itineraries)
        }

        var viewedItinerarylist = mutableListOf <ViewedItinerary>()

        val bundle = arguments
        if (bundle != null) {
            val itemId = bundle.getString("itemId")

            val description = database.child("itineraries/$itemId/description")

            description.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val info = dataSnapshot.getValue(String::class.java)
                    binding.itineraryDescription.setText(info)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Failed to read value.", error.toException())
                }
            })

            val name = database.child("itineraries/$itemId/name")

            name.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val info = dataSnapshot.getValue(String::class.java)
                    binding.itineraryName.setText(info)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Failed to read value.", error.toException())
                }
            })

            val notes = database.child("itineraries/$itemId/notes")

            notes.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val info = dataSnapshot.getValue(String::class.java)
                    binding.itineraryNotes.setText(info)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Failed to read value.", error.toException())
                }
            })


            val itineraryRef = database.child("itineraries/$itemId/experiences")

            itineraryRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val itinerary = dataSnapshot.getValue(Itinerary::class.java)
                    if (itinerary != null) {
                        val experiences = itinerary.experiences

                        val viewedItineraryList = mutableListOf<ViewedItinerary>()
                        val adapter = context?.let { ViewItineraryArrayAdapter(it, viewedItineraryList) }
                        binding.listItinerary.adapter = adapter

                        experiences?.forEach { (day, experienceIds) ->
                            // Iterate over each experience ID and fetch the corresponding Experience data
                            experienceIds.forEach { experienceId ->
                                // Query the database for the Experience item
                                val experienceRef = database.child("experiences/$experienceId")

                                experienceRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        val experience = dataSnapshot.getValue(Experience::class.java)
                                        if (experience != null) {
                                            // Create a new ViewedItinerary item and populate its data
                                            val viewedItinerary = ViewedItinerary(
                                                address = null,
                                                lat = experience.lat,
                                                lon = experience.lon,
                                                day = day,
                                                open = experience.openTime,
                                                closed = experience.closingTime,
                                                time = null,
                                                experienceTitle = experience.name
                                            )
                                            // Add the ViewedItinerary item to the list
                                            viewedItineraryList.add(viewedItinerary)
                                            // Update the adapter with the new list of items
                                            adapter?.notifyDataSetChanged()
                                        }
                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {
                                        // Handle any errors that occur during the database query
                                    }
                                })
                            }
                        }
                    } else {
                        // Handle the case when itinerary is null or not found
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w(TAG, "Failed to read value.", databaseError.toException())
                }
            })

        }


    }


}