package com.java110.user.cmd.user;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.org.OrgDto;
import com.java110.dto.org.OrgStaffRelDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.store.IOrgStaffRelV1InnerServiceSMO;
import com.java110.intf.user.IOrgStaffRelInnerServiceSMO;
import com.java110.intf.user.IOrgV1InnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.PinYinUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.api.staff.ApiStaffDataVo;
import com.java110.vo.api.staff.ApiStaffVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "query.staff.infos")
public class QueryStaffInfosCmd extends Cmd {

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IOrgV1InnerServiceSMO orgV1InnerServiceSMOImpl;

    @Autowired
    private IOrgStaffRelV1InnerServiceSMO orgStaffRelV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson,"page","请求报文中未包含page节点");
        Assert.hasKeyAndValue(reqJson,"row","请求报文中未包含rows节点");
        if (!reqJson.containsKey("storeId")) {
            String storeId = context.getReqHeaders().get("store-id");
            reqJson.put("storeId", storeId);
        }
        Assert.hasKeyAndValue(reqJson,"storeId","请求报文中未包含storeId节点");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        UserDto userDto = BeanConvertUtil.covertBean(reqJson, UserDto.class);

        int count = userInnerServiceSMOImpl.getStaffCount(userDto);

        List<ApiStaffDataVo> staffs = null;

        if (count > 0) {
            staffs = BeanConvertUtil.covertBeanList(userInnerServiceSMOImpl.getStaffs(userDto), ApiStaffDataVo.class);
            refreshInitials(staffs);
            refreshOrgs(staffs,reqJson.getString("storeId"));
        } else {
            staffs = new ArrayList<>();
        }

        ApiStaffVo apiStaffVo = new ApiStaffVo();

        apiStaffVo.setTotal(count);
        apiStaffVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiStaffVo.setStaffs(staffs);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiStaffVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    private void refreshOrgs(List<ApiStaffDataVo> staffs,String storeId) {
        if(staffs == null ||  staffs.size()<1){
            return ;
        }

        List<String>  staffIds  = new ArrayList<>();
        for(ApiStaffDataVo apiStaffDataVo : staffs){
            staffIds.add(apiStaffDataVo.getUserId());
        }

        OrgDto orgDto = new OrgDto();
        orgDto.setStoreId(storeId);
        List<OrgDto> orgDtos = orgV1InnerServiceSMOImpl.queryOrgs(orgDto);
        if (orgDtos == null || orgDtos.size() < 1) {
            return;
        }
        OrgStaffRelDto orgStaffRelDto = new OrgStaffRelDto();
        orgStaffRelDto.setStaffIds(staffIds.toArray(new String[staffIds.size()]));
        orgStaffRelDto.setStoreId(storeId);
        List<OrgStaffRelDto> orgStaffRels = orgStaffRelV1InnerServiceSMOImpl.queryOrgStaffRels(orgStaffRelDto);

        if (orgStaffRels == null || orgStaffRels.size() < 1) {
            return;
        }


        for(ApiStaffDataVo apiStaffDataVo : staffs){
            for(OrgStaffRelDto tmpOrgStaffRelDto : orgStaffRels){
                if(!apiStaffDataVo.getUserId().equals(tmpOrgStaffRelDto.getStaffId())){
                    continue;
                }
               apiStaffDataVo.setOrgId(tmpOrgStaffRelDto.getOrgId());
            }
        }

        for(ApiStaffDataVo apiStaffDataVo : staffs) {
            if(StringUtil.isEmpty(apiStaffDataVo.getOrgId())){
                continue;
            }
            apiStaffDataVo.setParentOrgId(apiStaffDataVo.getOrgId());

            findParents(apiStaffDataVo, orgDtos, null,0);

        }

    }


    private void findParents(ApiStaffDataVo apiStaffDataVo, List<OrgDto> orgDtos, OrgDto curOrgDto,int orgDeep) {
        for (OrgDto orgDto : orgDtos) {
            curOrgDto = orgDto;
            if (!apiStaffDataVo.getParentOrgId().equals(orgDto.getOrgId())) { // 他自己跳过
                continue;
            }
            apiStaffDataVo.setParentOrgId(orgDto.getParentOrgId());

            if (StringUtil.isEmpty(apiStaffDataVo.getOrgName())) {
                apiStaffDataVo.setOrgName(orgDto.getOrgName() );
                continue;
            }
            apiStaffDataVo.setOrgName(orgDto.getOrgName() + " / " + apiStaffDataVo.getOrgName());
        }

        if (curOrgDto != null && OrgDto.ORG_LEVEL_STORE.equals(curOrgDto.getOrgLevel())) {
            return;
        }

        if (curOrgDto != null && curOrgDto.getParentOrgId().equals(curOrgDto.getOrgId())) {
            return;
        }

        if (curOrgDto != null && "-1".equals(curOrgDto.getParentOrgId())) {
            return;
        }

        orgDeep +=1;

        if(orgDeep > 20){
            return ;
        }

        findParents(apiStaffDataVo, orgDtos, curOrgDto,orgDeep);
    }

    /**
     * 刷入首字母
     * @param staffs
     */
    private void refreshInitials(List<ApiStaffDataVo> staffs) {

        for(ApiStaffDataVo staffDataVo : staffs){
            if(StringUtil.isEmpty(staffDataVo.getName())){
                continue;
            }
            staffDataVo.setInitials(PinYinUtil.getFirstSpell(staffDataVo.getName()).toUpperCase().charAt(0)+"");
        }
    }
}
