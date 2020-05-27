package com.adr.imagemachine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.adr.imagemachine.database.MachineDataEntity;
import com.adr.imagemachine.database.MachineDatabase;
import com.adr.imagemachine.fragments.MachineDataFragment;
import com.google.zxing.Result;

import java.util.List;
import java.util.concurrent.ExecutionException;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView scannerView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scannerView = new ZXingScannerView(this);

        setContentView(scannerView);
    }

    @Override
    public void onResume() {
        super.onResume();

        scannerView.setResultHandler(this);
        scannerView.startCamera();
        scannerView.setAutoFocus(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        //TODO search in database
        MachineDataEntity searchData = new SearchData(this, Integer.parseInt(result.getText())).searchData();
        if (searchData != null){
            List<MachineDataEntity> listData = new ScannerActivity.GetAllData(this).getAllData();
            int position = 0;

            if (listData != null) {
                for (MachineDataEntity machineDataEntity: listData){
                    if (machineDataEntity == searchData){
                        position = listData.indexOf(machineDataEntity);
                        break;
                    }
                }

                Intent intent = new Intent(this, MachineDataDetailActivity.class);
                intent.putExtra("dataposition", position);
                startActivity(intent);
            } else {
                Toast.makeText(this, "No data in database.", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Data not found.", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    private static class SearchData extends AsyncTask<Void, Void, MachineDataEntity> {

        private Context context;
        private int qrNumber;

        SearchData(Context context, int qrNumber) {
            this.context = context;
            this.qrNumber = qrNumber;
        }

        @Override
        protected MachineDataEntity doInBackground(Void... voids) {
            return MachineDatabase.getInstance(context).machineDataDAO().searchMachineData(qrNumber);
        }

        private MachineDataEntity searchData(){
            try {
                return new ScannerActivity.SearchData(context, qrNumber).execute().get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            return null;
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
                return new ScannerActivity.GetAllData(context).execute().get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
