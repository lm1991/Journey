package com.mesor.journey.framework;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.avos.avoscloud.AVAnalytics;
import com.mesor.journey.R;
import com.mesor.journey.application.App;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Limeng on 2016/8/25.
 */
public class BaseActivity extends AppCompatActivity implements BaseView {

    public final static String NO_TITLE = "no_title_toolbar";

    private View rootView;

    @BindView(R.id.toolLayout)
    public ToolLayout toolLayout;

    protected void showNavigationResId(int resId) {
        toolLayout.showNavigationResId(resId);
    }

    @Override
    public void setTitle(CharSequence title) {
        toolLayout.setTitle(title);
    }

    protected void setOnTitleListener(ToolLayout.OnTitleListener listener) {
        toolLayout.setOnTitleListener(listener);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_base, null);
        setContentView(rootView);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent == null || !intent.getBooleanExtra(NO_TITLE, false)) {
            setSupportActionBar(toolLayout);
        } else {
            toolLayout.setVisibility(View.GONE);
        }
        ActivityManager.getInstance().pushActivity(this);
    }

    /**
     * set tool bar.
     *
     * @param savedInstanceState
     * @param layoutId
     */
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
        t.addToBackStack(fragmentClass.getName());
        t.replace(R.id.content, fragment);
        t.commit();
    }

    protected void setContentFragment(String fragmentClassName, Bundle arguments) {
        Fragment fragment = Fragment.instantiate(this, fragmentClassName, arguments);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.content, fragment);
        t.commit();
    }

    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
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
