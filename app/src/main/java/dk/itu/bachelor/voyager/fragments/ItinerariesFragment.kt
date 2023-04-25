package dk.itu.bachelor.voyager.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dk.itu.bachelor.voyager.R
import dk.itu.bachelor.voyager.adapters.ItineraryArrayAdapter
import dk.itu.bachelor.voyager.databinding.FragmentItinerariesBinding
import dk.itu.bachelor.voyager.models.Itinerary
import dk.itu.bachelor.voyager.utilities.DATABASE_URL
import dk.itu.bachelor.voyager.utilities.ItineraryItemClickListener

class ItinerariesFragment : Fragment(), ItineraryItemClickListener {

    private var _binding: FragmentItinerariesBinding? = null
    private val binding get() = checkNotNull(_binding) {
        "Binding is null"
    }


    //private lateinit var authentication: FirebaseAuth
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //authentication = FirebaseAuth.getInstance()
        database = Firebase.database(DATABASE_URL).reference
        database.keepSynced(true)
        //storage = Firebase.storage(BUCKET_URL)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItinerariesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            fab.setOnClickListener {
                findNavController().navigate(R.id.show_plan_your_trip)
            }

            val query = database.child("itineraries")
                .orderByChild("id")

            val options = FirebaseRecyclerOptions.Builder<Itinerary>()
                .setQuery(query, Itinerary::class.java)
                .setLifecycleOwner(this@ItinerariesFragment)
                .build()

            listItinerariesView.layoutManager = LinearLayoutManager(requireContext())
            listItinerariesView.addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            )

            listItinerariesView.adapter = ItineraryArrayAdapter(this@ItinerariesFragment, options)
        }
    }

    override fun onItemClickListener(itenerary: Itinerary, position: Int) {
        TODO("Not yet implemented")
    }



}