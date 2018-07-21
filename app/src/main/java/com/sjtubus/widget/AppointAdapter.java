package com.sjtubus.widget;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sjtubus.R;
import com.sjtubus.activity.BaseActivity;
import com.sjtubus.activity.OrderActivity;
import com.sjtubus.activity.RecordActivity;
import com.sjtubus.model.AppointInfo;
import com.sjtubus.model.RecordInfo;
import com.sjtubus.model.response.RecordInfoResponse;
import com.sjtubus.network.RetrofitClient;
import com.sjtubus.user.UserManager;
import com.sjtubus.utils.StringCalendarUtils;
import com.sjtubus.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

public class AppointAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Context context;
    private List<AppointInfo> appointInfoList = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private OnScrollListener onScrollListener;

    private boolean hasConflictSchedule = false;

    public AppointAdapter(Context context){
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setDataList(List<AppointInfo> appointInfoList){
        this.appointInfoList = appointInfoList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case AppointInfo.PARENT_ITEM:
                view = layoutInflater.inflate(R.layout.item_appoint_parent, parent, false);
                return new AppointParentViewHolder(context, view);
            case AppointInfo.CHILD_ITEM:
                view = layoutInflater.inflate(R.layout.item_appoint_child, parent, false);
                return new AppointChildViewHolder(context, view);
            default:
                view = layoutInflater.inflate(R.layout.item_appoint_parent, parent, false);
                return new AppointParentViewHolder(context, view);
        }
    }

    /*
     * 根据不同的类型绑定不同的view
     */
    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case AppointInfo.PARENT_ITEM:
                AppointParentViewHolder parentViewHolder = (AppointParentViewHolder)holder;
                parentViewHolder.bindView(appointInfoList.get(position), position, appointItemClickListener);
                break;
            case AppointInfo.CHILD_ITEM:
                AppointChildViewHolder childViewHolder = (AppointChildViewHolder)holder;
                childViewHolder.bindView(appointInfoList.get(position), position, ChildListener);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return appointInfoList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return appointInfoList.get(position).getType();
    }

    private AppointItemClickListener appointItemClickListener = new AppointItemClickListener() {
        @Override
        public void onExpandChildItem(AppointInfo bean) {
            int position = getCurrentPosition(bean.getId()); //确定当前点击的item位置
            AppointInfo child = getChildDataBean(bean); //获取要展示的子布局数据对象
            if (child == null){
                return;
            }
            add(child, position+1); //在当前的item下方插入
            if (position == appointInfoList.size()-2 && onScrollListener != null){
                onScrollListener.scrollTo(position + 1); //向下滚动，使得子布局能够完全展示
            }
        }

        @Override
        public void onHideChildItem(AppointInfo bean) {
            int position = getCurrentPosition(bean.getId()); //确定当前点击的item位置
            AppointInfo child = getChildDataBean(bean);
            if (child == null){
                return;
            }
            remove(position + 1); //删除
            if (onScrollListener != null){
                onScrollListener.scrollTo(position);
            }
        }
    };

    private View.OnClickListener ChildListener = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.appointitem_reservebtn:
                    AppointInfo info = appointInfoList.get((int)v.getTag()-1);
                    Log.i("APPOINT-TAG",String.valueOf((int)v.getTag()));
                    String departure_time = info.getDeparture_time();
                    String arrive_time = info.getArrive_time();
                    String departure_date = info.getDate();
