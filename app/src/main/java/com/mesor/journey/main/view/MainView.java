package com.mesor.journey.main.view;

import android.content.Context;

import com.amap.api.maps.AMap;
import com.amap.api.services.cloud.CloudSearch;
import com.mesor.journey.framework.BaseView;

/**
 * Created by Limeng on 2016/8/27.
 */
public interface MainView extends BaseView, CloudSearch.OnCloudSearchListener, AMap.OnMarkerClickListener {

    Context getContext();

    void refreshMap();

}
