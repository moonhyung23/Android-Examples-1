package com.example.ex_realmdb_1

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class User  (
    @PrimaryKey
    var id: Int? = 0,
    var name: String? = null,
    var age: Int? = 0,
) : RealmObject()