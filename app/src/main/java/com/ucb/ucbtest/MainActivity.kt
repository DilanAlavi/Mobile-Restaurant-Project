package com.ucb.ucbtest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ucb.ucbtest.counter.CounterUI
import com.ucb.ucbtest.counter.CounterUIV2
import com.ucb.ucbtest.gitalias.GitaliasUI
import com.ucb.ucbtest.home.HomeUI
import com.ucb.ucbtest.login.LoginUI
import com.ucb.ucbtest.navigation.AppNavigation
import com.ucb.ucbtest.takephoto.TakePhotoUI
import com.ucb.ucbtest.ui.theme.UcbtestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Ucbtest)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNavigation()
        }
    }
}

