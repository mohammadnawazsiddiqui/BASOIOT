package com.example.nawaz.basoiot;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AttendanceActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private TextView tvDate;

    String[] months = new String[]{
      "January",
      "Febuary",
      "March",
      "April",
      "June",
      "July",
      "August",
      "September",
      "November",
      "December"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        calendarView = findViewById(R.id.cvSelect);
        tvDate = findViewById(R.id.tvDate);
        final RecyclerView rvAdminDash = findViewById(R.id.rvDateWise);
        rvAdminDash.setLayoutManager(new LinearLayoutManager(this));

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Toast.makeText(AttendanceActivity.this, "data changes", Toast.LENGTH_SHORT).show();
                int monthvalue = month ;
                tvDate.setText(year + "/" + months[monthvalue] + "/" + dayOfMonth);
                String date = tvDate.getText().toString();
                String sol = "Attendance/" + date;
                Log.d("Iee",sol);
                FirebaseDatabase.getInstance().getReference(sol).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<DataSnapshot> data = new ArrayList<>();
                        if (dataSnapshot.hasChildren()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                data.add(snapshot);

                            }
                            AttendanceAdapter adapter = new AttendanceAdapter(data);
                            rvAdminDash.setAdapter(adapter);

                        }else{
                            Toast.makeText(AttendanceActivity.this, "No data for that date", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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
    public String getcurrentdate() {
        DateFormat df = new SimpleDateFormat("yyyy/MMMM/d");
        String date = df.format(Calendar.getInstance().getTime());
        Log.d("Iee",date);
        return date;
    }
}

