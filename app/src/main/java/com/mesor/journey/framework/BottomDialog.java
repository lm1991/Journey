package com.mesor.journey.framework;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mesor.journey.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Limeng on 2016/8/28.
 */
public class BottomDialog extends Dialog {

    @BindView(R.id.itemsContainer)
    protected LinearLayout container;

    private List<TextView> textViewList;

    public BottomDialog(Context context) {
        super(context);
        init();
    }

    public BottomDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    protected BottomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init() {
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.Anim_Dialog_Bottom);  //添加动画
    }

    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = getContext().getResources().getDisplayMetrics().widthPixels; //设置宽度
        window.setAttributes(lp);
    }

    /**
     * Dialog创建之前调用
     *
     * @param text
     * @param listener
     * @return
     */
    public BottomDialog addItem(String text, final View.OnClickListener listener) {
        if (textViewList == null) {
            textViewList = new ArrayList<>();
        }
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setTextSize(getContext().getResources().getDimension(R.dimen.usual_txt_size) / getContext().getResources().getDisplayMetrics().density);
        textView.setTextColor(getContext().getResources().getColor(R.color.black_3));
        textView.setBackgroundResource(R.drawable.bg_round_item);
        textView.setGravity(Gravity.CENTER);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomDialog.this.dismiss();
                listener.onClick(v);
            }
        });
        textViewList.add(textView);
        return this;
    }

    @OnClick({R.id.cancelTv, R.id.rootView})
    public void onClick(View view) {
        dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_bottom, null);
        setContentView(view);
        ButterKnife.bind(this, view);
        for (TextView textView : textViewList) {
            container.addView(textView);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textView.getLayoutParams();
            params.setMargins(0, (int) getContext().getResources().getDimension(R.dimen.px_20), 0, 0);
            params.height = (int) getContext().getResources().getDimension(R.dimen.px_120);
            params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        }
    }
}
