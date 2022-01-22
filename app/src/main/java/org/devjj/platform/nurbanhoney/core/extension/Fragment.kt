package org.devjj.platform.nurbanhoney.core.extension

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import org.devjj.platform.nurbanhoney.core.platform.BaseActivity
import org.devjj.platform.nurbanhoney.core.platform.BaseFragment

inline fun FragmentManager.inTransaction(func : FragmentTransaction.() -> FragmentTransaction) =
    beginTransaction().func().addToBackStack(null).commit()

val BaseFragment.appContext: Context get() = activity?.applicationContext!!

fun BaseFragment.close() = fragmentManager?.popBackStack()