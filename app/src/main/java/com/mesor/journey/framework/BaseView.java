package com.mesor.journey.framework;

import com.mesor.journey.application.App;

/**
 * Created by Limeng on 2016/8/27.
 */
public interface BaseView {

    App getApplicationContext();
    void showMessage(String message);

}
