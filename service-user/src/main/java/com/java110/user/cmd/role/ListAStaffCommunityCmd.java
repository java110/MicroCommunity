package com.java110.user.cmd.role;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.staffCommunity.StaffCommunityDto;
import com.java110.intf.user.IStaffCommunityV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "role.listAStaffCommunity")
public class ListAStaffCommunityCmd extends Cmd {
    @Autowired
    private IStaffCommunityV1InnerServiceSMO staffCommunityV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        super.validateAdmin(cmdDataFlowContext);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        String storeId = CmdContextUtils.getStoreId(cmdDataFlowContext);
        StaffCommunityDto staffCommunityDto = BeanConvertUtil.covertBean(reqJson, StaffCommunityDto.class);
        staffCommunityDto.setStoreId(storeId);
        int count = staffCommunityV1InnerServiceSMOImpl.queryStaffCommunitysCount(staffCommunityDto);

        List<StaffCommunityDto> staffCommunityDtos = null;

        if (count > 0) {
            staffCommunityDtos = staffCommunityV1InnerServiceSMOImpl.queryStaffCommunitys(staffCommunityDto);
        } else {
            staffCommunityDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, staffCommunityDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        cmdDataFlowContext.setResponseEntity(responseEntity);
    }
}
