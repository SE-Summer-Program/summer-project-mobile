package com.sjtubus.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sjtubus.R;
import com.sjtubus.model.AppointInfo;

import java.util.ArrayList;
import java.util.List;

public class AppointAdapter extends RecyclerView.Adapter<BaseViewHolder>{

    private Context context;
    private List<AppointInfo> appointInfoList = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private OnScrollListener onScrollListener;

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
        View view = null;
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
                childViewHolder.bindView(appointInfoList.get(position), position);
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

}
