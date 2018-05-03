package com.example.nawaz.basoiot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        final RecyclerView rvReport = findViewById(R.id.rvReport);
        rvReport.setLayoutManager(new LinearLayoutManager(this));

        String month = getcurrentdate();
        FirebaseDatabase.getInstance().getReference("Attendance/" + month).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<DataSnapshot> reports= new ArrayList<>();
                if (dataSnapshot.hasChildren()) {
                    long count = dataSnapshot.getChildrenCount();
                    if (count > 20) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            reports.add(snapshot);
                        }
                        final ReportAdapter adapter = new ReportAdapter(reports);
                        rvReport.setAdapter(adapter);

                    }else{
                        Toast.makeText(ReportActivity.this, "you have not collected that much data for the month", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ReportActivity.this, "no data yet", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public String getcurrentdate() {
        DateFormat df = new SimpleDateFormat("yyyy/MMMM");
        String date = df.format(Calendar.getInstance().getTime());
        Log.d("Iee", date);
        return date;
    }
}
