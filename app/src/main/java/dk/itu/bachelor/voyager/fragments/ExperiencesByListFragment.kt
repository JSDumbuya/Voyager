package dk.itu.bachelor.voyager.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dk.itu.bachelor.voyager.utilities.DATABASE_URL
import dk.itu.bachelor.voyager.adapters.ExperienceArrayAdapter
import dk.itu.bachelor.voyager.databinding.FragmentExperiencesByListBinding
import dk.itu.bachelor.voyager.models.Experience


/**
 * A simple [Fragment] subclass.
 * Use the [ExperiencesByListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExperiencesByListFragment : Fragment() {

    //Setting up authentication
    private lateinit var auth: FirebaseAuth

    //Binding
    private var _binding: FragmentExperiencesByListBinding ? = null

    /**
     * This property is only valid between `onCreateView()` and `onDestroyView()` methods.
     */
    private val binding get() = _binding!!

    //Setting up the database
    private lateinit var database: DatabaseReference

    companion object {
        private lateinit var adapter: ExperienceArrayAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


       //Binding between layout and fragment
        _binding = FragmentExperiencesByListBinding.inflate(layoutInflater)

        //Initialize FireBase Auth.
        auth = FirebaseAuth.getInstance()
        //Connect to realtime database
        database = Firebase.database(DATABASE_URL).reference

        // Enable offline capabilities.
        database.keepSynced(true)


        // Create the search query.
        val query = database.child("experiences")


        // A class provide by FirebaseUI to make a query in the database to fetch appropriate data.
        val options = FirebaseRecyclerOptions.Builder<Experience>()
            .setQuery(query, Experience::class.java)
            .setLifecycleOwner(this)
            .build()


        adapter = ExperienceArrayAdapter(this, options)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        )


        binding.recyclerView.adapter = adapter

        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun onItemClickListener(experience: Experience , position: Int) {
        TODO("Not yet implemented")
    }

}