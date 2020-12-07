package ru.stivosha.finalwork.data.db.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import ru.stivosha.finalwork.data.db.dbentity.PostDbEntity

@Dao
interface PostDao {

    @Query("SELECT * FROM Post")
    fun getAll(): Single<List<PostDbEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg posts: PostDbEntity)

    @Query("SELECT * FROM Post WHERE id=:id")
    fun getById(id: Int): Single<PostDbEntity>

    @Update
    fun update(post: PostDbEntity): Completable

    @Delete
    fun delete(post: PostDbEntity): Completable

    @Query("DELETE FROM Post")
    fun clearTable()
}