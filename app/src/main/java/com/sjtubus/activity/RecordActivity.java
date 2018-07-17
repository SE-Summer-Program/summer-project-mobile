package com.sjtubus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.support.v7.widget.Toolbar;

import com.sjtubus.R;
import com.sjtubus.model.RecordInfo;
import com.sjtubus.widget.RecordAdapter;

import java.util.List;

public class RecordActivity extends BaseActivity implements RecordAdapter.OnItemClickListener{

    private Toolbar mToolbar;
    private RecyclerView recyclerView;
    private RecordInfo recordInfo;
    private List<RecordInfo> recordInfos;
    private RecordAdapter recordAdapter;

    private SwipeRefreshLayout swipeRefresh;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar();
    }

    private void initToolbar(){
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

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.refresh_record);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshRecord();
            }
        });
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_record;
    }

    @Override
    public void onItemClick(View view) {

    }

    private void refreshRecord(){

    }
}
