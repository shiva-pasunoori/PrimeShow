package com.venya.primeshow.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import javax.annotation.processing.Generated

/**
 * Created by Shiva Pasunoori on 26,February,2024
 */

@Entity(tableName = "favTvShows", indices = [Index(value = ["id"], unique = true)])
data class FavTvShow(
    val id:Int,
    val name:String,
    val posterPath :String,
    val voteAverage :String){

    @PrimaryKey(autoGenerate  =true)
    var showId: Int =0
}