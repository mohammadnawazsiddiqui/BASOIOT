package com.example.nawaz.basoiot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nawaz.basoiot.Models.Staff;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{


    private final List<Staff> staffArrayList;

    public MyAdapter(List<Staff> staffArrayList){
        this.staffArrayList = staffArrayList;
    }


    //Ctrl + O


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.staff_item, parent, false);

        return new MyViewHolder(view);
    }



    // @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Staff staff = staffArrayList.get(position);
        holder.tvName.setText(staff.name);
        holder.tvDept.setText(staff.department);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView tvName;
        TextView tvDept;

        public MyViewHolder(View itemView) {
            super(itemView);
             tvName = itemView.findViewById(R.id.tvName);
             tvDept = itemView.findViewById(R.id.tvDept);
        }
    }

    @Override
    public int getItemCount() {
        return staffArrayList.size();
    }
}



