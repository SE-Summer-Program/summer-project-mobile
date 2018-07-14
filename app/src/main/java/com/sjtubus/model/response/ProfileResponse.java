package com.sjtubus.model.response;

import com.google.gson.annotations.SerializedName;
import com.sjtubus.model.User;

public class ProfileResponse extends HttpResponse {
    @SerializedName("user")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
