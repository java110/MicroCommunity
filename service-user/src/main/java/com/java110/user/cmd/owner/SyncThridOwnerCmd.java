package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.client.OutRestTemplate;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.owner.OwnerAttrDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.user.IOwnerAttrInnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.po.owner.OwnerAttrPo;
import com.java110.po.owner.OwnerPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
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
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    public static final String DEFAULT_COMMUNITY_ID = "123"; //特殊化需求 这里写死

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
     *
     * @param reqJson
     * @return
     */
    private String saveOwner(JSONObject reqJson) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + reqJson.getString("token"));

        HttpEntity httpEntity = new HttpEntity("", headers);

        ResponseEntity<String> userInfo = outRestTemplate.exchange(getUserInfo + "?userId=" + reqJson.getString("userId"), HttpMethod.GET, httpEntity, String.class);


        JSONObject result = JSONObject.parseObject(userInfo.getBody());

        if (result.getIntValue("code") != 200) {
            throw new CmdException(result.getString("msg"));
        }

        JSONObject data = result.getJSONObject("data");

        OwnerPo ownerPo = new OwnerPo();
        ownerPo.setOwnerId(GenerateCodeFactory.getGeneratorId("99"));
        ownerPo.setMemberId(ownerPo.getOwnerId());
        ownerPo.setAge("1");
        ownerPo.setOwnerFlag(OwnerDto.OWNER_FLAG_TRUE);
        ownerPo.setOwnerTypeCd(OwnerDto.OWNER_TYPE_CD_OWNER);
        ownerPo.setAddress("无");
        ownerPo.setCommunityId(DEFAULT_COMMUNITY_ID);
        ownerPo.setIdCard(data.getString("idNo"));
        ownerPo.setLink(data.getString("phoneNumber"));
        ownerPo.setName(data.getString("realName"));
        ownerPo.setRemark("通过接口新增");
        ownerPo.setSex(data.getString("sex") == null ? "1" : "0");
        ownerPo.setState(OwnerDto.STATE_FINISH);
        ownerPo.setUserId("-1");
        int flag = ownerV1InnerServiceSMOImpl.saveOwner(ownerPo);
        if (flag < 1) {
            throw new CmdException("保存业主失败");
        }

        OwnerAttrPo ownerAttrPo = new OwnerAttrPo();
        ownerAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId("11"));
        ownerAttrPo.setCommunityId(DEFAULT_COMMUNITY_ID);
        ownerAttrPo.setValue(reqJson.getString("userId"));
        ownerAttrPo.setMemberId(ownerPo.getMemberId());
        ownerAttrPo.setSpecCd(OwnerAttrDto.SPEC_CD_EXT_OWNER_ID);

        flag = ownerAttrInnerServiceSMOImpl.saveOwnerAttr(ownerAttrPo);
        if (flag < 1) {
            throw new CmdException("保存业主失败");
        }
        return ownerPo.getOwnerId();
    }
}
