package com.java110.api.bmo.machineTranslate;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.machine.MachineUserResultDto;

/**
 * @ClassName IOwnerMachineTranslateBMO
 * @Description TODO 员工人脸同步实现类
 * @Author wuxw
 * @Date 2020/6/5 8:21
 * @Version 1.0
 * add by wuxw 2020/6/5
 **/
public interface IStaffMachineTranslateBMO {

    /**
     * 查询人脸信息
     *
     * @return
     */
    MachineUserResultDto getPhotoInfo(JSONObject reqJson);
}
