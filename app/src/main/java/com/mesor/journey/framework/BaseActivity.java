package com.mesor.journey.framework;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.avos.avoscloud.AVAnalytics;
import com.mesor.journey.R;
import com.mesor.journey.application.App;

import butterknife.ButterKnife;

/**
 * Created by Limeng on 2016/8/25.
 */
public class BaseActivity extends AppCompatActivity implements BaseView {

    private View rootView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_base, null);
        setContentView(rootView);
        ButterKnife.bind(this);
        ActivityManager.getInstance().pushActivity(this);
    }

    public void onCreate(Bundle savedInstanceState, int layoutId) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(layoutId, null);
        setContentView(rootView);
        ButterKnife.bind(this);
        ActivityManager.getInstance().pushActivity(this);
    }

    protected void setContentFragment(Class<? extends BaseFragment> fragmentClass) {
        Bundle arguments = null;
        if (getIntent() != null) {
            arguments = getIntent().getExtras();
        }
        setContentFragment(fragmentClass, arguments);
    }

    protected void setContentFragment(Class<? extends BaseFragment> fragmentClass, Bundle arguments) {
        Fragment fragment = Fragment.instantiate(this, fragmentClass.getName(), arguments);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.content, fragment);
        t.commit();
    }

    protected void setContentFragment(Class<? extends BaseFragment> fragmentClass, Bundle arguments, int contentId) {
        Fragment fragment = Fragment.instantiate(this, fragmentClass.getName(), arguments);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(contentId, fragment);
        t.commit();
    }

    protected void setContentFragment(String fragmentClassName, Bundle arguments) {
        Fragment fragment = Fragment.instantiate(this, fragmentClassName, arguments);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.content, fragment);
        t.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AVAnalytics.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AVAnalytics.onPause(this);
    }

    @Override
    protected void onDestroy() {
        ActivityManager.getInstance().removeTopActivity();
        super.onDestroy();
    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public App getApplicationContext() {
        return (App) super.getApplicationContext();
    }
}
