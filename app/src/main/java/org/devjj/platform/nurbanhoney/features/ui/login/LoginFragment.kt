package org.devjj.platform.nurbanhoney.features.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.kakao.sdk.user.UserApiClient
//import com.nhn.android.naverlogin.OAuthLogin
//import com.nhn.android.naverlogin.OAuthLoginHandler
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
    @Inject
    lateinit var navigator: Navigator

    private val viewModel by viewModels<LoginViewModel>()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    //lateinit var mOAuthLoginModule: OAuthLogin
  //  private val RC_SIGN_IN = 9001

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
        with(viewModel) {
            observe(nurbanToken, ::tokenHandler)
            observe(isValid, ::loggedIn)
            failure(failure, ::handleFailure)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //카카오 로그인
        kakaoLoginBtnListener(binding.loginKakaoClo)
        //네이버 로그인인
        //naverLoginBtnListener(binding.loginNaverBtn)
        //구글 로그인
 //       googleLoginBtnListener(binding.loginGoogleClo)

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun tokenHandler(nurbanToken: NurbanToken?) {
        notify(R.string.test_string)
        if (nurbanToken != null) {
            viewModel.isTokenValid(nurbanToken.token)
        }
    }

    private fun loggedIn(isValid: TokenStatus?) {
        if (isValid != null) {
            //navigator.showHome(requireContext())
            requireActivity().onBackPressed()
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
                Log.d("login_check__", "카카오 로그인 실패 ${error.localizedMessage}")
            } else if (token != null) {
                viewModel.getNurbanToken("kakao", token.accessToken)
                Log.d("login_check__", "카카오 로그인 성공 ${token.accessToken}")
            }
        }
    }
//
//    private fun naverLoginBtnListener(view: View) = view.setOnSingleClickListener {
//
//        mOAuthLoginModule = OAuthLogin.getInstance()
//        mOAuthLoginModule.init(
//            requireContext(),
//            getString(R.string.naver_client_id),
//            getString(R.string.naver_client_secret),
//            getString(R.string.naver_client_name)
//        )
//
//        mOAuthLoginModule.startOauthLoginActivity(requireActivity(), mOAuthLoginHandler)
//    }

//    private val mOAuthLoginHandler = object : OAuthLoginHandler() {
//        override fun run(success: Boolean) {
//            if (success) {
//                val accessToken: String = mOAuthLoginModule.getAccessToken(requireContext())
//                val refreshToken: String = mOAuthLoginModule.getRefreshToken(requireContext())
//                val expiresAt: Long = mOAuthLoginModule.getExpiresAt(requireContext())
//                val tokenType: String = mOAuthLoginModule.getTokenType(requireContext())
//                Log.i("LoginData", "accessToken : " + accessToken);
//                Log.i("LoginData", "refreshToken : " + refreshToken);
//                Log.i("LoginData", "expiresAt : " + expiresAt);
//                Log.i("LoginData", "tokenType : " + tokenType);
//
//                viewModel.getNurbanToken("naver", accessToken)
//            } else {
//                val errorCode: String = mOAuthLoginModule.getLastErrorCode(requireContext()).code
//                val errorDesc = mOAuthLoginModule.getLastErrorDesc(requireContext())
//
//                Toast.makeText(
//                    requireContext(), "errorCode:" + errorCode
//                            + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//    }

//    private fun googleLoginBtnListener(view: View) = view.setOnSingleClickListener {
//
//        var signInIntent = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestEmail()
//            .build().let {
//                GoogleSignIn.getClient(requireActivity(), it)
//            }.signInIntent
//
//        startActivityForResult(signInIntent, RC_SIGN_IN)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == RC_SIGN_IN) {
//            try {
//                var task = GoogleSignIn.getSignedInAccountFromIntent(data)
//                    .getResult(ApiException::class.java)
//
//                Log.d("google_check__", task.toString())
//            } catch (e: ApiException) {
//                Log.d("google_check__", "signInResult:failed code=" + e.statusCode)
//            }
//
//        }
//    }
}
