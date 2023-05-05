package dk.itu.bachelor.voyager.models

import android.location.Location
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VoyagerVM : ViewModel() {

    /**
     * A list of all available fragments used by the main activity. P.S.: These instances are
     * created only once, when the app executes the `onCreate()` method for the first time.s
     */
    private val fragments = ArrayList<Fragment>()

    /**
     * The current selected fragment to show in the user interface.
     */
    private val fragment = MutableLiveData<Fragment>()

    /**
     * A `LiveData` which publicly exposes any update in the current shown fragment.
     *
     * @return The current selected fragment.
     */
    val fragmentState: LiveData<Fragment>
        get() = fragment

    /**
     * This method will be executed when the user selects a new fragment to show in the main
     * activity. It sets the text into the LiveData instance.
     *
     * @param index An ID of the selected fragment.
     */
    fun setFragment(index: Int) {
        fragment.value = fragments.elementAt(index)
    }

    /**
     * The current user location defined by the location-aware service.
     */
    private val location = MutableLiveData<Location>()

    /**
     * A LiveData which publicly exposes any update in the location-aware service.
     *
     * @return The current user location.
     */
    val locationState: LiveData<Location>
        get() = location

    /**
     * This method will be executed when the location-aware service updates the current user
     * location. It sets the updated location into the LiveData instance.
     *
     * @param location The current user's location obtained from the location-aware service.
     */
    fun onLocationChanged(location: Location) {
        this.location.value = location
    }
}