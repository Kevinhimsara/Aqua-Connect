package com.kevin.aquaconnect;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {
    private List<ServiceRequest> list;

    public RequestAdapter(List<ServiceRequest> list) {
        this.list = list;
    }

    public void setList(List<ServiceRequest> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceRequest req = list.get(position);

        if (req != null) {
            holder.type.setText(req.getServiceType());
            holder.address.setText(req.getAddress());
            holder.urgency.setText("Urgency: " + req.getUrgency());

            // Handle Status and Colors
            String status = req.getStatus() != null ? req.getStatus() : "Pending";
            holder.status.setText(status);

            if (status.equalsIgnoreCase("Completed")) {
                holder.status.setTextColor(android.graphics.Color.GREEN);
            } else if (status.equalsIgnoreCase("Processing")) {
                holder.status.setTextColor(android.graphics.Color.BLUE);
            } else {
                holder.status.setTextColor(android.graphics.Color.GRAY);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView type, address, urgency, status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.tvHistoryServiceType);
            address = itemView.findViewById(R.id.tvHistoryAddress);
            urgency = itemView.findViewById(R.id.tvHistoryUrgency);
            status = itemView.findViewById(R.id.tvHistoryStatus);
        }
    }
}