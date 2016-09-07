package com.mesor.journey.login.registe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.mesor.journey.R;
import com.mesor.journey.framework.BaseFragment;
import com.mesor.journey.framework.SharedFragmentActivity;
import com.mesor.journey.framework.ToolLayout;
import com.mesor.journey.login.registe.presenter.SetPasswordPresenter;
import com.mesor.journey.login.registe.view.SetPasswordView;
import com.mesor.journey.model.InfoAccount;
import com.mesor.journey.utils.Constants;
import com.mesor.journey.utils.StringUtil;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Limeng on 2016/8/28.
 */
public class SetPasswordFragment extends BaseFragment implements SetPasswordView {

    private SetPasswordPresenter setPasswordPresenter;

    private InfoAccount infoAccount;
    private boolean isResetPassword = false;

    @BindView(R.id.registerTipTv)
    TextView registerTipTv;
    @BindView(R.id.codeEt)
    EditText codeEt;
    @BindView(R.id.passwordEt)
    EditText passwordEt;
    @BindView(R.id.reSendTv)
    TextView reSendTv;
    @BindView(R.id.nextStepTv)
    TextView nextStepTv;

    private Timer timer;
    private int delay = Constants.SEND_VERIFY_CODE_DELAY;

    @OnClick({R.id.nextStepTv, R.id.reSendTv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reSendTv:
                reSendTv.setEnabled(false);
                if (isResetPassword) {
                    setPasswordPresenter.reSendResetPasswordCode(infoAccount.getMobilePhoneNumber());
                } else {
                    setPasswordPresenter.reSendCode(infoAccount.getMobilePhoneNumber());
                }
                break;
            case R.id.nextStepTv:
                String code = codeEt.getText().toString();
                String password = passwordEt.getText().toString();
                if (code.length() != 6 || !StringUtil.isNumber(code)) {
                    showMessage("验证码格式错误");
                    return;
                }
                if (password.length() < 6) {
                    showMessage("密码格式错误");
                    return;
                }
                setNextStepEnable(false);
                infoAccount.setPassword(password);
                if (isResetPassword) {
                    setPasswordPresenter.verifyResetPasswordCode(code, infoAccount.getPassword());
                } else {
                    setPasswordPresenter.verifyCode(code, infoAccount.getMobilePhoneNumber());
                }
                break;
        }
    }

    @Override
    public void showMessage(String message) {
        setNextStepEnable(true);
        super.showMessage(message);
    }

    private void setNextStepEnable(boolean enable) {
        nextStepTv.setEnabled(enable);
        nextStepTv.setText(enable ? (isResetPassword ? "提交" : "下一步") : "验证数据中...");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        infoAccount = getArguments().getParcelable("account");
        isResetPassword = getArguments().getBoolean("reset_password", false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState, R.layout.fragment_set_password);
    }

    @Override
    public void initView() {
        setTitle(isResetPassword ? "重设密码" : "注册");
        nextStepTv.setText(isResetPassword ? "提交" : "下一步");
        setOnTitleListener(new ToolLayout.OnTitleListener() {
            @Override
            public void clickBack() {
                finish();
            }

            @Override
            public void clickRight(MenuItem item) {

            }
        });
        registerTipTv.setText(getString(R.string.register_send_code_tip, infoAccount.getMobilePhoneNumber()));
        if(isResetPassword){
            registerTipTv.setText(String.format(Locale.CHINA, "短信验证码已发送至手机号 +86 %s", infoAccount.getMobilePhoneNumber()));
        }
    }

    Handler refreshUiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (delay > 1) {
                if (reSendTv.isEnabled())
                    reSendTv.setEnabled(false);
                delay--;
                reSendTv.setText("重发验证码\n(" + delay + ")");
            } else if (delay == 1) {
                delay--;
                reSendTv.setEnabled(true);
                reSendTv.setText("重发验证码");
            }
        }
    };

    @Override
    public void initData() {
        setPasswordPresenter = new SetPasswordPresenter();
        setPasswordPresenter.attachView(this);
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                refreshUiHandler.sendEmptyMessage(0);
            }
        };
        timer.schedule(task, 1000, 1000);
    }

    @Override
    public void onDestroy() {
        setPasswordPresenter.detachView();
        timer.cancel();
        super.onDestroy();
    }

    @Override
    public void nextStep() {
        if (isResetPassword) {
            Intent intent = new Intent();
            intent.putExtra("mobile", infoAccount.getMobilePhoneNumber());
            getActivity().setResult(Activity.RESULT_OK, intent);
            finish();
        } else {
            Bundle bundle = new Bundle();
            bundle.putParcelable("account", infoAccount);
            SharedFragmentActivity.startFragmentActivityForResult(this, SetUserNameFragment.class, Constants.CODE_REGISTER, bundle);
            setNextStepEnable(true);
        }
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

    @Override
    public void sendCodeResult(String message, boolean success) {
        super.showMessage(message);
        if (success) {
            delay = Constants.SEND_VERIFY_CODE_DELAY;
            reSendTv.setText("重发验证码\n(" + delay + ")");
        }
        reSendTv.setEnabled(!success);
    }
}
