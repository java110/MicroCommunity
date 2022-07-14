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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.CommunityMemberDto;
import com.java110.dto.community.CommunityAttrDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.org.OrgDto;
import com.java110.dto.org.OrgStaffRelDto;
import com.java110.dto.store.StoreAttrDto;
import com.java110.dto.store.StoreUserDto;
import com.java110.entity.order.Business;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.store.IStoreAttrInnerServiceSMO;
import com.java110.intf.store.IStoreInnerServiceSMO;
import com.java110.intf.user.IOrgInnerServiceSMO;
import com.java110.intf.user.IOrgStaffRelInnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.adapt.hcGov.HcGovConstant;
import com.java110.job.adapt.hcGov.asyn.BaseHcGovSendAsyn;
import com.java110.po.user.UserPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 新增业主信息同步HC政务接口
 * <p>
 * 接口协议地址： https://gitee.com/java110/microCommunityInformation/tree/master/info-doc#1%E6%A5%BC%E6%A0%8B%E4%B8%8A%E4%BC%A0
 *
 * @desc add by 吴学文 16:20
 */
@Component(value = "addStaffToHcGovAdapt")
public class AddStaffToHcGovAdapt extends DatabusAdaptImpl {

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;
    @Autowired
    private BaseHcGovSendAsyn baseHcGovSendAsynImpl;
    @Autowired
    private IStoreInnerServiceSMO storeInnerServiceSMOImpl;
    @Autowired
    private IStoreAttrInnerServiceSMO storeAttrInnerServiceSMOImpl;
    @Autowired
    private IOrgStaffRelInnerServiceSMO orgStaffRelInnerServiceSMOImpl;
    @Autowired
    private IOrgInnerServiceSMO orgInnerServiceSMOImpl;

    /**
     * @param business   当前处理业务
     * @param businesses 所有业务信息
     */
    @Override
    public void execute(Business business, List<Business> businesses) {
        JSONObject data = business.getData();
        JSONArray businessUserPo = new JSONArray();
        if (data.containsKey(UserPo.class.getSimpleName())) {
            Object bObj = data.get(UserPo.class.getSimpleName());
            if (bObj instanceof JSONObject) {
                businessUserPo.add(bObj);
            } else if (bObj instanceof List) {
                businessUserPo = JSONArray.parseArray(JSONObject.toJSONString(bObj));
            } else {
                businessUserPo = (JSONArray) bObj;
            }
        }else {
            if (data instanceof JSONObject) {
                businessUserPo.add(data);
            }
        }

        //JSONObject businessOwnerCar = data.getJSONObject("businessOwnerCar");
        for (int bUserIndex = 0; bUserIndex < businessUserPo.size(); bUserIndex++) {
            JSONObject businessUserCar = businessUserPo.getJSONObject(bUserIndex);
            doAddUser(business, businessUserCar);

        }
    }

    private void doAddUser(Business business, JSONObject businessUserCar) {

        UserPo userPo = BeanConvertUtil.covertBean(businessUserCar, UserPo.class);
        StoreUserDto storeUserDto = new StoreUserDto();
        storeUserDto.setUserId(userPo.getUserId());
        List<StoreUserDto> storeUserDtos = storeInnerServiceSMOImpl.getStoreUserInfo(storeUserDto);
        if (storeUserDtos == null || storeUserDtos.size() < 1) {
            return;
        }
        storeUserDto = storeUserDtos.get(0);

        String extCompanyId = "";
        String ACTION_TYPE = HcGovConstant.ADD_STAFF_ACTION;
        String SoreId = storeUserDto.getStoreId();

        //查询商户对应的小区外部编码
        CommunityMemberDto communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setMemberId(SoreId);
        List<CommunityMemberDto> communityMemberDtos = communityInnerServiceSMOImpl.getCommunityMembers(communityMemberDto);
        JSONArray extCommunityId = new JSONArray();
        //获取商户对应所有小区的外部编码
        getCommunityExtId(communityMemberDtos, extCommunityId);

        if (extCommunityId == null || extCommunityId.size() < 1) {
            return;
        }
        StoreAttrDto storeAttrDto = new StoreAttrDto();
        //查询商户属性 外部编码
        storeAttrDto.setStoreId(SoreId);
        storeAttrDto.setSpecCd(HcGovConstant.EXT_COMMUNITY_ID);
        List<StoreAttrDto> storeAttrDtos = storeAttrInnerServiceSMOImpl.queryStoreAttrs(storeAttrDto);
        if (storeAttrDtos != null && storeAttrDtos.size() > 0) {
            extCompanyId = storeAttrDtos.get(0).getValue();
        }
        JSONObject body = new JSONObject();
        if (StringUtil.isEmpty(extCompanyId)) {
            body.put("companyInfo", getCarateStoreInfo(SoreId,storeUserDto));
            ACTION_TYPE=HcGovConstant.ADD_COMPANY_ACTION;
        }

        body.put("staffInfo", getCarateStaffInfo(userPo,extCompanyId));

        JSONObject kafkaData = baseHcGovSendAsynImpl.createHeadersOrBody(body, "622021090866160001", ACTION_TYPE, HcGovConstant.COMMUNITY_SECURE);
        baseHcGovSendAsynImpl.sendKafka(HcGovConstant.GOV_TOPIC, kafkaData, "622021090866160001", userPo.getUserId(), HcGovConstant.COMMUNITY_SECURE);
    }

