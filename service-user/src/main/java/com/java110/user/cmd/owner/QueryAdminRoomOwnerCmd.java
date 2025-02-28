package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.user.bmo.owner.IQueryOwnerStatisticsBMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * admin query room owner
 */
@Java110Cmd(serviceCode = "owner.queryAdminRoomOwner")
public class QueryAdminRoomOwnerCmd extends Cmd {

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;
    @Autowired
    private IQueryOwnerStatisticsBMO queryOwnerStatisticsBMOImpl;
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        super.validateAdmin(context);
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "roomId", "未包含房屋编号");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String userId = CmdContextUtils.getUserId(context);

        //todo 根据房屋查询时 先用 房屋信息查询 业主ID

        OwnerDto ownerDto = BeanConvertUtil.covertBean(reqJson, OwnerDto.class);

        int row = reqJson.getInteger("row");
        //查询总记录数
        int total = ownerInnerServiceSMOImpl.queryOwnersCount(ownerDto);
        List<OwnerDto> ownerDtos = null;
        if (total > 0) {
            ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
            // 查询统计数据
            ownerDtos = queryOwnerStatisticsBMOImpl.query(ownerDtos);
        } else {
            ownerDtos = new ArrayList<>();
        }
        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity((int) Math.ceil((double) total / (double) row), total, ownerDtos);
        context.setResponseEntity(responseEntity);
    }
}
