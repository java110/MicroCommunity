package com.java110.fee.cmd.carMonth;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.IotDataDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.intf.job.IIotInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * 查询停车月卡
 */
@Java110Cmd(serviceCode = "carMonth.queryAppCarMonthCard")
public class QueryAppCarMonthCardCmd extends Cmd {

    @Autowired
    private IIotInnerServiceSMO iotInnerServiceSMOImpl;


    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
        Assert.hasKeyAndValue(reqJson, "carId", "未包含carId");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setMemberId(reqJson.getString("carId"));
        ownerCarDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        if (ListUtil.isNull(ownerCarDtos)) {
            throw new CmdException("车辆不存在");
        }

        reqJson.put("paNum", ownerCarDtos.get(0).getAreaNum());

        ResultVo data = iotInnerServiceSMOImpl.postIotData(new IotDataDto("queryCarMonthCardBmoImpl", reqJson));

        context.setResponseEntity(ResultVo.createResponseEntity(data));


    }
}
