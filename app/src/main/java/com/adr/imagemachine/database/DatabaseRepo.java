package com.adr.imagemachine.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DatabaseRepo {

    public static class InsertData extends AsyncTask<Void, Void, Boolean>{

        private MachineDataEntity machineDataEntity;
        private Context context;

        public InsertData(MachineDataEntity machineDataEntity, Context context) {
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
                Toast.makeText(context, "Data successfully inserted.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static class DeleteData extends AsyncTask<Void, Void, Boolean> {

        private MachineDataEntity machineDataEntity;
        private Context context;

        public DeleteData(MachineDataEntity machineDataEntity, Context context) {
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
                Toast.makeText(context, "Data successfully deleted.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static class GetAllData extends AsyncTask<Void, Void, List<MachineDataEntity>>{

        private Context context;

        public GetAllData(Context context) {
            this.context = context;
        }

        @Override
        protected List<MachineDataEntity> doInBackground(Void... voids) {
            return MachineDatabase.getInstance(context).machineDataDAO().getAllMachineData();
        }

        public List<MachineDataEntity> getAllData(){
            try {
                return new GetAllData(context).execute().get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public static class UpdateData extends AsyncTask<Void, Void, Boolean> {

        private Context context;
        private MachineDataEntity machineDataEntity;

        public UpdateData(Context context, MachineDataEntity machineDataEntity) {
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
                Toast.makeText(context, "Data successfully updated.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static class SearchData extends AsyncTask<Void, Void, MachineDataEntity> {

        private Context context;
        private int qrNumber;

        public SearchData(Context context, int qrNumber) {
            this.context = context;
            this.qrNumber = qrNumber;
        }

        @Override
        protected MachineDataEntity doInBackground(Void... voids) {
            return MachineDatabase.getInstance(context).machineDataDAO().searchMachineData(qrNumber);
        }

        public MachineDataEntity searchData(){
            try {
                return new SearchData(context, qrNumber).execute().get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
