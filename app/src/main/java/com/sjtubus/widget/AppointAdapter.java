package com.sjtubus.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sjtubus.R;
import com.sjtubus.activity.AppointDoubleActivity;
import com.sjtubus.activity.OrderActivity;
import com.sjtubus.model.AppointInfo;
import com.sjtubus.model.RecordInfo;
import com.sjtubus.model.ShiftInfo;
import com.sjtubus.model.response.RecordInfoResponse;
import com.sjtubus.model.response.ShiftInfoResponse;
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
    private CompositeDisposable compositeDisposable;

    private boolean hasConflictSchedule = false;

    private boolean isSingleWayFlag;
    private boolean isSecondPageFlag;
    private String double_date_str;
    private AppointInfo appointInfo = new AppointInfo();

    /* 单程构造函数 */
    public AppointAdapter(Context context){
        this.context = context;
        this.isSingleWayFlag = true;
        this.layoutInflater = LayoutInflater.from(context);
    }

    /* 往返第一页构造函数 */
    public AppointAdapter(Context context, String double_date_str){
        this.context = context;
        this.isSingleWayFlag = false;
        this.isSecondPageFlag = false;
        this.double_date_str = double_date_str;
        this.layoutInflater = LayoutInflater.from(context);
    }

    /* 往返第二页构造函数 */
    public AppointAdapter(Context context, AppointInfo appointInfo){
        this.context = context;
        this.isSingleWayFlag = false;
        this.isSecondPageFlag = true;
        this.appointInfo.copy(appointInfo);
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

    private static String TAG = "appointadapter";

    private View.OnClickListener ChildListener = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.appointitem_reservebtn:
                    AppointInfo info_reserve = appointInfoList.get((int)v.getTag()-1);
                    Log.i("APPOINT-TAG",String.valueOf((int)v.getTag()));
                    String departure_time = info_reserve.getDeparture_time();
                    String arrive_time = info_reserve.getArrive_time();
                    String departure_date = info_reserve.getDate();

                    hasConflictSchedule = false;
                    Log.i(TAG, departure_time + " " + arrive_time+ " " + departure_date);
                    retrofitRecord(departure_time, arrive_time, departure_date, info_reserve);

                    break;

                case R.id.appointitem_collectbtn:
                    ToastUtils.showShort("班次收藏功能还不能使用哦~");
                    break;

                case R.id.appointitem_infobtn:
                    AppointInfo info_schedule = appointInfoList.get((int)v.getTag()-1);
                    String shiftid = info_schedule.getShiftid();
                    retrofitShiftInfo(shiftid);
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

    private void retrofitRecord(final String departure_time, final String arrive_time, final String departure_date, final AppointInfo info_reserve){
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
                    addDisposable(d);
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

                    Log.i(TAG, "appoint_starttime" + appoint_starttime);
                    Log.i(TAG, "appoint_endtime" + appoint_endtime);
                    List<RecordInfo> recordInfos = response.getRecordInfos();
                    //输出为空
                    if(recordInfos != null) {
                        for (RecordInfo recordInfo : recordInfos) {
                            String record_starttime = recordInfo.getDepartureDate() + " " + recordInfo.getDepartureTime();
                            String record_endtime = recordInfo.getDepartureDate() + " " + recordInfo.getArriveTime();

                            Log.i(TAG, "record_starttime" + record_starttime);
                            Log.i(TAG, "record_endtime" + record_endtime);
                            //ToastUtils.showShort(record_starttime + " " + record_endtime);
                            //记录上出发时间比预约的结束时间晚，或者结束时间比预约的出发时间早，就没问题
                            if (StringCalendarUtils.isBeforeTimeOfSecondPara(appoint_endtime, record_starttime))
                                hasConflictSchedule = false;
                            else if (StringCalendarUtils.isBeforeTimeOfSecondPara(record_endtime, appoint_starttime))
                                hasConflictSchedule = false;
                            else {
                                hasConflictSchedule = true;
                                break;
                            }
                            Log.i(TAG, "false");
                            Log.i(TAG, "--------");
                        }
                    }

                    if (hasConflictSchedule){
                        ToastUtils.showShort("不能预约行程冲突的班次哦~");
                        return;
                    }

                    pressReserveButton(departure_time, arrive_time, departure_date, info_reserve);
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

    private void retrofitShiftInfo(final String shiftid){
        //获取当前用户的username
//        String username = UserManager.getInstance().getUser().getUsername();

        RetrofitClient.getBusApi()
                .getShiftInfos(shiftid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ShiftInfoResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(ShiftInfoResponse response) {
                        ShiftInfo shiftInfo = response.getShiftInfo();
                        String message = "班次序列号：" + shiftInfo.getShiftid()
                                + "\n线路名称：" + shiftInfo.getLineNameCn()
                                + "\n发车时间：" + shiftInfo.getDepartureTime()
                                + "\n车牌号：" + shiftInfo.getBusPlateNum()
                                + "\n核载人数：" + shiftInfo.getBusSeatNum()
                                + "\n司机姓名：" + shiftInfo.getDriverName()
                                + "\n司机联系方式：" + shiftInfo.getDriverPhone()
                                + "\n备注：" + shiftInfo.getComment();
                        new AlertDialog.Builder(context)
                                .setTitle("班次详细信息")
                                .setMessage(message)
                                .setPositiveButton("确定", null)
                                .show();
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

    public void addDisposable(Disposable s) {
        if (this.compositeDisposable == null) {
            this.compositeDisposable = new CompositeDisposable();
        }

        this.compositeDisposable.add(s);
    }

    private void pressReserveButton(String departure_time, String arrive_time, String departure_date, AppointInfo info_reserve){
        /* 单程 */
        if (isSingleWayFlag) {
            Intent orderIntent = new Intent(context, OrderActivity.class);
            orderIntent.putExtra("departure_place", info_reserve.getDeparture_place());
            orderIntent.putExtra("arrive_place", info_reserve.getArrive_place());
            orderIntent.putExtra("departure_time", StringCalendarUtils.HHmmssToHHmm(departure_time));
            orderIntent.putExtra("arrive_time", StringCalendarUtils.HHmmssToHHmm(arrive_time));
            orderIntent.putExtra("departure_date", departure_date);
            orderIntent.putExtra("shiftid", info_reserve.getShiftid());
            orderIntent.putExtra("shift_type", info_reserve.getLine_type());

            orderIntent.putExtra("appoint_type", 0);
            context.startActivity(orderIntent);
        }

        /* 往返去程 */
        else if (! isSecondPageFlag){
            Intent appointDoubleIntent = new Intent(context, AppointDoubleActivity.class);

            appointDoubleIntent.putExtra("single_departure_place", info_reserve.getDeparture_place());
            appointDoubleIntent.putExtra("single_arrive_place", info_reserve.getArrive_place());
            appointDoubleIntent.putExtra("single_departure_time", StringCalendarUtils.HHmmssToHHmm(departure_time));
            //ToastUtils.showShort(StringCalendarUtils.HHmmssToHHmm(departure_time));
            appointDoubleIntent.putExtra("single_arrive_time", StringCalendarUtils.HHmmssToHHmm(arrive_time));
            appointDoubleIntent.putExtra("single_departure_date", departure_date);
            appointDoubleIntent.putExtra("single_shiftid", info_reserve.getShiftid());
            appointDoubleIntent.putExtra("single_shift_type", info_reserve.getLine_type());

            appointDoubleIntent.putExtra("double_departure_date", double_date_str);
            appointDoubleIntent.putExtra("target_page", 1);

            context.startActivity(appointDoubleIntent);

        }

        /* 往返返程 */
        else{
            String single_starttime = appointInfo.getDate() + " " + appointInfo.getDeparture_time();
            String single_endtime =  appointInfo.getDate() + " " + appointInfo.getArrive_time();
            String double_starttime = departure_date + " " + StringCalendarUtils.HHmmssToHHmm(departure_time);
            String double_endtime = departure_date + " " + StringCalendarUtils.HHmmssToHHmm(arrive_time);
            // ToastUtils.showLong(single_starttime + " " + single_endtime + " " + double_starttime + " " + double_endtime);

            if (StringCalendarUtils.isBeforeTimeOfSecondParaHHmm(double_endtime, single_starttime)){
                ToastUtils.showShort("返程的时间不能早于去程哦~");
                return;
            } else if (! StringCalendarUtils.isBeforeTimeOfSecondParaHHmm(single_endtime, double_starttime)){
                ToastUtils.showShort("不能预约行程冲突的班次哦~~");
                return;
            }

            Intent orderDoubleIntent = new Intent(context, OrderActivity.class);

            orderDoubleIntent.putExtra("departure_place", appointInfo.getDeparture_place());
            orderDoubleIntent.putExtra("arrive_place", appointInfo.getArrive_place());
            orderDoubleIntent.putExtra("departure_time", appointInfo.getDeparture_time());
            orderDoubleIntent.putExtra("arrive_time", appointInfo.getArrive_time());
            orderDoubleIntent.putExtra("departure_date", appointInfo.getDate());
            orderDoubleIntent.putExtra("shiftid", appointInfo.getShiftid());
            orderDoubleIntent.putExtra("shift_type", appointInfo.getLine_type());

            orderDoubleIntent.putExtra("double_departure_place", info_reserve.getDeparture_place());
            orderDoubleIntent.putExtra("double_arrive_place", info_reserve.getArrive_place());
            orderDoubleIntent.putExtra("double_departure_time", StringCalendarUtils.HHmmssToHHmm(departure_time));
            orderDoubleIntent.putExtra("double_arrive_time", StringCalendarUtils.HHmmssToHHmm(arrive_time));
            orderDoubleIntent.putExtra("double_departure_date", departure_date);
            orderDoubleIntent.putExtra("double_shiftid", info_reserve.getShiftid());
            orderDoubleIntent.putExtra("double_shift_type", info_reserve.getLine_type());

            orderDoubleIntent.putExtra("appoint_type", 1);
            context.startActivity(orderDoubleIntent);
        }
    }
}
