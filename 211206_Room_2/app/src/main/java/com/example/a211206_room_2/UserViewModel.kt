package com.example.a211206_room_2

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = UserRepository(application)

    // ViewModel에 파라미터를 넘기기 위해서, 파라미터를 포함한 Factory 객체를 생성하기 위한 클래스
    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return UserViewModel(application) as T
        }
    }

    // 새로운 유저정보 추가시 옵저버가 감지하여 updateUserList 함수를 호출하기 때문에 자동으로 뷰 갱신
    fun addUser(name: String, age: String) {
        val user = User(name, age)

        CoroutineScope(Dispatchers.IO).launch {
            repository.insert(user)
        }
    }

    fun getAll(): LiveData<List<User>> {
        return repository.getAll()
    }

    // 유저정보 삭제시 옵저버가 감지하여 updateUserList 함수를 호출하기 때문에 자동으로 뷰 갱신
    fun deleteAll() {
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteAll()
        }
    }

}