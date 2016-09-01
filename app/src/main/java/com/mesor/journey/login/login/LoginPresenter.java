package com.mesor.journey.login.login;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.google.gson.Gson;
import com.mesor.journey.framework.BasePresenter;
import com.mesor.journey.login.registe.info.InfoError;
import com.mesor.journey.model.InfoAccount;

/**
 * Created by Limeng on 2016/8/29.
 */
public class LoginPresenter extends BasePresenter<LoginView> {
    private LoginView loginView;

    @Override
    public void attachView(LoginView view) {
        this.loginView = view;
    }

    @Override
    public void detachView() {
        super.detachView();
        this.loginView = null;
    }

    public void login(final String mobile, final String password){
        AVUser.loginByMobilePhoneNumberInBackground(mobile, password, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if(e == null) {
                    InfoAccount infoAccount = loginView.getApplicationContext().getInfoAccount();
                    infoAccount.setAvatar(avUser.getString("avatar"));
                    infoAccount.setUsername(avUser.getUsername());
                    infoAccount.setUserId(avUser.getInt("userId"));
                    infoAccount.setIntroduction(avUser.getString("introduction"));
                    infoAccount.setSex(avUser.getString("sex"));
                    infoAccount.setMobilePhoneNumber(mobile);
                    infoAccount.setPassword(password);
                    infoAccount.setObjectId(avUser.getObjectId());
                    infoAccount.save(loginView.getApplicationContext().getSharedPreferences());
                    loginView.showMessage("登录成功");
                }else{
                    InfoError infoError = new Gson().fromJson(e.getMessage(), InfoError.class);
                    loginView.showMessage(infoError.getError());
                }
            }
        });
    }
}
