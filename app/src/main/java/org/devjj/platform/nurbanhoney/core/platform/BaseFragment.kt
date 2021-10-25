package org.devjj.platform.nurbanhoney.core.platform

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import org.devjj.platform.nurbanhoney.databinding.FramentTextEditorBinding

@AndroidEntryPoint
abstract class BaseFragment : Fragment() {
    abstract fun layoutId(): Int

    open fun onBackPressed() {}
    internal fun notify(@StringRes message: Int) =
        Snackbar.make(this.requireView().rootView, message, Snackbar.LENGTH_SHORT).show()

    internal fun firstTimeCreated(savedInstanceState: Bundle?) = savedInstanceState == null
}