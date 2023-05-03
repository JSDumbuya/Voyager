package dk.itu.bachelor.voyager.fragments

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dk.itu.bachelor.voyager.databinding.FragmentExperienceElementBinding
import dk.itu.bachelor.voyager.utilities.DATABASE_URL



class ExperienceElementFragment : Fragment() {
    private var _binding: FragmentExperienceElementBinding? = null


    /**
     * This property is only valid between `onCreateView()` and `onDestroyView()` methods.
     */
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = Firebase.database(DATABASE_URL).reference
        database.keepSynced(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentExperienceElementBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        if (bundle != null) {
            val itemId = bundle.getString("experienceId")

            val name = database.child("experiences/$itemId/name")

            name.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val info = dataSnapshot.getValue(String::class.java)
                    binding.title.setText(info)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
                }
            })

            val rating = database.child("experiences/$itemId/rating")

            rating.addValueEventListener(object : ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val info = dataSnapshot.getValue(Double::class.java)
                    binding.rating.text = "Rating: ${info.toString()}"
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
                }
            })

            val description = database.child("experiences/$itemId/description")

            description.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val info = dataSnapshot.getValue(String::class.java)
                    binding.description.setText(info)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
                }
            })

            /*val picture = database.child("experiences/$itemId/pictureUrls/0")

            //val storageRef = Firebase.storage.reference
            //val imageRef = storageRef.child("experiences/$itemId/pictureUrls/0")

           picture.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    Glide.with(requireActivity())
                        .load(picture)
                        .into(binding.exphoto)

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
                }
            })*/
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
