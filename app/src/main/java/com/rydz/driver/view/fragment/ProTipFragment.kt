package com.rydz.driver.view.fragment

import com.rydz.driver.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ProTipFragment : BaseFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_protip, container, false)
        return view
    }
    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id)
        {

        }
    }
}
