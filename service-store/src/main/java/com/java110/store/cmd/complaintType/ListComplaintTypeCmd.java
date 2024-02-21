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

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.complaintTypeUser.ComplaintTypeUserDto;
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
import com.java110.dto.complaintType.ComplaintTypeDto;

import java.util.List;
import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 类表述：查询
 * 服务编码：complaintType.listComplaintType
 * 请求路劲：/app/complaintType.ListComplaintType
 * add by 吴学文 at 2024-02-21 12:06:00 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "complaintType.listComplaintType")
public class ListComplaintTypeCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ListComplaintTypeCmd.class);
    @Autowired
    private IComplaintTypeV1InnerServiceSMO complaintTypeV1InnerServiceSMOImpl;


    @Autowired
    private IComplaintTypeUserV1InnerServiceSMO complaintTypeUserV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        ComplaintTypeDto complaintTypeDto = BeanConvertUtil.covertBean(reqJson, ComplaintTypeDto.class);

        int count = complaintTypeV1InnerServiceSMOImpl.queryComplaintTypesCount(complaintTypeDto);

        List<ComplaintTypeDto> complaintTypeDtos = null;

        if (count > 0) {
            complaintTypeDtos = complaintTypeV1InnerServiceSMOImpl.queryComplaintTypes(complaintTypeDto);
        } else {
            complaintTypeDtos = new ArrayList<>();
        }

        //todo 查询类型员工
        toQueryStaff(complaintTypeDtos);

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, complaintTypeDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        cmdDataFlowContext.setResponseEntity(responseEntity);
    }

    private void toQueryStaff(List<ComplaintTypeDto> complaintTypeDtos) {

        if (ListUtil.isNull(complaintTypeDtos)) {
            return;
        }

        List<String> typeCds = new ArrayList<>();
        for (ComplaintTypeDto complaintTypeDto : complaintTypeDtos) {
            typeCds.add(complaintTypeDto.getTypeCd());
        }

        ComplaintTypeUserDto complaintTypeUserDto = new ComplaintTypeUserDto();
        complaintTypeUserDto.setTypeCds(typeCds.toArray(new String[typeCds.size()]));

        List<ComplaintTypeUserDto> complaintTypeUserDtos = complaintTypeUserV1InnerServiceSMOImpl.queryComplaintTypeUsers(complaintTypeUserDto);

        if (ListUtil.isNull(complaintTypeUserDtos)) {
            return;
        }
        List<ComplaintTypeUserDto> staffs = null;
        for (ComplaintTypeDto complaintTypeDto : complaintTypeDtos) {
            staffs = new ArrayList<>();
            for(ComplaintTypeUserDto complaintTypeUserDto1 : complaintTypeUserDtos){
                if(complaintTypeDto.getTypeCd().equals(complaintTypeUserDto1.getTypeCd())){
                    staffs.add(complaintTypeUserDto1);
                }
            }
            complaintTypeDto.setStaffs(staffs);
        }

    }
}
