package com.adr.imagemachine.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.adr.imagemachine.R;
import com.adr.imagemachine.adapters.MachineDataRVAdapter;
import com.adr.imagemachine.database.MachineDataDAO;
import com.adr.imagemachine.database.MachineDataEntity;
import com.adr.imagemachine.database.MachineDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 */
public class MachineDataFragment extends Fragment {

    private Date pickedDate;
    private String machineName;
    private String machineType;
    private int machineQRNumber;
    private MachineDataRVAdapter machineDataRVAdapter;

    public static MachineDataFragment newInstance() {
        return new MachineDataFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_machine_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rv = view.findViewById(R.id.rv_machine_data);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rv.setLayoutManager(linearLayoutManager);

        machineDataRVAdapter = new MachineDataRVAdapter();
        List<MachineDataEntity> listData = new GetAllData(getContext()).getAllData();
        if (listData != null){
            machineDataRVAdapter.setDataList(listData);
        } else {
            Log.d("Testing", "Data Null");
        }
        rv.setAdapter(machineDataRVAdapter);

        FloatingActionButton fab = view.findViewById(R.id.fab_machine_data);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogAddData();
            }
        });
    }

    private void alertDialogAddData(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        final View layoutInflater = getLayoutInflater().inflate(R.layout.alert_dialog_add_machine_data, null);
        alertDialog.setView(layoutInflater)
                .setTitle("Add Machine Data")
                .setCancelable(true)
                .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO save to database
                        EditText etName = layoutInflater.findViewById(R.id.et_name);
                        EditText etType = layoutInflater.findViewById(R.id.et_type);
                        EditText etQRNumber = layoutInflater.findViewById(R.id.et_qr_number);

                        machineName = etName.getText().toString();
                        machineType = etType.getText().toString();
                        machineQRNumber = Integer.parseInt(etQRNumber.getText().toString());

                        MachineDataEntity newData = new MachineDataEntity(machineName, machineType, machineQRNumber, pickedDate, null);
                        new InsertData(newData, getContext()).execute();

                        List<MachineDataEntity> listData = new GetAllData(getContext()).getAllData();
                        if (listData != null){
                            machineDataRVAdapter.setDataList(listData);
                        } else {
                            Log.d("Testing", "Data Null");
                        }
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
                //TODO show another custom alert dialog (datepicker)
                alertDialogDatePicker();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        List<MachineDataEntity> listData = new GetAllData(getContext()).getAllData();
        if (listData != null){
            machineDataRVAdapter.setDataList(listData);
        } else {
            Log.d("Testing", "Data Null");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void alertDialogDatePicker(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setView(getLayoutInflater().inflate(R.layout.alert_dialog_pick_date, null))
                .setTitle("Please choose a date")
                .setCancelable(false)
                .setPositiveButton("PICK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO assert ke public variable
                        //TODO nggak perlu di display
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

                        Log.d("Testing", dateFormat.format(pickedDate));
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = alertDialog.create();
        dialog.show();

        DatePicker datePicker = dialog.findViewById(R.id.date_picker);
        //TODO handle date not change
        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(year, monthOfYear, dayOfMonth);
                pickedDate = cal.getTime();
            }
        });
    }

    private static class InsertData extends AsyncTask<Void, Void, Boolean>{

        private MachineDataEntity machineDataEntity;
        private Context context;

        InsertData(MachineDataEntity machineDataEntity, Context context) {
            this.machineDataEntity = machineDataEntity;
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            MachineDatabase.getInstance(context).machineDataDAO().insertMachineData(machineDataEntity);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean){
                Log.d("Testing", "data inserted");
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
                return new GetAllData(context).execute().get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
