package com.sjtubus.network;

import com.sjtubus.model.response.HttpResponse;
import com.sjtubus.model.response.LoginResponse;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.internal.operators.observable.ObservableError;
import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface BusApi {
    @GET("bus/buses")
    Observable<List<Object>> getBuses();

    @POST("/login")
    Observable<LoginResponse> login(RequestBody info);

    @POST("/logout")
    Observable<HttpResponse> logout();

    @POST("/register")
    Observable<HttpResponse> register(RequestBody info);
}
