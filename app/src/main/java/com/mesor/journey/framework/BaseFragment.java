package com.mesor.journey.framework;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.mesor.journey.application.App;

import butterknife.ButterKnife;

/**
 * Created by Limeng on 2016/8/25.
 */
public abstract class BaseFragment extends Fragment implements BaseView, initUi {

    protected View rootView;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState, int layoutId) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(layoutId, null);
        ButterKnife.bind(this, rootView);
        initView();
        initData();
        return rootView;
    }

    protected void finish() {
        getActivity().finish();
    }

    @Override
    public App getApplicationContext() {
        return (App) getContext().getApplicationContext();
    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
    }

    protected void showMessage(String message, String actionText, View.OnClickListener onClickListener) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).setAction(actionText, onClickListener).show();
    }

    protected void hideInputMethod() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
//        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
    }
}
