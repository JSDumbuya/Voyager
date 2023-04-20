package dk.itu.bachelor.voyager.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.get
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dk.itu.bachelor.voyager.R
import dk.itu.bachelor.voyager.databinding.ActivityMainBinding

//relevant ift. når du sætter menuen: https://stackoverflow.com/questions/34011534/how-to-add-line-divider-for-menu-item-android
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

       val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.exploreFragment -> navController.navigate(R.id.exploreFragment)
                R.id.itinerariesFragment -> navController.navigate(R.id.itinerariesFragment)
                R.id.planFragment -> navController.navigate(R.id.planFragment)
            }
            true
        }

        setContentView(binding.root)

    }

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        with(item.itemId) {
        }
        return super.onOptionsItemSelected(item)
    }*/
}