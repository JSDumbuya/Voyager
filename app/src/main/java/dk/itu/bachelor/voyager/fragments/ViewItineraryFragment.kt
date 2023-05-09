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
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dk.itu.bachelor.voyager.R
import dk.itu.bachelor.voyager.adapters.ViewItineraryArrayAdapter
import dk.itu.bachelor.voyager.databinding.FragmentViewItineraryBinding
import dk.itu.bachelor.voyager.models.ViewedItinerary
import dk.itu.bachelor.voyager.utilities.DATABASE_URL

class ViewItineraryFragment : Fragment() {

    private var _binding: FragmentViewItineraryBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Binding is null"
        }

    private lateinit var database: DatabaseReference
    private lateinit var adapter: ViewItineraryArrayAdapter

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

    fun updateData (itemId: String?, newText: String?) {
        val notesRef = database.child("itineraries/$itemId/notes")
        notesRef.setValue(newText)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.closeButton.setOnClickListener {
            findNavController().navigate(R.id.show_itineraries)
        }

        var viewedItinerarylist = mutableListOf<ViewedItinerary>()

        binding.listItinerary.layoutManager = LinearLayoutManager(requireContext())
        binding.listItinerary.addItemDecoration(
            DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        )

        val bundle = arguments
        if (bundle != null) {
            val itemId = bundle.getString("itemId")

            val itinaryNotesInputField = binding.itineraryNotes

            itinaryNotesInputField.setOnClickListener {
                val valueText = itinaryNotesInputField.text.toString()
                updateData(itemId, valueText)
            }

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

            val itinerariesref = database.child("itineraries/$itemId/experiences")

            itinerariesref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val data = dataSnapshot.value as HashMap<String, List<Int>>

                    for ((key, value) in data) {
                        for (v in value) {
                            val viewedItinerary = ViewedItinerary()
                            viewedItinerary.day = key
                            //Log.d("itinerary", "Day:" + viewedItinerary.day)
                            val experienceId = v.toString()

                            val expname = database.child("experiences/$experienceId/name")
                            expname.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    val infoname = dataSnapshot.getValue(String::class.java)
                                    viewedItinerary.experienceTitle = infoname
                                    //Log.d("itinerary", "ExperienceTitle:" + viewedItinerary.experienceTitle)


                                    viewedItinerarylist.add(viewedItinerary)

                                    adapter = ViewItineraryArrayAdapter(viewedItinerarylist)
                                    binding.listItinerary.adapter = adapter
                                    adapter.notifyDataSetChanged()
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Log.w(TAG, "Failed to read value.", error.toException())
                                }
                            })

                        }

                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Failed to read value.", error.toException())
                }
            })

        }

    }

}