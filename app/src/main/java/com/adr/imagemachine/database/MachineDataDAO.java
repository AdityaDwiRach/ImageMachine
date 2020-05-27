package com.adr.imagemachine.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MachineDataDAO {
    @Insert
    void insertMachineData(MachineDataEntity machineDataEntity);

    @Delete
    void deleteMachineData(MachineDataEntity machineDataEntity);

    @Query("SELECT * FROM machine_data_table")
    List<MachineDataEntity> getAllMachineData();

    @Query("SELECT * FROM machine_data_table WHERE qrNumber LIKE :searchedQR")
    MachineDataEntity searchMachineData(int searchedQR);

    @Update
    void updateMachineData(MachineDataEntity machineDataEntity);
}
