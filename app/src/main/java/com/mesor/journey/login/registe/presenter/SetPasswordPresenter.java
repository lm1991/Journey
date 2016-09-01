package com.mesor.journey.login.registe.presenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.UpdatePasswordCallback;
import com.google.gson.Gson;
import com.mesor.journey.framework.BasePresenter;
import com.mesor.journey.login.registe.info.InfoError;
import com.mesor.journey.login.registe.view.SetPasswordView;

/**
 * Created by Limeng on 2016/8/28.
 */
public class SetPasswordPresenter extends BasePresenter<SetPasswordView> {

    private SetPasswordView setPasswordView;

    @Override
    public void attachView(SetPasswordView view) {
        this.setPasswordView = view;
    }

    @Override
    public void detachView() {
        super.detachView();
        this.setPasswordView = null;
    }

    public void verifyCode(String code, String mobile) {
        AVOSCloud.verifySMSCodeInBackground(code, mobile, new AVMobilePhoneVerifyCallback() {
            @Override
            public void done(AVException e) {
                if (setPasswordView == null) {
                    return;
                }
                if (e == null) {
                    setPasswordView.nextStep();
                } else {
                    InfoError infoError = new Gson().fromJson(e.getMessage(), InfoError.class);
                    setPasswordView.showMessage(infoError.getError());
                }
            }
        });
    }

    public void verifyResetPasswordCode(String code, String password) {
        AVUser.resetPasswordBySmsCodeInBackground(code, password, new UpdatePasswordCallback() {
            @Override
            public void done(AVException e) {
                if (setPasswordView == null) {
                    return;
                }
                if (e == null) {
                    setPasswordView.nextStep();
                } else {
                    InfoError infoError = new Gson().fromJson(e.getMessage(), InfoError.class);
                    setPasswordView.showMessage(infoError.getError());
                }
            }
        });
    }

    /**
     * 重置密码验证码， 与注册时不同
     *
     * @param mobile
     */
    public void reSendResetPasswordCode(final String mobile) {
        AVUser.requestPasswordResetBySmsCodeInBackground(mobile, new RequestMobileCodeCallback() {
            @Override
            public void done(AVException e) {
                if (setPasswordView == null) {
                    //界面已销毁
                    return;
                }
                if (e == null) {
                    // successfully
                    setPasswordView.sendCodeResult("发送成功", true);
                } else {
                    // failed
                    String error;
                    try {
                        InfoError infoError = new Gson().fromJson(e.getMessage(), InfoError.class);
                        error = infoError.getError();
                    } catch (Exception e1) {
//                        error = e.getMessage();
                        //使用无效的手机号码时会返回两次错误
                        //第一次错误信息为"Invalid Phone Number"
                        //第二次返回格式正确的InfoError
                        //暂时忽略第一次返回
                        return;
                    }
                    setPasswordView.sendCodeResult(error, false);
                }
            }
        });
    }

    public void reSendCode(final String mobile) {
        AVOSCloud.requestVoiceCodeInBackground(mobile, new RequestMobileCodeCallback() {
            @Override
            public void done(AVException e) {
                if (setPasswordView == null) {
                    //界面已销毁
                    return;
                }
                if (e == null) {
                    // successfully
                    setPasswordView.sendCodeResult("发送成功", true);
                } else {
                    // failed
                    String error;
                    try {
                        InfoError infoError = new Gson().fromJson(e.getMessage(), InfoError.class);
                        error = infoError.getError();
                    } catch (Exception e1) {
//                        error = e.getMessage();
                        //使用无效的手机号码时会返回两次错误
                        //第一次错误信息为"Invalid Phone Number"
                        //第二次返回格式正确的InfoError
                        //暂时忽略第一次返回
                        return;
                    }
                    setPasswordView.sendCodeResult(error, false);
                }
            }
        });
    }
}
