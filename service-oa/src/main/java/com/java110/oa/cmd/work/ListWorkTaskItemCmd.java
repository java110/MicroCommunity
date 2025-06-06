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
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.work.WorkPoolFileDto;
import com.java110.intf.oa.IWorkPoolFileV1InnerServiceSMO;
import com.java110.intf.oa.IWorkTaskItemV1InnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import com.java110.dto.work.WorkTaskItemDto;

import java.util.List;
import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 类表述：查询
 * 服务编码：workTaskItem.listWorkTaskItem
 * 请求路劲：/app/workTaskItem.ListWorkTaskItem
 * add by 吴学文 at 2024-10-31 00:45:24 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "task.listWorkTaskItem")
public class ListWorkTaskItemCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ListWorkTaskItemCmd.class);
    @Autowired
    private IWorkTaskItemV1InnerServiceSMO workTaskItemV1InnerServiceSMOImpl;

    @Autowired
    private IWorkPoolFileV1InnerServiceSMO workPoolFileV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        super.validateProperty(cmdDataFlowContext);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        WorkTaskItemDto workTaskItemDto = BeanConvertUtil.covertBean(reqJson, WorkTaskItemDto.class);

        int count = workTaskItemV1InnerServiceSMOImpl.queryWorkTaskItemsCount(workTaskItemDto);

        List<WorkTaskItemDto> workTaskItemDtos = null;

        if (count > 0) {
            workTaskItemDtos = workTaskItemV1InnerServiceSMOImpl.queryWorkTaskItems(workTaskItemDto);

            queryTaskFile(workTaskItemDtos);

        } else {
            workTaskItemDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, workTaskItemDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        cmdDataFlowContext.setResponseEntity(responseEntity);
    }

    private void queryTaskFile(List<WorkTaskItemDto> workTaskItemDtos) {
        if (ListUtil.isNull(workTaskItemDtos)) {
            return;
        }
        List<String> itemIds = new ArrayList<>();
        for (WorkTaskItemDto workTaskItemDto : workTaskItemDtos) {
            itemIds.add(workTaskItemDto.getItemId());
        }

        WorkPoolFileDto workPoolFileDto = new WorkPoolFileDto();
        workPoolFileDto.setFileType(WorkPoolFileDto.FILE_TYPE_END);
        workPoolFileDto.setItemIds(itemIds.toArray(new String[itemIds.size()]));
        List<WorkPoolFileDto> workPoolFileDtos = workPoolFileV1InnerServiceSMOImpl.queryWorkPoolFiles(workPoolFileDto);
        if (ListUtil.isNull(workPoolFileDtos)) {
            return;
        }
        List<String> pathUrls = null;
        String imgUrl = MappingCache.getValue(MappingConstant.FILE_DOMAIN, "IMG_PATH");

        for (WorkTaskItemDto workTaskItemDto : workTaskItemDtos) {
            pathUrls = new ArrayList<>();
            for (WorkPoolFileDto tWorkPoolFileDto : workPoolFileDtos) {
                if (!workTaskItemDto.getItemId().equals(tWorkPoolFileDto.getItemId())) {
                    continue;
                }
                pathUrls.add(imgUrl + tWorkPoolFileDto.getPathUrl());
            }
            workTaskItemDto.setPathUrls(pathUrls);
        }
    }


}
