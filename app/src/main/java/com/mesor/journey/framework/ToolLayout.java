package com.mesor.journey.framework;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.mesor.journey.R;

/**
 * Created by Limeng on 2016/8/28.
 */
public class ToolLayout extends Toolbar {

    private Context context;

    TextView titleTv;

    private OnTitleListener listener;

    public ToolLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.toolLayout);
        String string = array.getString(R.styleable.toolLayout_title_txt);
        if (!TextUtils.isEmpty(string)) {
            titleTv.setText(string);
        }
        array.recycle();
    }

    private void init() {
        super.setTitle("");
        titleTv = new TextView(context);
        titleTv.setTextSize(getResources().getDimension(R.dimen.t_62) / getResources().getDisplayMetrics().density);
        titleTv.setTextColor(0xffffffff);
        titleTv.setGravity(Gravity.CENTER);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        addView(titleTv, params);
        setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.clickBack();
            }
        });
        setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (listener != null) {
                    listener.clickRight();
                }
                return true;
            }
        });
    }

    public void showNavigationResId(int resId) {
        if (resId <= 0)
            setNavigationIcon(new ColorDrawable(0x00000000));
        else
            setNavigationIcon(resId);
    }

    public void showRightResId(int resId) {
        //TODO
    }

    @Override
    protected void onCreateContextMenu(ContextMenu menu) {
        super.onCreateContextMenu(menu);
    }

    @Override
    public void setTitle(@StringRes int resId) {
        setTitle(getContext().getText(resId));
    }

    @Override
    public void setTitle(CharSequence title) {
        if (titleTv == null) {
            return;
        }
        titleTv.setText(title);
    }

    public void setOnTitleListener(OnTitleListener l) {
        this.listener = l;
    }

    public interface OnTitleListener {
        void clickBack();

        void clickRight();
    }

}
