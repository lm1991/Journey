package com.mesor.journey.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.mesor.journey.model.InfoAccount;
import com.mesor.journey.model.JourneyService;
import com.mesor.journey.utils.Constants;

/**
 * Created by Limeng on 2016/8/25.
 */
public class App extends Application {

    private static App mApplication;
    private InfoAccount infoAccount;
    private SharedPreferences sharedPreferences;
    private JourneyService journeyService;

    public static synchronized App getApplication(Context context) {
        if (mApplication == null) {
            mApplication = (App) context.getApplicationContext();
        }
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.initialize(this, "LOdszyuBtA2Ca1nYAy6AT303-gzGzoHsz", "und51VwYqAa0WIK0I2J0K8jI");
        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            public void done(AVException e) {
                if (e == null) {
                    // 保存成功
                    getInfoAccount().setInstallationId(AVInstallation.getCurrentInstallation().getInstallationId());
//                    getInfoAccount().setObjectId(AVInstallation.getCurrentInstallation().getObjectId());
                    // 关联  installationId 到用户表等操作……
                } else {
                    // 保存失败，输出错误信息
                    Log.e(getPackageName(), "save login info error : " + e.getMessage());
                }
            }
        });
    }

    public JourneyService getService() {
        if(journeyService == null) {
            journeyService = JourneyService.Factory.create();
        }
        return journeyService;
    }

    public InfoAccount getInfoAccount() {
        if (infoAccount == null) {
            infoAccount = InfoAccount.defaultAccount(getSharedPreferences());
        }
        return infoAccount;
    }

    public SharedPreferences getSharedPreferences() {
        if (sharedPreferences == null) {
            sharedPreferences = getSharedPreferences(Constants.SHARE_PREFERENCE_STRING, MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    public void logout() {
        AVUser.logOut();
        sharedPreferences.edit().clear().commit();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
