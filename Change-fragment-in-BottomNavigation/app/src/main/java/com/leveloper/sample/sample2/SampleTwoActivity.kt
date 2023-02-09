package com.leveloper.sample.sample2

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.leveloper.sample.PageType
import com.leveloper.sample.R
import com.leveloper.sample.SampleFragment
import com.leveloper.sample.databinding.ActivitySampleTwoBinding
import java.lang.IllegalArgumentException

class SampleTwoActivity : AppCompatActivity() {
    val TAG = "SampleTwoActivity"

    private val binding by lazy { ActivitySampleTwoBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Log.e(TAG, "onCreate")

        binding.bnvMain.setOnItemSelectedListener {
            //선택한 프래그먼트 ID에 맞는 프래그먼트 Type을 반환
            val pageType = getPageType(it.itemId)
            //프래그먼트 전환
            changeFragment(pageType)
            true
        }

        changeFragment(PageType.PAGE1)
    }

    private fun changeFragment(pageType: PageType) {
        val transaction = supportFragmentManager.beginTransaction()
        //이전에 저장해 놓은 프래그먼트를 찾는다.
        var targetFragment = supportFragmentManager.findFragmentByTag(pageType.tag)

        //이전에 저장한 프래그먼트가 없는 경우
        if (targetFragment == null) {
            //새 프래그먼트 생성
            targetFragment = getFragment(pageType)
            //프래그먼트 등록
            transaction.add(R.id.container, targetFragment, pageType.tag)
        }

        //프래그먼트 보여주기
        transaction.show(targetFragment)

        //현재 펼쳐진 프래그먼트를 제외하고 다른 프래그먼트 숨기기
        PageType.values()
                //현재 펼쳐진 프래그먼트 제외
            .filterNot { it == pageType }
                //저장된 프래그먼트의 개수만큼 숨김
            .forEach { type ->
                supportFragmentManager.findFragmentByTag(type.tag)?.let {
                    transaction.hide(it)
                }
            }

        transaction.commitAllowingStateLoss()
    }

    //선택한 프래그먼트 Type을  반환
    private fun getPageType(menuItemId: Int): PageType {
        return when (menuItemId) {
            R.id.item_page_1 -> PageType.PAGE1
            R.id.item_page_2 -> PageType.PAGE2
            R.id.item_page_3 -> PageType.PAGE3
            else -> throw IllegalArgumentException("not found menu item id")
        }
    }

    private fun getFragment(pageType: PageType): Fragment {
        return SampleFragment.newInstance(pageType.title)
    }


    //엑티비티의 onCreate함수에서 changeFragment() 함수를 호출해 1번 페이지로 변경되게끔
    //고정했기 때문에 화면이 화면회전등이 일어나면 틀어진다.
    //기존에 보고있던 프래그먼트를 저장한 뒤 해당 프래그먼트로 변경해줘야 한다.
    //이는 ViewModel통해서 해결할 수 있다.

}