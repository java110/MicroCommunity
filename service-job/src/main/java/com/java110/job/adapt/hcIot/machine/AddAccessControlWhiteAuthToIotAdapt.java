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
import com.java110.dto.file.FileRelDto;
import com.java110.dto.owner.OwnerAttrDto;
import com.java110.entity.order.Business;
import com.java110.intf.common.*;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.adapt.hcIot.asyn.IIotSendAsyn;
import com.java110.po.accessControlWhite.AccessControlWhitePo;
import com.java110.po.accessControlWhiteAuth.AccessControlWhiteAuthPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * HC iot 添加门禁白名单同步iot
 * <p>
 * 接口协议地址： https://gitee.com/java110/MicroCommunityThings/blob/master/back/docs/api.md
 *
 * @desc add by 吴学文 18:58
 */
@Component(value = "addAccessControlWhiteAuthToIotAdapt")
public class AddAccessControlWhiteAuthToIotAdapt extends DatabusAdaptImpl {

    @Autowired
    private IIotSendAsyn hcMachineAsynImpl;
    @Autowired
    IMachineInnerServiceSMO machineInnerServiceSMOImpl;


    @Autowired
    private IAccessControlWhiteV1InnerServiceSMO accessControlWhiteV1InnerServiceSMOImpl;
    @Autowired
    private IAccessControlWhiteAuthV1InnerServiceSMO accessControlWhiteAuthV1InnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;


    /**
     * accessToken={access_token}
     * &extCommunityUuid=01000
     * &extCommunityId=1
     * &devSn=111111111
     * &name=设备名称
     * &positionType=0
     * &positionUuid=1
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

        System.out.printf("进入门禁白名单页面");

        AccessControlWhiteAuthPo accessControlWhiteAuthPo = BeanConvertUtil.covertBean(data, AccessControlWhiteAuthPo.class);

        AccessControlWhiteAuthDto accessControlWhiteAuthDto = new AccessControlWhiteAuthDto();
        accessControlWhiteAuthDto.setAcwaId(accessControlWhiteAuthPo.getAcwaId());
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
        List<AccessControlWhiteDto> accessControlWhiteDtos = accessControlWhiteV1InnerServiceSMOImpl.queryAccessControlWhites(accessControlWhiteDto);

        Assert.listOnlyOne(accessControlWhiteDtos, "门禁白名单不存在");

        AccessControlWhiteDto tmpAccessControlWhiteDto = accessControlWhiteDtos.get(0);

        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setObjId(accessControlWhiteAuthDtos.get(0).getAcwId());
        //fileRelDto.setRelTypeCd("10000");
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
        if (fileRelDtos == null || fileRelDtos.size() != 1) {
            return;
        }
        String fileName = fileRelDtos.get(0).getFileSaveName();


        if(StringUtil.isEmpty(fileName)){
            return ;
        }
        String imgUrl = MappingCache.getValue(MappingConstant.FILE_DOMAIN,"IMG_PATH");
        if(!fileName.startsWith("http")){
            fileName = imgUrl +fileName;
        }

        String faceBase64 = ImageUtils.getBase64ByImgUrl(fileName);
        if (StringUtil.isEmpty(faceBase64)) {
            return;
        }

        JSONObject postParameters = new JSONObject();

        postParameters.put("userId", tmpAccessControlWhiteDto.getPersonId());
        postParameters.put("faceBase64", faceBase64);
        postParameters.put("startTime", tmpAccessControlWhiteDto.getStartTime());
        postParameters.put("endTime", tmpAccessControlWhiteDto.getEndTime());
        postParameters.put("name", tmpAccessControlWhiteDto.getPersonName());
        postParameters.put("idNumber", tmpAccessControlWhiteDto.getIdCard());
        postParameters.put("link", tmpAccessControlWhiteDto.getTel());
        postParameters.put("machineCode", accessControlWhiteAuthDtos.get(0).getMachineCode());
        postParameters.put("extMachineId", accessControlWhiteAuthDtos.get(0).getMachineId());
        postParameters.put("extCommunityId", tmpAccessControlWhiteDto.getCommunityId());
        List<OwnerAttrDto> ownerAttrDtos = new ArrayList<>();
        OwnerAttrDto ownerAttrDto = new OwnerAttrDto();
        ownerAttrDto.setSpecCd(OwnerAttrDto.SPEC_CD_ACCESS_CONTROL_KEY);
        ownerAttrDto.setValue(tmpAccessControlWhiteDto.getAccessControlKey());
        ownerAttrDto.setCommunityId(tmpAccessControlWhiteDto.getCommunityId());
        ownerAttrDtos.add(ownerAttrDto);
        postParameters.put("attrs", ownerAttrDtos);
        hcMachineAsynImpl.addOwner(postParameters);

    }
}
