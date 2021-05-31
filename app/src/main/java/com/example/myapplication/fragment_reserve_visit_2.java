package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class fragment_reserve_visit_2 extends Fragment {

    ViewGroup viewGroup;
    CalendarView calendarView;
    TimePicker time_picker;
    TextView reserve_visit_year,reserve_visit_month, reserve_visit_day, reserve_visit_hour, reserve_visit_minute;

    int selectYear, selectMonth, selectDay, selectHour, selectMinute;
    Button check_reserveTime;
    LinearLayout reserveTime;

    private TextView textView_selected_time;
    private String selectedCal = "0000-00-00 ";
    private String selectedPic = "00:00";
    private Date savedDate;

    private static String reserve_content;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_reserve_visit_2, container, false);

        calendarView = (CalendarView)viewGroup.findViewById(R.id.calendarView);
        time_picker = (TimePicker)viewGroup.findViewById(R.id.time_picker);
        textView_selected_time = (TextView)viewGroup.findViewById(R.id.textview_selected_time);

        // 캘린터뷰 날짜 변경 리스너.
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfmonth) {
                // 선택된 날짜를 저장한다.
                selectYear = year;
                selectMonth = month + 1;
                selectDay = dayOfmonth;

                // 선택된 시간을 리프레쉬하는 메소드를 호출한다.
                refreshSelectedTime();
            }
        });
        // 타임피커 타임 변경 리스너
        time_picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                // 선택된 시간를 저장한다.
                selectHour = hour;
                selectMinute = minute;

                // 선택된 시간을 리프레쉬하는 메소드를 호출한다.
                refreshSelectedTime();
            }
        });



        return viewGroup;
    }

    // 선택된 시간을 리프레쉬하는 메소드
    private void refreshSelectedTime()
    {
        // 선택된 날짜를 stringBuilder로 append 하고 String 으로 저장한다.
        StringBuilder stringBuilder_calender = new StringBuilder();
        stringBuilder_calender.append(selectYear);
        stringBuilder_calender.append("-");
        stringBuilder_calender.append(selectMonth);
        stringBuilder_calender.append("-");
        stringBuilder_calender.append(selectDay);
        stringBuilder_calender.append(" ");

        selectedCal = stringBuilder_calender.toString();

        // 선택된 시간을 stringBuilder로 append 하고 String 으로 저장한다.
        StringBuilder stringBuilder_picker = new StringBuilder();
        stringBuilder_picker.append(selectHour);
        stringBuilder_picker.append(":");
        stringBuilder_picker.append(selectMinute);

        selectedPic = stringBuilder_picker.toString();

        // 위의 두 String을 append 한다.
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(selectedCal);
        stringBuilder.append(selectedPic);

        // 선택된 시간 정보를 String 타입으로 저장한다.
        reserve_content = stringBuilder.toString();

        textView_selected_time.setText(reserve_content);

    }
    // 위에서 저장한 선택된 시간 정보를 Date 형으로 형변환 하여 반환한다.
    public static Date getSelectedTime() {

        return DateString.StringToDate(reserve_content);
    }

    // View가 모두 생성되고 난 이후에 저장된 시간 정보를 캘린터뷰에 적용한다.
    // onCreateView 메소드에서 실행하면 적용이 안되는 문제가 발생하여 해당 방식을 선택했다.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        savedDate = getSelectedTime();

            if(savedDate != null)
            {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(savedDate);

                selectYear = calendar.get(Calendar.YEAR);
                selectMonth = calendar.get(Calendar.MONTH) + 1;
                selectDay = calendar.get(Calendar.DAY_OF_MONTH);
                selectHour = calendar.get(Calendar.HOUR_OF_DAY);
                selectMinute = calendar.get(Calendar.MINUTE);

                calendarView.setDate(savedDate.getTime());
                time_picker.setHour(selectHour);
                time_picker.setMinute(selectMinute);

                refreshSelectedTime();
            }
    }

}
