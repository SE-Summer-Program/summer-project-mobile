package com.sjtubus.model.response;

import com.google.gson.annotations.SerializedName;
import com.sjtubus.model.Station;

import java.util.ArrayList;

public class StationResponse extends HttpResponse {
    @SerializedName("stations")
    private ArrayList<String> stations;

    public ArrayList<String> getStations() {
        return stations;
    }

    public void setStations(ArrayList<String> stations) {
        this.stations = stations;
    }
}
