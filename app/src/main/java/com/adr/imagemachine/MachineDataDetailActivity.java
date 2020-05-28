package com.adr.imagemachine;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adr.imagemachine.adapters.MachineDataDetailRVAdapter;
import com.adr.imagemachine.converter.BitmapConverter;
import com.adr.imagemachine.database.MachineDataEntity;
import com.adr.imagemachine.database.MachineDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MachineDataDetailActivity extends AppCompatActivity {

    private final int GALLERY_REQUEST = 100;
    private MachineDataDetailRVAdapter machineDataDetailRVAdapter;
    private int machineID;
    private String machineName;
    private String machineType;
    private int machineQRNumber;
    private Date lastMaintain;
    private List<String> imageMachine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_data_detail);

        Button buttonMachineImage = findViewById(R.id.btn_image_machine_data_detail);
        Button buttonEdit = findViewById(R.id.btn_edit_machine_image_detail);
        Button buttonRemove = findViewById(R.id.btn_remove_machine_image_detail);
        TextView textViewBack = findViewById(R.id.tv_machine_data_detail);
        TextView textViewID = findViewById(R.id.tv_id_machine_data_detail);
        TextView textViewName = findViewById(R.id.tv_name_machine_data_detail);
        TextView textViewType = findViewById(R.id.tv_type_machine_data_detail);
        TextView textViewQRNumber = findViewById(R.id.tv_qr_number_machine_data_detail);
        TextView textViewLastMaintain = findViewById(R.id.tv_date_machine_data_detail);

        RecyclerView rv = findViewById(R.id.rv_machine_data_detail);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rv.setLayoutManager(linearLayoutManager);

        machineDataDetailRVAdapter = new MachineDataDetailRVAdapter();
        int position = getIntent().getIntExtra("dataposition", 0);
        List<MachineDataEntity> listData = new MachineDataDetailActivity.GetAllData(this).getAllData();

        if (position > -1 && listData != null){

            machineID = listData.get(position).getMachineId();
            machineName = listData.get(position).getMachineName();
            machineType = listData.get(position).getMachineType();
            machineQRNumber = listData.get(position).getQrNumber();
            lastMaintain = listData.get(position).getLastMaintainDate();
            imageMachine = listData.get(position).getMachineImage();

            textViewID.setText(String.valueOf(machineID));
            textViewName.setText(machineName);
            textViewType.setText(machineType);
            textViewQRNumber.setText(String.valueOf(machineQRNumber));

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            textViewLastMaintain.setText(dateFormat.format(lastMaintain));

            if (imageMachine != null){
                machineDataDetailRVAdapter.setDataList(imageMachine);
            }
        } else {
            Log.d("Testing", "Data Null");
        }

        rv.setAdapter(machineDataDetailRVAdapter);

        buttonMachineImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFromGallery();
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                alertDialogAddData();
            }
        });

        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MachineDataEntity deletedData = new MachineDataEntity(machineName, machineType, machineQRNumber, lastMaintain, imageMachine);
                deletedData.setMachineId(machineID);
                new DeleteData(deletedData, getApplicationContext()).execute();
                finish();
            }
        });

        textViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void chooseFromGallery() {
        Intent intent = new Intent().setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            if (data != null){
                List<String> listImagePath = new ArrayList<>();
                int totalImage;
                if (imageMachine != null) {
                    totalImage = imageMachine.size();
                } else {
                    totalImage = 0;
                }

                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        Uri uri = (clipData.getItemAt(i).getUri());
                        Bitmap bitmap = getBitmapFromUri(uri);
                        File file = saveBitmap(bitmap, String.valueOf(totalImage + i), getApplicationContext());
                        listImagePath.add((Uri.fromFile(file).toString()));
                    }
                }

                if (listImagePath != null && listImagePath.size() > 10){
                    Toast.makeText(this, "You have choose photo more than 10 photos.", Toast.LENGTH_SHORT).show();
                } else {
                    if (imageMachine == null) {
                        imageMachine = listImagePath;
                    } else {
                        imageMachine.addAll(listImagePath);
                    }
                    MachineDataEntity updateData = new MachineDataEntity(machineName, machineType, machineQRNumber, lastMaintain, imageMachine);
                    updateData.setMachineId(machineID);
                    new UpdateData(getApplicationContext(), updateData).execute();

                    machineDataDetailRVAdapter.setDataList(imageMachine);
                }
            } else {
                Toast.makeText(this, "You haven't choose photo.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "You haven't choose photo.", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap getBitmapFromUri(Uri uri){
        ParcelFileDescriptor parcelFileDescriptor = null;
        Bitmap bitmap = null;
        try {
            parcelFileDescriptor = this.getContentResolver().openFileDescriptor(uri, "r");
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

    private void alertDialogAddData(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        final View layoutInflater = getLayoutInflater().inflate(R.layout.alert_dialog_add_machine_data, null);

        final EditText etName = layoutInflater.findViewById(R.id.et_name);
        final EditText etType = layoutInflater.findViewById(R.id.et_type);
        final EditText etQRNumber = layoutInflater.findViewById(R.id.et_qr_number);

        etName.setText(machineName);
        etType.setText(machineType);
        etQRNumber.setText(String.valueOf(machineQRNumber));

        alertDialog.setView(layoutInflater)
                .setTitle("Add Machine Data")
                .setCancelable(true)
                .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO save to database

                        machineName = etName.getText().toString();
                        machineType = etType.getText().toString();
                        machineQRNumber = Integer.parseInt(etQRNumber.getText().toString());

                        MachineDataEntity updateData = new MachineDataEntity(machineName, machineType, machineQRNumber, lastMaintain, imageMachine);
                        updateData.setMachineId(machineID);
                        new MachineDataDetailActivity.UpdateData(getApplicationContext(), updateData).execute();

                        finish();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = alertDialog.create();
        dialog.show();
        Button pickDate = dialog.findViewById(R.id.btn_pick_date);
        pickDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                alertDialogDatePicker();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void alertDialogDatePicker(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(getLayoutInflater().inflate(R.layout.alert_dialog_pick_date, null))
                .setTitle("Please choose a date")
                .setCancelable(false)
                .setPositiveButton("PICK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO assert ke public variable
                        //TODO nggak perlu di display
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

                        Log.d("Testing", dateFormat.format(lastMaintain));
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = alertDialog.create();
        dialog.show();

        DatePicker datePicker = dialog.findViewById(R.id.date_picker);
        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(year, monthOfYear, dayOfMonth);
                lastMaintain = cal.getTime();
            }
        });
    }

    private File saveBitmap(Bitmap bmp, String index, Context context) {
        String machineId = String.valueOf(machineID);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
        File f = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                + File.separator + "id_" +  machineId + "_" + index + ".jpg");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return f;
    }

    private static class DeleteData extends AsyncTask<Void, Void, Boolean> {

        private MachineDataEntity machineDataEntity;
        private Context context;

        DeleteData(MachineDataEntity machineDataEntity, Context context) {
            this.machineDataEntity = machineDataEntity;
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            MachineDatabase.getInstance(context).machineDataDAO().deleteMachineData(machineDataEntity);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean){
                Log.d("Testingdelete", "data deleted");
            } else {
                Log.d("Testingdelete", "something wrong");
            }
        }
    }

    private static class GetAllData extends AsyncTask<Void, Void, List<MachineDataEntity>>{

        private Context context;

        GetAllData(Context context) {
            this.context = context;
        }

        @Override
        protected List<MachineDataEntity> doInBackground(Void... voids) {
            return MachineDatabase.getInstance(context).machineDataDAO().getAllMachineData();
        }

        private List<MachineDataEntity> getAllData(){
            try {
                return new MachineDataDetailActivity.GetAllData(context).execute().get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private static class UpdateData extends AsyncTask<Void, Void, Boolean>{

        private Context context;
        private MachineDataEntity machineDataEntity;

        UpdateData(Context context, MachineDataEntity machineDataEntity) {
            this.context = context;
            this.machineDataEntity = machineDataEntity;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            MachineDatabase.getInstance(context).machineDataDAO().updateMachineData(machineDataEntity);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean){
                Log.d("Testing", "data updated");
            } else {
                Log.d("Testing", "something wrong");

            }
        }
    }
}
