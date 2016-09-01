package com.mesor.journey.login.splash;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mesor.journey.R;
import com.mesor.journey.framework.BaseFragment;
import com.mesor.journey.framework.SharedFragmentActivity;
import com.mesor.journey.login.login.LoginFragment;
import com.mesor.journey.login.splash.presenter.SplashPresenter;
import com.mesor.journey.login.splash.view.SplashView;

/**
 * Created by Limeng on 2016/8/27.
 */
public class SplashFragment extends BaseFragment implements SplashView {

    SplashPresenter splashPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState, R.layout.fragment_splash);
    }

    @Override
    public void initView() {
        //TODO splash image
    }

    @Override
    public void initData() {
        splashPresenter = new SplashPresenter();
        splashPresenter.attachView(this);
        splashPresenter.checkAccount();
    }

    @Override
    public void onDestroy() {
        splashPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void startFragmentActivity(Class<? extends BaseFragment> fragment, Bundle bundle) {
        SharedFragmentActivity.startFragmentActivity(getContext(), fragment, bundle);
        finish();
    }

    @Override
    public void loginError(String error, String mobile) {
        showMessage(error);
        Bundle bundle = new Bundle();
        bundle.putString("mobile", mobile);
        SharedFragmentActivity.startFragmentActivity(getContext(), LoginFragment.class, bundle);
        finish();
    }
}
