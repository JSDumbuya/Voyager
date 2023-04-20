package dk.itu.bachelor.voyager.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import dk.itu.bachelor.voyager.ExperiencesByListFragment
import dk.itu.bachelor.voyager.MainFragment
import dk.itu.bachelor.voyager.R
import dk.itu.bachelor.voyager.databinding.ActivityMainBinding
import dk.itu.bachelor.voyager.models.VoyagerVM

class MainActivity : AppCompatActivity() {

    //View binding for MainActivity
    private lateinit var binding: ActivityMainBinding

    /**
     * Using lazy initialization to create the view model instance when the user access the object
     * for the first time.
     */
    private val viewModel: VoyagerVM by lazy {
        ViewModelProvider(this)
            .get(VoyagerVM::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)

        // Get the latest fragment added in the fragment manager.
        val currentFragment =
            supportFragmentManager.findFragmentById(androidx.fragment.R.id.fragment_container_view_tag)

        // Create the fragments instances.
        if (currentFragment == null) {
            viewModel.addFragment(MainFragment())
            viewModel.addFragment(ExperiencesByListFragment())
            viewModel.setFragment(0)
        }

        // Add the fragment into the activity.
        for (fragment in viewModel.getFragmentList())
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container_view_tag, fragment)
                .hide(fragment)
                .commit()


        // The current activity.
        var activeFragment: Fragment = viewModel.fragmentState.value!!

        // Execute this when the user sets a specific fragment.
        viewModel.fragmentState.observe(this) { fragment ->
            supportFragmentManager
                .beginTransaction()
                .hide(activeFragment)
                .show(fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit()
            activeFragment = fragment
        }

        setContentView(binding.root)
    }
}