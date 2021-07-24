package com.weylar.routinechecks.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.weylar.routinechecks.R
import com.weylar.routinechecks.navigator.AppNavDispatcher

class AppNavImpl : AppNavDispatcher {
    override fun goBack(fragment: Fragment) {
        NavHostFragment.findNavController(fragment).navigateUp()
    }

    override fun openRoutineDetail(fragment: Fragment, bundle: Bundle) {
        NavHostFragment.findNavController(fragment)
            .navigate(R.id.action_to_routine_detail_fragment, bundle)
    }

    override fun popUp(fragment: Fragment) {
        NavHostFragment.findNavController(fragment).popBackStack()
    }


}