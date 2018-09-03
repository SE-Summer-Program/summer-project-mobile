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
import com.sjtubus.activity.CollectActivity;
import com.sjtubus.model.Collection;
import com.sjtubus.model.ShiftInfo;
import com.sjtubus.model.User;
import com.sjtubus.model.response.HttpResponse;
import com.sjtubus.model.response.ShiftInfoResponse;
import com.sjtubus.network.RetrofitClient;
import com.sjtubus.user.UserManager;
import com.sjtubus.utils.ToastUtils;
import com.sjtubus.utils.ZxingUtils;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.FormBody;
import okhttp3.RequestBody;

import static android.content.ContentValues.TAG;

public class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.ViewHolder>{

    private List<Collection> collections;
    private CollectActivity context;
    private CollectAdapter.OnItemClickListener mItemClickListener;

    private CompositeDisposable compositeDisposable;

    private String TAG = "collectadapter";

    public void setItemClickListener(CollectAdapter.OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public void setDataList(List<Collection> collections) {
        this.collections = collections;
        notifyDataSetChanged();
    }

    public CollectAdapter(CollectActivity context){
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView linename;
        TextView shiftid;
        TextView detail;
        Button cancelbtn;
        Button fastappointbtn;

        ViewHolder(View view ){
            super(view);
            linename = view.findViewById(R.id.collect_linename);
            shiftid = view.findViewById(R.id.collect_shiftid);
            detail = view.findViewById(R.id.collect_shiftdetail);
            cancelbtn = view.findViewById(R.id.collect_cancel);
            fastappointbtn = view.findViewById(R.id.collect_fast);

//            remindbtn.setOnClickListener();
        }
    }

    @NonNull
    @Override
    public CollectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, parent, false);
        final CollectAdapter.ViewHolder holder = new CollectAdapter.ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(v);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CollectAdapter.ViewHolder holder, final int position) {
        final String shiftid = collections.get(position).getShiftid();

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
                    String linename = shiftInfo.getLineNameCn();
                    String detail = "发车时间：" + shiftInfo.getDepartureTime()
                            + "\n车牌号：" + shiftInfo.getBusPlateNum()
                            + "\n核载人数：" + shiftInfo.getBusSeatNum()
                            + "\n司机姓名：" + shiftInfo.getDriverName()
                            + "\n司机联系方式：" + shiftInfo.getDriverPhone()
                            + "\n备注：" + shiftInfo.getComment();

                    holder.linename.setText(linename);
                    holder.shiftid.setText(shiftid);
                    holder.detail.setText(detail);

                    holder.cancelbtn.setOnClickListener(ChildListener);
                    holder.cancelbtn.setTag(position);
                    holder.fastappointbtn.setOnClickListener(ChildListener);
                    holder.fastappointbtn.setTag(position);
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


    @Override
    public int getItemCount(){
        return collections.size();
    }

    public static interface OnItemClickListener {
        void onItemClick(View view);
    }

    private View.OnClickListener ChildListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v){
            switch (v.getId()){
                case R.id.collect_cancel:
                    final Collection info_cancel = collections.get((int)v.getTag());

                    new AlertDialog.Builder(context)
                            .setMessage("确认取消收藏吗？")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    User user = UserManager.getInstance().getUser();
                                    String shiftid = info_cancel.getShiftid();

                                    RequestBody requestBody = new FormBody.Builder()
                                            .add("userid", user.getUserId())
                                            .add("username", user.getUsername())
                                            .add("shiftid", shiftid)
                                            .build();
                                    retrieveData(requestBody);
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();

                    break;

                case R.id.collect_fast:
                    String shiftid = collections.get((int)v.getTag()).getShiftid();



                    break;
                default:
                    break;
            }
        }
    };

    private void retrieveData(RequestBody requestBody){
        RetrofitClient.getBusApi()
                .deleteCollection(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(HttpResponse response) {
                        if(response.getError()==0){
                            new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("取消收藏成功!")
                                    .setConfirmText("确定")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.cancel();
                                            context.finish();
                                        }
                                    })
                                    .show();
                        }
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
        for (int i = 0; i < collections.size(); i++) {
            if (uuid.equalsIgnoreCase(collections.get(i).getId())) { //有改动
                return i;
            }
        }
        return -1;
    }
}
