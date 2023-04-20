package dk.itu.bachelor.voyager

import android.app.Application
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

const val DATABASE_URL = "https://voyager-bf0a9-default-rtdb.europe-west1.firebasedatabase.app/"

internal class VoyagerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Firebase.database(DATABASE_URL).setPersistenceEnabled(true)
    }

}