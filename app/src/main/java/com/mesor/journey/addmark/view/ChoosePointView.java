package com.mesor.journey.addmark.view;

import android.content.Context;

import com.amap.api.services.cloud.CloudSearch;
import com.mesor.journey.framework.BaseView;

/**
 * Created by Limeng on 2016/9/5.
 */
public interface ChoosePointView extends BaseView, CloudSearch.OnCloudSearchListener {

    Context getContext();
}
