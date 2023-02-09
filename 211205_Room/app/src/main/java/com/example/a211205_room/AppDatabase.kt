package com.example.a211205_room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//1)주석 내에 데이터베이스와 연결된 항목의 목록(테이블)
@Database(entities = arrayOf(User::class), version = 1)
//2)RoomDatabase를 확장하는 추상클래스
abstract class AppDatabase : RoomDatabase() {
    //3)인수가 0개인 @Dao로 주석이 지정된 클래스를 반환하는 추상 메서드
    abstract fun userDao(): UserDao


    companion object {
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "user-database"
                    ).build()
                }
            }
            return instance
        }
    }
}
