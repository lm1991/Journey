package com.mesor.journey.login.registe;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.PermissionChecker;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mesor.journey.R;
import com.mesor.journey.framework.BaseFragment;
import com.mesor.journey.framework.BottomDialog;
import com.mesor.journey.framework.ToolLayout;
import com.mesor.journey.login.registe.presenter.SetUserNamePresenter;
import com.mesor.journey.login.registe.view.SetUserNameView;
import com.mesor.journey.model.InfoAccount;
import com.mesor.journey.utils.glide.GlideCircleTransform;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Limeng on 2016/8/28.
 */
public class SetUserNameFragment extends BaseFragment implements SetUserNameView {

    private InfoAccount infoAccount;

    private SetUserNamePresenter setUserNamePresenter;

    @BindView(R.id.avatarImageV)
    ImageView avatarImageV;
    @BindView(R.id.userNameEt)
    EditText userNameEt;
    @BindView(R.id.registerTv)
    TextView registerTv;

    private Uri fileUri, cropedUri;
    final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    final int CHOOSE_IMAGE_ACTIVITY_REQUEST_CODE = 101;
    final int PHOTO_REQUEST_CUT = 102;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        infoAccount = getArguments().getParcelable("account");
//        infoAccount = new InfoAccount();
//        infoAccount.setMobilePhoneNumber("15261596171");
//        infoAccount.setPassword("123456");
    }

    @OnClick({R.id.avatarImageV, R.id.registerTv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registerTv:
                registerTv.setEnabled(false);
                setUserNamePresenter.register(infoAccount);
                break;
            case R.id.avatarImageV:
                BottomDialog bottomDialog = new BottomDialog(getContext(), R.style.Transparent_Dialog).addItem("拍照", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED ||
                                    getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PermissionChecker.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 0);
                                return;
                            }
                        }
                        takePicture();
                    }
                }).addItem("从图库选择", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                                return;
                            }
                        }
                        choosePicture();
                    }
                });
                bottomDialog.show();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0:
                if (permissions.length != grantResults.length) {
                    showMessage("需要相机及读写存储权限以完成操作", "重试", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 0);
                        }
                    });
                } else {
                    takePicture();
                }
                break;
            case 1:
                if (permissions.length != grantResults.length) {
                    showMessage("需要读写存储权限以完成操作", "重试", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        }
                    });
                } else {
                    choosePicture();
                }
                break;
        }
    }

    private void choosePicture() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/jpeg");
//        try {
//            fileUri = Uri.fromFile(getOutputMediaFile()); // create a file to save the image
//        } catch (NullPointerException e) {
//            showMessage("创建缓存文件失败， 请重试");
//            return;
//        }
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
        // start the image capture Intent
        startActivityForResult(intent, CHOOSE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    private void takePicture() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            fileUri = Uri.fromFile(getOutputMediaFile()); // create a file to save the image
        } catch (NullPointerException e) {
            showMessage("创建缓存文件失败， 请重试");
            return;
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    /*
     * 剪切图片
     */
    private void crop() {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        try {
            cropedUri = Uri.fromFile(getOutputMediaFile());
        } catch (NullPointerException e) {
            showMessage("创建缓存文件失败， 请重试");
            return;
        }
        intent.setDataAndType(fileUri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 370);
        intent.putExtra("outputY", 370);

        intent.putExtra("outputFormat", "PNG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
//        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropedUri); // set the image file name
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;
        switch (requestCode) {
            case CHOOSE_IMAGE_ACTIVITY_REQUEST_CODE:
                fileUri = data.getData();
                if (fileUri == null) {
                    showMessage("选择图片失败， 请重试");
                    return;
                }
                crop();
                break;
            case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
                crop();
                break;
            case PHOTO_REQUEST_CUT:
                infoAccount.setAvatar(cropedUri.getPath());
                Glide.with(this).load(cropedUri).transform(new GlideCircleTransform(getContext()))
                        .crossFade()
                        .into(avatarImageV);
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState, R.layout.fragment_set_user_name);
    }

    @Override
    public void initView() {
        setTitle("完善信息");
        setOnTitleListener(new ToolLayout.OnTitleListener() {
            @Override
            public void clickBack() {
                finish();
            }

            @Override
            public void clickRight(MenuItem item) {

            }
        });
        userNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                infoAccount.setUsername(s.toString());
            }
        });
    }

    @Override
    public void initData() {
        setUserNamePresenter = new SetUserNamePresenter();
        setUserNamePresenter.attachView(this);
    }

    @Override
    public void onDestroy() {
        setUserNamePresenter.detachView();
        super.onDestroy();
    }

    /**
     * Create a File for saving an image
     */
    private static File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Journey");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
        return mediaFile;
    }

    @Override
    public void registerSuccess() {
        Intent intent = new Intent();
        intent.putExtra("mobile", infoAccount.getMobilePhoneNumber());
        getActivity().setResult(Activity.RESULT_OK, intent);
        finish();
        //register success!
//        registerTv.setEnabled(true);
//        registerTv.setText("注册");
    }

    @Override
    public void setRegisterStatus(String status) {
        registerTv.setText(status);
    }

    @Override
    public void showMessage(String message) {
        registerTv.setEnabled(true);
        registerTv.setText("注册");
        super.showMessage(message);
    }
}
