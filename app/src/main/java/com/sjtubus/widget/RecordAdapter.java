package com.sjtubus.widget;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sjtubus.R;
import com.sjtubus.activity.RecordActivity;
import com.sjtubus.model.RecordInfo;
import com.sjtubus.model.response.HttpResponse;
import com.sjtubus.network.RetrofitClient;
import com.sjtubus.user.UserManager;
import com.sjtubus.utils.ToastUtils;
import com.sjtubus.utils.ZxingUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.FormBody;
import okhttp3.RequestBody;

import static android.content.ContentValues.TAG;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder>{

    private List<RecordInfo> recordInfos = new ArrayList<>();
    private RecordActivity context;
    private RecordAdapter.OnItemClickListener mItemClickListener;
    private CompositeDisposable compositeDisposable;

    public void setItemClickListener(RecordAdapter.OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public void setDataList(List<RecordInfo> recordInfos) {
        this.recordInfos = recordInfos;
        notifyDataSetChanged();
    }

    public RecordAdapter(RecordActivity context){
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView submittime;
        TextView linename;
        TextView departuremsg;
        TextView shiftid;
        TextView status;
        Button remindbtn;
        Button cancelbtn;
        ImageView qrcode;

        ViewHolder(View view ){
            super(view);
            submittime = view.findViewById(R.id.record_submittime);
            linename = view.findViewById(R.id.record_linename);
            departuremsg = view.findViewById(R.id.record_departuremsg);
            shiftid = view.findViewById(R.id.record_shiftid);
            status = view.findViewById(R.id.record_status);
            remindbtn = view.findViewById(R.id.record_remindbtn);
            cancelbtn = view.findViewById(R.id.record_cancelbtn);
            qrcode = view.findViewById(R.id.record_qrcode);

//            remindbtn.setOnClickListener();
        }
    }

    @NonNull
    @Override
    public RecordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, parent, false);
        final RecordAdapter.ViewHolder holder = new RecordAdapter.ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(v);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecordAdapter.ViewHolder holder, int position) {
        String confirm_time = recordInfos.get(position).getConfirmDate();
        String line_name = recordInfos.get(position).getLineName();
        String departure_msg = recordInfos.get(position).getDepartureMsg();
        String shift_id = recordInfos.get(position).getShiftid();
        String submit_time = "预定时间： " + recordInfos.get(position).getSubmiTime();
        holder.submittime.setText(confirm_time);
        holder.linename.setText(line_name);
        holder.departuremsg.setText(departure_msg);
        holder.shiftid.setText(shift_id);
        holder.submittime.setText(submit_time);
        String info = shift_id + ";" + recordInfos.get(position).getDepartureDate() + ";"
                + UserManager.getInstance().getUser().getUsername();
        holder.qrcode.setImageBitmap(ZxingUtils.createQRImage(info,300,300));

        holder.remindbtn.setOnClickListener(ChildListener);
        holder.cancelbtn.setOnClickListener(ChildListener);
        holder.remindbtn.setTag(position);
        holder.cancelbtn.setTag(position);
    }

    @Override
    public int getItemCount(){
        return recordInfos.size();
    }

    public static interface OnItemClickListener {
        void onItemClick(View view);
    }

    private View.OnClickListener ChildListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v){
            switch (v.getId()){
                case R.id.record_remindbtn:
                    ToastUtils.showShort("预约提醒功能还不能使用哦~");
                    break;
                case R.id.record_cancelbtn:
                    final RecordInfo info_cancel = recordInfos.get((int)v.getTag());

                    new AlertDialog.Builder(context)
                        .setMessage("确认取消预约吗？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String username = UserManager.getInstance().getUser().getUsername();
                                String shiftid = info_cancel.getShiftid();
                                String appoint_date = info_cancel.getDepartureDate();

                                RequestBody requestBody = new FormBody.Builder()
                                        .add("username", username)
                                        .add("shiftid", shiftid)
                                        .add("appoint_date", appoint_date)
                                        .build();
                                retrieveData(requestBody);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();

                    break;
            }
        }
    };

    public void addDisposable(Disposable s) {
        if (this.compositeDisposable == null) {
            this.compositeDisposable = new CompositeDisposable();
        }

        this.compositeDisposable.add(s);
    }

    private void retrieveData(RequestBody requestBody) {
        RetrofitClient.getBusApi()
            .deleteAppoint(requestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<HttpResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    addDisposable(d);
                }

                @Override
                public void onNext(HttpResponse response) {
                    Log.i(TAG, "onNext: ");
                    
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

    /*
     * 确定当前点击的item位置并返回
     */
    private int getCurrentPosition(String uuid) {
        for (int i = 0; i < recordInfos.size(); i++) {
            if (uuid.equalsIgnoreCase(recordInfos.get(i).getId())) {
                return i;
            }
        }
        return -1;
    }
}
