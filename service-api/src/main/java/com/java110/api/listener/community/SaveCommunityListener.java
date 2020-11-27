package com.java110.api.listener.community;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.community.ICommunityBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.workflow.WorkflowDto;
import com.java110.intf.common.IWorkflowInnerServiceSMO;
import com.java110.po.workflow.WorkflowPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveCommunityListener")
public class SaveCommunityListener extends AbstractServiceApiPlusListener {

    @Autowired
    private ICommunityBMO communityBMOImpl;

    @Autowired
    private IWorkflowInnerServiceSMO workflowInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "name", "必填，请填写小区名称");
        Assert.hasKeyAndValue(reqJson, "address", "必填，请填写小区地址");
        Assert.hasKeyAndValue(reqJson, "nearbyLandmarks", "必填，请填写小区附近地标");

        //属性校验
        Assert.judgeAttrValue(reqJson);

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {


        communityBMOImpl.addCommunity(reqJson, context);
        communityBMOImpl.addCommunityMembers(reqJson, context);
        //产生物业费配置信息
        communityBMOImpl.addFeeConfigProperty(reqJson, context);
        communityBMOImpl.addFeeConfigRepair(reqJson, context); // 报修费用
        communityBMOImpl.addFeeConfigParkingSpaceTemp(reqJson, context);//地下出租

        dealAttr(reqJson, context);

        WorkflowPo workflowPo = null;
        workflowPo = new WorkflowPo();
        workflowPo.setCommunityId(reqJson.getString("communityId"));
        workflowPo.setFlowId("-1");
        workflowPo.setFlowName("投诉建议流程");
        workflowPo.setFlowType(WorkflowDto.FLOW_TYPE_COMPLAINT);
        workflowPo.setSkipLevel(WorkflowDto.DEFAULT_SKIP_LEVEL);
        workflowPo.setStoreId(reqJson.getString("storeId"));
        super.insert(context, workflowPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_WORKFLOW);
    }


    private void dealAttr(JSONObject paramObj, DataFlowContext context) {

        if (!paramObj.containsKey("attrs")) {
            return;
        }

        JSONArray attrs = paramObj.getJSONArray("attrs");
        if (attrs.size() < 1) {
            return;
        }


        JSONObject attr = null;
        for (int attrIndex = 0; attrIndex < attrs.size(); attrIndex++) {
            attr = attrs.getJSONObject(attrIndex);
            attr.put("communityId", paramObj.getString("communityId"));
            communityBMOImpl.addAttr(attr, context);
        }

    }


    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_SAVE_COMMUNITY;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

}
