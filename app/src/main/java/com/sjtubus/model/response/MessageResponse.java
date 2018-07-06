package com.sjtubus.model.response;

import com.google.gson.annotations.SerializedName;
import com.sjtubus.model.BusMessage;

import java.util.List;

public class MessageResponse extends HttpResponse {
    @SerializedName("messages")
    private List<BusMessage> messages;

    public List<BusMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<BusMessage> messages) {
        this.messages = messages;
    }
}
