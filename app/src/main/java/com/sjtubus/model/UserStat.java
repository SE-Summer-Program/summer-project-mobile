package com.sjtubus.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Allen on 2018/7/3.
 */

public class UserStat {
    @SerializedName("attend")
    private Integer attend;
    @SerializedName("favorite")
    private Integer favorite;
    @SerializedName("create")
    private Integer create;
    @SerializedName("team_private")
    private Integer teamPrivate;

    public Integer getAttend() {
        return attend;
    }

    public Integer getFavorite() {
        return favorite;
    }

    public Integer getCreate() {
        return create;
    }

    public Integer getTeamPrivate() {
        return teamPrivate;
    }
}
