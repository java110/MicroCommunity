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
package com.java110.job.adapt.hcGov.location;

import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.community.CommunityLocationAttrDto;
import com.java110.dto.hcGovTranslate.HcGovTranslateDto;
import com.java110.dto.reportData.ReportDataDto;
import com.java110.intf.common.IHcGovTranslateInnerServiceSMO;
import com.java110.intf.community.ICommunityLocationAttrInnerServiceSMO;
import com.java110.job.adapt.hcGov.HcGovConstant;
import com.java110.job.adapt.hcGov.IReportReturnDataAdapt;
import com.java110.po.communityLocationAttr.CommunityLocationAttrPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 新增楼栋同步HC政务接口 返回
 * <p>
 * 接口协议地址： https://gitee.com/java110/microCommunityInformation/tree/master/info-doc#1%E6%A5%BC%E6%A0%8B%E4%B8%8A%E4%BC%A0
 *
 * @desc add by 吴学文 16:20
 */
@Component(value = "ADD_LOCATION_RETURN")
public class AddLocationToHcGovReturnAdapt implements IReportReturnDataAdapt {

    @Autowired
    private ICommunityLocationAttrInnerServiceSMO communityLocationAttrInnerServiceSMOImpl;
    @Autowired
    private IHcGovTranslateInnerServiceSMO hcGovTranslateInnerServiceSMOImpl;

    @Override
    public void reportReturn(ReportDataDto reportDataDto, String extCommunityId) {

        HcGovTranslateDto hcGovTranslateDto = new HcGovTranslateDto();
        hcGovTranslateDto.setTranId(reportDataDto.getReportDataHeaderDto().getTranId());
        hcGovTranslateDto.setServiceCode(reportDataDto.getReportDataHeaderDto().getServiceCode());
        List<HcGovTranslateDto> hcGovTranslateDtos = hcGovTranslateInnerServiceSMOImpl.queryHcGovTranslates(hcGovTranslateDto);
        if (hcGovTranslateDtos == null || hcGovTranslateDtos.size() < 1) {
            throw new IllegalArgumentException("查询推送报文失败。不是同一订单信息");
        }

        CommunityLocationAttrDto communityLocationAttrDto = new CommunityLocationAttrDto();
        communityLocationAttrDto.setLocationId(hcGovTranslateDtos.get(0).getObjId());
        communityLocationAttrDto.setCommunityId(hcGovTranslateDtos.get(0).getCommunityId());
        communityLocationAttrDto.setSpecCd( HcGovConstant.EXT_COMMUNITY_ID);
        List<CommunityLocationAttrDto> communityLocationAttrDtos = communityLocationAttrInnerServiceSMOImpl.queryCommunityLocationAttrs(communityLocationAttrDto);

        CommunityLocationAttrPo communityLocationAttrPo = new CommunityLocationAttrPo();
        communityLocationAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_locationId));
        communityLocationAttrPo.setLocationId(communityLocationAttrDto.getLocationId());
        communityLocationAttrPo.setCommunityId(communityLocationAttrDto.getCommunityId());
        communityLocationAttrPo.setSpecCd(communityLocationAttrDto.getSpecCd());
        communityLocationAttrPo.setValue(reportDataDto.getReportDataBodyDto().getString("extLocationId"));
        if (communityLocationAttrDtos == null || communityLocationAttrDtos.size() < 1) {
            int flag = communityLocationAttrInnerServiceSMOImpl.saveCommunityLocationAttr(communityLocationAttrPo);
            if (flag < 1) {
                throw new IllegalArgumentException("保存楼栋属性失败");
            }
        }


    }
}
