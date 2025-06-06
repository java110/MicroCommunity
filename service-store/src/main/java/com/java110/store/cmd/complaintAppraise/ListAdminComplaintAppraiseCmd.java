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
package com.java110.store.cmd.complaintAppraise;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.complaint.ComplaintAppraiseDto;
import com.java110.intf.store.IComplaintAppraiseV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 类表述：查询
 * 服务编码：complaintAppraise.listComplaintAppraise
 * 请求路劲：/app/complaintAppraise.ListComplaintAppraise
 * add by 吴学文 at 2024-02-21 13:04:55 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "complaintAppraise.listAdminComplaintAppraise")
public class ListAdminComplaintAppraiseCmd extends Cmd {

  private static Logger logger = LoggerFactory.getLogger(ListAdminComplaintAppraiseCmd.class);
    @Autowired
    private IComplaintAppraiseV1InnerServiceSMO complaintAppraiseV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        super.validateAdmin(cmdDataFlowContext);

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

           ComplaintAppraiseDto complaintAppraiseDto = BeanConvertUtil.covertBean(reqJson, ComplaintAppraiseDto.class);

           int count = complaintAppraiseV1InnerServiceSMOImpl.queryComplaintAppraisesCount(complaintAppraiseDto);

           List<ComplaintAppraiseDto> complaintAppraiseDtos = null;

           if (count > 0) {
               complaintAppraiseDtos = complaintAppraiseV1InnerServiceSMOImpl.queryComplaintAppraises(complaintAppraiseDto);
           } else {
               complaintAppraiseDtos = new ArrayList<>();
           }

           ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, complaintAppraiseDtos);

           ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

           cmdDataFlowContext.setResponseEntity(responseEntity);
    }
}
