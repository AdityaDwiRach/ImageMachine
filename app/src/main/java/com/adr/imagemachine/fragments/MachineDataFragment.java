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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.adr.imagemachine.R;
import com.adr.imagemachine.adapters.MachineDataRVAdapter;
import com.adr.imagemachine.database.DatabaseRepo;
import com.adr.imagemachine.database.MachineDataDAO;
import com.adr.imagemachine.database.MachineDataEntity;
import com.adr.imagemachine.database.MachineDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static java.util.Collections.sort;

/**
 * A simple {@link Fragment} subclass.
 */
public class MachineDataFragment extends Fragment {

    private Date pickedDate;
    private String machineName;
    private String machineType;
    private int machineQRNumber;
    private MachineDataRVAdapter machineDataRVAdapter;
    private List<MachineDataEntity> listData;

    public static MachineDataFragment newInstance() {
        return new MachineDataFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_sort_name: {
                if (listData != null){
                    machineDataRVAdapter.setDataList(sortByName(listData));
                } else {
                    Log.e("MachineDataFragment", "Data Null");
                }
                break;
            }
            case R.id.item_sort_type: {
                if (listData != null){
                    List<MachineDataEntity> listSorted = new ArrayList<>(listData);
                    Collections.sort(listSorted, new Comparator<MachineDataEntity>() {
                        @Override
                        public int compare(MachineDataEntity a1, MachineDataEntity a2) {
                            return a1.getMachineType().compareTo(a2.getMachineType());
                        }
                    });
                    machineDataRVAdapter.setDataList(listSorted);
                } else {
                    Log.e("MachineDataFragment", "Data Null");
                }
                break;
            }
        }
        return true;
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
        listData = new DatabaseRepo.GetAllData(getContext()).getAllData();
        if (listData != null){
            machineDataRVAdapter.setDataList(listData);
        } else {
            Log.e("MachineDataFragment", "Data Null");
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
                        new DatabaseRepo.InsertData(newData, getContext()).execute();

                        listData = new DatabaseRepo.GetAllData(getContext()).getAllData();
                        if (listData != null){
                            machineDataRVAdapter.setDataList(sortByName(listData));
                        } else {
                            Log.e("MachineDataFragment", "Data Null");
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
                alertDialogDatePicker();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        listData = new DatabaseRepo.GetAllData(getContext()).getAllData();
        if (listData != null){
            machineDataRVAdapter.setDataList(sortByName(listData));
        } else {
            Log.e("MachineDataFragment", "Data Null");
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
                pickedDate = cal.getTime();
            }
        });
    }

    private List<MachineDataEntity> sortByName(List<MachineDataEntity> listData){
        List<MachineDataEntity> listSorted = new ArrayList<>(listData);
        Collections.sort(listSorted, new Comparator<MachineDataEntity>() {
            @Override
            public int compare(MachineDataEntity a1, MachineDataEntity a2) {
                return a1.getMachineName().compareTo(a2.getMachineName());
            }
        });
        return listSorted;
    }
}
