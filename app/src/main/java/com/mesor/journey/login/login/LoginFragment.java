package com.mesor.journey.login.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mesor.journey.R;
import com.mesor.journey.framework.BaseFragment;
import com.mesor.journey.framework.SharedFragmentActivity;
import com.mesor.journey.framework.ToolLayout;
import com.mesor.journey.login.registe.RegisteFragment;
import com.mesor.journey.utils.Constants;
import com.mesor.journey.utils.StringUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Limeng on 2016/8/29.
 */
public class LoginFragment extends BaseFragment implements LoginView {

    @BindView(R.id.textInputLayout)
    TextInputLayout textInputLayout;
    @BindView(R.id.mobileEt)
    EditText mobileEt;
    @BindView(R.id.passwordEt)
    EditText passwordEt;

    private LoginPresenter loginPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState, R.layout.fragment_login);
    }

    @Override
    public void loginSuccess() {
        showMessage("登录成功");
        getActivity().setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void initView() {
        setTitle("登录");
        setOnTitleListener(new ToolLayout.OnTitleListener() {
            @Override
            public void clickBack() {
                finish();
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
                if (s.length() == 11) {
                    if (!StringUtil.isMobile(s.toString())) {
                        textInputLayout.setErrorEnabled(true);
                        textInputLayout.setError("手机号格式错误");
                        return;
                    }
                }
                textInputLayout.setErrorEnabled(false);
            }
        });
        String mobile = getArguments().getString("mobile", null);
        if (!TextUtils.isEmpty(mobile)) {
            mobileEt.setText(mobile);
        }
    }

    @Override
    public void initData() {
        loginPresenter = new LoginPresenter();
        loginPresenter.attachView(this);
    }

    @OnClick({R.id.registerTv, R.id.resetPasswordTv, R.id.loginTv})
    public void onClick(View view) {
        hideInputMethod();
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.resetPasswordTv:
                bundle.putBoolean("reset_password", true);
            case R.id.registerTv:
                SharedFragmentActivity.startFragmentActivityForResult(this, RegisteFragment.class, Constants.CODE_REGISTER, bundle);
                break;
            case R.id.loginTv:
                String mobile = mobileEt.getText().toString();
                if (textInputLayout.isErrorEnabled() || mobile.length() != 11) {
                    showMessage("手机号格式错误");
                    return;
                }
                String password = passwordEt.getText().toString();
                if (password.length() < 6) {
                    showMessage("密码格式错误");
                    return;
                }
                loginPresenter.login(mobile, password);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.CODE_REGISTER:
                if (resultCode == Activity.RESULT_OK) {
                    if (TextUtils.isEmpty(mobileEt.getText())) {
                        mobileEt.setText(data.getStringExtra("mobile"));
                    }
                }
                break;
        }
    }
}
