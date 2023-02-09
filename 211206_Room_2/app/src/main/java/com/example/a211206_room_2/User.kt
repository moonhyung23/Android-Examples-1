package com.example.a211206_room_2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    var name: String,
    var age: String
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}