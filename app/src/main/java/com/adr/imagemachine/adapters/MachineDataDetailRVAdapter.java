package com.adr.imagemachine.adapters;

import android.graphics.Bitmap;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adr.imagemachine.R;
import com.adr.imagemachine.converter.BitmapConverter;

import java.util.List;

public class MachineDataDetailRVAdapter extends RecyclerView.Adapter<MachineDataDetailRVAdapter.ViewHolder> {

    private List<byte[]> dataList;

    @NonNull
    @Override
    public MachineDataDetailRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_image_machine_data_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MachineDataDetailRVAdapter.ViewHolder holder, int position) {
        Bitmap bitmap = new BitmapConverter().byteArraytoBitmap(dataList.get(position));
        holder.imageViewThumbnail.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        if (dataList != null) {
            return dataList.size();
        } else {
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        ImageView imageViewThumbnail = itemView.findViewById(R.id.iv_image_machine);
    }

    public void setDataList(List<byte[]> dataList){
        this.dataList = dataList;
        notifyDataSetChanged();
    }
}
