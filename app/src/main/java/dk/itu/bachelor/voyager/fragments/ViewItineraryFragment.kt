package dk.itu.bachelor.voyager.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import dk.itu.bachelor.voyager.models.ViewedItinerary
import dk.itu.bachelor.voyager.utilities.DATABASE_URL
import dk.itu.bachelor.voyager.utilities.DateUtilities.generateRandomTimestamp
import dk.itu.bachelor.voyager.utilities.LocationUtilities


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

        binding.beginRouteButton.setOnClickListener {
            throw NotImplementedError()
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


            val experiences = database.child("itineraries/$itemId/experiences")

            experiences.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val data = dataSnapshot.value as HashMap<String, List<Int>>

                    for ((key, value) in data) {
                        val viewedItinerary = ViewedItinerary()
                        for (v in value) {
                            viewedItinerary.day = key
                            val experienceId = v.toString()

                            val expname = database.child("experiences/$experienceId/name")
                            expname.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    val infoname = dataSnapshot.getValue(String::class.java)
                                    viewedItinerary.experienceTitle = infoname
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Log.w(TAG, "Failed to read value.", error.toException())
                                }
                            })


                            val explat = database.child("experiences/$experienceId/lat")
                            explat.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    val infolat = dataSnapshot.getValue(Double::class.java)
                                    viewedItinerary.lat = infolat
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Log.w(TAG, "Failed to read value.", error.toException())
                                }
                            })
                            val explon = database.child("experiences/$experienceId/lon")
                            explon.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    val infolon = dataSnapshot.getValue(Double::class.java)
                                    viewedItinerary.lon = infolon
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Log.w(TAG, "Failed to read value.", error.toException())
                                }
                            })
                            val expOpen = database.child("experiences/$experienceId/openTime")
                            expOpen.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    val infoOpen = dataSnapshot.getValue(String::class.java)
                                    viewedItinerary.open = infoOpen
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Log.w(TAG, "Failed to read value.", error.toException())
                                }
                            })
                            val expClose = database.child("experiences/$experienceId/ClosingTime")
                            expClose.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    val infoClosed = dataSnapshot.getValue(String::class.java)
                                    viewedItinerary.closed = infoClosed
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Log.w(TAG, "Failed to read value.", error.toException())
                                }
                            })

                            viewedItinerary.lat?.let { lat ->
                                viewedItinerary.lon?.let { lon ->
                                    viewedItinerary.address =
                                        LocationUtilities.getAddress(requireContext(), lat, lon)
                                }
                            }

                            /*val latCoord = viewedItinerary.lat
                            val lonCoord = viewedItinerary.lon
                            LocationUtilities.getAddress(requireContext(), latCoord, lonCoord)*/

                            viewedItinerary.time =
                                viewedItinerary.open?.let {
                                    viewedItinerary.closed?.let { it1 ->
                                        generateRandomTimestamp(
                                            it,
                                            it1
                                        ).toString()
                                    }
                                }

                            viewedItinerarylist.add(viewedItinerary)
                        }


                    }


                    binding.listItinerary.layoutManager = LinearLayoutManager(requireContext())
                    binding.listItinerary.addItemDecoration(
                        DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
                    )
                    binding.listItinerary.adapter = ViewItineraryArrayAdapter(viewedItinerarylist)

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Failed to read value.", error.toException())
                }
            })
        }


    }



}