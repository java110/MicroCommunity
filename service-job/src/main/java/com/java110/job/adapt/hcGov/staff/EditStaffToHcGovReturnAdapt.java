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
package com.java110.job.adapt.hcGov.staff;

import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.hcGovTranslate.HcGovTranslateDto;
import com.java110.dto.reportData.ReportDataDto;
import com.java110.dto.user.UserAttrDto;
import com.java110.intf.common.IHcGovTranslateInnerServiceSMO;
import com.java110.intf.store.IStoreAttrInnerServiceSMO;
import com.java110.intf.store.IStoreInnerServiceSMO;
import com.java110.intf.user.IUserAttrInnerServiceSMO;
import com.java110.job.adapt.hcGov.HcGovConstant;
import com.java110.job.adapt.hcGov.IReportReturnDataAdapt;
import com.java110.po.userAttr.UserAttrPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 新增房屋同步HC政务接口 返回
 * <p>
 * 接口协议地址： https://gitee.com/java110/microCommunityInformation/tree/master/info-doc#1%E6%A5%BC%E6%A0%8B%E4%B8%8A%E4%BC%A0
 *
 * @desc add by 吴学文 16:20
 */
@Component(value = "EDIT_STAFF_RETURN")
public class EditStaffToHcGovReturnAdapt implements IReportReturnDataAdapt {

    @Autowired
    private IStoreAttrInnerServiceSMO storeAttrInnerServiceSMOImpl;
    @Autowired
    private IUserAttrInnerServiceSMO userAttrInnerServiceSMOImpl;
    @Autowired
    private IHcGovTranslateInnerServiceSMO hcGovTranslateInnerServiceSMOImpl;
    @Autowired
    private IStoreInnerServiceSMO storeInnerServiceSMOImpl;

    @Override
    public void reportReturn(ReportDataDto reportDataDto, String extCommunityId) {

        HcGovTranslateDto hcGovTranslateDto = new HcGovTranslateDto();
        hcGovTranslateDto.setTranId(reportDataDto.getReportDataHeaderDto().getTranId());
        hcGovTranslateDto.setServiceCode(reportDataDto.getReportDataHeaderDto().getServiceCode());
        List<HcGovTranslateDto> hcGovTranslateDtos = hcGovTranslateInnerServiceSMOImpl.queryHcGovTranslates(hcGovTranslateDto);
        if (hcGovTranslateDtos == null || hcGovTranslateDtos.size() < 1) {
            throw new IllegalArgumentException("查询推送报文失败。不是同一订单信息");
        }

        UserAttrDto userAttrDto = new UserAttrDto();
        userAttrDto.setUserId(hcGovTranslateDtos.get(0).getObjId());
        userAttrDto.setSpecCd(HcGovConstant.EXT_COMMUNITY_ID);
        List<UserAttrDto> userAttrDtos = userAttrInnerServiceSMOImpl.queryUserAttrs(userAttrDto);

        UserAttrPo userAttrPo = new UserAttrPo();
        userAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_userAnId));
        userAttrPo.setUserId(userAttrDto.getUserId());
        userAttrPo.setSpecCd(userAttrDto.getSpecCd());
        userAttrDto.setValue(reportDataDto.getReportDataBodyDto().getString("extPersonId"));

        if (userAttrDtos == null || userAttrDtos.size() < 1) {
            int flag = userAttrInnerServiceSMOImpl.saveUserAttr(userAttrPo);
            if (flag < 1) {
                throw new IllegalArgumentException("保存员工属性失败");
            }
        } else {
            userAttrPo.setAttrId(userAttrDtos.get(0).getAttrId());
            int flag = userAttrInnerServiceSMOImpl.updateUserAttrInfoInstance(userAttrPo);
            if (flag < 1) {
                throw new IllegalArgumentException("修改员工属性失败");
            }

        }


    }
}
