package com.java110.api.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.AppAbstractComponentSMO;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.core.component.BaseComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.dto.org.OrgStaffRelDto;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.api.smo.IStaffServiceSMO;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.user.IOrgStaffRelInnerServiceSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * 员工服务类
 * Created by Administrator on 2019/4/2.
 */
@Service("staffServiceSMOImpl")
public class StaffServiceSMOImpl extends DefaultAbstractComponentSMO implements IStaffServiceSMO {
    private final static Logger logger = LoggerFactory.getLogger(StaffServiceSMOImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private IOrgStaffRelInnerServiceSMO iOrgStaffRelInnerServiceSMO;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    /**
     * 添加员工信息
     *
     * @param pd
     * @return
     */
    @Override
    public ResponseEntity<String> saveStaff(IPageData pd) {
        logger.debug("保存员工信息入参：{}", pd.toString());
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        Assert.hasKeyAndValue(reqJson, "username", "请求报文格式错误或未包含用户名信息");
        //Assert.hasKeyAndValue(reqJson, "email", "请求报文格式错误或未包含邮箱信息");
        Assert.hasKeyAndValue(reqJson, "tel", "请求报文格式错误或未包含手机信息");
        Assert.hasKeyAndValue(reqJson, "sex", "请求报文格式错误或未包含性别信息");
        Assert.hasKeyAndValue(reqJson, "address", "请求报文格式错误或未包含地址信息");
        Assert.hasKeyAndValue(reqJson, "orgId", "请求报文格式错误或未包含部门信息");
        Assert.hasKeyAndValue(reqJson, "relCd", "请求报文格式错误或未包含员工角色");

        if (reqJson.containsKey("email") && !StringUtil.isEmpty(reqJson.getString("email"))) {
            Assert.isEmail(reqJson, "email", "不是有效的邮箱格式");
        }

        ResponseEntity responseEntity = super.getStoreInfo(pd, restTemplate);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }
        Assert.jsonObjectHaveKey(responseEntity.getBody().toString(), "storeId", "根据用户ID查询商户ID失败，未包含storeId节点");

        String storeId = JSONObject.parseObject(responseEntity.getBody().toString()).getString("storeId");
        String storeTypeCd = JSONObject.parseObject(responseEntity.getBody().toString()).getString("storeTypeCd");
        // JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        reqJson.put("name", reqJson.getString("username"));
        reqJson.put("storeId", storeId);
        reqJson.put("storeTypeCd", storeTypeCd);
        responseEntity = this.callCenterService(restTemplate, pd, reqJson.toJSONString(), "user.staff.add", HttpMethod.POST);
        return responseEntity;
    }

    /**
     * 加载员工数据
     *
     * @param pd
     * @return
     */
    @Override
    public ResponseEntity<String> loadData(IPageData pd) {


        Assert.jsonObjectHaveKey(pd.getReqData(), "page", "请求报文中未包含page节点");
        Assert.jsonObjectHaveKey(pd.getReqData(), "row", "请求报文中未包含rows节点");

        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        Assert.isInteger(paramIn.getString("page"), "page不是数字");
        Assert.isInteger(paramIn.getString("row"), "rows不是数字");
        int page = Integer.parseInt(paramIn.getString("page"));
        int rows = Integer.parseInt(paramIn.getString("row"));
        String staffName = paramIn.getString("staffName");
        //2级别组织信息
        if (paramIn.containsKey("orgLevel") && paramIn.getString("orgLevel").equals("2")) {
            //默认只查看当前归属组织架构
            BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
            basePrivilegeDto.setResource("/viewAllOrganization");
            basePrivilegeDto.setUserId(pd.getUserId());
            List<Map> privileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);
            if (privileges.size() == 0) {
                //查询员工所属二级组织架构
                OrgStaffRelDto orgStaffRelDto = new OrgStaffRelDto();
                orgStaffRelDto.setStaffId(pd.getUserId());
                List<OrgStaffRelDto> orgStaffRelDtos = iOrgStaffRelInnerServiceSMO.queryOrgInfoByStaffIds(orgStaffRelDto);
                if (orgStaffRelDtos.size() > 0) {
                    paramIn.put("branchOrgId", orgStaffRelDtos.get(0).getCompanyId());//当前人虽归属的二级组织信息
                    paramIn.put("parentOrgId", orgStaffRelDtos.get(0).getCompanyId());//当前人虽归属的二级组织信息
                }

            }
        }

