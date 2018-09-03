package com.sjtubus.model.response;

import com.google.gson.annotations.SerializedName;

import com.sjtubus.model.Collection;

import java.util.ArrayList;
import java.util.List;

public class CollectionResponse extends HttpResponse {
    @SerializedName("collections")
    private List<Collection> collections;

    private List<String> shifts;

    public List<Collection> getCollections() {
        return collections;
    }

    public void setCollections(List<Collection> collections) {
        this.collections = collections;
    }

    public List<String> getShifts() {
        shifts = new ArrayList<>();
        for (int i = 0; i < collections.size(); i++){
            shifts.add(collections.get(i).getShiftid());
        }
        return shifts;
    }
}
