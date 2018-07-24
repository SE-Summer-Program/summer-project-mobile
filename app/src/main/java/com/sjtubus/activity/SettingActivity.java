package com.sjtubus.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.sjtubus.R;
import com.sjtubus.utils.StringCalendarUtils;
import com.sjtubus.utils.ToastUtils;

import java.util.Arrays;

public class SettingActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        initDetail();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_setting;
    }


    private void initView(){
        Toolbar toolbar = findViewById(R.id.toolbar_setting);
       // ToastUtils.showShort("设置功能还不能使用哦~");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    /******************************************************************************/
    /******************************************************************************/

    String[] AntiClockNonLoopFromSubway = {
            "07:30",
            "07:45",
            "08:00",
            "08:15"
    };

    String[] AntiClockLoop = {
            "08:25",
            "08:40",
            "09:00",
            "09:20",
            "09:40",
            "10:00",
            "10:20",
            "10:40",
            "11:00",
            "11:20",
            "11:40",
            "12:00",
            "13:00",
            "13:20",
            "13:40",
            "14:00",
            "14:20",
            "14:40",
            "15:00",
            "15:20",
            "15:40",
            "16:00",
            "16:20",
            "16:30",
            "17:00"
    };

    String[] AntiClockNonLoopToSubway = {
            "17:15",
            "17:30",
            "17:50",
            "18:00",
            "19:00",
            "20:10"
    };

    private TextView station_name;
    private TextView anticlockwise_time, clockwise_time, anticlockwise_holiday_time, clockwise_holiday_time;
    private TextView comment;

    private void initDetail(){
        station_name = findViewById(R.id.detail_station_name);
        station_name.setText("菁菁堂");

        anticlockwise_time = findViewById(R.id.detail_anticlockwise);
        clockwise_time = findViewById(R.id.detail_clockwise);
        anticlockwise_holiday_time = findViewById(R.id.detail_anticlockwise_holiday);
        clockwise_holiday_time = findViewById(R.id.detail_clockwise_holiday);
        comment = findViewById(R.id.detail_comment);

        initAnticlockwise();
        initClockwise();
        initAnticlockwiseHoliday();
        initclockwiseHoliday();
        initComment();
    }

    private void initAnticlockwise(){
        StringBuilder anticlockwise_text = new StringBuilder();
        for (String time : AntiClockNonLoopFromSubway){
            if (StringCalendarUtils.isBeforeCurrentTimeHHmm(time)){
                //灰色
                anticlockwise_text.append("<font color='#D1D1D1'>").append(time).append("</font>    ");
                continue;
            }
            //橘红色
            anticlockwise_text.append("<font color='#FF6347'>").append(time).append("</font>    ");
        }
        for (String time : AntiClockLoop){
            if (StringCalendarUtils.isBeforeCurrentTimeHHmm(time)){
                //灰色
                anticlockwise_text.append("<font color='#D1D1D1'>").append(time).append("</font>    ");
                continue;
            }
            //草绿色
            anticlockwise_text.append("<font color='#66CD00'>").append(time).append("</font>    ");
        }
        for (String time : AntiClockNonLoopToSubway){
            if (StringCalendarUtils.isBeforeCurrentTimeHHmm(time)){
                //灰色
                anticlockwise_text.append("<font color='#D1D1D1'>").append(time).append("</font>    ");
                continue;
            }
            //湖蓝色
            anticlockwise_text.append("<font color='#6495ED'>").append(time).append("</font>    ");
        }

        anticlockwise_time.setText(Html.fromHtml(anticlockwise_text.toString()));
    }

    private void initClockwise(){

    }

    private void initAnticlockwiseHoliday(){

    }

    private void initclockwiseHoliday(){

    }

    private void initComment(){
        String comment_text = "1. <font color='#FF6347'><strong>红色班次</strong></font>提前6分钟从东川路地铁站发车，" +
                "巴士站点设在地铁站南侧的公交车集中候车点内。" + "<br>" +
                "2. <font color='#6495ED'><strong>蓝色班次</strong></font>终点站为东川路地铁站校园巴士专用站。" + "<br>" +
                "3. 周六、周日及法定节假日停运。" + "<br>" +
                "4. 问询电话：后勤保障处 54743939" + "<br>";

        comment.setText(Html.fromHtml(comment_text));
    }
}

