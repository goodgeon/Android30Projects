package com.example.a12bookreview.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.a12bookreview.model.Review

@Dao
interface ReviewDao {
    @Query("SELECT * FROM REVIEW WHERE id = :id")
    fun getOneReview(id: Int): Review?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveReview(review: Review)
}