package com.example.ex_realmdb_1

import android.app.Application
import android.content.Context
import io.realm.*
import io.realm.kotlin.where


class App : Application() {
    companion object {
        lateinit var instance: App
        lateinit var realm: Realm
    }


    //객체 생성
    init {
        instance = this
    }


    override fun onCreate() {
        super.onCreate()
        // Realm 초기화
        Realm.init(applicationContext)
        val config: RealmConfiguration = RealmConfiguration.Builder()
            .allowWritesOnUiThread(true)
            .name("user.realm") // 생성할 realm db 이름
            .deleteRealmIfMigrationNeeded()
            .build()

        Realm.setDefaultConfiguration(config)

        realm = Realm.getDefaultInstance()
    }

    //Context -> 어느 엑티비티에서도 컨텍스트를 받아올 수 있음
    fun context(): Context {
        return instance.applicationContext
    }

    // 사용자 저장 또는 갱신하기
    fun insert(user: User) {
        realm.executeTransactionAsync {
            it.insertOrUpdate(user)
        }
    }

    fun edit(id: Int): User? {
        return realm.where<User>().equalTo("id", id).findFirst()
    }

    // 모든 사용자 읽어오기
    fun getAllUser(): RealmResults<User> {
        return realm.where<User>()
            .findAll()
            .sort("id", Sort.ASCENDING)
    }

    // 특정 사용자 읽어오기
    fun getUser(id: Int): User? = realm.where<User>()
        .equalTo("id", id)
        .findFirst()


    // 모든 사용자 삭제
    fun deleteAllUser() {
        realm.executeTransaction {
            it.where<User>().findAll().deleteAllFromRealm()
        }
    }

    // 특정 사용자 삭제
    fun deleteUser(id: Int) {
        realm.executeTransaction {
            it.where<User>().equalTo("id", id).findAll().deleteAllFromRealm()
        }
    }
}