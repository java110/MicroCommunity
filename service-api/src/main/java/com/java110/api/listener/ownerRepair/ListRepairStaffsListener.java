package com.java110.api.listener.ownerRepair;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.smo.community.IRepairInnerServiceSMO;
import com.java110.core.smo.community.IRepairUserInnerServiceSMO;
import com.java110.dto.repair.RepairUserDto;
import com.java110.utils.constant.ServiceCodeOwnerRepairConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listRepairStaffsListener")
public class ListRepairStaffsListener extends AbstractServiceApiListener {

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;


    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeOwnerRepairConstant.LIST_REPAIR_STAFFS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IRepairInnerServiceSMO getRepairInnerServiceSMOImpl() {
        return repairInnerServiceSMOImpl;
    }

    public void setRepairInnerServiceSMOImpl(IRepairInnerServiceSMO repairInnerServiceSMOImpl) {
        this.repairInnerServiceSMOImpl = repairInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        RepairUserDto repairUserDto = BeanConvertUtil.covertBean(reqJson, RepairUserDto.class);

        int count = repairUserInnerServiceSMOImpl.queryRepairUsersCount(repairUserDto);


        List<RepairUserDto> repairUserDtos = null;
        if (count > 0) {
            repairUserDtos = repairUserInnerServiceSMOImpl.queryRepairUsers(repairUserDto);
            refreshRepairUser(repairUserDtos);
        } else {
            repairUserDtos = new ArrayList<>();
        }

        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, repairUserDtos);


        context.setResponseEntity(responseEntity);

    }

    private void refreshRepairUser(List<RepairUserDto> repairUserDtos) {
        long duration = 0;
        for (RepairUserDto repairUserDto : repairUserDtos) {
            if (repairUserDto.getEndTime() == null) {
                duration = DateUtil.getCurrentDate().getTime() - repairUserDto.getStartTime().getTime();
            } else {
                duration = repairUserDto.getEndTime().getTime() - repairUserDto.getStartTime().getTime();
            }
            repairUserDto.setDuration(getCostTime(duration));
        }
    }


    public String getCostTime(Long time) {
        if (time == null) {
            return "00:00";
        }
        long hours = time / (1000 * 60 * 60);
        long minutes = (time - hours * (1000 * 60 * 60)) / (1000 * 60);
        String diffTime = "";
        if (minutes < 10) {
            diffTime = hours + ":0" + minutes;
        } else {
            diffTime = hours + ":" + minutes;
        }
        return diffTime;
    }
}
