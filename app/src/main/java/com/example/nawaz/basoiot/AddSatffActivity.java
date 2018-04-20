package com.example.nawaz.basoiot;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nawaz.basoiot.Models.Staff;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddSatffActivity extends AppCompatActivity {


    private EditText name;
    private EditText email;
    private EditText department;
    private EditText DOJ;
    private EditText subject;
    private EditText mobile;
    private EditText address;
    private TextView status;
    private ProgressDialog mProgressDialog;
    private GoogleApiClient mGoogleApiClient;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_satff);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        name = findViewById(R.id.editTextName);
        email = findViewById(R.id.editTextEmail);
        department = findViewById(R.id.editTextDepartment);
        DOJ = findViewById(R.id.editTextDOJ);
        subject = findViewById(R.id.editTextSubject);
        mobile = findViewById(R.id.editTextMobile);
        address = findViewById(R.id.editTextAddress);
        status = findViewById(R.id.textViewmessage);
        status.setVisibility(View.GONE);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_addstaff, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();
        switch (item.getItemId()) {
            case R.id.saveoption_settings:
                String staffname = this.name.getText().toString();
                String staffemail = this.email.getText().toString();
                String staffDOJ = DOJ.getText().toString();
                String staffdepatment = this.department.getText().toString();
                String staffsubject = this.subject.getText().toString();
                String satffmobile = this.mobile.getText().toString();
                String staffaddress = this.address.getText().toString();
                Staff staff = new Staff(staffname, staffemail, staffdepatment, staffDOJ, staffsubject, satffmobile, staffaddress);
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setTitle("Saving details...");
                mProgressDialog.setMessage("Almost done!");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();
                db.getReference().child("Staff").child(uid).push().setValue(staff).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        hideDialog();
                        if (task.isSuccessful()) {
                            name.setText("");
                            email.setText("");
                            department.setText("");
                            DOJ.setText("");
                            subject.setText("");
                            mobile.setText("");
                            address.setText("");
                        }
                        else{
                            Toast.makeText(AddSatffActivity.this, "Failed Text", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
                Intent intent =new Intent(AddSatffActivity.this, StaffActivity.class);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    private void hideDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


}


