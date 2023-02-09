package com.example.customdialogfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.customdialogfragment.databinding.CustomDialogFragmentBinding


class SampleFragmentDialog : DialogFragment() {
    private var fragmentInterfacer: MyFragmentInterfacer? = null

    //LetterWriteFragment에 데이터를 넘겨주기 위한 인터페이스
    interface MyFragmentInterfacer {
        fun onButtonClick(input: String?)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.dialog_fullscreen)
        //false로 설정해 주면 화면밖 혹은 뒤로가기 버튼시 다이얼로그라 dismiss 되지 않는다.
        isCancelable = true
    }

    private lateinit var binding: CustomDialogFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = CustomDialogFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val text = "Hello, Welcome to blackjin Tisotry"

        binding.tvSample.text = text

        binding.btnSample.setOnClickListener {
            //인터페이스 추상메서드 실행
            fragmentInterfacer?.onButtonClick("aaaaa")
            Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }

    fun setFragmentInterfacer(fragmentInterfacer: MyFragmentInterfacer) {
        this.fragmentInterfacer = fragmentInterfacer
    }


}