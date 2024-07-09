package com.depromeet.presentation.home

import android.annotation.SuppressLint
import android.os.Bundle
import com.depromeet.core.base.BaseActivity
import com.depromeet.presentation.R
import com.depromeet.presentation.databinding.ActivityHomeBinding

class HomeActivity : BaseActivity<ActivityHomeBinding>({
    ActivityHomeBinding.inflate(it)
}) {
    @SuppressLint("CommitTransaction")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fcv_home, HomeFragment())
                .commitNow()
        }
    }
}