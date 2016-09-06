package com.mesor.journey.addmark;

import android.Manifest;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.cloud.CloudItem;
import com.amap.api.services.cloud.CloudItemDetail;
import com.amap.api.services.cloud.CloudResult;
import com.mesor.journey.R;
import com.mesor.journey.addmark.model.CloudOverlayMarks;
import com.mesor.journey.addmark.presenter.ChoosePointPresenter;
import com.mesor.journey.addmark.view.ChoosePointView;
import com.mesor.journey.framework.BaseFragment;
import com.mesor.journey.utils.AMapUtil;
import com.mesor.journey.utils.Constants;
import com.mesor.journey.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Limeng on 2016/9/5.
 */
public class ChoosePointFragment extends BaseFragment implements ChoosePointView {

    @BindView(R.id.mapView)
    MapView mMapView;

    private ChoosePointPresenter choosePointPresenter;

    private CloudOverlayMarks mPoiCloudOverlay;
    private ArrayList<CloudItem> mCloudItems;
    private LatLng[] latLngs;
    private String TAG = getClass().getName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState, R.layout.fragment_add_mark_choose_point);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mMapView.onCreate(savedInstanceState);
        return view;
    }

    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        choosePointPresenter.detachView();
    }

    @Override
    public void initView() {
        setUpMap();
    }

    private void setUpMap() {
        mMapView.getMap().getUiSettings().setTiltGesturesEnabled(false);
        mMapView.getMap().getUiSettings().setZoomControlsEnabled(false);

        mMapView.getMap().setOnMapTouchListener(new AMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (mMapView.getMap().getScalePerPixel() > Constants.MAX_SCALE_PER_PIXEL) {
                        return;
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                            return;
                        }

                    int left = 10, right = mMapView.getWidth() - 10, top = 10, bottom = mMapView.getHeight() - 10;
                    Projection projection = mMapView.getMap().getProjection();
                    latLngs = new LatLng[]{projection.fromScreenLocation(new Point(left, bottom)), //projection.fromScreenLocation(new Point(left, bottom)),
                            // projection.fromScreenLocation(new Point(left, top)),
                            projection.fromScreenLocation(new Point(right, top))
                            //, projection.fromScreenLocation(new Point(right, bottom))
                    };
                    choosePointPresenter.searchMarks(latLngs);
                }
            }
        });
        mMapView.getMap().setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        return;
                    }

                int left = 10, right = mMapView.getWidth() - 10, top = 10, bottom = mMapView.getHeight() - 10;
                Projection projection = mMapView.getMap().getProjection();
                latLngs = new LatLng[]{projection.fromScreenLocation(new Point(left, bottom)), //projection.fromScreenLocation(new Point(left, bottom)),
                        // projection.fromScreenLocation(new Point(left, top)),
                        projection.fromScreenLocation(new Point(right, top))
                        //, projection.fromScreenLocation(new Point(right, bottom))
                };
                choosePointPresenter.searchMarks(latLngs);

            }
        });
    }

    @Override
    public void initData() {
        choosePointPresenter = new ChoosePointPresenter();
        choosePointPresenter.attachView(this);
    }

    @Override
    public Context getContext() {
        return super.getContext();
    }

    Marker mCloudIDMarker;

    @Override
    public void onCloudItemDetailSearched(CloudItemDetail item, int rCode) {
        if (rCode == 1000 && item != null) {
            mMapView.getMap().clear();
            LatLng position = AMapUtil.convertToLatLng(item.getLatLonPoint());
            mMapView.getMap().animateCamera(CameraUpdateFactory
                    .newCameraPosition(new CameraPosition(position, 18, 0, 30)));
            mCloudIDMarker = mMapView.getMap().addMarker(new MarkerOptions()
                    .position(position)
                    .title(item.getTitle())
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
//            items.add(item);
            Log.d(TAG, "_id" + item.getID());
            Log.d(TAG, "_location" + item.getLatLonPoint().toString());
            Log.d(TAG, "_name" + item.getTitle());
            Log.d(TAG, "_address" + item.getSnippet());
            Log.d(TAG, "_caretetime" + item.getCreatetime());
            Log.d(TAG, "_updatetime" + item.getUpdatetime());
            Log.d(TAG, "_distance" + item.getDistance());
            Iterator iter = item.getCustomfield().entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                Log.d(TAG, key + "   " + val);
            }
        } else {
            ToastUtil.showerror(getContext(), rCode);
        }

    }

    @Override
    public void onCloudSearched(CloudResult result, int rCode) {

        if (rCode == 1000) {
            if (result != null && result.getQuery() != null) {
                if (result.getQuery().equals(choosePointPresenter.getQuery())) {
                    mCloudItems = result.getClouds();

                    if (mCloudItems != null && mCloudItems.size() > 0) {
                        mMapView.getMap().clear();
                        mPoiCloudOverlay = new CloudOverlayMarks(mMapView.getMap(), mCloudItems);
                        mPoiCloudOverlay.removeFromMap();
                        mPoiCloudOverlay.addToMap();
                        // mPoiCloudOverlay.zoomToSpan();
                        for (CloudItem item : mCloudItems) {
//                            items.add(item);
                            Log.d(TAG, "_id " + item.getID());
                            Log.d(TAG, "_location "
                                    + item.getLatLonPoint().toString());
                            Log.d(TAG, "_name " + item.getTitle());
                            Log.d(TAG, "_address " + item.getSnippet());
                            Log.d(TAG, "_caretetime " + item.getCreatetime());
                            Log.d(TAG, "_updatetime " + item.getUpdatetime());
                            Log.d(TAG, "_distance " + item.getDistance());
                            Iterator iter = item.getCustomfield().entrySet()
                                    .iterator();
                            while (iter.hasNext()) {
                                Map.Entry entry = (Map.Entry) iter.next();
                                Object key = entry.getKey();
                                Object val = entry.getValue();
                                Log.d(TAG, key + "   " + val);
                            }
                        }
                    } else {
                        ToastUtil.show(getContext(), R.string.no_result);
                    }
                }
            } else {
                ToastUtil.show(getContext(), R.string.no_result);
            }
        } else {
            ToastUtil.showerror(getContext(), rCode);
        }

    }
}
