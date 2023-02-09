package com.example.a211206_room_2

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    //삽입에서 onConflict = OnConflictStrategy.REPLACE를 사용하면
    //데이터베이스 내에 중복된 (동일한 ID)토플을 삽입할 경우 덮어쒸우개 해 준다.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)

    //DB에 저장한 데이터를 전부 조회
    @Query("SELECT * FROM User")
    fun getAll(): LiveData<List<User>>

    @Query("DELETE FROM User ")
    fun deleteAll()
}