package com.mesor.journey.login;

import android.os.Bundle;

import com.mesor.journey.R;
import com.mesor.journey.framework.BaseActivity;
import com.mesor.journey.login.splash.SplashFragment;

/**
 * Created by Limeng on 2016/8/25.
 */
public class LoginActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new SplashFragment()).commitAllowingStateLoss();
    }
}
