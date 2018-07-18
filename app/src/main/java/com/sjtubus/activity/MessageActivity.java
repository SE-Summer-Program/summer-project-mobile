package com.sjtubus.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sjtubus.R;
import com.sjtubus.model.BusMessage;
import com.sjtubus.model.response.MessageResponse;
import com.sjtubus.network.RetrofitClient;
import com.sjtubus.utils.ToastUtils;
import com.sjtubus.widget.MessageAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

public class MessageActivity extends BaseActivity {

    private ProgressBar mProgressBar;
    MessageAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        initView();
    }

    public void initView(){
        RecyclerView messageView = findViewById(R.id.message_list);
        mProgressBar = findViewById(R.id.message_progress);
        mAdapter = new MessageAdapter(this);
        mProgressBar.setVisibility(View.VISIBLE);

        messageView.setLayoutManager(new LinearLayoutManager(this));
        messageView.setHasFixedSize(true);
        messageView.setAdapter(mAdapter);
        loadMessages();
    }

    public void loadMessages(){
        RetrofitClient.getBusApi().getMessages()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<MessageResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    addDisposable(d);
                }

                @Override
                public void onNext(MessageResponse response) {
                    Log.d(TAG, "onNext: ");
                    if(response.getMessages().size()!=0){
                        TextView no_message = findViewById(R.id.no_message);
                        no_message.setVisibility(View.GONE);
                    }
                    mAdapter.setDataList(response.getMessages());
                }

                @Override
                public void onError(Throwable e) {
                    ToastUtils.showShort("网络连接错误！请检查你的网络！");
                    mProgressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                }

                @Override
                public void onComplete() {
                    Log.d(TAG, "onComplete: ");
                    mProgressBar.setVisibility(View.GONE);
                }
            });
    }

    @Override
    public int getContentViewId(){
        return R.layout.activity_message;
    }

}
