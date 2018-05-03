package com.example.nawaz.basoiot;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nawaz.basoiot.Models.Staff;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView txtcurrentDate = findViewById(R.id.txtcurrentDate);
        final RecyclerView rvAdminDash = findViewById(R.id.rvAdminDash);
        rvAdminDash.setLayoutManager(new LinearLayoutManager(this));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String email = user.getEmail();

        String getcurrentdate = getcurrentdate();
        String sol = "Attendance/" + getcurrentdate;
        txtcurrentDate.setText(getcurrentdate);
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

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        View headerView = navigationView.getHeaderView(0);
        TextView tvHeaderName = headerView.findViewById(R.id.tvName);
        TextView tvHeaderEmail = headerView.findViewById(R.id.tvEmail);
        ImageView iv = headerView.findViewById(R.id.ivImage);
        tvHeaderEmail.setText(email);
        getName(email, tvHeaderName);


    }


    public void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuth.signOut();
                startActivity(new Intent(HomeActivity.this, SignInActivity.class));
                finish();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (email.equalsIgnoreCase("nawazpunna@gmail.com")) {
            isAdmin = true;
        }
        if (id == R.id.nav_dashboard) {
            if (isAdmin) {

            }
        } else if (id == R.id.nav_attendance) {

            Intent intent = new Intent(HomeActivity.this, AttendanceActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_reports) {
            Intent intent = new Intent(HomeActivity.this, ReportActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_managestaff) {

            if (isAdmin) {
                Intent intent = new Intent(HomeActivity.this, StaffActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.nav_logout) {

            FirebaseAuth instance = FirebaseAuth.getInstance();
            instance.signOut();
            Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
            startActivity(intent);

            return true;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return false;
    }

    public void findstaffbyid(final String id) {
        final List<Staff> staffmembers = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("staff").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    staffmembers.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Staff staff = snapshot.getValue(Staff.class);
                        if (id.equalsIgnoreCase(snapshot.getKey())) {
                            staffmembers.add(staff);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void getName(final String email, final TextView tvName) {
        tvName.setText("Loading...");
        FirebaseDatabase.getInstance().getReference("Staff").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.child("email").getValue(String.class).equals(email)) {
                            tvName.setText(snapshot.child("name").getValue(String.class));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public String getcurrentdate() {
        DateFormat df = new SimpleDateFormat("yyyy/MMMM/d");
        String date = df.format(Calendar.getInstance().getTime());
        Log.d("Iee", date);
        return date;
    }


}
