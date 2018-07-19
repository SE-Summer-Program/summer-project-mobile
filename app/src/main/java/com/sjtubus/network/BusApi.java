package com.sjtubus.network;

import com.sjtubus.model.response.AppointResponse;
import com.sjtubus.model.response.HttpResponse;
import com.sjtubus.model.response.LineInfoResponse;
import com.sjtubus.model.response.LoginResponse;
import com.sjtubus.model.response.MessageResponse;
import com.sjtubus.model.response.ProfileResponse;
import com.sjtubus.model.response.RecordInfoResponse;
import com.sjtubus.model.response.ScheduleResponse;
import com.sjtubus.model.response.StationResponse;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Allen on 2018/7/4.
 */

public interface BusApi {
    @GET("bus/buses")
    Observable<List<Object>> getBuses();

    @POST("account/login")
    Observable<LoginResponse> login(@Body RequestBody info);

    @POST("account/logout")
    Observable<HttpResponse> logout();

    @POST("account/register")
    Observable<HttpResponse> register(@Body RequestBody info);

    @GET("account/profile")
    Observable<ProfileResponse> getProfile();

    @GET("account/messages")
    Observable<MessageResponse> getMessages();

    @GET("/shift/search_schedule")
    Observable<ScheduleResponse> getSchedule(@Query("type") String type, @Query("line_name") String line_name);

    @GET("/line/stations")
    Observable<StationResponse> getLineStation(@Query("line_name") String line_name);

    @GET("/line/infos")
    Observable<LineInfoResponse> getLineInfos(@Query("type") String type);

    @GET("/appointment/infos")
    Observable<AppointResponse> getAppointment(@Query("line_name") String line_name, @Query("type") String type, @Query("appoint_date") String appoint_date);

    @GET("/appointment/record")
    Observable<RecordInfoResponse> getRecordInfos(@Query("username")String username);

    @POST("/appointment/appoint")
    Observable<HttpResponse> appoint(@Body RequestBody appointinfo);
}
