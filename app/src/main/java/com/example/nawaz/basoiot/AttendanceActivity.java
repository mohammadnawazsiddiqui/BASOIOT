package com.example.nawaz.basoiot;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class AttendanceActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private TextView textViewdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        calendarView = findViewById(R.id.calendarView);
        textViewdate = findViewById(R.id.textViewDate);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Toast.makeText(AttendanceActivity.this, "data changes", Toast.LENGTH_SHORT).show();
                int monthvalue = month + 1;
                textViewdate.setText(year + "-" + monthvalue + "-" + dayOfMonth);
            }
        });

    }


    private void openCalendar(Date date) {
        calendarView.setDate(date.getTime());
        calendarView.setVisibility(View.VISIBLE);
    }

    public void selectDate(View view) {
        Date date = new Date(calendarView.getDate());
        // do whatever you need with date
        dismissCalendar(view);
    }

    public void dismissCalendar(View view) {
        calendarView.setVisibility(View.INVISIBLE);
    }

}

