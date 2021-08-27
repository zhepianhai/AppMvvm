package com.slb.media_lib.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.slb.media_lib.R

/**
 * A simple [Fragment] subclass.
 */
class TestFragment : Fragment() {
    companion object {
        fun newInstance(): TestFragment =
            TestFragment()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test, container, false)
    }

}
