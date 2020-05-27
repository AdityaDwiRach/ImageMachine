package com.adr.imagemachine.database;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

import kotlin.Unit;

public class DatabaseRepo {

    //TODO need test

    private MachineDataDAO machineDataDAO;

    public DatabaseRepo(Context context) {
        MachineDatabase machineDatabase = MachineDatabase.getInstance(context);
        this.machineDataDAO = machineDatabase.machineDataDAO();
    }

    public void insert(MachineDataEntity machineDataEntity){
        InsertMachineDataAsyncTask insertMachineDataAsyncTask = new InsertMachineDataAsyncTask(machineDataDAO);
        insertMachineDataAsyncTask.execute(machineDataEntity);
    }

    public void delete(MachineDataEntity machineDataEntity){
        DeleteMachineDataAsyncTask deleteMachineDataAsyncTask = new DeleteMachineDataAsyncTask(machineDataDAO);
        deleteMachineDataAsyncTask.execute(machineDataEntity);
    }

    public List<MachineDataEntity> getAllHistory() {
        try {
            return new GetAllMachineDataAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class InsertMachineDataAsyncTask extends AsyncTask<MachineDataEntity, Unit, Unit>{
        MachineDataDAO machineDataDAO;

        InsertMachineDataAsyncTask(MachineDataDAO machineDataDAO) {
            this.machineDataDAO = machineDataDAO;
        }

        @Override
        protected Unit doInBackground(MachineDataEntity... machineDataEntities) {
            machineDataDAO.insertMachineData(machineDataEntities[0]);
            return null;
        }
    }

    private static class DeleteMachineDataAsyncTask extends AsyncTask<MachineDataEntity, Unit, Unit>{
        MachineDataDAO machineDataDAO;

        DeleteMachineDataAsyncTask(MachineDataDAO machineDataDAO) {
            this.machineDataDAO = machineDataDAO;
        }

        @Override
        protected Unit doInBackground(MachineDataEntity... machineDataEntities) {
            machineDataDAO.deleteMachineData(machineDataEntities[0]);
            return null;
        }
    }

    private static class GetAllMachineDataAsyncTask extends AsyncTask<Void, Void, List<MachineDataEntity>>{

        @Override
        protected List<MachineDataEntity> doInBackground(Void... voids) {
            return null;
        }
    }


}
