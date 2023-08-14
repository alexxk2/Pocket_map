package com.example.pocketmap.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pocketmap.data.db.dto.PlaceDto

@Database(entities = [PlaceDto::class], version = 1, exportSchema = false)
abstract class PlacesDatabase : RoomDatabase() {

    abstract fun placesDao(): PlacesDao

    companion object {
        private var INSTANCE: PlacesDatabase? = null

        fun getDatabase(context: Context): PlacesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    PlacesDatabase::class.java,
                    "places_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}