    public void getCommunityExtId(List<CommunityMemberDto> communityMemberDtos, JSONArray extCommunityId) {
        if (communityMemberDtos == null || communityMemberDtos.size() == 0) {
            return;
        }
        for (CommunityMemberDto communityMember : communityMemberDtos) {
            CommunityDto communityDto = new CommunityDto();
            communityDto.setCommunityId(communityMember.getCommunityId());
            List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);
            if (communityDtos == null || communityDtos.size() == 0) {
                continue;
            }
            extracted(extCommunityId, communityDtos);
        }
    }

    private void extracted(JSONArray extCommunityId, List<CommunityDto> communityDtos) {
        for (CommunityDto community : communityDtos) {
            for (CommunityAttrDto communityAttrDto : community.getCommunityAttrDtos()) {
                if (HcGovConstant.EXT_COMMUNITY_ID.equals(communityAttrDto.getSpecCd())) {
                    extCommunityId.add(communityAttrDto.getValue());
                }
            }
        }
    }

    private JSONObject getCarateStaffInfo(UserPo userPo,String extCompanyId) {

        String orgName = "";
        String relCd = "";
        JSONObject staffInfo = new JSONObject();

        staffInfo.put("personName", userPo.getName());
        staffInfo.put("personTel", userPo.getTel());
        staffInfo.put("personSex", userPo.getSex());
        staffInfo.put("prePersonName", userPo.getName());

        OrgStaffRelDto orgStaffRelDto = new OrgStaffRelDto();
        orgStaffRelDto.setStaffId(userPo.getUserId());
        List<OrgStaffRelDto> orgStaffRelDtos = orgStaffRelInnerServiceSMOImpl.queryOrgStaffRels(orgStaffRelDto);
        if (orgStaffRelDtos != null || orgStaffRelDtos.size() > 0) {
            OrgDto orgDto = new OrgDto();
            orgDto.setOrgId(orgStaffRelDtos.get(0).getOrgId());
            List<OrgDto> orgDtos = orgInnerServiceSMOImpl.queryOrgs(orgDto);
            if (orgDtos != null || orgDtos.size() > 0) {
                orgName = orgDtos.get(0).getOrgName();
            }
            relCd = orgStaffRelDtos.get(0).getRelCd();
        }
        staffInfo.put("govOrgName", orgName);
        staffInfo.put("relCd", relCd);
        if(!StringUtil.isEmpty(extCompanyId)){
            staffInfo.put("extCompanyId",extCompanyId);
        }

        return staffInfo;
    }

    private JSONObject getCarateStoreInfo(String SoreId, StoreUserDto storeUserDto) {

        String artificialPerson = "";
        String registerTime = "";
        String idCard = "";
        JSONObject companyInfo = new JSONObject();

        StoreAttrDto storeAttrDto = new StoreAttrDto();
        //查询商户属性 企业法人
        storeAttrDto.setStoreId(SoreId);
        storeAttrDto.setSpecCd(HcGovConstant.STORE_ATTR_ARTIFICIALPERSON);
        List<StoreAttrDto> storeAttrDtos = storeAttrInnerServiceSMOImpl.queryStoreAttrs(storeAttrDto);
        if (storeAttrDtos != null && storeAttrDtos.size() > 0) {
            artificialPerson = storeAttrDtos.get(0).getValue();
        }
        //查询商户属性 成立日期
        storeAttrDto.setStoreId(SoreId);
        storeAttrDto.setSpecCd(HcGovConstant.STORE_ATTR_REGISTERTIME);
        storeAttrDtos = storeAttrInnerServiceSMOImpl.queryStoreAttrs(storeAttrDto);
        if (storeAttrDtos != null && storeAttrDtos.size() > 0) {
            registerTime = storeAttrDtos.get(0).getValue();
        }
        //查询商户属性 营业执照
        storeAttrDto.setStoreId(SoreId);
        storeAttrDto.setSpecCd(HcGovConstant.STORE_ATTR_IDCARD);
        storeAttrDtos = storeAttrInnerServiceSMOImpl.queryStoreAttrs(storeAttrDto);
        if (storeAttrDtos != null && storeAttrDtos.size() > 0) {
            idCard = storeAttrDtos.get(0).getValue();
        }
        companyInfo.put("companyName", storeUserDto.getName());
        companyInfo.put("companyType", "2002");
        companyInfo.put("idCard", idCard);
        companyInfo.put("artificialPerson", artificialPerson);
        companyInfo.put("companyAddress", storeUserDto.getAddress());
        companyInfo.put("registerTime", registerTime);
        companyInfo.put("personName", artificialPerson);
        companyInfo.put("personTel", storeUserDto.getTel());
        companyInfo.put("personIdCard", idCard);

        return companyInfo;
    }

}
