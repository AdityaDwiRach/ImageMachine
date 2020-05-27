package com.adr.imagemachine.database;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.adr.imagemachine.converter.ByteArrayConverter;
import com.adr.imagemachine.converter.DateConverter;


import java.util.Date;
import java.util.List;

@Entity(tableName = "machine_data_table")
public class MachineDataEntity implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int machineId;
    @ColumnInfo(name = "machineName")
    private String machineName;
    @ColumnInfo(name = "machineType")
    private String machineType;
    @ColumnInfo(name = "qrNumber")
    private int qrNumber;
    @TypeConverters(DateConverter.class)
    @ColumnInfo(name = "lastMaintainDate")
    private Date lastMaintainDate;
    @TypeConverters(ByteArrayConverter.class)
    @ColumnInfo(name = "machineImage")
    private List<byte[]> machineImage = null;

    MachineDataEntity(int machineId, String machineName, String machineType, int qrNumber, Date lastMaintainDate, List<byte[]> machineImage) {
        this.machineId = machineId;
        this.machineName = machineName;
        this.machineType = machineType;
        this.qrNumber = qrNumber;
        this.lastMaintainDate = lastMaintainDate;
        this.machineImage = machineImage;
    }

    @Ignore
    public MachineDataEntity(String machineName, String machineType, int qrNumber, Date lastMaintainDate, List<byte[]> machineImage) {
        this.machineName = machineName;
        this.machineType = machineType;
        this.qrNumber = qrNumber;
        this.lastMaintainDate = lastMaintainDate;
        this.machineImage = machineImage;
    }

    protected MachineDataEntity(Parcel in) {
        machineId = in.readInt();
        machineName = in.readString();
        machineType = in.readString();
        qrNumber = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(machineId);
        dest.writeString(machineName);
        dest.writeString(machineType);
        dest.writeInt(qrNumber);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MachineDataEntity> CREATOR = new Creator<MachineDataEntity>() {
        @Override
        public MachineDataEntity createFromParcel(Parcel in) {
            return new MachineDataEntity(in);
        }

        @Override
        public MachineDataEntity[] newArray(int size) {
            return new MachineDataEntity[size];
        }
    };

    public List<byte[]> getMachineImage() {
        return machineImage;
    }

    public void setMachineImage(List<byte[]> machineImage) {
        this.machineImage = machineImage;
    }

    public int getMachineId() {
        return machineId;
    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getMachineType() {
        return machineType;
    }

    public void setMachineType(String machineType) {
        this.machineType = machineType;
    }

    public int getQrNumber() {
        return qrNumber;
    }

    public void setQrNumber(int qrNumber) {
        this.qrNumber = qrNumber;
    }

    public Date getLastMaintainDate() {
        return lastMaintainDate;
    }

    public void setLastMaintainDate(Date lastMaintainDate) {
        this.lastMaintainDate = lastMaintainDate;
    }
}
