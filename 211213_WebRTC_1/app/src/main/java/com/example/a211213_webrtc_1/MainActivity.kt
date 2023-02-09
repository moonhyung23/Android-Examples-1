package com.example.a211213_webrtc_1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a211213_webrtc_1.databinding.ActivityLoginBinding
import com.example.a211213_webrtc_1.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    //Class MainActivity
    lateinit var binding: ActivityMainBinding

    var array: MutableList<UserDTO> = arrayListOf()

    //친구의 uid가 담긴 array
    var uids: MutableList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        //데이터 베이스를 읽어올 수 있는 코드
        //-읽어올 컬렉션 -> Users
        FirebaseFirestore.getInstance().collection("users").get().addOnCompleteListener { task ->
            array.clear()
            uids.clear()
            //for문을 통해 data를 하나하나 읽어가면서 array에 데이터를 넣어준다.
            for (item in task.result!!.documents) {
                array.add(item.toObject(UserDTO::class.java)!!)
                uids.add(item.id)
            }
            binding.peopleListRecyclerview.adapter?.notifyDataSetChanged()
        }
        binding.peopleListRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.peopleListRecyclerview.adapter = RecyclerviewAdapter()

    }

    inner class RecyclerviewAdapter : RecyclerView.Adapter<RecyclerviewAdapter.ViewHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): RecyclerviewAdapter.ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_person, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerviewAdapter.ViewHolder, position: Int) {
            holder.itemEmail.text = array[position].email
            holder.itemView.setOnClickListener {
                openVideoActivity("howlab")
            }
        }

        override fun getItemCount(): Int {
            return array.size
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val itemEmail = view.findViewById<TextView>(R.id.item_email)
        }
    }

    fun openVideoActivity(channelId: String) {
        val i = Intent(this, VideoActivity::class.java)
        i.putExtra("channelId", channelId)
        startActivity(i)
    }

}






