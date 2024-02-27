package com.venya.primeshow

import android.app.Application
import androidx.room.Room
import com.venya.primeshow.data.local.MovieDatabase
import com.venya.primeshow.utils.Constants
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by Shiva Pasunoori on 25,February,2024
 */
@HiltAndroidApp
class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()

    }
}