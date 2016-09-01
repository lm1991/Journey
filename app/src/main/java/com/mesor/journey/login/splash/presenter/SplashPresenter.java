package com.mesor.journey.login.splash.presenter;

import android.os.Handler;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.google.gson.Gson;
import com.mesor.journey.framework.BasePresenter;
import com.mesor.journey.login.LoginOrRegisterFragment;
import com.mesor.journey.login.registe.info.InfoError;
import com.mesor.journey.login.splash.view.SplashView;
import com.mesor.journey.model.InfoAccount;

/**
 * Created by Limeng on 2016/8/27.
 */
public class SplashPresenter extends BasePresenter<SplashView> {

    private SplashView splashView;

    @Override
    public void attachView(SplashView view) {
        this.splashView = view;
    }

    @Override
    public void detachView() {
        super.detachView();
        this.splashView = null;
    }

    /**
     * 验证本地用户信息
     * 决定跳转至登录注册还是进入应用
     */
    public void checkAccount() {
        InfoAccount infoAccount = splashView.getApplicationContext().getInfoAccount();
        if (infoAccount.getUserId() == -1) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(splashView != null){
                        splashView.startFragmentActivity(LoginOrRegisterFragment.class, null);
                    }
                }
            }, 1000);
        }else {
            AVUser.loginByMobilePhoneNumberInBackground(infoAccount.getMobilePhoneNumber(), infoAccount.getPassword(), new LogInCallback<AVUser>() {
                @Override
                public void done(AVUser avUser, AVException e) {
                    if(splashView == null){
                        return;
                    }
                    InfoAccount infoAccount = splashView.getApplicationContext().getInfoAccount();
                    if(e == null) {
                        infoAccount.setAvatar(avUser.getString("avatar"));
                        infoAccount.setUsername(avUser.getUsername());
                        infoAccount.setUserId(avUser.getInt("userId"));
                        infoAccount.setIntroduction(avUser.getString("introduction"));
                        infoAccount.setSex(avUser.getString("sex"));
                        infoAccount.setObjectId(avUser.getObjectId());
                        infoAccount.save(splashView.getApplicationContext().getSharedPreferences());
                        splashView.showMessage("登录成功");
                        //TODO  go to app
                    }else{
                        InfoError infoError = new Gson().fromJson(e.getMessage(), InfoError.class);
                        splashView.loginError(infoError.getError(), infoAccount.getMobilePhoneNumber());
                    }
                }
            });
        }
    }
}
