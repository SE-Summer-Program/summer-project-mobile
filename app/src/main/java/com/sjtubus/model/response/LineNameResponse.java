package com.sjtubus.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LineNameResponse extends HttpResponse {
    @SerializedName("linenames")
    private List<String> linenames;

    public List<String> getLinenames() {
        return linenames;
    }

    public void setLinenames(List<String> linenames) {
        this.linenames = linenames;
    }
}
