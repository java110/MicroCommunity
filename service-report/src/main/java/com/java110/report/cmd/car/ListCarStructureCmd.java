package com.java110.report.cmd.car;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.intf.report.IReportCommunityInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/*
     查询车位结构图
 */
@Java110Cmd(serviceCode = "car.listCarStructure")
public class ListCarStructureCmd extends Cmd {

    @Autowired
    private IReportCommunityInnerServiceSMO reportCommunityInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "unitId", "未传入单元信息");
        Assert.hasKeyAndValue(reqJson, "communityId", "未传入房屋信息");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        OwnerCarDto carDto = BeanConvertUtil.covertBean(reqJson, OwnerCarDto.class);
        List<OwnerCarDto> ownerCarDtos = reportCommunityInnerServiceSMOImpl.queryCarStructures(carDto);

        cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(ownerCarDtos));
    }


}
