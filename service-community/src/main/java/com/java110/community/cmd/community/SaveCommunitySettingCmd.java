package com.java110.community.cmd.community;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.community.CommunitySettingDto;
import com.java110.intf.community.ICommunitySettingInnerServiceSMO;
import com.java110.po.community.CommunitySettingPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "community.saveCommunitySetting")
public class SaveCommunitySettingCmd extends Cmd {

    @Autowired
    private ICommunitySettingInnerServiceSMO communitySettingInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
        Assert.hasKeyAndValue(reqJson, "settingType", "未包含类型");

        if (!reqJson.containsKey("keys")) {
            throw new CmdException("未包内容");
        }

        JSONArray keys = reqJson.getJSONArray("keys");
        if (keys == null || keys.size() < 1) {
            throw new CmdException("未包内容");
        }

        JSONObject keyObj = null;
        for (int keyIndex = 0; keyIndex < keys.size(); keyIndex++) {
            keyObj = keys.getJSONObject(keyIndex);
            Assert.hasKeyAndValue(keyObj, "settingValue", "未包含值");
            Assert.hasKeyAndValue(keyObj, "settingKey", "未包含键");
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        JSONArray keys = reqJson.getJSONArray("keys");

        JSONObject keyObj = null;
        List<CommunitySettingDto> settings = null;
        CommunitySettingPo communitySettingPo = null;
        for (int keyIndex = 0; keyIndex < keys.size(); keyIndex++) {
            keyObj = keys.getJSONObject(keyIndex);

            CommunitySettingDto communitySettingDto = new CommunitySettingDto();
            communitySettingDto.setSettingKey(keyObj.getString("settingKey"));
            communitySettingDto.setSettingType(reqJson.getString("settingType"));
            communitySettingDto.setCommunityId(reqJson.getString("communityId"));
            settings = communitySettingInnerServiceSMOImpl.queryCommunitySettings(communitySettingDto);
            communitySettingPo = new CommunitySettingPo();
            communitySettingPo.setSettingType(reqJson.getString("settingType"));
            communitySettingPo.setCommunityId(reqJson.getString("communityId"));
            communitySettingPo.setSettingKey(keyObj.getString("settingKey"));
            communitySettingPo.setSettingValue(keyObj.getString("settingValue"));
            communitySettingPo.setSettingName(keyObj.getString("settingName"));
            communitySettingPo.setRemark(keyObj.getString("remark"));

            if (settings == null || settings.size() < 1) {
                communitySettingPo.setCsId(GenerateCodeFactory.getGeneratorId("11"));

                communitySettingInnerServiceSMOImpl.saveCommunitySetting(communitySettingPo);
                continue;
            }

            communitySettingPo.setCsId(settings.get(0).getCsId());
            communitySettingInnerServiceSMOImpl.updateCommunitySetting(communitySettingPo);

            //将结果写入缓存
            CommunitySettingFactory.getCommunitySettingFromDb(communitySettingPo.getCommunityId(), communitySettingPo.getSettingKey());

        }



    }
}
