package com.example.iikagennnishiro.data.database

import android.content.Context
import androidx.room.*
import com.example.iikagennnishiro.data.dao.SalesDao
import com.example.iikagennnishiro.data.entity.SalesRecord

@Database(entities = [SalesRecord::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun salesDao(): SalesDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sales_database"
                ).fallbackToDestructiveMigration() // ✅ 追加
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
