package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.client.OutRestTemplate;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.owner.OwnerAttrDto;
import com.java110.intf.user.IOwnerAttrInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.List;

/***
 * 特定三方平台专用
 */
@Java110Cmd(serviceCode = "owner.syncThridOwner")
public class SyncThridOwnerCmd extends Cmd {

    @Autowired
    private IOwnerAttrInnerServiceSMO ownerAttrInnerServiceSMOImpl;

    @Autowired
    private OutRestTemplate outRestTemplate;

    private static final String getUserInfo = "https://lgtest.iflysec.com/visitorUser/api/user/userInfo";

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "token", "未包含token");
        Assert.hasKeyAndValue(reqJson, "userId", "未包含userId");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        OwnerAttrDto ownerAttrDto = new OwnerAttrDto();
        ownerAttrDto.setSpecCd(OwnerAttrDto.SPEC_CD_EXT_OWNER_ID);
        ownerAttrDto.setValue(reqJson.getString("userId"));
        List<OwnerAttrDto> ownerAttrDtos = ownerAttrInnerServiceSMOImpl.queryOwnerAttrs(ownerAttrDto);

        String ownerId = "";

        if (ownerAttrDtos == null || ownerAttrDtos.size() < 1) {
            ownerId = saveOwner(reqJson);
        }

        context.setResponseEntity(ResultVo.createResponseEntity(ownerId));
    }

    /**
     * 添加业主
     * @param reqJson
     * @return
     */
    private String saveOwner(JSONObject reqJson) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+reqJson.getString("token"));

        HttpEntity httpEntity = new HttpEntity("",headers);

        ResponseEntity<String> userInfo = outRestTemplate.exchange(getUserInfo+"?userId="+reqJson.getString("userId"), HttpMethod.GET,httpEntity,String.class);


        return "";
    }
}
