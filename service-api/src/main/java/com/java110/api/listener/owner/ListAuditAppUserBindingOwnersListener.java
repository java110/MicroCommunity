package com.java110.api.listener.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserInnerServiceSMO;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.auditAppUserBindingOwner.ApiAuditAppUserBindingOwnerDataVo;
import com.java110.vo.api.auditAppUserBindingOwner.ApiAuditAppUserBindingOwnerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 查询小区侦听类
 */
@Java110Listener("listAuditAppUserBindingOwnersListener")
public class ListAuditAppUserBindingOwnersListener extends AbstractServiceApiListener {

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.LIST_AUDIT_APPUSERBINDINGOWNERS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");

        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        //获取当前用户id
        String userId = context.getUserId();
        OwnerAppUserDto ownerAppUserDto = BeanConvertUtil.covertBean(reqJson, OwnerAppUserDto.class);

        int count = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsersCount(ownerAppUserDto);

        List<ApiAuditAppUserBindingOwnerDataVo> auditAppUserBindingOwners = null;
        List<ApiAuditAppUserBindingOwnerDataVo> ownerDtos = new ArrayList<>();
        if (count > 0) {
            auditAppUserBindingOwners = BeanConvertUtil.covertBeanList(ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto), ApiAuditAppUserBindingOwnerDataVo.class);
            List<Map> mark = getPrivilegeOwnerList("/roomCreateFee", userId);
            for (ApiAuditAppUserBindingOwnerDataVo owner : auditAppUserBindingOwners) {
                //对业主身份证号隐藏处理
                String idCard = owner.getIdCard();
                if (mark.size() == 0 && idCard != null && !idCard.equals("")) {
                    idCard = idCard.substring(0, 6) + "**********" + idCard.substring(16);
                    owner.setIdCard(idCard);
                }
                //对业主手机号隐藏处理
                String link = owner.getLink();
                if (mark.size() == 0 && link != null && !link.equals("")) {
                    link = link.substring(0, 3) + "****" + link.substring(7);
                    owner.setLink(link);
                }
                ownerDtos.add(owner);
            }
        } else {
            auditAppUserBindingOwners = new ArrayList<>();
            ownerDtos.addAll(auditAppUserBindingOwners);
        }

        ApiAuditAppUserBindingOwnerVo apiAuditAppUserBindingOwnerVo = new ApiAuditAppUserBindingOwnerVo();

        apiAuditAppUserBindingOwnerVo.setTotal(count);
        apiAuditAppUserBindingOwnerVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiAuditAppUserBindingOwnerVo.setAuditAppUserBindingOwners(ownerDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiAuditAppUserBindingOwnerVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }

    public IOwnerAppUserInnerServiceSMO getOwnerAppUserInnerServiceSMOImpl() {
        return ownerAppUserInnerServiceSMOImpl;
    }

    public void setOwnerAppUserInnerServiceSMOImpl(IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl) {
        this.ownerAppUserInnerServiceSMOImpl = ownerAppUserInnerServiceSMOImpl;
    }

    /**
     * 脱敏处理
     *
     * @return
     */
    public List<Map> getPrivilegeOwnerList(String resource, String userId) {
        BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
        basePrivilegeDto.setResource(resource);
        basePrivilegeDto.setUserId(userId);
        List<Map> privileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);
        return privileges;
    }
}
