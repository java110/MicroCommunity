package com.java110.vo.api.machineAuth;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiMachineAuthVo extends MorePageVo implements Serializable {

    List<ApiMachineAuthDataVo> machineAuths;


    public List<ApiMachineAuthDataVo> getMachineAuths() {
        return machineAuths;
    }

    public void setMachineAuths(List<ApiMachineAuthDataVo> machineAuths) {
        this.machineAuths = machineAuths;
    }

}
