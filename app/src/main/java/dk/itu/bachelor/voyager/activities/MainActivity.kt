package dk.itu.bachelor.voyager.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDestination
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.location.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.firebase.auth.FirebaseAuth
import dk.itu.bachelor.voyager.R
import dk.itu.bachelor.voyager.databinding.ActivityMainBinding
import dk.itu.bachelor.voyager.models.VoyagerVM
import java.util.concurrent.TimeUnit

//relevant ift. når du sætter menuen: https://stackoverflow.com/questions/34011534/how-to-add-line-divider-for-menu-item-android
class MainActivity : AppCompatActivity() {

    //Setting up authentication
    private lateinit var auth: FirebaseAuth

    //View binding for MainActivity
    private lateinit var binding: ActivityMainBinding

    /**
     * The primary instance for receiving location updates.
     */
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    /**
     * This callback is called when `FusedLocationProviderClient` has a new `Location`.
     */
    private lateinit var locationCallback: LocationCallback

    /**
     * A set of static attributes used in this activity class.
     */
    companion object {
        private const val ALL_PERMISSIONS_RESULT = 1011
    }

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

        with(binding){
            bottomNavigation.setupWithNavController(navController)

            bottomNavigation.setOnItemSelectedListener {
                when(it.itemId) {
                    R.id.exploreFragment -> navController.navigate(R.id.exploreFragment)
                    R.id.itinerariesFragment -> navController.navigate(R.id.itinerariesFragment)
                    R.id.planFragment -> navController.navigate(R.id.planFragment)
                    R.id.osterbroFragment -> navController.navigate(R.id.osterbroFragment)
                    R.id.norrebroFragment -> navController.navigate(R.id.norrebroFragment)
                }
                true
            }

            /**Logic for the bottom navigation menu, should be hidden for "Attractions by list",
             * and show different options for different pages */
            navController.addOnDestinationChangedListener{_, nd: NavDestination, _->
                if(nd.id == R.id.experiFrag || nd.id == R.id.experienceElement) {
                    bottomNavigation.visibility = View.GONE
                } else if(nd.id == R.id.osterbroFragment || nd.id == R.id.norrebroFragment) {
                    bottomNavigation.visibility = View.VISIBLE
                    bottomNavigation.menu.findItem(R.id.exploreFragment).setVisible(false)
                    bottomNavigation.menu.findItem(R.id.planFragment).setVisible(false)
                    bottomNavigation.menu.findItem(R.id.itinerariesFragment).setVisible(false)
                    bottomNavigation.menu.findItem(R.id.norrebroFragment).setVisible(true)
                    bottomNavigation.menu.findItem(R.id.osterbroFragment).setVisible(true)
                } else {
                    bottomNavigation.visibility = View.VISIBLE
                    bottomNavigation.menu.findItem(R.id.exploreFragment).setVisible(true)
                    bottomNavigation.menu.findItem(R.id.planFragment).setVisible(true)
                    bottomNavigation.menu.findItem(R.id.itinerariesFragment).setVisible(true)
                    bottomNavigation.menu.findItem(R.id.norrebroFragment).setVisible(false)
                    bottomNavigation.menu.findItem(R.id.osterbroFragment).setVisible(false)
                }
            }

            topAppBar.setupWithNavController(navController)

            topAppBar.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.explore -> navController.navigate(R.id.exploreFragment)
                    R.id.itineraries -> navController.navigate(R.id.itinerariesFragment)
                    R.id.att_by_list -> navController.navigate(R.id.experiFrag)
                    R.id.neighborhoods -> navController.navigate(R.id.osterbroFragment)
                    R.id.sign_out -> AuthUI.getInstance().signOut(applicationContext).addOnCompleteListener{
                        //startLoginActivity()
                    }
                }
                true
            }
        }
        // Start the location-aware method.
        startLocationAware()

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

    override fun onResume() {
        super.onResume()
        subscribeToLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        unsubscribeToLocationUpdates()
    }

    /**
     * Start the location-aware instance and defines the callback to be called when the GPS sensor
     * provides a new user's location.
     */
    private fun startLocationAware() {

        // Show a dialog to ask the user to allow the application to access the device's location.
        requestUserPermissions()

        // Start receiving location updates.
        fusedLocationProviderClient = LocationServices
            .getFusedLocationProviderClient(this)

        // Initialize the `LocationCallback`.
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.lastLocation?.let { viewModel.onLocationChanged(it) }
            }
        }
    }

    /**
     * Create a set of dialogs to show to the users and ask them for permissions to get the device's
     * resources.
     */
    private fun requestUserPermissions() {
        // An array with location-aware permissions.
        val permissions: ArrayList<String> = ArrayList()
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)

        // Check which permissions is needed to ask to the user.
        val permissionsToRequest = permissionsToRequest(permissions)

        // Show the permissions dialogs to the user.
        if (permissionsToRequest.size > 0)
            requestPermissions(
                permissionsToRequest.toTypedArray(),
                ALL_PERMISSIONS_RESULT
            )
    }

    /**
     * Create an array with the permissions to show to the user.
     *
     * @param permissions An array with the permissions needed by this applications.
     *
     * @return An array with the permissions needed to ask to the user.
     */
    private fun permissionsToRequest(permissions: ArrayList<String>): ArrayList<String> {
        val result: ArrayList<String> = ArrayList()
        for (permission in permissions)
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                result.add(permission)
        return result
    }

    /**
     * This method checks if the user allows the application uses all location-aware resources to
     * monitor the user's location.
     *
     * @return A boolean value with the user permission agreement.
     */
    private fun checkPermission() =
        ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED

    /**
     * Subscribes this application to get the location changes via the `locationCallback()`.
     */
    @SuppressLint("MissingPermission")
    private fun subscribeToLocationUpdates() {
        // Check if the user allows the application to access the location-aware resources.
        if (checkPermission())
            return

        // Sets the accuracy and desired interval for active location updates.
        val locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(5)
            fastestInterval = TimeUnit.SECONDS.toMillis(2)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        // Subscribe to location changes.
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.getMainLooper()
        )
    }

    /**
     * Unsubscribes this application of getting the location changes from  the `locationCallback()`.
     */
    private fun unsubscribeToLocationUpdates() {
        // Unsubscribe to location changes.
        fusedLocationProviderClient
            .removeLocationUpdates(locationCallback)
    }

    private fun toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, text, duration).show()
    }

}


