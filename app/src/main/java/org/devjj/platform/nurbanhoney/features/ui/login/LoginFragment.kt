package org.devjj.platform.nurbanhoney.features.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.exception.Failure
import org.devjj.platform.nurbanhoney.core.exception.Failure.NetworkConnection
import org.devjj.platform.nurbanhoney.core.exception.Failure.ServerError
import org.devjj.platform.nurbanhoney.core.extension.close
import org.devjj.platform.nurbanhoney.core.extension.failure
import org.devjj.platform.nurbanhoney.core.extension.observe
import org.devjj.platform.nurbanhoney.core.extension.setOnSingleClickListener
import org.devjj.platform.nurbanhoney.core.navigation.Navigator
import org.devjj.platform.nurbanhoney.core.platform.BaseFragment
import org.devjj.platform.nurbanhoney.databinding.FragmentLoginBinding
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment() {
    override fun layoutId() = R.layout.fragment_login

    @Inject
    lateinit var navigator: Navigator

    private val loginViewModel by viewModels<LoginViewModel>()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(loginViewModel) {
            observe(nurbanToken, ::tokenHandler)
            observe(isValid, ::loggedIn)
            failure(failure, ::handleFailure)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        kakaoLoginBtnListener(binding.loginKakao)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun tokenHandler(nurbanToken: NurbanToken?) {
        notify(R.string.test_string)
        if (nurbanToken != null) {
            loginViewModel.isTokenValid(nurbanToken.token)
        }
    }

    private fun loggedIn(isValid: TokenStatus?) {
        if (isValid != null) {
            navigator.showHome(requireContext())
        }
    }

    private fun handleFailure(failure: Failure?) {
        when (failure) {
            is NetworkConnection -> {
                notify(R.string.failure_network_connection); close()
            }
            is ServerError -> {
                notify(R.string.failure_server_error); close()
            }
            else -> {
                notify(R.string.failure_else_error); close()
            }
        }
    }

    private fun kakaoLoginBtnListener(view: View) = view.setOnSingleClickListener {
        UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
            if (error != null) {
                Log.d("login_check__", "kakao login failed")
            } else if (token != null) {
                loginViewModel.getNurbanToken("kakao", token.accessToken)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().finishAfterTransition()
    }
}

