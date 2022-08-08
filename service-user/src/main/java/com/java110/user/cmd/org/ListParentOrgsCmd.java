package com.java110.user.cmd.org;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.org.OrgDto;
import com.java110.intf.user.IOrgInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.org.ApiOrgDataVo;
import com.java110.vo.api.org.ApiOrgVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "org.listParentOrgs")
public class ListParentOrgsCmd extends Cmd {

    @Autowired
    private IOrgInnerServiceSMO orgInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        //super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "storeId", "必填，请填写商户ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        OrgDto orgDto = BeanConvertUtil.covertBean(reqJson, OrgDto.class);



        List<ApiOrgDataVo> parentOrgs = BeanConvertUtil.covertBeanList(orgInnerServiceSMOImpl.queryParentOrgs(orgDto), ApiOrgDataVo.class);


        ApiOrgVo apiOrgVo = new ApiOrgVo();

        apiOrgVo.setOrgs(parentOrgs);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiOrgVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
