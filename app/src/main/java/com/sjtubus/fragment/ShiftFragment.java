package com.sjtubus.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sjtubus.R;
import com.sjtubus.model.response.ScheduleResponse;
import com.sjtubus.network.RetrofitClient;
import com.sjtubus.utils.ToastUtils;
import com.sjtubus.widget.ShiftAdapter;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

/**
 * Created by Allen
 * Time  2018/7/8
 */

public class ShiftFragment extends BaseFragment {
    private static ShiftFragment mFragment = null;

//    private String type = "NormalWorkday";
//    private String line_name = "MinToXu";
    private String type;
    private String line_name;

    private ShiftAdapter mAdapter;

    public static ShiftFragment getInstance(String type,String line_name) {
        if(mFragment == null){
            mFragment = new ShiftFragment();
        }
        mFragment.setTypeAndLine(type,line_name);
        return mFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shift, container, false);

        RecyclerView mRecyclerView = view.findViewById(R.id.shift_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        mAdapter = new ShiftAdapter(mRecyclerView.getContext());
        mRecyclerView.setAdapter(mAdapter);
        retrieveData();
        return view;
    }

    private void setTypeAndLine(String type,String line_name){
        this.type = type;
        this.line_name = line_name;
    }

    private void retrieveData() {
        RetrofitClient.getBusApi()
            .getSchedule(type,line_name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<ScheduleResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    addDisposable(d);
                }

                @Override
                public void onNext(ScheduleResponse response) {
                    Log.d(TAG, "onNext: ");
                    mAdapter.setDataList(response.getSchedule());
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                    ToastUtils.showShort("网络请求失败！请检查你的网络！");
                }

                @Override
                public void onComplete() {
                    Log.d(TAG, "onComplete: ");
                    //mProgressBar.setVisibility(View.GONE);
                }
            });
    }

}