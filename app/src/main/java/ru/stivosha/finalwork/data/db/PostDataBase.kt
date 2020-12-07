package ru.stivosha.finalwork.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.stivosha.finalwork.data.db.dao.PostDao
import ru.stivosha.finalwork.data.db.dbentity.PostDbEntity

@Database(entities = [PostDbEntity::class], version = 1)
abstract class PostDataBase : RoomDatabase() {
    abstract fun postDao(): PostDao
}