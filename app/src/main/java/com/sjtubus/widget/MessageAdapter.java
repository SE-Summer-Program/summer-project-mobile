package com.sjtubus.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sjtubus.R;
import com.sjtubus.model.BusMessage;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private List<BusMessage> messages;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView messageSender;
        TextView messageContent;
        TextView messageTitle;
        TextView messageTime;

        ViewHolder(View view){
            super(view);
            messageTitle = (TextView) view.findViewById(R.id.message_title);
            messageContent = (TextView) view.findViewById(R.id.message_content);
            messageSender = (TextView) view.findViewById(R.id.message_sender);
            messageTime = (TextView) view.findViewById(R.id.message_time);
        }
    }

    public void setDataList(List<BusMessage> list) {
        messages = list;
        notifyDataSetChanged();
    }

    public MessageAdapter(Context context){
        this.context = context;
        this.messages = new ArrayList<>();
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        MessageAdapter.ViewHolder holder = new MessageAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        BusMessage message = messages.get(position);
        holder.messageTitle.setText(message.getTitle());
        holder.messageContent.setText(message.getContent());
        holder.messageSender.setText(message.getSender());
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
        String time=format.format(message.getTime());
        holder.messageTime.setText(time);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
