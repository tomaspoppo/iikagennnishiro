package com.example.iikagennnishiro.data.dao

import androidx.room.*
import com.example.iikagennnishiro.data.entity.SalesRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface SalesDao {

    @Query("SELECT * FROM sales_records ORDER BY date DESC")
    fun getAllSales(): Flow<List<SalesRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(salesRecord: SalesRecord) // ✅ Continuation 削除

    @Update
    suspend fun update(salesRecord: SalesRecord) // ✅ Continuation 削除

    @Delete
    suspend fun delete(salesRecord: SalesRecord) // ✅ Continuation 削除
}
