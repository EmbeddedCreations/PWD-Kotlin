package com.example.pwd_app.Utilities

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


object General {
    fun replaceFragment(
        fragmentManager: FragmentManager?,
        containerId: Int,
        fragment: Fragment?,
        addToBackStack: Boolean,
        fragmentTag: String?,
        enterAnim: Int,
        exitAnim: Int,
        popEnterAnim: Int,
        popExitAnim: Int
    ) {
        if (fragmentManager != null && fragment != null) {
            val transaction = fragmentManager.beginTransaction()

            // Set custom animations if needed
            transaction.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
            transaction.replace(containerId, fragment, fragmentTag)
            if (addToBackStack) {
                transaction.addToBackStack(fragmentTag)
            }
            transaction.commit()
        }
    }


}