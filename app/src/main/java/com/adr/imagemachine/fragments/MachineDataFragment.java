package com.adr.imagemachine.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adr.imagemachine.R;
import com.adr.imagemachine.adapters.MachineDataRVAdapter;
import com.adr.imagemachine.database.DatabaseRepo;
import com.adr.imagemachine.database.MachineDataDAO;
import com.adr.imagemachine.database.MachineDataEntity;
import com.adr.imagemachine.database.MachineDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import javax.crypto.Mac;

/**
 * A simple {@link Fragment} subclass.
 */
public class MachineDataFragment extends Fragment {

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
                })
                .show();
    }
}
