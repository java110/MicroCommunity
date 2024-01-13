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
package com.java110.store.cmd.resourceStore;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.privilege.BasePrivilegeDto;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.store.IResourceStoreUseRecordInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import com.java110.dto.resource.ResourceStoreUseRecordDto;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类表述：查询
 * 服务编码：resourceStoreUseRecord.listResourceStoreUseRecord
 * 请求路劲：/app/resourceStoreUseRecord.ListResourceStoreUseRecord
 * add by 吴学文 at 2022-08-08 14:39:58 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "resourceStore.listResourceStoreUseRecords")
public class ListResourceStoreUseRecordCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ListResourceStoreUseRecordCmd.class);

    @Autowired
    private IResourceStoreUseRecordInnerServiceSMO resourceStoreUseRecordInnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        ResourceStoreUseRecordDto resourceStoreUseRecordDto = BeanConvertUtil.covertBean(reqJson, ResourceStoreUseRecordDto.class);

        //查询物品所有使用记录权限
        BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
        basePrivilegeDto.setResource("/allResourceStoreUseRecord");
        basePrivilegeDto.setUserId(reqJson.getString("userId"));
        List<Map> privileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);
        if (privileges.size() == 0) {
            resourceStoreUseRecordDto.setUserId(reqJson.getString("userId"));
            resourceStoreUseRecordDto.setUserName(reqJson.getString("userName"));
        } else {
            resourceStoreUseRecordDto.setUserId("");
            resourceStoreUseRecordDto.setUserName("");
        }

        int count = resourceStoreUseRecordInnerServiceSMOImpl.queryResourceStoreUseRecordsCount(resourceStoreUseRecordDto);

        List<ResourceStoreUseRecordDto> resourceStoreUseRecordDtos = null;

        if (count > 0) {
            resourceStoreUseRecordDtos = resourceStoreUseRecordInnerServiceSMOImpl.queryResourceStoreUseRecords(resourceStoreUseRecordDto);
        } else {
            resourceStoreUseRecordDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, resourceStoreUseRecordDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
