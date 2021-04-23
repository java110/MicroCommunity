package com.java110.vo.api.machine;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiMachineVo extends MorePageVo implements Serializable {
    List<ApiMachineDataVo> machines;


    public List<ApiMachineDataVo> getMachines() {
        return machines;
    }

    public void setMachines(List<ApiMachineDataVo> machines) {
        this.machines = machines;
    }
}
