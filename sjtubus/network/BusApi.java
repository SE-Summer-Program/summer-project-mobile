package com.sjtubus.network;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface BusApi {
    @GET("bus/buses")
    Observable<List<Object>> getBuses();
}
