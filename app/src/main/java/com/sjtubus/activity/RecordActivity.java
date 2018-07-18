package com.sjtubus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.sjtubus.R;
import com.sjtubus.model.RecordInfo;
import com.sjtubus.model.response.RecordInfoResponse;
import com.sjtubus.network.RetrofitClient;
import com.sjtubus.user.UserManager;
import com.sjtubus.utils.StringCalendarUtils;
import com.sjtubus.utils.ToastUtils;
import com.sjtubus.widget.RecordAdapter;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

public class RecordActivity extends BaseActivity implements View.OnClickListener, RecordAdapter.OnItemClickListener{

    private Toolbar mToolbar;
    private RecyclerView recyclerView;
    private RecordInfo recordInfo;
    private List<RecordInfo> recordInfos;
    private RecordAdapter recordAdapter;

    private SwipeRefreshLayout swipeRefresh;
    private LinearLayoutManager layoutManager;

//    /* 测试item_record样式专用，用recycleview后删除 */
//    TextView confirmtime;
//    TextView linename;
//    TextView departuremsg;
//    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView(){
        mToolbar = findViewById(R.id.toolbar_record);
        mToolbar.setTitle("");
        mToolbar.setNavigationIcon(R.drawable.back_32);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecordActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycle_record);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager); //设置布局管理器
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//      recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)); //分割线

        recordAdapter = new RecordAdapter(this);
        recordAdapter.setItemClickListener(this);
        recyclerView.setAdapter(recordAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        swipeRefresh = findViewById(R.id.refresh_record);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshRecord();
            }
        });

        refreshRecord();
//        /* 测试item_record样式专用，用recycleview后删除 */
//        confirmtime = findViewById(R.id.record_confirmtime);
//        linename = findViewById(R.id.record_linename);
//        departuremsg = findViewById(R.id.record_departuremsg);
//        button = findViewById(R.id.record_btn);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_record;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(View view) {

    }

    private void refreshRecord(){
        //获取当前用户的username
        String username = UserManager.getInstance().getUser().getUsername();
        //获取当前的时间
        String currenttime = StringCalendarUtils.getCurrentTime();

        RetrofitClient.getBusApi()
            .getRecordInfos(username, currenttime)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<RecordInfoResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    addDisposable(d);
                }

                @Override
                public void onNext(RecordInfoResponse response) {
                    Log.d(TAG, "onNext: ");
                    if(response.getRecordInfos()!=null) recordAdapter.setDataList(response.getRecordInfos());
                    swipeRefresh.setRefreshing(false);
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                    swipeRefresh.setRefreshing(false);
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
