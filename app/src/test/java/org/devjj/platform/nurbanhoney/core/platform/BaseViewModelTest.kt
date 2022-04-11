package org.devjj.platform.nurbanhoney.core.platform

import androidx.lifecycle.MutableLiveData
import org.amshove.kluent.shouldBeInstanceOf
import org.devjj.platform.nurbanhoney.AndroidTest
import org.devjj.platform.nurbanhoney.core.exception.Failure
import org.devjj.platform.nurbanhoney.core.exception.Failure.NetworkConnection
import org.junit.jupiter.api.Test

class BaseViewModelTest {
    @Test
    fun `should handle failure by updating live data`() {
        val viewModel = MyViewModel()

        viewModel.handleError(NetworkConnection)

        val failure = viewModel.failure
        val error = viewModel.failure.value

        failure shouldBeInstanceOf MutableLiveData::class.java
        error shouldBeInstanceOf NetworkConnection::class.java
    }

    private class MyViewModel : BaseViewModel() {
        fun handleError(failure: Failure) = handleFailure(failure)
    }
}