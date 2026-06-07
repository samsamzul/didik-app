package com.agartha.didik.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.agartha.didik.data.local.dao.UserDao
import com.agartha.didik.data.local.dao.UlasanDao
import com.agartha.didik.data.local.entity.KategoriEntity
import com.agartha.didik.data.local.entity.PerusahaanEntity
import com.agartha.didik.data.local.entity.UlasanEntity
import com.agartha.didik.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, KategoriEntity::class, PerusahaanEntity::class, UlasanEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DidikDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun ulasanDao(): UlasanDao

    companion object {
        @Volatile
        private var INSTANCE: DidikDatabase? = null

        fun getDatabase(context: Context): DidikDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DidikDatabase::class.java,
                    "didik_database" // Nama file database lokal di HP
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}