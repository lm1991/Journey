package com.mesor.journey.utils;

/**
 * Created by Limeng on 2016/8/25.
 */
public class Constants {

    public static final int SEND_VERIFY_CODE_DELAY = 120;

    public static final int MAX_SCALE_PER_PIXEL = 130;//每像素130以下时， 请求标记
    public static final String MAP_KEY_CLOUD = "6bacd0a8bdd2849d83ca951f8b200c67";
    public static final String MAP_ID_MAP_1 = "57c01c4b2376c174828d8fa9";

    /**
     * 地图标记有效半径(米)， 内部不允许有其它标记
     */
    public static final int VALID_RADIUS = 20;

    /**
     * 高德地图 添加标记
     */
    public static final String URL_MAP_ADD_MARD= "http://yuntuapi.amap.com/datamanage/data/create";

    public static final String SHARE_PREFERENCE_STRING = "com.mesor.journey";

    /************** start activity for result, request code **************/
    public final static int CODE_REGISTER = 0;
    public final static int CODE_LOGIN = 1;
}
