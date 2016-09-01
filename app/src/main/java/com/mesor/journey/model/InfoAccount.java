package com.mesor.journey.model;

import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Limeng on 2016/8/25.
 */
public class InfoAccount implements Parcelable {
    private String installationId;

    private String objectId;

    private int userId;
    private String username;
    private String sex;
    private String avatar;
    private String password;
    private String introduction;
    private String mobilePhoneNumber;

    public static InfoAccount defaultAccount(SharedPreferences sp) {
        InfoAccount infoAccount = new InfoAccount();
        infoAccount.installationId = sp.getString("installation_id", null);
        infoAccount.objectId = sp.getString("object_id", null);
        infoAccount.userId = sp.getInt("user_id", -1);
        infoAccount.username = sp.getString("user_name", null);
        infoAccount.sex = sp.getString("sex", null);
        infoAccount.avatar = sp.getString("avatar", null);
        infoAccount.password = sp.getString("password", null);
        infoAccount.introduction = sp.getString("introduction", null);
        infoAccount.mobilePhoneNumber = sp.getString("mobile", null);
        return infoAccount;
    }

    public void save(SharedPreferences sp) {
        sp.edit().putString("installation_id", installationId).commit();
        sp.edit().putString("object_id", objectId).commit();
        sp.edit().putInt("user_id", userId).commit();
        sp.edit().putString("user_name", username).commit();
        sp.edit().putString("sex", sex).commit();
        sp.edit().putString("avatar", avatar).commit();
        sp.edit().putString("password", password).commit();
        sp.edit().putString("introduction", introduction).commit();
        sp.edit().putString("mobile", mobilePhoneNumber).commit();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public String getInstallationId() {
        return installationId;
    }

    public void setInstallationId(String installationId) {
        this.installationId = installationId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.installationId);
        dest.writeString(this.objectId);
        dest.writeInt(this.userId);
        dest.writeString(this.username);
        dest.writeString(this.sex);
        dest.writeString(this.avatar);
        dest.writeString(this.password);
        dest.writeString(this.introduction);
        dest.writeString(this.mobilePhoneNumber);
    }

    public InfoAccount() {
    }

    protected InfoAccount(Parcel in) {
        this.installationId = in.readString();
        this.objectId = in.readString();
        this.userId = in.readInt();
        this.username = in.readString();
        this.sex = in.readString();
        this.avatar = in.readString();
        this.password = in.readString();
        this.introduction = in.readString();
        this.mobilePhoneNumber = in.readString();
    }

    public static final Parcelable.Creator<InfoAccount> CREATOR = new Parcelable.Creator<InfoAccount>() {
        @Override
        public InfoAccount createFromParcel(Parcel source) {
            return new InfoAccount(source);
        }

        @Override
        public InfoAccount[] newArray(int size) {
            return new InfoAccount[size];
        }
    };
}
