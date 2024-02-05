package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.privilege.BasePrivilegeDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.api.ApiOwnerDataVo;
import com.java110.vo.api.ApiOwnerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
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
        // Assert.jsonObjectHaveKey(reqJson, "ownerTypeCd", "请求中未包含ownerTypeCd信息");
        if (!reqJson.containsKey("page")) {
            reqJson.put("page", 1);
        }
        if (!reqJson.containsKey("row")) {
            reqJson.put("row", 10);
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        String userId = context.getReqHeaders().get("user-id");
        OwnerDto ownerDto = BeanConvertUtil.covertBean(reqJson, OwnerDto.class);
        ownerDto.setOwnerTypeCds(new String[]{"1002", "1003", "1004", "1005"});

        int row = reqJson.getInteger("row");
        //查询总记录数
        int total = ownerInnerServiceSMOImpl.queryOwnersMemberCount(ownerDto);

        List<OwnerDto> ownerDtoList = null;
        if (total > 0) {
            ownerDtoList = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);
        } else {
            ownerDtoList = new ArrayList<>();
        }
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
        String imgUrl = MappingCache.getValue(MappingConstant.FILE_DOMAIN, "IMG_PATH");

        for (OwnerDto ownerdto : ownerDtoList) {
            FileRelDto fileRelDto = new FileRelDto();
            fileRelDto.setObjId(ownerdto.getMemberId());
            List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
            if (ListUtil.isNull(fileRelDtos)) {
                continue;
            }
            ownerdto.setUrl(fileRelDtos.get(0).getFileSaveName());
            if (fileRelDtos.get(0).getFileSaveName().startsWith("http")) {
                ownerdto.setUrl(fileRelDtos.get(0).getFileSaveName());
            } else {
                ownerdto.setUrl(imgUrl + fileRelDtos.get(0).getFileSaveName());
            }
        }
        ApiOwnerVo apiOwnerVo = new ApiOwnerVo();
        apiOwnerVo.setOwners(BeanConvertUtil.covertBeanList(ownerDtoList, ApiOwnerDataVo.class));
        apiOwnerVo.setTotal(total);
        apiOwnerVo.setRecords((int) Math.ceil((double) total / (double) row));

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiOwnerVo), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }
}
