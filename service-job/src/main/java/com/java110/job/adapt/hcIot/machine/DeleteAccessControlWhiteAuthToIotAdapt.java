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
package com.java110.job.adapt.hcIot.machine;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.accessControlWhite.AccessControlWhiteAuthDto;
import com.java110.dto.accessControlWhite.AccessControlWhiteDto;
import com.java110.entity.order.Business;
import com.java110.intf.common.IAccessControlWhiteAuthV1InnerServiceSMO;
import com.java110.intf.common.IAccessControlWhiteV1InnerServiceSMO;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.adapt.hcIot.asyn.IIotSendAsyn;
import com.java110.po.accessControlWhite.AccessControlWhitePo;
import com.java110.po.accessControlWhiteAuth.AccessControlWhiteAuthPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * HC  添加业主同步iot
 * <p>
 * 接口协议地址： https://gitee.com/java110/MicroCommunityThings/blob/master/back/docs/api.md
 *
 * @desc add by 吴学文 18:58
 */
@Component(value = "deleteAccessControlWhiteAuthToIotAdapt")
public class DeleteAccessControlWhiteAuthToIotAdapt extends DatabusAdaptImpl {

    @Autowired
    private IIotSendAsyn hcMachineAsynImpl;
    @Autowired
    IMachineInnerServiceSMO machineInnerServiceSMOImpl;


    @Autowired
    private IAccessControlWhiteV1InnerServiceSMO accessControlWhiteV1InnerServiceSMOImpl;

    @Autowired
    private IAccessControlWhiteAuthV1InnerServiceSMO accessControlWhiteAuthV1InnerServiceSMOImpl;


    /**
     * {
     * "userId": "702020042194860037",
     * "machineCode": "101010"
     * }
     *
     * @param business   当前处理业务
     * @param businesses 所有业务信息
     */
    @Override
    public void execute(Business business, List<Business> businesses) {
        JSONObject data = business.getData();
        doSendMachine(business, data);
    }

    private void doSendMachine(Business business, JSONObject data) {

        AccessControlWhiteAuthPo accessControlWhiteAuthPo = BeanConvertUtil.covertBean(data, AccessControlWhiteAuthPo.class);

        AccessControlWhiteAuthDto accessControlWhiteAuthDto = new AccessControlWhiteAuthDto();
        accessControlWhiteAuthDto.setAcwaId(accessControlWhiteAuthPo.getAcwaId());
        accessControlWhiteAuthDto.setStatusCd("1");
        List<AccessControlWhiteAuthDto> accessControlWhiteAuthDtos
                = accessControlWhiteAuthV1InnerServiceSMOImpl.queryAccessControlWhiteAuths(accessControlWhiteAuthDto);
        if(accessControlWhiteAuthDtos == null || accessControlWhiteAuthDtos.size()<1){
            return ;
        }

        AccessControlWhiteDto accessControlWhiteDto = new AccessControlWhiteDto();
        accessControlWhiteDto.setAcwId(accessControlWhiteAuthDtos.get(0).getAcwId());
        accessControlWhiteDto.setCommunityId(accessControlWhiteDto.getCommunityId());
        accessControlWhiteDto.setPage(1);
        accessControlWhiteDto.setRow(1);
        accessControlWhiteDto.setStatusCd(""); //这个时候已经删除了 所以查询删除记录
        List<AccessControlWhiteDto> accessControlWhiteDtos = accessControlWhiteV1InnerServiceSMOImpl.queryAccessControlWhites(accessControlWhiteDto);
        Assert.listOnlyOne(accessControlWhiteDtos, "门禁白名单不存在");

        AccessControlWhiteDto tmpAccessControlWhiteDto = accessControlWhiteDtos.get(0);
        JSONObject postParameters = new JSONObject();
        postParameters.put("machineCode",  accessControlWhiteAuthDtos.get(0).getMachineCode());
        postParameters.put("userId", tmpAccessControlWhiteDto.getPersonId());
        postParameters.put("name", tmpAccessControlWhiteDto.getPersonName());
        postParameters.put("extMachineId", accessControlWhiteAuthDtos.get(0).getMachineId());
        postParameters.put("extCommunityId", tmpAccessControlWhiteDto.getCommunityId());
        hcMachineAsynImpl.sendDeleteOwner(postParameters);


    }
}
