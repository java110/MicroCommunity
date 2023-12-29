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
package com.java110.oa.cmd.work;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.workPool.WorkPoolDto;
import com.java110.intf.oa.*;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.workCopy.WorkCopyPo;
import com.java110.po.workCycle.WorkCyclePo;
import com.java110.po.workPool.WorkPoolPo;
import com.java110.po.workPoolContent.WorkPoolContentPo;
import com.java110.po.workPoolFile.WorkPoolFilePo;
import com.java110.po.workTask.WorkTaskPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 类表述：删除
 * 服务编码：workPool.deleteWorkPool
 * 请求路劲：/app/workPool.DeleteWorkPool
 * add by 吴学文 at 2023-12-25 15:31:02 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "work.deleteWorkPool")
public class DeleteWorkPoolCmd extends Cmd {
  private static Logger logger = LoggerFactory.getLogger(DeleteWorkPoolCmd.class);

    @Autowired
    private IWorkPoolV1InnerServiceSMO workPoolV1InnerServiceSMOImpl;

    @Autowired
    private IWorkPoolContentV1InnerServiceSMO workPoolContentV1InnerServiceSMOImpl;

    @Autowired
    private IWorkPoolFileV1InnerServiceSMO workPoolFileV1InnerServiceSMOImpl;

    @Autowired
    private IWorkCycleV1InnerServiceSMO workCycleV1InnerServiceSMOImpl;

    @Autowired
    private IWorkTaskV1InnerServiceSMO workTaskV1InnerServiceSMOImpl;

    @Autowired
    private IWorkCopyV1InnerServiceSMO workCopyV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "workId", "workId不能为空");
        String storeId = CmdContextUtils.getStoreId(cmdDataFlowContext);
        reqJson.put("storeId",storeId);

        String userId = CmdContextUtils.getUserId(cmdDataFlowContext);

        WorkPoolDto workPoolDto = new WorkPoolDto();
        workPoolDto.setWorkId(reqJson.getString("workId"));
        workPoolDto.setStoreId(storeId);
        workPoolDto.setCreateUserId(userId);
        int count = workPoolV1InnerServiceSMOImpl.queryWorkPoolsCount(workPoolDto);
        if(count < 1){
            throw new CmdException("您没有权限删除");
        }
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

       WorkPoolPo workPoolPo = BeanConvertUtil.covertBean(reqJson, WorkPoolPo.class);
        int flag = workPoolV1InnerServiceSMOImpl.deleteWorkPool(workPoolPo);

        if (flag < 1) {
            throw new CmdException("删除数据失败");
        }


        WorkPoolContentPo workPoolContentPo = null;

        workPoolContentPo = new WorkPoolContentPo();
        workPoolContentPo.setWorkId(workPoolPo.getWorkId());
        workPoolContentPo.setStoreId(workPoolPo.getStoreId());
        workPoolContentV1InnerServiceSMOImpl.deleteWorkPoolContent(workPoolContentPo);


        WorkTaskPo workTaskPo = new WorkTaskPo();
        workTaskPo.setWorkId(workPoolPo.getWorkId());
        workTaskPo.setStoreId(workPoolPo.getStoreId());
        workTaskV1InnerServiceSMOImpl.deleteWorkTask(workTaskPo);

        WorkPoolFilePo workPoolFilePo = new WorkPoolFilePo();
        workPoolFilePo.setWorkId(workPoolPo.getWorkId());
        workPoolFilePo.setStoreId(workPoolPo.getStoreId());
        workPoolFileV1InnerServiceSMOImpl.deleteWorkPoolFile(workPoolFilePo);

        WorkCyclePo workCyclePo = new WorkCyclePo();
        workCyclePo.setWorkId(workPoolPo.getWorkId());
        workCyclePo.setCommunityId(workPoolPo.getCommunityId());
        workCyclePo.setStoreId(workPoolPo.getStoreId());
        workCycleV1InnerServiceSMOImpl.deleteWorkCycle(workCyclePo);

        WorkCopyPo workCopyPo = null;
        workCopyPo = new WorkCopyPo();
        workCopyPo.setStoreId(reqJson.getString("storeId"));
        workCopyPo.setWorkId(workPoolPo.getWorkId());

        workCopyV1InnerServiceSMOImpl.deleteWorkCopy(workCopyPo);

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
