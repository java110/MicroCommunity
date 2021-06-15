package com.java110.common.smo.impl;


import com.alibaba.fastjson.JSONArray;
import com.java110.common.dao.IMachineRecordServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.machine.MachineRecordDto;
import com.java110.intf.common.IMachineRecordInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.machine.MachineRecordPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 设备上报内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class MachineRecordInnerServiceSMOImpl extends BaseServiceSMO implements IMachineRecordInnerServiceSMO {

    @Autowired
    private IMachineRecordServiceDao machineRecordServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<MachineRecordDto> queryMachineRecords(@RequestBody MachineRecordDto machineRecordDto) {

        //校验是否传了 分页信息

        int page = machineRecordDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            machineRecordDto.setPage((page - 1) * machineRecordDto.getRow());
        }

        List<MachineRecordDto> machineRecords = BeanConvertUtil.covertBeanList(machineRecordServiceDaoImpl.getMachineRecordInfo(BeanConvertUtil.beanCovertMap(machineRecordDto)), MachineRecordDto.class);


        return machineRecords;
    }


    @Override
    public int queryMachineRecordsCount(@RequestBody MachineRecordDto machineRecordDto) {
        return machineRecordServiceDaoImpl.queryMachineRecordsCount(BeanConvertUtil.beanCovertMap(machineRecordDto));
    }

    @Override
    public JSONArray getAssetsMachineRecords(@RequestBody String communityId) {

        Map<String, Object> info = new HashMap<>();
        info.put("communityId", communityId);

        List<Map> records = machineRecordServiceDaoImpl.getAssetsMachineRecords(info);

        return JSONArray.parseArray(JSONArray.toJSONString(records));
    }

    @Override
    public int saveMachineRecords(@RequestBody List<MachineRecordPo> machineRecordPos) {
        List<Map> machineRecords = new ArrayList<>();
        for (MachineRecordPo payFeePo : machineRecordPos) {
            machineRecords.add(BeanConvertUtil.beanCovertMap(payFeePo));
        }

        Map info = new HashMap();
        info.put("machineRecords", machineRecords);
        return machineRecordServiceDaoImpl.saveMachineRecords(info);
    }

    public IMachineRecordServiceDao getMachineRecordServiceDaoImpl() {
        return machineRecordServiceDaoImpl;
    }

    public void setMachineRecordServiceDaoImpl(IMachineRecordServiceDao machineRecordServiceDaoImpl) {
        this.machineRecordServiceDaoImpl = machineRecordServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
