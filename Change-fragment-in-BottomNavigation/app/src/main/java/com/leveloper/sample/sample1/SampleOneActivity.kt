package com.leveloper.sample.sample1

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.leveloper.sample.R
import com.leveloper.sample.databinding.ActivitySampleOneBinding
import com.leveloper.sample.SampleFragment
import java.lang.IllegalArgumentException

class SampleOneActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySampleOneBinding.inflate(layoutInflater) }
    val TAG = "SampleOneActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Log.e(TAG, "onCreate")
        binding.bnvMain.setOnItemSelectedListener {
            //선택한 프래그먼트의 id 입력
            changeFragment(it.itemId)
            true
        }

        // init fragment
        changeFragment(R.id.item_page_1)
    }

    private fun getFragment(menuItemId: Int): Fragment {
        val title = when (menuItemId) {
            R.id.item_page_1 -> "page1"
            R.id.item_page_2 -> "page2"
            R.id.item_page_3 -> "page3"
            else -> throw IllegalArgumentException("not found menu item id")
        }
        return SampleFragment.newInstance(title)
    }

    private fun changeFragment(menuItemId: Int) {
        //선택한 ID에 맞는 프래그먼트 객체 반환
        val targetFragment = getFragment(menuItemId)

        //선택한 프래그먼트로 전환
        supportFragmentManager.beginTransaction()
            //replace를 통해 화면이 전환되므로 새로운 프래그먼트가 생성되어 재활용 하지 못하는 단점이 있음.
            .replace(R.id.container, targetFragment)
            .commitAllowingStateLoss()
    }


}