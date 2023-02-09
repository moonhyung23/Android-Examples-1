package com.example.a211205_room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//데이터 목록 (테이블)
@Entity
data class Friend(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?
)