package com.mapr.credibanco.model.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [DataAuthorization::class], version = 1, exportSchema = false
)

abstract class DaoDatabase : RoomDatabase() {
    abstract fun dataAuthDao(): DataAuthorizationDao
}