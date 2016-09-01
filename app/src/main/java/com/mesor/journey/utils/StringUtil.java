package com.mesor.journey.utils;

import android.text.TextUtils;

import com.autonavi.amap.mapcore.Md5Utility;

import java.util.Locale;

/**
 * Created by Limeng on 2016/8/28.
 */
public class StringUtil {
    /**
     * 正则表达式：验证手机号
     */
    private static final String REGEX_MOBILE = "^((13[0-9])|(15[^4,\\D])|(17[6-8])|(18[0,5-9]))\\d{8}$";

    public static boolean validUserName(String username) {
        if (TextUtils.isEmpty(username)) {
            return false;
        }
        return username.matches("^[a-zA-Z\\u4e00-\\u9fa5][·\\._\\-a-zA-Z0-9\\u4e00-\\u9fa5]{1,20}$");
    }

    public static boolean isMobile(String number) {
        if (TextUtils.isEmpty(number) || number.length() < 11) {
            return false;
        }
        return number.matches(REGEX_MOBILE);
    }

    public static boolean isNumber(String string) {
        if (TextUtils.isEmpty(string)) {
            return false;
        }
        return string.matches("\\d*");
    }

    public static String getMd5(String string) {
        return Md5Utility.getStringMD5(string).toLowerCase(Locale.CHINA);
    }

}
