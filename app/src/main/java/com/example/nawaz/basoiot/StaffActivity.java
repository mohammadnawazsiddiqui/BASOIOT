package com.example.nawaz.basoiot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.nawaz.basoiot.Models.Staff;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StaffActivity extends AppCompatActivity {


    RecyclerView myrecyclerView;
    List<Staff> listData;
    FirebaseDatabase FDB;
    DatabaseReference DBR;

    private static final String TAG = "fetchingdata";
    private RecyclerView.Adapter adapter;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Staff");


        final RecyclerView myrecyclerView = findViewById(R.id.myrecyclerView);
        myrecyclerView.setLayoutManager(new LinearLayoutManager(this));


        listData = new ArrayList<>();


        FDB = FirebaseDatabase.getInstance();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DBR = FDB.getReference().child("Staff").child(uid);
        showDialog();
        DBR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    listData.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Staff staff = snapshot.getValue(Staff.class);
                        listData.add(staff);
                    }
                    hideDialog();
                    myrecyclerView.setAdapter(new MyAdapter(listData));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideDialog();
            }
        });
    }

    private void hideDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void showDialog() {
        dialog = new ProgressDialog(this);
        dialog.setTitle("loading staff");
        dialog.setMessage("wait...");
        dialog.show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_staff, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addoption_settings:
                //showOrder();

                Intent intent = new Intent(StaffActivity.this, AddSatffActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}