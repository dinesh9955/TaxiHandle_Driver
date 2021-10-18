package com.rydz.driver.interfaces

import androidx.fragment.app.Fragment

public interface DrawerView {

    fun OpenDrawer()
    fun onReplaceFragment(fragment: Fragment, tag:String)
    fun onChangingfragment(type:String)
}