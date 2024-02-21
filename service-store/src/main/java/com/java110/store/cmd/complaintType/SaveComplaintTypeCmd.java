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
package com.java110.store.cmd.complaintType;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.store.IComplaintTypeUserV1InnerServiceSMO;
import com.java110.intf.store.IComplaintTypeV1InnerServiceSMO;
import com.java110.po.complaintType.ComplaintTypePo;
import com.java110.po.complaintTypeUser.ComplaintTypeUserPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类表述：保存
 * 服务编码：complaintType.saveComplaintType
 * 请求路劲：/app/complaintType.SaveComplaintType
 * add by 吴学文 at 2024-02-21 12:06:00 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "complaintType.saveComplaintType")
public class SaveComplaintTypeCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveComplaintTypeCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IComplaintTypeV1InnerServiceSMO complaintTypeV1InnerServiceSMOImpl;

    @Autowired
    private IComplaintTypeUserV1InnerServiceSMO complaintTypeUserV1InnerServiceSMOImpl;

    /**
     * {"typeCd":"","typeName":"123","notifyWay":"WECHAT","appraiseReply":"Y","remark":"123",
     * "staffs":[{"address":"无","age":0,"email":"","initials":"B","levelCd":"01","name":"bbbb","orgId":"842024012258220036","orgLevel":"1",
     * "orgName":"物联网同步物业 / 物联网同步物业","parentOrgId":"-1","parentTwoOrgId":"-1","relCd":"1000","relCdName":"普通员工",
     * "relId":"842024020671010069","sex":"0","storeId":"102024012228150026","tel":"14545456666","userId":"302024020663670065","userName":"bbbb"}],
     * "communityId":"2024012252790005"}
     *
     * @param event              事件对象
     * @param cmdDataFlowContext 请求报文数据
     * @param reqJson
     */
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "typeName", "请求报文中未包含typeName");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "notifyWay", "请求报文中未包含notifyWay");
        Assert.hasKeyAndValue(reqJson, "appraiseReply", "请求报文中未包含appraiseReply");


        JSONArray staffs = reqJson.getJSONArray("staffs");
        if (ListUtil.isNull(staffs)) {
            throw new CmdException("未包含人员");
        }

        JSONObject staff = null;
        for(int staffIndex = 0;staffIndex < staffs.size(); staffIndex++){
            staff = staffs.getJSONObject(staffIndex);
            Assert.hasKeyAndValue(staff, "userId", "请求报文中未包含userId");
            Assert.hasKeyAndValue(staff, "name", "请求报文中未包含userName");

        }

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        ComplaintTypePo complaintTypePo = BeanConvertUtil.covertBean(reqJson, ComplaintTypePo.class);
        complaintTypePo.setTypeCd(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        int flag = complaintTypeV1InnerServiceSMOImpl.saveComplaintType(complaintTypePo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        JSONArray staffs = reqJson.getJSONArray("staffs");
        JSONObject staff = null;
        for(int staffIndex = 0;staffIndex < staffs.size(); staffIndex++){
            staff = staffs.getJSONObject(staffIndex);
            ComplaintTypeUserPo complaintTypeUserPo = new ComplaintTypeUserPo();
            complaintTypeUserPo.setTypeCd(complaintTypePo.getTypeCd());
            complaintTypeUserPo.setTypeUserId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
            complaintTypeUserPo.setStaffName(staff.getString("name"));
            complaintTypeUserPo.setCommunityId(complaintTypePo.getCommunityId());
            complaintTypeUserPo.setStaffId(staff.getString("userId"));
            complaintTypeUserV1InnerServiceSMOImpl.saveComplaintTypeUser(complaintTypeUserPo);
        }


        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
