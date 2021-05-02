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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class fragment_reserve_visit_2 extends Fragment {

    ViewGroup viewGroup;
    CalendarView calendarView;
    TimePicker time_picker;
    TextView reserve_visit_year,reserve_visit_month, reserve_visit_day, reserve_visit_hour, reserve_visit_minute;
    String reserve_content;
    int selectYear, selectMonth, selectDay, selectHour, selectMinute;
    Button check_reserveTime;
    LinearLayout reserveTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_reserve_visit_2, container, false);

        calendarView = (CalendarView)viewGroup.findViewById(R.id.calendarView);
        time_picker = (TimePicker)viewGroup.findViewById(R.id.time_picker);
        reserve_visit_year = (TextView)viewGroup.findViewById(R.id.reserve_visit_year);
        reserve_visit_month = (TextView)viewGroup.findViewById(R.id.reserve_visit_month);
        reserve_visit_day = (TextView)viewGroup.findViewById(R.id.reserve_visit_day);
        reserve_visit_hour = (TextView)viewGroup.findViewById(R.id.reserve_visit_hour);
        reserve_visit_minute = (TextView)viewGroup.findViewById(R.id.reserve_visit_minute);
        check_reserveTime = (Button)viewGroup.findViewById(R.id.check_reserveTime);
        reserveTime = (LinearLayout)viewGroup.findViewById(R.id.reserveTime);


        reserveTime.setVisibility(View.INVISIBLE);

        check_reserveTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reserveTime.setVisibility(View.VISIBLE);

                reserve_visit_year.setText(Integer.toString(selectYear));
                reserve_visit_month.setText(Integer.toString(selectMonth));
                reserve_visit_day.setText(Integer.toString(selectDay));
                reserve_visit_hour.setText(Integer.toString(selectHour));
                reserve_visit_minute.setText(Integer.toString(selectMinute));

                reserve_content = reserve_visit_year.getText().toString()+"년"+reserve_visit_month.getText().toString()+"월"
                        +reserve_visit_day.getText().toString()+"일"+reserve_visit_hour.getText().toString()+"시"
                        +reserve_visit_minute.getText().toString()+"분";

            }
        });
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfmonth) {

                selectYear = year;
                selectMonth = month + 1;
                selectDay = dayOfmonth;

            }
        });

        time_picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                selectHour = hour;
                selectMinute = minute;
            }
        });



        return viewGroup;
    }
}
