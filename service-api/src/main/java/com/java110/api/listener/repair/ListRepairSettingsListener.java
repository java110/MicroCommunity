package com.java110.api.listener.repair;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.smo.repairSetting.IRepairSettingInnerServiceSMO;
import com.java110.dto.repair.RepairSettingDto;
import com.java110.utils.constant.ServiceCodeRepairSettingConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询小区侦听类
 */
@Java110Listener("listRepairSettingsListener")
public class ListRepairSettingsListener extends AbstractServiceApiListener {

    @Autowired
    private IRepairSettingInnerServiceSMO repairSettingInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeRepairSettingConstant.LIST_REPAIRSETTINGS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IRepairSettingInnerServiceSMO getRepairSettingInnerServiceSMOImpl() {
        return repairSettingInnerServiceSMOImpl;
    }

    public void setRepairSettingInnerServiceSMOImpl(IRepairSettingInnerServiceSMO repairSettingInnerServiceSMOImpl) {
        this.repairSettingInnerServiceSMOImpl = repairSettingInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        RepairSettingDto repairSettingDto = BeanConvertUtil.covertBean(reqJson, RepairSettingDto.class);

        int count = repairSettingInnerServiceSMOImpl.queryRepairSettingsCount(repairSettingDto);

        List<RepairSettingDto> repairSettingDtos = null;

        if (count > 0) {
            repairSettingDtos = repairSettingInnerServiceSMOImpl.queryRepairSettings(repairSettingDto);
        } else {
            repairSettingDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, repairSettingDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
