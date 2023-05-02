package dk.itu.bachelor.voyager.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import dk.itu.bachelor.voyager.R
import dk.itu.bachelor.voyager.databinding.ActivityMainBinding
import dk.itu.bachelor.voyager.models.VoyagerVM

//relevant ift. når du sætter menuen: https://stackoverflow.com/questions/34011534/how-to-add-line-divider-for-menu-item-android
class MainActivity : AppCompatActivity() {

    //Setting up authentication
    private lateinit var auth: FirebaseAuth

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
        binding = ActivityMainBinding.inflate(layoutInflater)

        //Initialize FireBase Auth.
        auth = FirebaseAuth.getInstance()

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

    override fun onStart() {
        super.onStart()
        if (auth.currentUser == null)
            startLoginActivity()
        val user = auth.currentUser
    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        //finish()
    }


    private fun toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, text, duration).show()
    }
}