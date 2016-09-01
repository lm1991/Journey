package com.mesor.journey.login.registe.presenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.google.gson.Gson;
import com.mesor.journey.framework.BasePresenter;
import com.mesor.journey.login.registe.info.InfoError;
import com.mesor.journey.login.registe.view.RegisterView;
import com.mesor.journey.utils.Constants;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Limeng on 2016/8/28.
 */
public class RegisterPresenter extends BasePresenter<RegisterView> {

    private Map<String, Integer> codeTimer;
    private Timer timer;
    private TimerTask timerTask;

    RegisterView registerView;

    @Override
    public void attachView(RegisterView view) {
        this.registerView = view;
        codeTimer = new HashMap<>();
    }

    @Override
    public void detachView() {
        super.detachView();
        if (timer != null)
            timer.cancel();
        this.registerView = null;
        if (codeTimer != null)
            codeTimer.clear();
    }

    /**
     * 验证手机号是否合法
     * 发送验证码
     *
     * @param mobile
     */
    public void sendCode(final String mobile) {
        if (codeTimer != null && codeTimer.containsKey(mobile)) {
            int delay = codeTimer.get(mobile);
            if (delay > 0) {
                registerView.setPassword(mobile);
                return;
            }
        }
        AVOSCloud.requestVoiceCodeInBackground(mobile, new RequestMobileCodeCallback() {
            @Override
            public void done(AVException e) {
                if (registerView == null) {
                    //界面已销毁
                    return;
                }
                if (e == null) {
                    // successfully
                    registerView.setPassword(mobile);
                    if (timer == null) {
                        timer = new Timer();
                        timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                if (codeTimer == null || codeTimer.size() == 0) {
                                    return;
                                }
                                Iterator<String> iterator = codeTimer.keySet().iterator();
                                while (iterator.hasNext()) {
                                    String key = iterator.next();
                                    int delay = codeTimer.get(key);
                                    if (delay <= 0)
                                        continue;
                                    codeTimer.put(key, delay--);
                                }
                            }
                        };
                        timer.schedule(timerTask, 1000, 1000);
                    }
                    codeTimer.put(mobile, Constants.SEND_VERIFY_CODE_DELAY);
                } else {
                    // failed
                    InfoError infoError = new Gson().fromJson(e.getMessage(), InfoError.class);
//                    if (infoError.getCode() == LeanCloudErrorCode.ERROR_CODE_MOBILE_PHONE_NUMBER) {
//                        registerView.setPassword(mobile);
//                        return;
//                    }
                    if (infoError.getCode() == 601) {
                        infoError.setError("抱歉！根据运营商要求，每天最多发送5条验证短信。");
                    }
                    registerView.showMessage(infoError.getError());
                }
            }
        });
    }

    /**
     * 重置密码验证码， 与注册时不同
     * @param mobile
     */
    public void sendResetPasswordCode(final String mobile) {
        if (codeTimer != null && codeTimer.containsKey(mobile)) {
            int delay = codeTimer.get(mobile);
            if (delay > 0) {
                registerView.setPassword(mobile);
                return;
            }
        }
        AVUser.requestPasswordResetBySmsCodeInBackground(mobile, new RequestMobileCodeCallback() {
            @Override
            public void done(AVException e) {
                if (registerView == null) {
                    //界面已销毁
                    return;
                }
                if (e == null) {
                    // successfully
                    registerView.setPassword(mobile);
                    if (timer == null) {
                        timer = new Timer();
                        timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                if (codeTimer == null || codeTimer.size() == 0) {
                                    return;
                                }
                                Iterator<String> iterator = codeTimer.keySet().iterator();
                                while (iterator.hasNext()) {
                                    String key = iterator.next();
                                    int delay = codeTimer.get(key);
                                    if (delay <= 0)
                                        continue;
                                    codeTimer.put(key, delay--);
                                }
                            }
                        };
                        timer.schedule(timerTask, 1000, 1000);
                    }
                    codeTimer.put(mobile, Constants.SEND_VERIFY_CODE_DELAY);
                } else {
                    // failed
                    InfoError infoError = new Gson().fromJson(e.getMessage(), InfoError.class);
//                    if (infoError.getCode() == LeanCloudErrorCode.ERROR_CODE_MOBILE_PHONE_NUMBER) {
//                        registerView.setPassword(mobile);
//                        return;
//                    }
                    registerView.showMessage(infoError.getError());
                }
            }
        });
    }
}
