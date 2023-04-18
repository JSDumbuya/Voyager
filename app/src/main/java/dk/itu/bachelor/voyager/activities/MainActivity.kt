package dk.itu.bachelor.voyager.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import dk.itu.bachelor.voyager.R
import dk.itu.bachelor.voyager.databinding.ActivityMainBinding

//relevant ift. når du sætter menuen: https://stackoverflow.com/questions/34011534/how-to-add-line-divider-for-menu-item-android
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        /*val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController*/

        setContentView(binding.root)

    }
}