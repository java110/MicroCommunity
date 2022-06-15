package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.auditAppUserBindingOwner.ApiAuditAppUserBindingOwnerDataVo;
import com.java110.vo.api.auditAppUserBindingOwner.ApiAuditAppUserBindingOwnerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Java110Cmd(serviceCode = "owner.listAuditAppUserBindingOwners")
public class ListAuditAppUserBindingOwnersCmd extends Cmd {

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");

        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        //获取当前用户id
        String userId = reqJson.getString("userId");
        OwnerAppUserDto ownerAppUserDto = BeanConvertUtil.covertBean(reqJson, OwnerAppUserDto.class);
        ownerAppUserDto.setUserId("");
        int count = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsersCount(ownerAppUserDto);

        List<ApiAuditAppUserBindingOwnerDataVo> auditAppUserBindingOwners = null;
        List<ApiAuditAppUserBindingOwnerDataVo> ownerDtos = new ArrayList<>();
        if (count > 0) {
            auditAppUserBindingOwners = BeanConvertUtil.covertBeanList(ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto), ApiAuditAppUserBindingOwnerDataVo.class);
            List<Map> mark = getPrivilegeOwnerList("/roomCreateFee", userId);
            for (ApiAuditAppUserBindingOwnerDataVo owner : auditAppUserBindingOwners) {
                //区分小程序和和公众号
                if (owner.getAppType().equals("WECHAT")) {
                    owner.setAppTypeName("公众号");
                } else if (owner.getAppType().equals("WECHAT_MINA")) {
                    owner.setAppTypeName("小程序");
                }
                //对业主身份证号隐藏处理
                String idCard = owner.getIdCard();
                if (mark.size() == 0 && idCard != null && !idCard.equals("") && idCard.length() > 16) {
                    idCard = idCard.substring(0, 6) + "**********" + idCard.substring(16);
                    owner.setIdCard(idCard);
                }
                //对业主手机号隐藏处理
                String link = owner.getLink();
                if (mark.size() == 0 && link != null && !link.equals("") && link.length() == 11) {
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

        cmdDataFlowContext.setResponseEntity(responseEntity);
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
