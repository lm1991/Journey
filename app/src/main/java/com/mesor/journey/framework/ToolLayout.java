package com.mesor.journey.framework;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mesor.journey.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Limeng on 2016/8/28.
 */
public class ToolLayout extends LinearLayout {

    private Context context;

    @BindView(R.id.backV)
    ImageView backV;
    @BindView(R.id.rightImgV)
    ImageView rightImageV;
    @BindView(R.id.titleTv)
    TextView titleTv;
    @BindView(R.id.rightTv)
    TextView rightTv;

    private OnTitleListener listener;

    public ToolLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        this.context = context;
        init();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.toolLayout);
        String string = array.getString(R.styleable.toolLayout_title);
        if(!TextUtils.isEmpty(string)) {
            titleTv.setText(string);
        }
        array.recycle();
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_tool, null);
        addView(view);
        ButterKnife.bind(this, view);
    }

    public void setBackVIsVisible(boolean isVisible) {
        backV.setVisibility(isVisible ? VISIBLE : INVISIBLE);
    }

    public void setTitle(String title) {
        titleTv.setText(title);
    }

    @OnClick({R.id.backV, R.id.rightTv, R.id.rightImgV, R.id.titleTv})
    public void OnClick(View view) {
        if (listener == null)
            return;
        switch (view.getId()) {
            case R.id.backV:
                listener.clickBack();
                break;
            case R.id.rightImgV:
            case R.id.rightTv:
                listener.clickRight();
                break;
            case R.id.titleTv:
                listener.clickTitle();
                break;
        }
    }

    public void setOnTitleListener(OnTitleListener listener) {
        this.listener = listener;
    }

    public interface OnTitleListener {
        void clickBack();

        void clickTitle();

        void clickRight();
    }

}
