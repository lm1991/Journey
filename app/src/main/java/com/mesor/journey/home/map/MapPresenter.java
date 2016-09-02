package com.mesor.journey.home.map;

import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.cloud.CloudSearch;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.google.gson.Gson;
import com.mesor.journey.framework.BasePresenter;
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
public class MapPresenter extends BasePresenter<MapView> implements AMapLocationListener, LocationSource {

    private MapView mapView;

    private CloudSearch.Query mQuery;

    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    private AMapLocationClient mlocationClient;
    private OnLocationChangedListener mListener;

    public CloudSearch.Query getmQuery() {
        return mQuery;
    }

    @Override
    public void attachView(MapView view) {
        this.mapView = view;
    }

    @Override
    public void detachView() {
       deactivate();
        super.detachView();
        this.mapView = null;
    }

    public void searchMarks(LatLng... latLngs) {
        CloudSearch cloudSearch = new CloudSearch(mapView.getContext());
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
        cloudSearch.setOnCloudSearchListener(mapView);
        try {
            mQuery = new CloudSearch.Query(Constants.MAP_ID_WATER, "测试", bound);
//            cloudSearch.searchCloudDetailAsyn(Constants.MAP_ID_WATER, "1");
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
        infoMapForm.put("tableid", Constants.MAP_ID_WATER);
        infoMapForm.put("data", new Gson().toJson(infoMapMark));
        subscription = mapView.getApplicationContext().getService().addMark(Constants.URL_MAP_ADD_MARD, infoMapForm)
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
                            mapView.showMessage("添加成功");
                        else {
                            mapView.showMessage("添加失败。 code: " + infoMapResult.getInfo());
                        }
                    }
                });
        subscriptionMap.put(Constants.URL_MAP_ADD_MARD, subscription);
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
                mapView.showMessage(errText);
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(mapView.getContext());
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setInterval(5000);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }
}
