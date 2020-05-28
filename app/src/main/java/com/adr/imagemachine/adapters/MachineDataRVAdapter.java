package com.adr.imagemachine.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adr.imagemachine.MachineDataDetailActivity;
import com.adr.imagemachine.R;
import com.adr.imagemachine.database.MachineDataEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MachineDataRVAdapter extends RecyclerView.Adapter<MachineDataRVAdapter.ViewHolder> {

    private ArrayList<MachineDataEntity> dataList;
    private Context context;

    @NonNull
    @Override
    public MachineDataRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_machine_data, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MachineDataRVAdapter.ViewHolder holder, final int position) {
        holder.name.setText(dataList.get(position).getMachineName());
        holder.type.setText(dataList.get(position).getMachineType());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MachineDataDetailActivity.class);
                intent.putExtra("dataposition", position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        TextView name = itemView.findViewById(R.id.tv_name_machine_data);
        TextView type = itemView.findViewById(R.id.tv_type_machine_data);
    }

    public void setDataList(List<MachineDataEntity> dataList){
        this.dataList = new ArrayList<>(dataList);
        notifyDataSetChanged();
    }
}
