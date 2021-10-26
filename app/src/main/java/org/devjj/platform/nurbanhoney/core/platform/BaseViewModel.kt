/**
 * Copyright (C) 2020 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.devjj.platform.nurbanhoney.core.platform

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.internal.notify
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.exception.Failure

/**
 * Base ViewModel class with default Failure handling.
 * @see ViewModel
 * @see Failure
 */
abstract class BaseViewModel : ViewModel() {

    private val _failure: MutableLiveData<Failure> = MutableLiveData()
    val failure: LiveData<Failure> = _failure

    protected fun handleFailure(failure: Failure) {
        _failure.value = failure

        Log.d("token_check__", failure.toString())
        when (failure) {
            is Failure.NetworkConnection -> {
                Log.d("viewmodel_failure",R.string.failure_network_connection.toString())
            }
            is Failure.ServerError -> {
                Log.d("viewmodel_failure", R.string.failure_server_error.toString())
            }
            else -> {
                Log.d("viewmodel_failure", R.string.failure_server_error.toString())
            }
        }
    }
}