package com.adr.imagemachine.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adr.imagemachine.R;
import com.adr.imagemachine.database.MachineDataEntity;
import com.adr.imagemachine.database.MachineDatabase_Impl;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MachineDataRVAdapter extends RecyclerView.Adapter<MachineDataRVAdapter.ViewHolder> {

    private ArrayList<MachineDataEntity> dataList = new ArrayList<>();
    @NonNull
    @Override
    public MachineDataRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_machine_data, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MachineDataRVAdapter.ViewHolder holder, int position) {
        holder.name.setText(dataList.get(position).getMachineName());
        holder.type.setText(dataList.get(position).getMachineType());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        TextView name = itemView.findViewById(R.id.tv_name_machine_data);
        TextView type = itemView.findViewById(R.id.tv_type_machine_data);
    }

    public void setDataList(List<MachineDataEntity> dataList){
        this.dataList = new ArrayList<>(dataList);
    }
}
