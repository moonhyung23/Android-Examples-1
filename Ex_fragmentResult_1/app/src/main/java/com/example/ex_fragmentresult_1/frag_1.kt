package com.example.ex_fragmentresult_1

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.ex_fragmentresult_1.databinding.Frag1Binding

class frag_1 : Fragment() {

    lateinit var binding: Frag1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = Frag1Binding.inflate(inflater, container, false)
        init()

        binding.run {
            radioMeasure.clipToOutline = true
            radioMeasure.setOnCheckedChangeListener { group, checkedId ->
                when (checkedId) {
                    R.id.radio_measure_tap -> {
                        val fragmentTransaction: FragmentTransaction =
                            childFragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.container_measure,
                            MeasureTapFragment(),
                            "tap").commit()
                    }
                    R.id.radio_measure_dolittle -> {
                        val fragmentTransaction: FragmentTransaction =
                            childFragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.container_measure,
                            MeasureDolittleFragment(),
                            "dolittle").commit()
                    }
                }
            }
        }
        return binding.root
    }

    fun init() {
        val fragmentTransaction: FragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.container_measure, MeasureDolittleFragment(), "dolittle")
            .commit()
    }
}