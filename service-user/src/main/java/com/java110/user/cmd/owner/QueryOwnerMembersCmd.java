package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.api.ApiOwnerDataVo;
import com.java110.vo.api.ApiOwnerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@Java110Cmd(serviceCode = "owner.queryOwnerMembers")
public class QueryOwnerMembersCmd extends Cmd {

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求中未包含communityId信息");
        Assert.jsonObjectHaveKey(reqJson, "ownerId", "请求中未包含ownerId信息");
        // Assert.jsonObjectHaveKey(reqJson, "ownerTypeCd", "请求中未包含ownerTypeCd信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        String userId = context.getReqHeaders().get("user-id");
        OwnerDto ownerDto = BeanConvertUtil.covertBean(reqJson, OwnerDto.class);
        ownerDto.setOwnerTypeCds(new String[]{"1002", "1003", "1004", "1005"});
        List<OwnerDto> ownerDtoList = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);
        //查询是否有脱敏权限
        List<Map> privileges = null;
        BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
        basePrivilegeDto.setResource("/roomCreateFee");
        basePrivilegeDto.setUserId(userId);
        privileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);
        if (privileges == null || privileges.size() == 0) {
            for (OwnerDto owner : ownerDtoList) {
                String idCard = owner.getIdCard();
                if (!StringUtil.isEmpty(idCard)) {
                    idCard = idCard.substring(0, 6) + "**********" + idCard.substring(16);
                    owner.setIdCard(idCard);
                }
                String link = owner.getLink();
                if (!StringUtil.isEmpty(link)) {
                    link = link.substring(0, 3) + "****" + link.substring(7);
                    owner.setLink(link);
                }
            }
        }
        for (OwnerDto ownerdto : ownerDtoList) {
            FileRelDto fileRelDto = new FileRelDto();
            fileRelDto.setObjId(ownerdto.getMemberId());
            List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
            if(fileRelDtos != null && fileRelDtos.size() > 0){
                ownerdto.setUrl(fileRelDtos.get(0).getFileSaveName());
            }
        }
        ApiOwnerVo apiOwnerVo = new ApiOwnerVo();
        apiOwnerVo.setOwners(BeanConvertUtil.covertBeanList(ownerDtoList, ApiOwnerDataVo.class));
        apiOwnerVo.setTotal(ownerDtoList.size());
        apiOwnerVo.setRecords(1);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiOwnerVo), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }
}
