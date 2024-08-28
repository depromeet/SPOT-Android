package com.dpm.presentation.login.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.lifecycle.asLiveData
import com.depromeet.presentation.databinding.FragmentKakaoSignupBinding
import com.dpm.core.base.BaseActivity
import com.dpm.presentation.extension.getCompatibleParcelableExtra
import com.dpm.presentation.extension.toast
import com.dpm.presentation.home.HomeActivity
import com.dpm.presentation.login.ui.compose.KakaoSignupScreen
import com.dpm.presentation.login.viewmodel.KakaoSignupViewModel
import com.dpm.presentation.login.viewmodel.LoginUiState
import com.dpm.presentation.scheme.SchemeKey
import com.dpm.presentation.scheme.viewmodel.SchemeState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class KakaoSignupActivity : BaseActivity<FragmentKakaoSignupBinding>({
    FragmentKakaoSignupBinding.inflate(it)
}) {
    private val signUpViewModel by viewModels<KakaoSignupViewModel>()

    private val googleSignInClient: GoogleSignInClient by lazy { onGoogleLogin() }
    private val googleAuthLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

        try {
            val account = task.getResult(ApiException::class.java)

            val userName = account.givenName
            val serverAuth = account.serverAuthCode
            account.idToken

            signUpViewModel.updateGoogleToken(account.serverAuthCode.orEmpty())

//            Intent(this, NicknameInputActivity::class.java).apply {
//                putExtra("kakaoToken", account.idToken)
//                startActivity(this)
//            }

        } catch (e: ApiException) {
            Timber.e(e)
        }
    }

    private val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            toast("카카오 로그인에 실패하였습니다.")
        } else if (token != null) {
            signUpViewModel.updateKakaoToken(token.accessToken)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initComposeView()
        initObservers()
    }

    private fun initComposeView() {
        binding.kakaoSignComposeView.setContent {
            MaterialTheme {
                KakaoSignupScreen(
                    onKakaoLoginClick = {
                        kakaoLoginCallBack()
                    },
                    onGoogleLoginClick = {
                        requestGoogleLogin()
                    }
                )
            }
        }
    }

    private fun kakaoLoginCallBack() {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled ) {
                        return@loginWithKakaoTalk
                    }
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                } else if (token != null) {
                    Timber.d("KakaoToken: $token")
                    signUpViewModel.updateKakaoToken(token.accessToken)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }

    private fun onGoogleLogin(): GoogleSignInClient {
        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode("513574163474-0jmn0tks8hiolpl9r7gmc037n5mcg64t.apps.googleusercontent.com")
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(this, googleSignInOption)
    }

    private fun requestGoogleLogin() {
        googleSignInClient.signOut()
        val signInIntent = googleSignInClient.signInIntent
        googleAuthLauncher.launch(signInIntent)
    }

    private fun initObservers() {
        signUpViewModel.kakaoToken.observe(this) { token ->
            if (token.isNotEmpty()) {
                Intent(this, NicknameInputActivity::class.java).apply {
                    putExtra("kakaoToken", token)
                    when (val data = handleIntentExtra()) {
                        is SchemeState.NavReview -> {
                            putExtra(SchemeKey.NAV_REVIEW, data)
                        }
                        is SchemeState.NavReviewDetail -> {
                            putExtra(SchemeKey.NAV_REVIEW_DETAIL, data)
                        }
                        else -> Unit
                    }
                    startActivity(this)
                }
            }
        }

        signUpViewModel.googleToken.observe(this) { token ->
            if (token.isNotEmpty()) {
                Intent(this, NicknameInputActivity::class.java).apply {
                    putExtra("googleToken", token)
                    when (val data = handleIntentExtra()) {
                        is SchemeState.NavReview -> {
                            putExtra(SchemeKey.NAV_REVIEW, data)
                        }
                        is SchemeState.NavReviewDetail -> {
                            putExtra(SchemeKey.NAV_REVIEW_DETAIL, data)
                        }
                        else -> Unit
                    }
                    startActivity(this)
                }
            }
        }

        signUpViewModel.loginUiState.asLiveData().observe(this) { state ->
            when (state) {
                LoginUiState.LoginSuccess -> {
                    Intent(this, HomeActivity::class.java).apply {
                        startActivity(this)
                        finish()
                    }
                }
                LoginUiState.Loading -> {

                }
                LoginUiState.Initial -> { }
            }
        }
    }

    private fun handleIntentExtra(): SchemeState {
        val navReview = intent.getCompatibleParcelableExtra<SchemeState.NavReview>(SchemeKey.NAV_REVIEW)
        if (navReview != null) {
            return navReview
        }

        val navReviewDetail = intent.getCompatibleParcelableExtra<SchemeState.NavReviewDetail>(
            SchemeKey.NAV_REVIEW_DETAIL)
        if (navReviewDetail != null) {
            return navReviewDetail
        }

        return SchemeState.Nothing
    }
}