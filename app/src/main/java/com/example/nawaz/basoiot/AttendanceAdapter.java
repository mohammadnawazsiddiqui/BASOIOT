package com.example.nawaz.basoiot;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nawaz.basoiot.Models.Staff;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.MyViewHolder>{


    private final List<DataSnapshot> staffArrayList;

    public AttendanceAdapter(List<DataSnapshot> staffArrayList){
        this.staffArrayList = staffArrayList;
    }


    //Ctrl + O


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendancedata, parent, false);

        return new MyViewHolder(view);
    }



    // @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        DataSnapshot staff = staffArrayList.get(position);
        getName(staff.getKey(),holder.tvName);
        holder.tvId.setText(staff.getKey());
        holder.tvTime.setText(staff.child("intime").getValue(String.class));


    }

    private void getName(final String key, final TextView tvName) {
        tvName.setText("Loading...");
        FirebaseDatabase.getInstance().getReference("Staff").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.getKey().equals(key)){
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


    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView tvName;
        TextView tvId;
        TextView tvTime;

        public MyViewHolder(View itemView) {
            super(itemView);
              tvName = itemView.findViewById(R.id.tvName);
              tvId = itemView.findViewById(R.id.tvId);
              tvTime = itemView.findViewById(R.id.tvTime);

        }
    }

    @Override
    public int getItemCount() {
        return staffArrayList.size();
    }
}