        if (rows > 50) {
            return new ResponseEntity<String>("rows 数量不能大于50", HttpStatus.BAD_REQUEST);
        }
        // page = (page - 1) * rows;
        ResponseEntity responseEntity = super.getStoreInfo(pd, restTemplate);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }
        Assert.jsonObjectHaveKey(responseEntity.getBody().toString(), "storeId", "根据用户ID查询商户ID失败，未包含storeId节点");

        String storeId = JSONObject.parseObject(responseEntity.getBody().toString()).getString("storeId");
        //paramIn.put("page", page);
        paramIn.put("storeId", storeId);
        //if (StringUtil.isEmpty(staffName)) {
        responseEntity = this.callCenterService(restTemplate, pd, "",
                "query.staff.infos" + super.mapToUrlParam(paramIn), HttpMethod.GET);
       /* } else {
            responseEntity = this.callCenterService(restTemplate, pd, "",
                    "query.staff.byName?rows=" + rows + "&page=" + page + "&storeId=" + storeId + "&name=" + staffName, HttpMethod.GET);
        }*/
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }

        JSONObject resultObjs = JSONObject.parseObject(responseEntity.getBody().toString());
        resultObjs.put("row", rows);
        resultObjs.put("page", page);
        return responseEntity;
    }

    /**
     * 修改员工信息
     *
     * @param pd
     * @return
     */
    @Override
    public ResponseEntity<String> modifyStaff(IPageData pd) {

        ResponseEntity<String> responseEntity = null;
        //校验 前台数据
        modifyStaffValidate(pd);
        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        paramIn.put("name", paramIn.getString("username"));
        //修改用户信息
        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                "user.staff.modify", HttpMethod.POST);
        return responseEntity;
    }

    /**
     * 删除工号
     *
     * @param pd
     * @return
     */
    @Override
    public ResponseEntity<String> delete(IPageData pd) {
        ResponseEntity<String> responseEntity = null;
        Assert.jsonObjectHaveKey(pd.getReqData(), "userId", "请求报文格式错误或未包含用户ID信息");
        //Assert.jsonObjectHaveKey(pd.getReqData(), "storeId", "请求报文格式错误或未包含商户ID信息");
        ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);
        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        JSONObject newParam = new JSONObject();
        newParam.put("userId", paramIn.getString("userId"));
        newParam.put("storeId", result.getStoreId());
        //修改用户信息
        responseEntity = this.callCenterService(restTemplate, pd, newParam.toJSONString(),
                "user.staff.delete", HttpMethod.POST);
        return responseEntity;
    }

    /**
     * 查询 员工没有绑定的权限组
     *
     * @param pd
     * @return
     */
    @Override
    public ResponseEntity<String> listNoAddPrivilegeGroup(IPageData pd) {

        ResponseEntity<String> responseEntity = null;
        Assert.jsonObjectHaveKey(pd.getReqData(), "userId", "请求报文格式错误或未包含用户ID信息");
        JSONObject _paramObj = JSONObject.parseObject(pd.getReqData());
        responseEntity = super.getStoreInfo(pd, restTemplate);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }
        Assert.jsonObjectHaveKey(responseEntity.getBody().toString(), "storeId", "根据用户ID查询商户ID失败，未包含storeId节点");

        String storeId = JSONObject.parseObject(responseEntity.getBody().toString()).getString("storeId");
        String storeTypeCd = JSONObject.parseObject(responseEntity.getBody().toString()).getString("storeTypeCd");
        //修改用户信息
        responseEntity = this.callCenterService(restTemplate, pd, "",
                "query.privilegeGroup.noAddPrivilegeGroup?userId="
                        + _paramObj.getString("userId") + "&storeId=" + storeId + "&storeTypeCd=" + storeTypeCd,
                HttpMethod.GET);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }

        JSONObject outDataObj = JSONObject.parseObject(responseEntity.getBody());
        return new ResponseEntity<String>(outDataObj.getJSONArray("privilgeGroups").toJSONString(), HttpStatus.OK);
    }

    /**
     * 查询 员工没有绑定的权限
     *
     * @param pd
     * @return
     */
    @Override
    public ResponseEntity<String> listNoAddPrivilege(IPageData pd) {
        ResponseEntity<String> responseEntity = null;
        Assert.jsonObjectHaveKey(pd.getReqData(), "userId", "请求报文格式错误或未包含用户ID信息");
        JSONObject _paramObj = JSONObject.parseObject(pd.getReqData());
        responseEntity = super.getStoreInfo(pd, restTemplate);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }
        Assert.jsonObjectHaveKey(responseEntity.getBody().toString(), "storeId", "根据用户ID查询商户ID失败，未包含storeId节点");

        String storeId = JSONObject.parseObject(responseEntity.getBody().toString()).getString("storeId");
        String storeTypeCd = JSONObject.parseObject(responseEntity.getBody().toString()).getString("storeTypeCd");
        //修改用户信息
        responseEntity = this.callCenterService(restTemplate, pd, "",
                "query.privilege.noAddPrivilege?userId="
                        + _paramObj.getString("userId") + "&storeId=" + storeId + "&storeTypeCd=" + storeTypeCd,
                HttpMethod.GET);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }

        JSONObject outDataObj = JSONObject.parseObject(responseEntity.getBody());
        return new ResponseEntity<String>(outDataObj.getJSONArray("privilges").toJSONString(), HttpStatus.OK);
    }

    /**
     * 添加权限 或权限组
     *
     * @param pd
     * @return
     */
    @Override
    public ResponseEntity<String> addStaffPrivilegeOrPrivilegeGroup(IPageData pd) {
        ResponseEntity<String> responseEntity = null;
        Assert.jsonObjectHaveKey(pd.getReqData(), "userId", "请求报文格式错误或未包含用户ID信息");
        Assert.jsonObjectHaveKey(pd.getReqData(), "pIds", "请求报文格式错误或未包含权限ID信息");
        Assert.jsonObjectHaveKey(pd.getReqData(), "pFlag", "请求报文格式错误");
        JSONObject _paramObj = JSONObject.parseObject(pd.getReqData());
        responseEntity = super.getStoreInfo(pd, restTemplate);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }
        Assert.jsonObjectHaveKey(responseEntity.getBody().toString(), "storeId", "根据用户ID查询商户ID失败，未包含storeId节点");

        String storeId = JSONObject.parseObject(responseEntity.getBody().toString()).getString("storeId");
        String storeTypeCd = JSONObject.parseObject(responseEntity.getBody().toString()).getString("storeTypeCd");
        _paramObj.put("storeId", storeId);
        _paramObj.put("storeTypeCd", storeTypeCd);

        //修改用户信息
        responseEntity = this.callCenterService(restTemplate, pd, _paramObj.toJSONString(),
                "add.privilege.userPrivilege",
                HttpMethod.POST);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> deleteStaffPrivilege(IPageData pd) {
        ResponseEntity<String> responseEntity = null;
        Assert.jsonObjectHaveKey(pd.getReqData(), "userId", "请求报文格式错误或未包含用户ID信息");
        Assert.jsonObjectHaveKey(pd.getReqData(), "pId", "请求报文格式错误或未包含权限ID信息");
        Assert.jsonObjectHaveKey(pd.getReqData(), "pFlag", "请求报文格式错误");
        JSONObject _paramObj = JSONObject.parseObject(pd.getReqData());
        responseEntity = super.getStoreInfo(pd, restTemplate);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }
        Assert.jsonObjectHaveKey(responseEntity.getBody().toString(), "storeId", "根据用户ID查询商户ID失败，未包含storeId节点");

        String storeId = JSONObject.parseObject(responseEntity.getBody().toString()).getString("storeId");
        String storeTypeCd = JSONObject.parseObject(responseEntity.getBody().toString()).getString("storeTypeCd");
        _paramObj.put("storeId", storeId);
        _paramObj.put("storeTypeCd", storeTypeCd);

        //修改用户信息
        responseEntity = this.callCenterService(restTemplate, pd, _paramObj.toJSONString(),
                "delete.privilege.userPrivilege",
                HttpMethod.POST);

        return responseEntity;
    }

    /**
     * 修改员工 数据校验
     *
     * @param pd
     */
    private void modifyStaffValidate(IPageData pd) {
        Assert.jsonObjectHaveKey(pd.getReqData(), "userId", "请求报文格式错误或未包含用户ID信息");
        Assert.jsonObjectHaveKey(pd.getReqData(), "username", "请求报文格式错误或未包含用户名信息");
        //Assert.jsonObjectHaveKey(pd.getReqData(), "email", "请求报文格式错误或未包含邮箱信息");
        Assert.jsonObjectHaveKey(pd.getReqData(), "tel", "请求报文格式错误或未包含手机信息");
        Assert.jsonObjectHaveKey(pd.getReqData(), "sex", "请求报文格式错误或未包含性别信息");
        Assert.jsonObjectHaveKey(pd.getReqData(), "address", "请求报文格式错误或未包含地址信息");
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        if (reqJson.containsKey("email") && !StringUtil.isEmpty(reqJson.getString("email"))) {
            Assert.isEmail(reqJson, "email", "不是有效的邮箱格式");
        }
    }


    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
