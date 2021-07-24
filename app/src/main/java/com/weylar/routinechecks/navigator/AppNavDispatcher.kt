package com.weylar.routinechecks.navigator

import android.os.Bundle
import androidx.fragment.app.Fragment

interface AppNavDispatcher : NavDispatcher {
    override fun goBack(fragment: Fragment)
    fun openRoutineDetail(fragment: Fragment, bundle: Bundle)
}