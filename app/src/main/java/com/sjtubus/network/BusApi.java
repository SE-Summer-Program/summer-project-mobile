package com.sjtubus.network;

import com.sjtubus.model.response.HttpResponse;
import com.sjtubus.model.response.LineNameResponse;
import com.sjtubus.model.response.LoginResponse;
import com.sjtubus.model.response.MessageResponse;
import com.sjtubus.model.response.ScheduleResponse;
import com.sjtubus.model.response.StationResponse;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.internal.operators.observable.ObservableError;
import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Allen on 2018/7/4.
 */

public interface BusApi {
    @GET("bus/buses")
    Observable<List<Object>> getBuses();

    @POST("/login")
    Observable<LoginResponse> login(RequestBody info);

    @POST("/logout")
    Observable<HttpResponse> logout();

    @POST("/register")
    Observable<HttpResponse> register(RequestBody info);

    @GET("/shift/line_name")
    Observable<LineNameResponse> getLinenames(@Query("type")String type);

    @GET("/messages")
    Observable<MessageResponse> getMessages();

    @GET("/shift/schedule")
    Observable<ScheduleResponse> getSchedule(@Query("type")String type, @Query("line_name")String line_name);

    @GET("/station")
    Observable<StationResponse> getLineStation(@Query("line_name")String line_name);
}
