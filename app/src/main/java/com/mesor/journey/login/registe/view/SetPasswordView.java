package com.mesor.journey.login.registe.view;

import com.mesor.journey.framework.BaseView;

/**
 * Created by Limeng on 2016/8/28.
 */
public interface SetPasswordView extends BaseView {
    void nextStep();
    void sendCodeResult(String message, boolean success);
}
