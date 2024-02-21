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
 * 类表述：更新
 * 服务编码：complaintType.updateComplaintType
 * 请求路劲：/app/complaintType.UpdateComplaintType
 * add by 吴学文 at 2024-02-21 12:06:00 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "complaintType.updateComplaintType")
public class UpdateComplaintTypeCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(UpdateComplaintTypeCmd.class);


    @Autowired
    private IComplaintTypeV1InnerServiceSMO complaintTypeV1InnerServiceSMOImpl;

    @Autowired
    private IComplaintTypeUserV1InnerServiceSMO complaintTypeUserV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "typeCd", "typeCd不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");


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
        int flag = complaintTypeV1InnerServiceSMOImpl.updateComplaintType(complaintTypePo);

        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }

        ComplaintTypeUserPo complaintTypeUserPo = new ComplaintTypeUserPo();
        complaintTypeUserPo.setTypeCd(complaintTypePo.getTypeCd());
        complaintTypeUserV1InnerServiceSMOImpl.deleteComplaintTypeUser(complaintTypeUserPo);


        JSONArray staffs = reqJson.getJSONArray("staffs");
        JSONObject staff = null;
        for(int staffIndex = 0;staffIndex < staffs.size(); staffIndex++){
            staff = staffs.getJSONObject(staffIndex);
             complaintTypeUserPo = new ComplaintTypeUserPo();
            complaintTypeUserPo.setTypeCd(complaintTypePo.getTypeCd());
            complaintTypeUserPo.setTypeUserId(GenerateCodeFactory.getGeneratorId("11"));
            complaintTypeUserPo.setStaffName(staff.getString("name"));
            complaintTypeUserPo.setCommunityId(complaintTypePo.getCommunityId());
            complaintTypeUserPo.setStaffId(staff.getString("userId"));
            complaintTypeUserV1InnerServiceSMOImpl.saveComplaintTypeUser(complaintTypeUserPo);
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
