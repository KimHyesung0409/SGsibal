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

        /*
        reserve_visit_year = (TextView)viewGroup.findViewById(R.id.reserve_visit_year);
        reserve_visit_month = (TextView)viewGroup.findViewById(R.id.reserve_visit_month);
        reserve_visit_day = (TextView)viewGroup.findViewById(R.id.reserve_visit_day);
        reserve_visit_hour = (TextView)viewGroup.findViewById(R.id.reserve_visit_hour);
        reserve_visit_minute = (TextView)viewGroup.findViewById(R.id.reserve_visit_minute);
        check_reserveTime = (Button)viewGroup.findViewById(R.id.check_reserveTime);
        reserveTime = (LinearLayout)viewGroup.findViewById(R.id.reserveTime);


        reserveTime.setVisibility(View.INVISIBLE);
        */
        /*
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
         */
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfmonth) {

                selectYear = year;
                selectMonth = month + 1;
                selectDay = dayOfmonth;

                refreshSelectedTime();
            }
        });

        time_picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                selectHour = hour;
                selectMinute = minute;

                refreshSelectedTime();
            }
        });



        return viewGroup;
    }

    private void refreshSelectedTime()
    {
        /*
        reserveTime.setVisibility(View.VISIBLE);

        reserve_visit_year.setText(Integer.toString(selectYear));
        reserve_visit_month.setText(Integer.toString(selectMonth));
        reserve_visit_day.setText(Integer.toString(selectDay));
        reserve_visit_hour.setText(Integer.toString(selectHour));
        reserve_visit_minute.setText(Integer.toString(selectMinute));

        /*
        reserve_content = reserve_visit_year.getText().toString()+"년"+reserve_visit_month.getText().toString()+"월"
                +reserve_visit_day.getText().toString()+"일"+reserve_visit_hour.getText().toString()+"시"
                +reserve_visit_minute.getText().toString()+"분";
         */

        StringBuilder stringBuilder_calender = new StringBuilder();
        stringBuilder_calender.append(selectYear);
        stringBuilder_calender.append("-");
        stringBuilder_calender.append(selectMonth);
        stringBuilder_calender.append("-");
        stringBuilder_calender.append(selectDay);
        stringBuilder_calender.append(" ");

        selectedCal = stringBuilder_calender.toString();

        StringBuilder stringBuilder_picker = new StringBuilder();
        stringBuilder_picker.append(selectHour);
        stringBuilder_picker.append(":");
        stringBuilder_picker.append(selectMinute);

        selectedPic = stringBuilder_picker.toString();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(selectedCal);
        stringBuilder.append(selectedPic);

        reserve_content = stringBuilder.toString();

        textView_selected_time.setText(reserve_content);

    }

    public static Date getSelectedTime() {

        return DateString.StringToDate(reserve_content);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        savedDate = getSelectedTime();

            if(savedDate != null)
            {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(savedDate);

                selectYear = calendar.get(Calendar.YEAR);
                selectMonth = calendar.get(Calendar.MONTH);
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
