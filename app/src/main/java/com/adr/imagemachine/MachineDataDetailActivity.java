package com.adr.imagemachine;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.adr.imagemachine.adapters.MachineDataDetailRVAdapter;
import com.adr.imagemachine.adapters.MachineDataRVAdapter;
import com.adr.imagemachine.converter.BitmapConverter;
import com.adr.imagemachine.database.MachineDataEntity;
import com.adr.imagemachine.database.MachineDatabase;
import com.adr.imagemachine.fragments.MachineDataFragment;

import org.w3c.dom.Text;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static android.icu.text.DateTimePatternGenerator.PatternInfo.OK;

public class MachineDataDetailActivity extends AppCompatActivity {

    private final int GALLERY_REQUEST = 100;
    private MachineDataDetailRVAdapter machineDataDetailRVAdapter;

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

            textViewID.setText(String.valueOf(listData.get(position).getMachineId()));
            textViewName.setText(listData.get(position).getMachineName());
            textViewType.setText(listData.get(position).getMachineType());
            textViewQRNumber.setText(String.valueOf(listData.get(position).getQrNumber()));

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            textViewLastMaintain.setText(dateFormat.format(listData.get(position).getLastMaintainDate()));

            machineDataDetailRVAdapter.setDataList(listData.get(position).getMachineImage());
        } else {
            Log.d("Testing", "Data Null");
        }
        rv.setAdapter(machineDataDetailRVAdapter);

        buttonMachineImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //TODO choose image in gallery
                chooseFromGallery();
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

            }
        });

        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                List<byte[]> listByteArray = new ArrayList<>();
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        Uri uri = (clipData.getItemAt(i).getUri());
                        Bitmap bitmap = getBitmapFromUri(uri);
                        listByteArray.add(new BitmapConverter().bitmaptoByteArray(bitmap));
                    }
                }

                if (listByteArray != null && listByteArray.size() > 10){
                    Toast.makeText(this, "You have choose photo more than 10 photos.", Toast.LENGTH_SHORT).show();
                } else {
                    machineDataDetailRVAdapter.setDataList(listByteArray);
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

//    private void alertDialogAddData(){
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
//        final View layoutInflater = getLayoutInflater().inflate(R.layout.alert_dialog_add_machine_data, null);
//        alertDialog.setView(layoutInflater)
//                .setTitle("Add Machine Data")
//                .setCancelable(true)
//                .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //TODO save to database
//                        EditText etName = layoutInflater.findViewById(R.id.et_name);
//                        EditText etType = layoutInflater.findViewById(R.id.et_type);
//                        EditText etQRNumber = layoutInflater.findViewById(R.id.et_qr_number);
//
//                        machineName = etName.getText().toString();
//                        machineType = etType.getText().toString();
//                        machineQRNumber = Integer.parseInt(etQRNumber.getText().toString());
//
//                        MachineDataEntity newData = new MachineDataEntity(machineName, machineType, machineQRNumber, pickedDate, null);
//                        new InsertData(newData, getContext()).execute();
//
//                        List<MachineDataEntity> listData = new MachineDataFragment.GetAllData(getContext()).getAllData();
//                        if (listData != null){
//                            machineDataRVAdapter.setDataList(listData);
//                        } else {
//                            Log.d("Testing", "Data Null");
//                        }
//                    }
//                })
//                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        AlertDialog dialog = alertDialog.create();
//        dialog.show();
//        Button pickDate = dialog.findViewById(R.id.btn_pick_date);
//        pickDate.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            public void onClick(View v) {
//                alertDialogDatePicker();
//            }
//        });
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void alertDialogDatePicker(){
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
//        alertDialog.setView(getLayoutInflater().inflate(R.layout.alert_dialog_pick_date, null))
//                .setTitle("Please choose a date")
//                .setCancelable(false)
//                .setPositiveButton("PICK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //TODO assert ke public variable
//                        //TODO nggak perlu di display
//                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
//
//                        Log.d("Testing", dateFormat.format(pickedDate));
//                        dialog.dismiss();
//                    }
//                });
//
//        AlertDialog dialog = alertDialog.create();
//        dialog.show();
//
//        DatePicker datePicker = dialog.findViewById(R.id.date_picker);
//        //TODO handle date not change
//        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
//            @Override
//            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                Calendar cal = Calendar.getInstance();
//                cal.set(year, monthOfYear, dayOfMonth);
//                pickedDate = cal.getTime();
//            }
//        });
//    }

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
                Log.d("Testing", "data deleted");
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
                Log.d("Testing", "data inserted");
            }
        }
    }
}
