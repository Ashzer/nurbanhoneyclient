package org.devjj.platform.nurbanhoney.core.platform

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.exception.Failure

@AndroidEntryPoint
abstract class BaseFragment : Fragment() {

    internal fun notify(@StringRes message: Int) =
        Snackbar.make(this.requireView().rootView, message, Snackbar.LENGTH_SHORT).show()

    internal fun firstTimeCreated(savedInstanceState: Bundle?) = savedInstanceState == null

    fun failureHandler(failure: Failure?) {
        Log.d("failure_check__", failure?.toString() ?: "failure null")
        when (failure) {
            is Failure.NetworkConnection -> {
                Log.d("Fragment_failure", R.string.failure_network_connection.toString())
            }
            is Failure.ServerError -> {
                Log.d("Fragment_failure", R.string.failure_server_error.toString())
            }
            is Failure.TokenError -> {
                AlertDialog.Builder(this.requireContext())
                    .setMessage("로그인이 필요한 서비스입니다.")
                    .setPositiveButton("확인") { _, _ ->
                        Failure.TokenError(this.requireContext())
                    }
                    .setNegativeButton("취소") { _,_ ->
                        //Toast.makeText(this.requireContext(),"로그인이 취소 되었습니다", Toast.LENGTH_SHORT).show()
                        notify(R.string.snack_bar_msg_login_canceled)
                    }
                    .show()
            }
            else -> {
                Log.d("Fragment_failure", R.string.failure_else_error.toString())
            }
        }
    }
}