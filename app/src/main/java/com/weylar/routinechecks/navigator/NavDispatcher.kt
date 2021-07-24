package com.weylar.routinechecks.navigator

import androidx.fragment.app.Fragment

interface NavDispatcher {
    fun goBack(fragment: Fragment)
    fun popUp(fragment: Fragment)
}