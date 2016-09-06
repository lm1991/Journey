package com.mesor.journey.addmark.model;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.cloud.CloudItem;
import com.mesor.journey.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Limeng on 2016/9/6.
 */
public class CloudOverlayMarks {
    private List<CloudItem> mPois;
    private AMap mAMap;
    private ArrayList<Circle> mPoiMarks = new ArrayList<>();

    public CloudOverlayMarks(AMap amap, List<CloudItem> pois) {
        mAMap = amap;
        mPois = pois;
    }

    public void addToMap() {
        for (int i = 0; i < mPois.size(); i++) {
            Circle circle = mAMap.addCircle(getCircleOptions(i));
            mPoiMarks.add(circle);
        }
    }

    public void removeFromMap() {
        for (Circle circle : mPoiMarks) {
            if(circle.isVisible()) {
                circle.setVisible(false);
            }
            circle.remove();
        }
        mPoiMarks.clear();
    }

    public void zoomToSpan() {
        if (mPois != null && mPois.size() > 0) {
            if (mAMap == null)
                return;
            LatLngBounds bounds = getLatLngBounds();
            mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));
        }
    }

    private LatLngBounds getLatLngBounds() {
        LatLngBounds.Builder b = LatLngBounds.builder();
        for (int i = 0; i < mPois.size(); i++) {
            b.include(new LatLng(mPois.get(i).getLatLonPoint().getLatitude(),
                    mPois.get(i).getLatLonPoint().getLongitude()));
        }
        return b.build();
    }

    private CircleOptions getCircleOptions(int index) {
        return new CircleOptions().center(new LatLng(mPois.get(index).getLatLonPoint()
                .getLatitude(), mPois.get(index)
                .getLatLonPoint().getLongitude()))
                .fillColor(0x55ff0000)
                .radius(Constants.VALID_RADIUS)
                .strokeColor(0xffff0000).strokeWidth(5);
    }

    private MarkerOptions getMarkerOptions(int index) {
        return new MarkerOptions()
                .position(
                        new LatLng(mPois.get(index).getLatLonPoint()
                                .getLatitude(), mPois.get(index)
                                .getLatLonPoint().getLongitude()))
                .title(getTitle(index)).snippet(getSnippet(index))
                .icon(getBitmapDescriptor(index));
    }

    protected BitmapDescriptor getBitmapDescriptor(int index) {
        return null;
    }

    protected String getTitle(int index) {
        return mPois.get(index).getTitle();
    }

    protected String getSnippet(int index) {
        return mPois.get(index).getSnippet();
    }

}
