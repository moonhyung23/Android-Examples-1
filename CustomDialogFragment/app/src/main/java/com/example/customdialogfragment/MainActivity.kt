package com.example.customdialogfragment


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.customdialogfragment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var sampleDialog: SampleFragmentDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        sampleDialog = SampleFragmentDialog()
        binding.btnSample.setOnClickListener {
            sampleDialog?.show(supportFragmentManager, "SampleDialog")

        }
        sampleDialog?.setFragmentInterfacer(object : SampleFragmentDialog.MyFragmentInterfacer {
            override fun onButtonClick(input: String?) {
                Toast.makeText(applicationContext, "전달 값: $input", Toast.LENGTH_SHORT).show()
            }
        }

        )
    }


}


