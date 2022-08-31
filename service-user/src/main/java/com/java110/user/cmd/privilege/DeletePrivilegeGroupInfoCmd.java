package com.java110.user.cmd.privilege;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.privilegeRel.PrivilegeRelDto;
import com.java110.dto.privilegeUser.PrivilegeUserDto;
import com.java110.intf.order.IPrivilegeInnerServiceSMO;
import com.java110.intf.store.IStoreV1InnerServiceSMO;
import com.java110.intf.user.IPrivilegeGroupV1InnerServiceSMO;
import com.java110.intf.user.IPrivilegeRelV1InnerServiceSMO;
import com.java110.intf.user.IPrivilegeUserV1InnerServiceSMO;
import com.java110.po.privilegeGroup.PrivilegeGroupPo;
import com.java110.po.privilegeRel.PrivilegeRelPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "delete.privilegeGroup.info")
public class DeletePrivilegeGroupInfoCmd extends Cmd{

    @Autowired
    private IStoreV1InnerServiceSMO storeV1InnerServiceSMOImpl;

    @Autowired
    private IPrivilegeInnerServiceSMO privilegeInnerServiceSMOImpl;

    @Autowired
    private IPrivilegeGroupV1InnerServiceSMO privilegeGroupV1InnerServiceSMOImpl;

    @Autowired
    private IPrivilegeRelV1InnerServiceSMO privilegeRelV1InnerServiceSMOImpl;

    @Autowired
    private IPrivilegeUserV1InnerServiceSMO privilegeUserV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson,"pgId","角色不存在");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        PrivilegeUserDto privilegeUserDto = new PrivilegeUserDto();
        privilegeUserDto.setpId(reqJson.getString("pgId"));
        List<PrivilegeUserDto> privilegeUserDtos = privilegeUserV1InnerServiceSMOImpl.queryPrivilegeUsers(privilegeUserDto);
        if(privilegeUserDtos != null && privilegeUserDtos.size() > 0){
            throw new IllegalArgumentException("该角色下有关联员工，请先删除关联员工！");
        }
        PrivilegeGroupPo privilegeGroupPo = BeanConvertUtil.covertBean(reqJson,PrivilegeGroupPo.class);
        int flag = privilegeGroupV1InnerServiceSMOImpl.deletePrivilegeGroup(privilegeGroupPo);
        if(flag  < 1){
            throw new CmdException("删除失败");
        }
        PrivilegeRelDto privilegeRelDto = new PrivilegeRelDto();
        privilegeRelDto.setPgId(reqJson.getString("pgId"));
        List<PrivilegeRelDto> privilegeRelDtos = privilegeRelV1InnerServiceSMOImpl.queryPrivilegeRels(privilegeRelDto);
        if(privilegeRelDtos == null || privilegeRelDtos.size()<1){
            return ;
        }
        PrivilegeRelPo privilegeRelPo = null;
        for(PrivilegeRelDto tmpPrivilegeDto: privilegeRelDtos){
            privilegeRelPo = new PrivilegeRelPo();
            privilegeRelPo.setRelId(tmpPrivilegeDto.getRelId());
            flag = privilegeRelV1InnerServiceSMOImpl.deletePrivilegeRel(privilegeRelPo);
            if(flag <1){
                throw new CmdException("删除失败");
            }
        }
    }
}
