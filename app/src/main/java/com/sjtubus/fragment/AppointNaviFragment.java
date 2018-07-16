package com.sjtubus.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.sjtubus.R;
import com.sjtubus.activity.AppointActivity;
import com.sjtubus.utils.ToastUtils;

import java.util.Calendar;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class AppointNaviFragment extends BaseFragment {

    private Toolbar toolbar;
    private TextView singleway;
    private TextView doubleway;
    private TextView departure_place;
    private TextView arrive_place;
    private TextView singleway_date;
    private TextView doubleway_date;
    private Button search_btn;

    private ToastUtils toastUtils;
    private String[] station_list = {"闵行校区", "徐汇校区", "七宝校区"};
    private int year, month, day;

    public static AppointNaviFragment getInstance() {
        AppointNaviFragment appointNaviFragment = new AppointNaviFragment();
        return appointNaviFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appoint, container, false);

        departure_place = view.findViewById(R.id.appoint_departureplace);
        arrive_place = view.findViewById(R.id.appoint_arriveplace);
        singleway_date = view.findViewById(R.id.appoint_singlewaydate);
        doubleway_date = view.findViewById(R.id.appoint_doublewaydate);
        search_btn = view.findViewById(R.id.appoint_searchbtn);

        departure_place.setOnClickListener(new MyListener());
        arrive_place.setOnClickListener(new MyListener());
        singleway_date.setOnClickListener(new MyListener());
        doubleway_date.setOnClickListener(new MyListener());
        search_btn.setOnClickListener(new MyListener());

        getConrrentDay();
        singleway_date.setText(year+"-"+month+"-"+day);

        return view;
    }

    class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.appoint_departureplace:
                case R.id.appoint_arriveplace:
                    //  toastUtils.showShort("hello");
                    final TextView textView_place = (TextView) v.findViewById(v.getId());
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("选择站点");
                    builder.setSingleChoiceItems(station_list, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            textView_place.setText(station_list[which]);
                        }
                    });
                    builder.setNegativeButton("取消", null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    break;

                case R.id.appoint_singlewaydate:
                case R.id.appoint_doublewaydate:
                    final TextView textView_date = (TextView) v.findViewById(v.getId());

                    new DatePickerDialog(Objects.requireNonNull(getActivity()), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year_choose, int month_choose, int dayOfMonth_choose) {
                            textView_date.setText(year_choose+"-"+(month_choose+1)+"-"+dayOfMonth_choose);
                            year = year_choose;
                            month = month_choose+1;
                            day = dayOfMonth_choose;
                        }
                    }, year,month,day).show();
                    break;

                case R.id.appoint_searchbtn:
                    Log.d("alive-start", "start");
                    if (departure_place.getText() == arrive_place.getText()){
                        toastUtils.showShort("起点和终点不能相同！");
                        break;
                    }
                    String data1 = (String) departure_place.getText();
                    String data2 = (String) arrive_place.getText();
                    String data3 = (String) singleway_date.getText();
                    Intent appointIntent = new Intent(getActivity(), AppointActivity.class);
                    appointIntent.putExtra("departure_place", data1);
                    appointIntent.putExtra("arrive_place", data2);
                    appointIntent.putExtra("singleway_date", data3);
                    startActivityForResult(appointIntent, 1);
                    break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK){
                   //Log.d("appointfragment", data.getStringExtra("singleway_date"));
                    toastUtils.showShort(data.getStringExtra("singleway_date"));
                    departure_place.setText(data.getStringExtra("departure_place"));
                    arrive_place.setText(data.getStringExtra("arrive_place"));
                    singleway_date.setText(data.getStringExtra("singleway_date"));
                }
            default:
                break;
        }
    }

    private void getConrrentDay() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);       //获取年月日时分秒
        month = calendar.get(Calendar.MONTH)+1;   //获取到的月份是从0开始计数
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }
}