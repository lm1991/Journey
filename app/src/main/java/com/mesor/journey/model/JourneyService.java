package com.mesor.journey.model;

import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Limeng on 2016/8/27.
 */
public interface JourneyService {

    @Headers("Content-Type:application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST
    Observable<InfoMapResult> addMark(@Url String url, @FieldMap Map<String, Object> formData);

    class Factory {
        public static JourneyService create() {
            Retrofit retrofit = new Retrofit.Builder().addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create()).baseUrl("https://www.google.com").build();
            return retrofit.create(JourneyService.class);
        }
    }
}
