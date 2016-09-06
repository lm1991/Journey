package com.mesor.journey.main.presenter;

import android.util.Log;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.cloud.CloudSearch;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.google.gson.Gson;
import com.mesor.journey.framework.BasePresenter;
import com.mesor.journey.main.view.MainView;
import com.mesor.journey.model.InfoMapMark;
import com.mesor.journey.model.InfoMapResult;
import com.mesor.journey.utils.Constants;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Limeng on 2016/8/27.
 */
public class MainPresenter extends BasePresenter<MainView> {

    private MainView mainView;

    private CloudSearch.Query mQuery;

    public CloudSearch.Query getmQuery() {
        return mQuery;
    }

    @Override
    public void attachView(MainView view) {
        this.mainView = view;
    }

    @Override
    public void detachView() {
        super.detachView();
        this.mainView = null;
    }

    public void searchMarks(LatLng... latLngs) {
        CloudSearch cloudSearch = new CloudSearch(mainView.getContext());
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
        cloudSearch.setOnCloudSearchListener(mainView);
        try {
            mQuery = new CloudSearch.Query(Constants.MAP_ID_MAP_1, "测试", bound);
            cloudSearch.searchCloudAsyn(mQuery);
        } catch (AMapException e) {
            e.printStackTrace();
        }
    }

    public void addMark(String location) {
        Subscription subscription = subscriptionMap.get(Constants.URL_MAP_ADD_MARD);
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        InfoMapMark infoMapMark = new InfoMapMark();
        infoMapMark.set_location(location);
        infoMapMark.set_address("测试添加地址坐标 : " + location);
        infoMapMark.set_name("测试");
        Map infoMapForm = new HashMap();
        infoMapForm.put("key", Constants.MAP_KEY_CLOUD);
        infoMapForm.put("tableid", Constants.MAP_ID_MAP_1);
        infoMapForm.put("data", new Gson().toJson(infoMapMark));
        subscription = mainView.getApplicationContext().getService().addMark(Constants.URL_MAP_ADD_MARD, infoMapForm)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<InfoMapResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(getClass().getName(), e.getMessage());
                    }

                    @Override
                    public void onNext(InfoMapResult infoMapResult) {
                        if (infoMapResult.getStatus() == 1)
                            mainView.showMessage("添加成功");
                        else {
                            mainView.showMessage("添加失败。 code: " + infoMapResult.getInfo());
                        }
                    }
                });
        subscriptionMap.put(Constants.URL_MAP_ADD_MARD, subscription);
    }
}
