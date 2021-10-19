package org.devjj.platform.nurbanhoney.features.ui.login

import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.platform.BaseFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.*
import okhttp3.internal.wait
import org.devjj.platform.nurbanhoney.AndroidApplication
import org.devjj.platform.nurbanhoney.core.extension.observe
import org.devjj.platform.nurbanhoney.core.navigation.Navigator
import org.devjj.platform.nurbanhoney.databinding.FragmentLoginBinding
import org.devjj.platform.nurbanhoney.features.network.LoginService
import retrofit2.await
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    @Inject
    internal lateinit var loginService : LoginService
    private val loginViewModel by viewModels<LoginViewModel>()
    override fun layoutId() = R.layout.fragment_login

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var navigator: Navigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(loginViewModel){
            observe(nurbanToken, ::tokenHandler)
            observe(isValid, ::validated)
        }

    }

    private fun tokenHandler(nurbanToken: NurbanToken?){
        notify(R.string.test_string)
        if(nurbanToken != null ) {
            loginViewModel.isTokenValid(nurbanToken.token)
        }

    }

    private fun validated( isValid:TokenValidation?){
        if(isValid != null) navigator.showHome(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginKakao.setOnClickListener {
            UserApiClient.instance.loginWithKakaoTalk(requireContext()){ token, error ->
                if(error != null){
                    Log.d("login_check__", "login failed")
                }
                else if( token != null){
                    Log.d("login_check__", "login succeed ${token.accessToken}")

                    loginViewModel.getNurbanToken("kakao",token.accessToken)
                    Log.d("token_check__", loginViewModel.getToken())
                    //Log.d("token_check__",loginViewModel.isTokenValid(loginViewModel.getToken()).toString())


                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}