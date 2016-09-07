package com.mesor.journey.addmark.presenter;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.cloud.CloudItemDetail;
import com.amap.api.services.cloud.CloudResult;
import com.amap.api.services.cloud.CloudSearch;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.mesor.journey.addmark.view.ChoosePointView;
import com.mesor.journey.framework.BasePresenter;
import com.mesor.journey.utils.Constants;

/**
 * Created by Limeng on 2016/9/5.
 */
public class ChoosePointPresenter extends BasePresenter<ChoosePointView> implements CloudSearch.OnCloudSearchListener {
    private ChoosePointView choosePointView;
    private CloudSearch.Query mQuery, checkValidQuery;

    public CloudSearch.Query getQuery() {
        return mQuery;
    }

    @Override
    public void attachView(ChoosePointView view) {
        this.choosePointView = view;
    }

    @Override
    public void detachView() {
        super.detachView();
        this.choosePointView = null;
    }

    /**
     * 验证标记位置是否合法
     *
     * @param latLng
     */
    public void checkMark(LatLng latLng) {
        CloudSearch cloudSearch = new CloudSearch(choosePointView.getContext());
        CloudSearch.SearchBound bound = new CloudSearch.SearchBound(new LatLonPoint(latLng.latitude, latLng.longitude), Constants.VALID_RADIUS);
        cloudSearch.setOnCloudSearchListener(this);
        try {
            checkValidQuery = new CloudSearch.Query(Constants.MAP_ID_MAP_1, " ", bound);
            cloudSearch.searchCloudAsyn(checkValidQuery);
        } catch (AMapException e) {
            e.printStackTrace();
        }
    }

    public void searchMarks(LatLng... latLngs) {
        CloudSearch cloudSearch = new CloudSearch(choosePointView.getContext());
        /**
         * LatLonPoint构造函数为（latitude, longitude）
         */
        CloudSearch.SearchBound bound = new CloudSearch.SearchBound(new LatLonPoint(latLngs[0].latitude, latLngs[0].longitude),
                new LatLonPoint(latLngs[1].latitude, latLngs[1].longitude));
        cloudSearch.setOnCloudSearchListener(choosePointView);
        try {
            mQuery = new CloudSearch.Query(Constants.MAP_ID_MAP_1, " ", bound);
            cloudSearch.searchCloudAsyn(mQuery);
        } catch (AMapException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCloudSearched(CloudResult cloudResult, int i) {
        choosePointView.setValid(false);
        if (!cloudResult.getQuery().equals(checkValidQuery)) {
            return;
        }
        if (i == 1000) {
            if (cloudResult.getClouds() != null && cloudResult.getClouds().size() > 0) {
                choosePointView.showMessage("标记有效区域内已有其它标记");
            } else {
                choosePointView.setValid(true);
            }
        } else {
            choosePointView.showMessage("网络异常，请稍后再试");
        }
    }

    @Override
    public void onCloudItemDetailSearched(CloudItemDetail cloudItemDetail, int i) {

    }
}
