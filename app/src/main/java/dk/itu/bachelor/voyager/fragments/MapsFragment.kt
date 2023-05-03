package dk.itu.bachelor.voyager.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.*
import com.google.android.gms.maps.OnMapsSdkInitializedCallback

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dk.itu.bachelor.voyager.R
import dk.itu.bachelor.voyager.databinding.FragmentMapsBinding
import dk.itu.bachelor.voyager.models.Experience
import dk.itu.bachelor.voyager.utilities.DATABASE_URL

class MapsFragment : Fragment(), OnMapsSdkInitializedCallback, View.OnClickListener {

    private var _binding: FragmentMapsBinding? = null

    private var experience: String = ""

    // Store the selected marker as a class-level variable
    private var selectedMarker: Marker? = null

    /**
     * This property is only valid between `onCreateView()` and `onDestroyView()` methods.
     */
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapsInitializer.initialize(
            requireContext(),
            MapsInitializer.Renderer.LATEST, this
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMapsBinding.inflate(inflater, container, false)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkPermission() =
        ActivityCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        // Add a marker in ITU and move the camera
        // Check if the user has granted location permission
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permission
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            // Get the user's current location
            val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, object :
                LocationListener {
                override fun onLocationChanged(location: Location) {
                    // Set the camera position to the user's current location
                    val latLng = LatLng(location.latitude, location.longitude)
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))

                    // Remove the location listener to conserve battery
                    locationManager.removeUpdates(this)
                }

                override fun onProviderDisabled(provider: String) {}
                override fun onProviderEnabled(provider: String) {}
                override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            })
        }

        Firebase.database(DATABASE_URL).reference.apply {
            keepSynced(true)
        }.child("experiences").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val tempExp = postSnapshot.getValue(Experience::class.java)
                    var tempPosition =
                        tempExp?.let { LatLng(it.getLat(), tempExp.getLon()) }
                    if (tempExp != null) {
                        tempPosition?.let {
                            MarkerOptions()
                                .position(it)
                                .title(tempExp.name.toString())
                        }?.let {
                            googleMap.addMarker(
                                it
                            )
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //If post failed, log message
                Log.d(TAG, "Loadpost: onCancelled")
            }
        })

        with(binding) {
            fab.setOnClickListener(this@MapsFragment)
        }

        //Floating action button appears when marker is selected
        googleMap.setOnMarkerClickListener { marker ->
            experience = marker.title ?: "Unknown"
            selectedMarker = marker // add this line to assign clicked marker to the selectedMarker variable
            if (marker.isInfoWindowShown) {
                Log.d(TAG, marker.toString())
                with(binding) {
                    fab.show()
                }
            } else {
                marker.showInfoWindow()
                with(binding) {
                    fab.show()
                }
                Log.d(TAG, marker.toString() + "helo")
            }
            true
        }

        if (!checkPermission()) {
            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = true
        }

    }

    override fun onMapsSdkInitialized(renderer: MapsInitializer.Renderer) {
        when (renderer) {
            MapsInitializer.Renderer.LATEST ->
                Log.d(
                    TAG,
                    "The latest version of the renderer is used."
                )
            MapsInitializer.Renderer.LEGACY ->
                Log.d(
                    TAG,
                    "The legacy version of the renderer is used."
                )
        }
    }

    companion object {
        private val TAG = MapsFragment::class.qualifiedName
    }

    override fun onClick(view: View?) {
        // If no marker is selected, return early
        selectedMarker ?: return

        // Get the latitude and longitude of the selected marker
        val latitude = selectedMarker?.position?.latitude
        val longitude = selectedMarker?.position?.longitude

        // Create the intent to launch Google Maps
        val uri = Uri.parse("google.navigation:q=$latitude,$longitude")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")

        // Start the intent
        startActivity(intent)

    }

}