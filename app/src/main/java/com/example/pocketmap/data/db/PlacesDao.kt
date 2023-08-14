package com.example.pocketmap.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pocketmap.data.db.dto.PlaceDto

@Dao
interface PlacesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNewItem(placeDto: PlaceDto)

    @Update
    suspend fun editItem(placeDto: PlaceDto)

    @Delete
    suspend fun deleteItem(placeDto: PlaceDto)

    @Query("SELECT * FROM places ORDER BY id ASC")
    suspend fun getAllItems(): List<PlaceDto>

    @Query("SELECT * FROM places WHERE id =:id")
    suspend fun getItem(id: Int): PlaceDto

    @Query("DELETE FROM places")
    suspend fun deleteAllItems()


}