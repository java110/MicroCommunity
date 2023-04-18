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
package com.java110.job.adapt.hcGov.inoutRecord;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.businessDatabus.CustomBusinessDatabusDto;
import com.java110.dto.community.CommunityAttrDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.community.CommunityLocationAttrDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.machine.MachineDto;
import com.java110.entity.order.Business;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.ICommunityLocationAttrInnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.adapt.hcGov.HcGovConstant;
import com.java110.job.adapt.hcGov.asyn.BaseHcGovSendAsyn;
import com.java110.po.machine.MachineRecordPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 开门记录同步HC政务接口
 * <p>
 * 接口协议地址： https://gitee.com/java110/microCommunityInformation/tree/master/info-doc#1%E6%A5%BC%E6%A0%8B%E4%B8%8A%E4%BC%A0
 *
 * @desc add by 吴学文 16:20
 */
@Component(value = "addInoutRecordToHcGovAdapt")
public class AddInoutRecordToHcGovAdapt extends DatabusAdaptImpl {

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private BaseHcGovSendAsyn baseHcGovSendAsynImpl;

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    @Autowired
    private ICommunityLocationAttrInnerServiceSMO communityLocationAttrInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;


    /**
     * @param customBusinessDatabusDto 当前处理业务
     */
    @Override
    public void customExchange(CustomBusinessDatabusDto customBusinessDatabusDto) {
        JSONObject data = customBusinessDatabusDto.getData();
        doInoutRecord(null, data);
    }

    private void doInoutRecord(Business business, JSONObject businessInoutRecord) {

        MachineRecordPo machineRecordPo = BeanConvertUtil.covertBean(businessInoutRecord, MachineRecordPo.class);
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(machineRecordPo.getCommunityId());
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);

        Assert.listNotNull(communityDtos, "未包含小区信息");
        CommunityDto tmpCommunityDto = communityDtos.get(0);
        String extCommunityId = "";
        String communityId = tmpCommunityDto.getCommunityId();
        String machineRecordId = machineRecordPo.getMachineRecordId();

        for (CommunityAttrDto communityAttrDto : tmpCommunityDto.getCommunityAttrDtos()) {
            if (HcGovConstant.EXT_COMMUNITY_ID.equals(communityAttrDto.getSpecCd())) {
                extCommunityId = communityAttrDto.getValue();
            }
        }

        //查询设备对应的位置
        MachineDto machineDto = new MachineDto();
        machineDto.setCommunityId(communityId);
        machineDto.setMachineId(machineRecordPo.getMachineId());
        List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);

        Assert.listOnlyOne(machineDtos, "不包含 设备信息");

        String locationId = machineDtos.get(0).getLocationTypeCd();

        CommunityLocationAttrDto communityLocationAttrDto = new CommunityLocationAttrDto();
        communityLocationAttrDto.setCommunityId(machineDtos.get(0).getCommunityId());
        communityLocationAttrDto.setLocationId(locationId);
        communityLocationAttrDto.setSpecCd(HcGovConstant.EXT_COMMUNITY_ID);
        List<CommunityLocationAttrDto> communityLocationAttrDtos
                = communityLocationAttrInnerServiceSMOImpl.queryCommunityLocationAttrs(communityLocationAttrDto);

        Assert.listOnlyOne(communityLocationAttrDtos, "未找到 位置外部ID");

        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setRelTypeCd("60000");
        fileRelDto.setObjId(machineRecordPo.getMachineRecordId());
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
        String url = "";
        String imgUrl = MappingCache.getValue(MappingConstant.FILE_DOMAIN,"IMG_PATH");
        if (fileRelDtos != null && fileRelDtos.size() > 0) {
            url = imgUrl + fileRelDtos.get(0).getFileRealName();
        }

        JSONObject body = new JSONObject();
        body.put("extLocationId", communityLocationAttrDtos.get(0).getValue());
        body.put("name", machineRecordPo.getName());
        body.put("openTypeCd", machineRecordPo.getOpenTypeCd());
        body.put("tel", machineRecordPo.getTel());
        body.put("idCard", machineRecordPo.getIdCard());
        body.put("recordTypeCd", machineRecordPo.getRecordTypeCd());
        body.put("faceUrl", url);
        String state = "F";
        if (StringUtil.isNumber(machineRecordPo.getSimilar())) {
            double similar = Double.parseDouble(machineRecordPo.getSimilar());
            if (similar > 0.5) {
                state = "C";
            }
        }
        body.put("state", state);

        JSONObject kafkaData = baseHcGovSendAsynImpl.createHeadersOrBody(body, extCommunityId, HcGovConstant.ADD_INOUT_RECORD_ACTION, HcGovConstant.COMMUNITY_SECURE);
        baseHcGovSendAsynImpl.sendKafka(HcGovConstant.GOV_TOPIC, kafkaData, communityId, machineRecordId, HcGovConstant.COMMUNITY_SECURE);
    }

}
