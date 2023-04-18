/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.community.cmd.maintainance;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.maintainance.MaintainanceItemDto;
import com.java110.intf.community.IMaintainanceItemV1InnerServiceSMO;
import com.java110.intf.community.IMaintainanceItemValueV1InnerServiceSMO;
import com.java110.po.maintainanceItem.MaintainanceItemPo;
import com.java110.po.maintainanceItemValue.MaintainanceItemValuePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类表述：保存
 * 服务编码：maintainance.saveMaintainanceItem
 * 请求路劲：/app/maintainance.SaveMaintainanceItem
 * add by 吴学文 at 2022-11-06 14:23:46 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "maintainance.saveMaintainanceItem")
public class SaveMaintainanceItemCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveMaintainanceItemCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IMaintainanceItemV1InnerServiceSMO maintainanceItemV1InnerServiceSMOImpl;

    @Autowired
    private IMaintainanceItemValueV1InnerServiceSMO maintainanceItemValueV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "itemTitle", "请求报文中未包含itemTitle");
        Assert.hasKeyAndValue(reqJson, "titleType", "请求报文中未包含titleType");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "seq", "请求报文中未包含seq");

        JSONArray titleValues = null;
        if (!MaintainanceItemDto.TITLE_TYPE_QUESTIONS.equals(reqJson.getString("titleType"))) {
            titleValues = reqJson.getJSONArray("titleValues");
            if (titleValues.size() < 1) {
                throw new IllegalArgumentException("未包含选项");
            }
        }
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        MaintainanceItemPo maintainanceItemPo = BeanConvertUtil.covertBean(reqJson, MaintainanceItemPo.class);
        maintainanceItemPo.setItemId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        int flag = maintainanceItemV1InnerServiceSMOImpl.saveMaintainanceItem(maintainanceItemPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }


        if (MaintainanceItemDto.TITLE_TYPE_QUESTIONS.equals(maintainanceItemPo.getTitleType())) {
            cmdDataFlowContext.setResponseEntity(ResultVo.success());
            return;
        }
        JSONArray titleValues = reqJson.getJSONArray("titleValues");
        MaintainanceItemValuePo maintainanceItemValuePo = null;
        for (int titleValueIndex = 0; titleValueIndex < titleValues.size(); titleValueIndex++) {
            maintainanceItemValuePo = new MaintainanceItemValuePo();
            maintainanceItemValuePo.setItemValue(titleValues.getJSONObject(titleValueIndex).getString("itemValue"));
            maintainanceItemValuePo.setSeq(titleValues.getJSONObject(titleValueIndex).getString("seq"));
            maintainanceItemValuePo.setItemId(maintainanceItemPo.getItemId());
            maintainanceItemValuePo.setValueId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_valueId));
            maintainanceItemValuePo.setCommunityId(maintainanceItemPo.getCommunityId());
            maintainanceItemValueV1InnerServiceSMOImpl.saveMaintainanceItemValue(maintainanceItemValuePo);
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
