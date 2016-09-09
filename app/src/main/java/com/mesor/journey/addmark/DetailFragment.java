package com.mesor.journey.addmark;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mesor.journey.R;
import com.mesor.journey.framework.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Limeng on 2016/9/7.
 */
public class DetailFragment extends BaseFragment {

    @BindView(R.id.editTv)
    TextInputEditText editTv;
    @BindView(R.id.textCountTv)
    TextView textCountTv;
    @BindView(R.id.imageV1)
    ImageView imageV1;
    @BindView(R.id.imageV2)
    ImageView imageV2;
    @BindView(R.id.imageV3)
    ImageView imageV3;
    @BindView(R.id.imageV4)
    ImageView imageV4;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState, R.layout.fragment_add_mark_detail);
    }

    @Override
    public void initView() {
        editTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textCountTv.setText(String.format("%d/500", s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.imageV1})
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        getActivity().startActivityForResult(Intent.createChooser(intent, "Select a Photo"), 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
