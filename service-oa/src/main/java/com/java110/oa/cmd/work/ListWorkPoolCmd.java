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
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.workPoolContent.WorkPoolContentDto;
import com.java110.intf.oa.IWorkPoolContentV1InnerServiceSMO;
import com.java110.intf.oa.IWorkPoolV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import com.java110.dto.workPool.WorkPoolDto;
import java.util.List;
import java.util.ArrayList;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 类表述：查询
 * 服务编码：workPool.listWorkPool
 * 请求路劲：/app/workPool.ListWorkPool
 * add by 吴学文 at 2023-12-25 15:31:02 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "work.listWorkPool")
public class ListWorkPoolCmd extends Cmd {

  private static Logger logger = LoggerFactory.getLogger(ListWorkPoolCmd.class);
    @Autowired
    private IWorkPoolV1InnerServiceSMO workPoolV1InnerServiceSMOImpl;

    @Autowired
    private IWorkPoolContentV1InnerServiceSMO workPoolContentV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        String storeId = CmdContextUtils.getStoreId(cmdDataFlowContext);
        reqJson.put("storeId",storeId);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

           WorkPoolDto workPoolDto = BeanConvertUtil.covertBean(reqJson, WorkPoolDto.class);

           int count = workPoolV1InnerServiceSMOImpl.queryWorkPoolsCount(workPoolDto);

           List<WorkPoolDto> workPoolDtos = null;

           if (count > 0) {
               workPoolDtos = workPoolV1InnerServiceSMOImpl.queryWorkPools(workPoolDto);
           } else {
               workPoolDtos = new ArrayList<>();
           }

        queryContent(workPoolDtos);

           ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, workPoolDtos);

           ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

           cmdDataFlowContext.setResponseEntity(responseEntity);
    }

    /**
     * 查询内容
     *
     * @param workPoolDtos
     */
    private void queryContent(List<WorkPoolDto> workPoolDtos) {
        if (ListUtil.isNull(workPoolDtos)) {
            return;
        }

        if (workPoolDtos.size() != 1) {
            return;
        }

        WorkPoolContentDto workPoolContentDto = new WorkPoolContentDto();
        workPoolContentDto.setWorkId(workPoolDtos.get(0).getWorkId());

        List<WorkPoolContentDto> workPoolContentDtos = workPoolContentV1InnerServiceSMOImpl.queryWorkPoolContents(workPoolContentDto);

        if (ListUtil.isNull(workPoolContentDtos)) {
            return;
        }

        workPoolDtos.get(0).setContent(workPoolContentDtos.get(0).getContent());
    }
}
