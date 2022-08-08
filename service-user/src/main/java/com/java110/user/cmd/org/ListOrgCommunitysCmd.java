package com.java110.user.cmd.org;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.org.OrgCommunityDto;
import com.java110.intf.user.IOrgCommunityInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.org.ApiOrgCommunityDataVo;
import com.java110.vo.api.org.ApiOrgCommunityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


@Java110Cmd(serviceCode = "org.listOrgCommunitys")
public class ListOrgCommunitysCmd extends Cmd {

    @Autowired
    private IOrgCommunityInnerServiceSMO orgCommunityInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "storeId", "必填，请填写商户ID");
        Assert.hasKeyAndValue(reqJson, "orgId", "必填，请填写组织ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        OrgCommunityDto orgCommunityDto = BeanConvertUtil.covertBean(reqJson, OrgCommunityDto.class);

        int count = orgCommunityInnerServiceSMOImpl.queryOrgCommunitysCount(orgCommunityDto);

        List<ApiOrgCommunityDataVo> orgCommunitys = null;

        if (count > 0) {
            orgCommunitys = BeanConvertUtil.covertBeanList(orgCommunityInnerServiceSMOImpl.queryOrgCommunitys(orgCommunityDto), ApiOrgCommunityDataVo.class);
        } else {
            orgCommunitys = new ArrayList<>();
        }

        ApiOrgCommunityVo apiOrgVo = new ApiOrgCommunityVo();

        apiOrgVo.setTotal(count);
        apiOrgVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiOrgVo.setOrgCommunitys(orgCommunitys);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiOrgVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
