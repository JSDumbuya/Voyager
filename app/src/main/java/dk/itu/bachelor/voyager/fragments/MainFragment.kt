package dk.itu.bachelor.voyager.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dk.itu.bachelor.voyager.R
import dk.itu.bachelor.voyager.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    //View binding for MainActivity
    private var _binding: FragmentMainBinding? = null

    /**
     * This property is only valid between `onCreateView()` and `onDestroyView()` methods.
     */
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(layoutInflater)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fm = parentFragmentManager

        with(binding){
            hey.setOnClickListener {
                //Start the application
                fm
                    .beginTransaction()
                    .replace(R.id.fragment_container, ExperiencesByListFragment())
                    .commit()
                //Log.d(TAG, "Database called")
            }
        }
    }

    companion object {

    }
}