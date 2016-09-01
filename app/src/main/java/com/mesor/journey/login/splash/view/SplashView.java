package com.mesor.journey.login.splash.view;

import android.os.Bundle;

import com.mesor.journey.framework.BaseFragment;
import com.mesor.journey.framework.BaseView;

/**
 * Created by Limeng on 2016/8/27.
 */
public interface SplashView extends BaseView {

    void startFragmentActivity(Class<? extends BaseFragment> fragment, Bundle bundle);

    /**
     * 自动登录失败， 重新登录
     * @param error
     * @param mobile
     */
    void loginError(String error, String mobile);
}
