package com.sjtubus.model.response;

import com.google.gson.annotations.SerializedName;
import com.sjtubus.model.RecordInfo;
import com.sjtubus.model.ShiftInfo;

import java.util.List;

public class ShiftInfoResponse extends HttpResponse{

    @SerializedName("shiftInfos")
    private ShiftInfo shiftInfo;

    public ShiftInfo getShiftInfo() { return shiftInfo; }

    public void setShiftInfo(ShiftInfo shiftInfo) {
        this.shiftInfo = shiftInfo;
    }
}
