package com.mesor.journey.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mesor.journey.R;
import com.mesor.journey.framework.BaseFragment;
import com.mesor.journey.framework.SharedFragmentActivity;
import com.mesor.journey.login.login.LoginFragment;
import com.mesor.journey.login.registe.RegisteFragment;
import com.mesor.journey.utils.Constants;

import butterknife.OnClick;

/**
 * Created by Limeng on 2016/8/29.
 */
public class LoginOrRegisterFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState, R.layout.fragment_login_or_register);
    }

    @OnClick({R.id.registerTv, R.id.loginTv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registerTv:
                SharedFragmentActivity.startFragmentActivityForResult(LoginOrRegisterFragment.this, RegisteFragment.class, Constants.CODE_REGISTER, null);
                break;
            case R.id.loginTv:
                SharedFragmentActivity.startFragmentActivityForResult(this, LoginFragment.class, Constants.CODE_LOGIN, null);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.CODE_REGISTER:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = new Bundle();
                    bundle.putString("mobile", data.getStringExtra("mobile"));
                    SharedFragmentActivity.startFragmentActivityForResult(this, LoginFragment.class, Constants.CODE_LOGIN, bundle);
                }
                break;
            case Constants.CODE_LOGIN:
                if (resultCode == Activity.RESULT_OK) {
                    //TODO go to app
                }
                break;
        }
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
