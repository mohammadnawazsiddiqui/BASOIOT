package com.example.nawaz.basoiot;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nawaz.basoiot.Models.Staff;
import com.google.firebase.database.DataSnapshot;

import java.util.List;

class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.Holder> {

    private final List<DataSnapshot> reportList;

    public ReportAdapter(List<DataSnapshot> reportList) {
        this.reportList = reportList;
    }

    @NonNull
    @Override
    public ReportAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_card, parent, false);

        return new ReportAdapter.Holder(view);
    }


    // @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        try {
            DataSnapshot staff = reportList.get(position);
            holder.tvName.setText(staff.getValue(String.class));
            holder.totalAttd.setText(staff.getValue(String.class));
            holder.reportDetails.setText(staff.getValue(String.class));
        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView totalAttd;
        TextView reportDetails;

        public Holder(View itemView) {
            super(itemView);
        }
    }
}
