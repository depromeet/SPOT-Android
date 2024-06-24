package com.depromeet.androidApp.presentation

import android.os.Bundle
import com.depromeet.androidApp.R
import com.depromeet.androidApp.databinding.ActivityMainBinding
import com.depromeet.core.base.BindingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
