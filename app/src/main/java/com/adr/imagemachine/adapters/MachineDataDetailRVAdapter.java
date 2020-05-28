package com.adr.imagemachine.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adr.imagemachine.R;
import com.adr.imagemachine.database.DatabaseRepo;
import com.adr.imagemachine.database.MachineDataEntity;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MachineDataDetailRVAdapter extends RecyclerView.Adapter<MachineDataDetailRVAdapter.ViewHolder> {

    private List<String> dataList = new ArrayList<>();
    private MachineDataEntity currentMachineData;
    private Context context;
    private Activity activity;

    @NonNull
    @Override
    public MachineDataDetailRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        activity = (Activity) parent.getContext();
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_image_machine_data_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MachineDataDetailRVAdapter.ViewHolder holder, final int position) {
        Uri uri = Uri.parse(dataList.get(position));
        final Bitmap bitmap = getBitmapFromUri(uri);

        holder.imageViewThumbnail.setImageBitmap(bitmap);
        holder.imageViewThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogImagePreview(bitmap, position);
            }
        });
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

    public void setDataList(List<String> dataList){
        this.dataList = dataList;
        notifyDataSetChanged();
    }
    public void setMachineData(MachineDataEntity data){
        this.currentMachineData = data;
        notifyDataSetChanged();
    }

    private Bitmap getBitmapFromUri(Uri uri){
        ParcelFileDescriptor parcelFileDescriptor = null;
        Bitmap bitmap = null;
        try {
            parcelFileDescriptor = activity.getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor fileDescriptor = null;
            if (parcelFileDescriptor != null) {
                fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                parcelFileDescriptor.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private void alertDialogImagePreview(Bitmap bitmap, final int position){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        View view = View.inflate(context,R.layout.alert_dialog_image_preview, null);

        alertDialog.setView(view)
                .setCancelable(true);

        final AlertDialog dialog = alertDialog.create();
        dialog.show();

        ImageView image = dialog.findViewById(R.id.iv_image_preview);
        image.setImageBitmap(bitmap);

        Button buttonDeleteImage = dialog.findViewById(R.id.btn_delete_image);
        buttonDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataList.remove(position);
                MachineDataEntity updateMachineData = currentMachineData;
                updateMachineData.setMachineImage(dataList);
                new DatabaseRepo.UpdateData(context, updateMachineData).execute();
                dialog.dismiss();
                notifyDataSetChanged();
            }
        });
    }
}
