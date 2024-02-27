package com.venya.primeshow.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Created by Shiva Pasunoori on 26,February,2024
 */

@Database(entities = [ FavTvShow::class ], exportSchema = false, version = 1)
abstract class MovieDatabase : RoomDatabase()
{
    abstract fun getDao() : MovieDao
}