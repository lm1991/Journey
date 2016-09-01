package com.mesor.journey.login.registe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.mesor.journey.R;
import com.mesor.journey.framework.BaseFragment;
import com.mesor.journey.framework.SharedFragmentActivity;
import com.mesor.journey.framework.ToolLayout;
import com.mesor.journey.login.registe.presenter.RegisterPresenter;
import com.mesor.journey.login.registe.view.RegisterView;
import com.mesor.journey.model.InfoAccount;
import com.mesor.journey.utils.Constants;
import com.mesor.journey.utils.StringUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Limeng on 2016/8/28.
 */
public class RegisteFragment extends BaseFragment implements RegisterView {

    @BindView(R.id.toolLayout)
    ToolLayout toolLayout;
    @BindView(R.id.mobileEt)
    EditText mobileEt;
    @BindView(R.id.textInputLayout)
    TextInputLayout textInputLayout;
    @BindView(R.id.sendCodeTv)
    TextView sendCodeTv;

    private RegisterPresenter registerPresenter;
    private InfoAccount infoAccount;

    private boolean isResetPassword = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isResetPassword = getArguments().getBoolean("reset_password", false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState, R.layout.fragment_registe);
    }

    @OnClick({R.id.rootView, R.id.sendCodeTv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rootView:
                break;
            case R.id.sendCodeTv:
                hideInputMethod();
                String mobile = mobileEt.getText().toString();
                if (mobileEt.length() < 11 || textInputLayout.isErrorEnabled()) {
                    showMessage("手机号格式错误");
                    break;
                }
                setSendCodeEnable(false);
                if (!isResetPassword) {
                    registerPresenter.sendCode(mobile);
                } else {
                    registerPresenter.sendResetPasswordCode(mobile);
                }
                break;
        }
    }

    @Override
    public void initView() {
        if (!isResetPassword)
            toolLayout.setBackVIsVisible(false);
        toolLayout.setTitle(isResetPassword ? "找回密码" : "注册");
        toolLayout.setOnTitleListener(new ToolLayout.OnTitleListener() {
            @Override
            public void clickBack() {
                finish();
            }

            @Override
            public void clickTitle() {

            }

            @Override
            public void clickRight() {

            }
        });
        mobileEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 11 && !StringUtil.isMobile(s.toString())) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError("手机号格式错误");
                } else {
                    textInputLayout.setErrorEnabled(false);
                }
            }
        });
    }

    @Override
    public void initData() {
        infoAccount = new InfoAccount();
        registerPresenter = new RegisterPresenter();
        registerPresenter.attachView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        registerPresenter.detachView();
    }

    @Override
    public void showMessage(String message) {
        setSendCodeEnable(true);
        super.showMessage(message);
    }

    @Override
    public void setPassword(String mobile) {
//        showMessage(isResetPassword ? "请求成功！稍后将收到验证码的短信通知，请注意查看" : "请求成功！稍后将收到验证码的电话通知，请注意接听");
        infoAccount.setMobilePhoneNumber(mobile);
        Bundle bundle = new Bundle();
        bundle.putBoolean("reset_password", isResetPassword);
        bundle.putParcelable("account", infoAccount);
        SharedFragmentActivity.startFragmentActivityForResult(this, SetPasswordFragment.class, Constants.CODE_REGISTER, bundle);
        setSendCodeEnable(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.CODE_REGISTER:
                if(resultCode != Activity.RESULT_OK){
                    return;
                }
                getActivity().setResult(resultCode, data);
                finish();
                break;
        }
    }

    private void setSendCodeEnable(boolean enable) {
        sendCodeTv.setEnabled(enable);
        sendCodeTv.setText(enable ? "获取验证码" : "发送请求中...");
    }
}
