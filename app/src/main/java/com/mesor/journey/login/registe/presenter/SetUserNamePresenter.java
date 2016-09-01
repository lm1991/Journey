package com.mesor.journey.login.registe.presenter;

import android.text.TextUtils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;
import com.google.gson.Gson;
import com.mesor.journey.framework.BasePresenter;
import com.mesor.journey.login.registe.info.InfoError;
import com.mesor.journey.login.registe.view.SetUserNameView;
import com.mesor.journey.model.InfoAccount;
import com.mesor.journey.utils.StringUtil;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Limeng on 2016/8/29.
 */
public class SetUserNamePresenter extends BasePresenter<SetUserNameView> {

    private SetUserNameView setUserNameView;

    @Override
    public void attachView(SetUserNameView view) {
        setUserNameView = view;
    }

    @Override
    public void detachView() {
        super.detachView();
        setUserNameView = null;
    }

    public void register(final InfoAccount infoAccount) {
        if (!TextUtils.isEmpty(infoAccount.getAvatar()) && !infoAccount.getAvatar().startsWith("http") && !new File(infoAccount.getAvatar()).exists()) {
            setUserNameView.showMessage("头像文件不存在");
            return;
        }
        if (!StringUtil.validUserName(infoAccount.getUsername())) {
            setUserNameView.showMessage("昵称只能包含中英文、数字、_和-，2-20个字符，且以中英文开头");
            return;
        }
        if(TextUtils.isEmpty(infoAccount.getAvatar()) || infoAccount.getAvatar().startsWith("http")) {
            setUserNameView.setRegisterStatus("注册用户中...");
            AVUser user = new AVUser();// 新建 AVUser 对象实例
            user.setUsername(infoAccount.getUsername());// 设置用户名
            user.setPassword(infoAccount.getPassword());// 设置密码
            user.setMobilePhoneNumber(infoAccount.getMobilePhoneNumber());
            user.put("mobilePhoneVerified", true);
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        // 注册成功
                        setUserNameView.registerSuccess();
                    } else {
                        // 失败的原因可能有多种，常见的是用户名已经存在。
                        InfoError infoError = new Gson().fromJson(e.getMessage(), InfoError.class);
                        setUserNameView.showMessage(infoError.getError());
                    }
                }
            });
            return;
        }
        try {
            setUserNameView.setRegisterStatus("上传头像中...");
            final AVFile file = AVFile.withAbsoluteLocalPath(infoAccount.getAvatar().replaceAll(".*" + File.separator, ""), infoAccount.getAvatar());
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    setUserNameView.setRegisterStatus("注册用户中...");
                    AVUser user = new AVUser();// 新建 AVUser 对象实例
                    user.setUsername(infoAccount.getUsername());// 设置用户名
                    user.setPassword(infoAccount.getPassword());// 设置密码
                    user.setMobilePhoneNumber(infoAccount.getMobilePhoneNumber());
                    user.put("mobilePhoneVerified", true);
                    user.put("avatar", file.getUrl());//返回一个唯一的 Url 地址
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                // 注册成功
                                setUserNameView.registerSuccess();
                            } else {
                                // 失败的原因可能有多种，常见的是用户名已经存在。
                                InfoError infoError = new Gson().fromJson(e.getMessage(), InfoError.class);
                                setUserNameView.showMessage(infoError.getError());
                            }
                        }
                    });
                }
            });
        } catch (FileNotFoundException e) {
            setUserNameView.showMessage("头像文件不存在");
            e.printStackTrace();
        }
    }
}
