package com.venya.primeshow.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

/**
 * Created by Shiva Pasunoori on 26,February,2024
 */
@Dao
interface MovieDao
{
    @Query("Select * from favTvShows")
    suspend fun getFavMovies() : List<FavTvShow>

    @Query("Select * from favTvShows where id=:id")
    suspend fun getFavMovie(id: Int) : FavTvShow

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavShow(favTvShow: FavTvShow)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavShows(favTvShow: List<FavTvShow>)

    @Update
    suspend fun updateFavShow(favTvShow: FavTvShow)

    @Query("Delete from favTvShows where id=:id")
    suspend fun deleteFavShow(id: Int)

    @Query("Delete from favTvShows")
    suspend fun deleteAllFavShows()

}