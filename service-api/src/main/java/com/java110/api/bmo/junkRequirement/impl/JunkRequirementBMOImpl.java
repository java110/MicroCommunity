package com.java110.api.bmo.junkRequirement.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.junkRequirement.IJunkRequirementBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.user.IJunkRequirementInnerServiceSMO;
import com.java110.dto.junkRequirement.JunkRequirementDto;
import com.java110.po.file.FileRelPo;
import com.java110.po.junkRequirement.JunkRequirementPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("junkRequirementBMOImpl")
public class JunkRequirementBMOImpl extends ApiBaseBMO implements IJunkRequirementBMO {

    @Autowired
    private IJunkRequirementInnerServiceSMO junkRequirementInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addJunkRequirement(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject businessJunkRequirement = new JSONObject();
        businessJunkRequirement.putAll(paramInJson);
        if (!paramInJson.containsKey("junkRequirementId")) {
            businessJunkRequirement.put("junkRequirementId", "-1");
        }
        JunkRequirementPo junkRequirementPo = BeanConvertUtil.covertBean(businessJunkRequirement, JunkRequirementPo.class);

        super.insert(dataFlowContext, junkRequirementPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_JUNK_REQUIREMENT);

    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateJunkRequirement(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        JunkRequirementDto junkRequirementDto = new JunkRequirementDto();
        junkRequirementDto.setJunkRequirementId(paramInJson.getString("junkRequirementId"));
//        junkRequirementDto.setCommunityId(paramInJson.getString("communityId"));
        List<JunkRequirementDto> junkRequirementDtos = junkRequirementInnerServiceSMOImpl.queryJunkRequirements(junkRequirementDto);

        Assert.listOnlyOne(junkRequirementDtos, "未找到需要修改的活动 或多条数据");

        JSONObject businessJunkRequirement = new JSONObject();
        businessJunkRequirement.putAll(BeanConvertUtil.beanCovertMap(junkRequirementDtos.get(0)));
        businessJunkRequirement.putAll(paramInJson);
        JunkRequirementPo junkRequirementPo = BeanConvertUtil.covertBean(businessJunkRequirement, JunkRequirementPo.class);

        super.update(dataFlowContext, junkRequirementPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_JUNK_REQUIREMENT);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteJunkRequirement(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject businessJunkRequirement = new JSONObject();
        businessJunkRequirement.putAll(paramInJson);
        JunkRequirementPo junkRequirementPo = BeanConvertUtil.covertBean(paramInJson, JunkRequirementPo.class);

        super.delete(dataFlowContext, junkRequirementPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_JUNK_REQUIREMENT);
    }

    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addPhoto(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        JSONObject businessUnit = new JSONObject();
        businessUnit.put("fileRelId", "-1");
        businessUnit.put("relTypeCd", "80000");
        businessUnit.put("saveWay", "ftp");
        businessUnit.put("objId", paramInJson.getString("junkRequirementId"));
        businessUnit.put("fileRealName", paramInJson.getString("photoId"));
        businessUnit.put("fileSaveName", paramInJson.getString("fileSaveName"));
        FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);

        super.insert(dataFlowContext, fileRelPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FILE_REL);
    }

}
