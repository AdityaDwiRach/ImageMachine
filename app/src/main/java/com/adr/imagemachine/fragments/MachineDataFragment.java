package com.adr.imagemachine.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.adr.imagemachine.R;
import com.adr.imagemachine.adapters.MachineDataRVAdapter;
import com.adr.imagemachine.database.DatabaseRepo;
import com.adr.imagemachine.database.MachineDataDAO;
import com.adr.imagemachine.database.MachineDataEntity;
import com.adr.imagemachine.database.MachineDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.crypto.Mac;

/**
 * A simple {@link Fragment} subclass.
 */
public class MachineDataFragment extends Fragment {

    private Date pickedDate;

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
        final MachineDataRVAdapter machineDataRVAdapter = new MachineDataRVAdapter();
        RecyclerView.Adapter rvAdapter = machineDataRVAdapter;

        rv.setAdapter(rvAdapter);

        final MachineDataDAO machineDataDAO = MachineDatabase.getInstance(getContext()).machineDataDAO();

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
        alertDialog.setView(getLayoutInflater().inflate(R.layout.alert_dialog_add_machine_data, null))
                .setTitle("Add Machine Data")
                .setCancelable(true)
                .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO save to database
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
}
