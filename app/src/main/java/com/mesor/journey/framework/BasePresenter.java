package com.mesor.journey.framework;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import rx.Subscription;

/**
 * Created by Limeng on 2016/8/27.
 */
public abstract class BasePresenter<V> {

    protected Map<String, Subscription> subscriptionMap = new HashMap<>();

    public abstract void attachView(V view);

    public void detachView() {
        if(subscriptionMap.size() > 0) {
            Iterator<Subscription> iterator = subscriptionMap.values().iterator();
            while (iterator.hasNext()) {
                Subscription subscription = iterator.next();
                if(subscription != null && !subscription.isUnsubscribed()) {
                    subscription.unsubscribe();
                }
            }
        }
    }

}
