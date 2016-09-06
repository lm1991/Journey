package com.mesor.journey.addmark.presenter;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.cloud.CloudSearch;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.mesor.journey.addmark.view.ChoosePointView;
import com.mesor.journey.framework.BasePresenter;
import com.mesor.journey.utils.Constants;

/**
 * Created by Limeng on 2016/9/5.
 */
public class ChoosePointPresenter extends BasePresenter<ChoosePointView> {
    private ChoosePointView choosePointView;
    private CloudSearch.Query mQuery;

    public CloudSearch.Query getQuery(){
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

    public void searchMarks(LatLng... latLngs) {
        CloudSearch cloudSearch = new CloudSearch(choosePointView.getContext());
//        List<LatLonPoint> points = new ArrayList<>();
//        for(int index = 0; index < latLngs.length; index++) {
//            points.add(new LatLonPoint(latLngs[index].longitude, latLngs[index].latitude));
//        }
//        CloudSearch.SearchBound bound =new CloudSearch.SearchBound(points);
        /**
         * LatLonPoint构造函数为（latitude, longitude）
         */
        CloudSearch.SearchBound bound = new CloudSearch.SearchBound(new LatLonPoint(latLngs[0].latitude, latLngs[0].longitude),
                new LatLonPoint(latLngs[1].latitude, latLngs[1].longitude));
        cloudSearch.setOnCloudSearchListener(choosePointView);
        try {
            mQuery = new CloudSearch.Query(Constants.MAP_ID_MAP_1, "测试", bound);
            cloudSearch.searchCloudAsyn(mQuery);
        } catch (AMapException e) {
            e.printStackTrace();
        }
    }
}