//                    hasConflictSchedule = false;
//                    refreshRecord(departure_date, arrive_time, departure_date);
//                    if (hasConflictSchedule){
//                        ToastUtils.showShort("不能预约行程冲突的班次哦~");
//                        break;
//                    }
                    Intent orderIntent = new Intent(context, OrderActivity.class);
                    orderIntent.putExtra("departure_place", info.getDeparture_place());
                    orderIntent.putExtra("arrive_place", info.getArrive_place());
                    orderIntent.putExtra("departure_time", StringCalendarUtils.HHmmssToHHmm(departure_time));
                    orderIntent.putExtra("arrive_time", StringCalendarUtils.HHmmssToHHmm(arrive_time));
                    orderIntent.putExtra("departure_date", departure_date);
                    orderIntent.putExtra("shiftid", info.getShiftid());
                    orderIntent.putExtra("shift_type", info.getLine_type());
                    context.startActivity(orderIntent);
                    break;
                case R.id.appointitem_infobtn:
                    break;
            }
        }
    };

    /*
     * 在父布局下方插入一条数据
     */
    public void add(AppointInfo bean, int position) {
        appointInfoList.add(position, bean);
        notifyItemInserted(position);
    }

    /*
     *移除子布局数据
     */
    private void remove(int position) {
        appointInfoList.remove(position);
        notifyItemRemoved(position);
    }

    /*
     * 确定当前点击的item位置并返回
     */
    private int getCurrentPosition(String uuid) {
        for (int i = 0; i < appointInfoList.size(); i++) {
            if (uuid.equalsIgnoreCase(appointInfoList.get(i).getId())) {
                return i;
            }
        }
        return -1;
    }

    /*
     * 封装子布局数据对象并返回
     * 注意，此处只是重新封装一个DataBean对象，为了标注Type为子布局数据，进而展开，展示数据
     * 要和onHideChildren方法里的getChildBean()区分开来
     */
    private AppointInfo getChildDataBean(AppointInfo bean) {
        AppointInfo child = new AppointInfo();
        child.setLine_type(bean.getLine_type());
        child.setDate(bean.getDate());
        child.setAppoint_status(bean.getAppoint_status());
        child.setShiftid(bean.getShiftid());
        child.setId(bean.getId());
        child.setRemain_seat(bean.getRemain_seat());
        child.setDeparture_time(bean.getDeparture_time());
        child.setArrive_time(bean.getArrive_time());
        child.setDeparture_place(bean.getDeparture_place());
        child.setArrive_place(bean.getArrive_place());
        child.setType(1);
        //child.setChild_msg(bean.getChild_msg());
        return child;
    }

    public interface OnScrollListener{
        void scrollTo(int pos);
    }


    public void setOnScrollListener(OnScrollListener onScrollListener){
        this.onScrollListener = onScrollListener;
    }


    private void refreshRecord(final String departure_time, final String arrive_time, final String departure_date){
        //获取当前用户的username
        String username = UserManager.getInstance().getUser().getUsername();

        RetrofitClient.getBusApi()
            .getRecordInfos(username)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<RecordInfoResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    /*
                     * 因为飘红删去了
                     */
                  //  ToastUtils.showShort("hello");
            //        addDisposable(d);

                }

                @Override
                public void onNext(RecordInfoResponse response) {
                    Log.d(TAG, "onNext: ");
//                    if(response.getRecordInfos()!=null && response.getRecordInfos().size()!=0){
////                        RecordAdapter recordAdapter = new RecordAdapter( );
////                        recordAdapter.setDataList(response.getRecordInfos());
//                        hasConflictSchedule = false;
//                    }

                    String appoint_starttime = departure_date + " " + departure_time;
                    String appoint_endtime = departure_date + " " + arrive_time;
                    List<RecordInfo> recordInfos = response.getRecordInfos();
                   // ToastUtils.showShort(recordInfos.size());
                    //输出为空
                    for (RecordInfo recordInfo : recordInfos) {
                        String record_starttime = recordInfo.getDepartureDate() + " " + recordInfo.getDepartureTime();
                        String record_endtime = recordInfo.getDepartureDate() + " " + recordInfo.getArriveTime();
                        //记录上出发时间比预约的结束时间晚，或者结束时间比预约的出发时间早，就没问题
                        if (StringCalendarUtils.isBeforeTimeOfSecondPara(appoint_endtime, record_starttime))
                            hasConflictSchedule = false;
                        else if (StringCalendarUtils.isBeforeTimeOfSecondPara(record_endtime, appoint_starttime))
                            hasConflictSchedule = false;
                        else {
                            hasConflictSchedule = true;
                            break;
                        }
                    }
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
