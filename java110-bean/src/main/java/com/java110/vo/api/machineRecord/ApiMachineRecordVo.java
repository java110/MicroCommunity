package com.java110.vo.api.machineRecord;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiMachineRecordVo extends MorePageVo implements Serializable {
    List<ApiMachineRecordDataVo> machineRecords;


    public List<ApiMachineRecordDataVo> getMachineRecords() {
        return machineRecords;
    }

    public void setMachineRecords(List<ApiMachineRecordDataVo> machineRecords) {
        this.machineRecords = machineRecords;
    }
}
