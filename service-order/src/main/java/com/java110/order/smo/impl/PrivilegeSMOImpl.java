package com.java110.order.smo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.order.dao.IPrivilegeDAO;
import com.java110.order.smo.IPrivilegeSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 用户权限处理类
 * Created by Administrator on 2019/4/1.
 */

@Service("privilegeSMOImpl")
public class PrivilegeSMOImpl implements IPrivilegeSMO {

    private final static Logger logger = LoggerFactory.getLogger(PrivilegeSMOImpl.class);

    @Autowired
    private IPrivilegeDAO privilegeDAOImpl;

    @Override
    public ResponseEntity<String> saveUserDefaultPrivilege(String privilegeInfo) {

        Assert.isJsonObject(privilegeInfo, "请求报文不是有效的json格式");

        Assert.jsonObjectHaveKey(privilegeInfo, "userId", "请求报文中未包含userId节点");
        Assert.jsonObjectHaveKey(privilegeInfo, "storeId", "请求报文中未包含storeId节点");

        Assert.jsonObjectHaveKey(privilegeInfo, "storeTypeCd", "请求报文中未包含storeTypeCd节点");

        Assert.jsonObjectHaveKey(privilegeInfo, "userFlag", "请求报文中未包含userFlag节点");


        JSONObject privilegeObj = JSONObject.parseObject(privilegeInfo);

        String storeTypeCd = privilegeObj.getString("storeTypeCd");

        String privilegeDomain = "admin".equals(privilegeObj.getString("userFlag"))
                ? MappingConstant.DOMAIN_DEFAULT_PRIVILEGE_ADMIN : MappingConstant.DOMAIN_DEFAULT_PRIVILEGE;

        String defaultPrivilege = MappingCache.getValue(privilegeDomain, storeTypeCd);

        Assert.hasLength(defaultPrivilege, "在c_mapping 表中未配置商户类型为" + storeTypeCd + " 的默认权限组");
        privilegeObj.put("pId", defaultPrivilege);

        if (privilegeDAOImpl.saveUserDefaultPrivilege(privilegeObj)) {
            return new ResponseEntity<String>("成功", HttpStatus.OK);
        }

        return new ResponseEntity<String>("未知异常", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 删除用户权限
     *
     * @param privilegeInfo
     * @return
     */
    @Override
    public ResponseEntity<String> deleteUserAllPrivilege(String privilegeInfo) {
        Assert.isJsonObject(privilegeInfo, "请求报文不是有效的json格式");

        Assert.jsonObjectHaveKey(privilegeInfo, "userId", "请求报文中未包含userId节点");

        JSONObject privilegeObj = JSONObject.parseObject(privilegeInfo);
        if (privilegeDAOImpl.deleteUserAllPrivilege(privilegeObj)) {
            return new ResponseEntity<String>("成功", HttpStatus.OK);
        }

        return new ResponseEntity<String>("未知异常", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> savePrivilegeGroup(String privilegeGroupInfo) {

        Assert.isJsonObject(privilegeGroupInfo, "请求报文不是有效的json格式");

        Assert.jsonObjectHaveKey(privilegeGroupInfo, "name", "请求报文中未包含name节点");

        Assert.jsonObjectHaveKey(privilegeGroupInfo, "storeId", "请求报文中未包含storeId节点");

        Assert.jsonObjectHaveKey(privilegeGroupInfo, "storeTypeCd", "请求报文中未包含storeTypeCd节点");
        JSONObject privilegeGroupObj = JSONObject.parseObject(privilegeGroupInfo);

        privilegeGroupObj.put("pgId", GenerateCodeFactory.getPgId());

        if (privilegeDAOImpl.savePrivilegeGroup(privilegeGroupObj)) {
            return new ResponseEntity<String>("成功", HttpStatus.OK);
        }

        return new ResponseEntity<String>("未知异常", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> editPrivilegeGroup(String privilegeGroupInfo) {

        Assert.isJsonObject(privilegeGroupInfo, "请求报文不是有效的json格式");

        Assert.jsonObjectHaveKey(privilegeGroupInfo, "name", "请求报文中未包含name节点");
        Assert.jsonObjectHaveKey(privilegeGroupInfo, "pgId", "请求报文中未包含pgId节点");

        Assert.jsonObjectHaveKey(privilegeGroupInfo, "storeId", "请求报文中未包含storeId节点");

        Assert.jsonObjectHaveKey(privilegeGroupInfo, "storeTypeCd", "请求报文中未包含storeTypeCd节点");
        JSONObject privilegeGroupObj = JSONObject.parseObject(privilegeGroupInfo);

        if (privilegeDAOImpl.updatePrivilegeGroup(privilegeGroupObj)) {
            return new ResponseEntity<String>("成功", HttpStatus.OK);
        }

        return new ResponseEntity<String>("未知异常", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 删除权限组
     *
     * @param privilegeGroupInfo
     * @return
     */
    @Override
    public ResponseEntity<String> deletePrivilegeGroup(String privilegeGroupInfo) {

        Assert.isJsonObject(privilegeGroupInfo, "请求报文不是有效的json格式");

        Assert.jsonObjectHaveKey(privilegeGroupInfo, "pgId", "请求报文中未包含pgId节点");

        Assert.jsonObjectHaveKey(privilegeGroupInfo, "storeId", "请求报文中未包含storeId节点");

        Assert.jsonObjectHaveKey(privilegeGroupInfo, "storeTypeCd", "请求报文中未包含storeTypeCd节点");
        JSONObject privilegeGroupObj = JSONObject.parseObject(privilegeGroupInfo);
        //删除权限组
        privilegeDAOImpl.deletePrivilegeGroup(privilegeGroupObj);

        List<Map> privileges = privilegeDAOImpl.queryPrivilegeRel(privilegeGroupObj);

        if (privileges != null && privileges.size() > 0) {
            //删除权限组和权限关系
            privilegeDAOImpl.deletePrivilegeRel(privilegeGroupObj);
        }

        return new ResponseEntity<String>("成功", HttpStatus.OK);

    }

    @Override
    public ResponseEntity<String> addPrivilegeToPrivilegeGroup(String privilegeInfo) {
        Assert.isJsonObject(privilegeInfo, "请求报文不是有效的json格式");

        Assert.jsonObjectHaveKey(privilegeInfo, "pgId", "请求报文中未包含pgId节点");

        Assert.jsonObjectHaveKey(privilegeInfo, "storeId", "请求报文中未包含storeId节点");
        JSONObject privilegeObj = JSONObject.parseObject(privilegeInfo);
        //Assert.jsonObjectHaveKey(privilegeInfoObj,"pId","请求报文中未包含权限ID 节点");
        if (!privilegeObj.containsKey("pIds") || privilegeObj.getJSONArray("pIds").size() < 1) {
            throw new IllegalArgumentException("请求报文中未包含权限");
        }

        //根据权限组ID和商户ID查询是否有数据
        List<Map> privilegeGroups = privilegeDAOImpl.queryPrivilegeGroup(privilegeObj);
        Assert.isNotNull(privilegeGroups, "当前没有权限操作权限组pgId = " + privilegeObj.getString("pgId"));

        JSONArray pIds = privilegeObj.getJSONArray("pIds");
        int errorCount = 0;
        JSONObject tmpPId = null;
        for (int pIdIndex = 0; pIdIndex < pIds.size(); pIdIndex++) {
            try {
                tmpPId = pIds.getJSONObject(pIdIndex);
                privilegeObj.put("pId", tmpPId.getString("pId"));
                if (!privilegeDAOImpl.addPrivilegeRel(privilegeObj)) {
                    errorCount++;
                }

            } catch (Exception e) {
                logger.error("保存权限关系失败", e);
                errorCount++;
            }
        }

        JSONObject paramOut = new JSONObject();
        paramOut.put("success", pIds.size() - errorCount);
        paramOut.put("error", errorCount);

        return new ResponseEntity<String>(paramOut.toJSONString(), HttpStatus.OK);
    }

    /**
     * @param privilegeInfo
     * @return
     */
    @Override
    public ResponseEntity<String> deletePrivilegeToPrivilegeGroup(String privilegeInfo) {

        Assert.isJsonObject(privilegeInfo, "请求报文不是有效的json格式");

        Assert.jsonObjectHaveKey(privilegeInfo, "pgId", "请求报文中未包含pgId节点");

        Assert.jsonObjectHaveKey(privilegeInfo, "storeId", "请求报文中未包含storeId节点");

//        Assert.jsonObjectHaveKey(privilegeInfo, "pId", "请求报文中未包含pId节点");

        JSONObject privilegeObj = JSONObject.parseObject(privilegeInfo);
        //根据权限组ID和商户ID查询是否有数据
        List<Map> privilegeGroups = privilegeDAOImpl.queryPrivilegeGroup(privilegeObj);
        Assert.isNotNull(privilegeGroups, "当前没有权限操作权限组pgId = " + privilegeObj.getString("pgId"));

        if (privilegeObj.containsKey("pIds")) {
            JSONArray pIds = privilegeObj.getJSONArray("pIds");
            int errorCount = 0;
            JSONObject tmpPId = null;
            for (int pIdIndex = 0; pIdIndex < pIds.size(); pIdIndex++) {
                try {
                    tmpPId = pIds.getJSONObject(pIdIndex);
                    privilegeObj.put("pId", tmpPId.getString("pId"));
                    if (!privilegeDAOImpl.deletePrivilegeRel(privilegeObj)) {
                        errorCount++;
                    }

                } catch (Exception e) {
                    logger.error("保存权限关系失败", e);
                    errorCount++;
                }
            }
        } else {
            if (!privilegeDAOImpl.deletePrivilegeRel(privilegeObj)) {
                return new ResponseEntity<String>("删除权限失败", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<String>("成功", HttpStatus.OK);
    }

    /**
     * 员工添加权限或权限组
     *
     * @param privilegeInfo
     * @return
     */
    @Override
    public ResponseEntity<String> addStaffPrivilegeOrPrivilegeGroup(String privilegeInfo) {

        Assert.jsonObjectHaveKey(privilegeInfo, "pIds", "请求报文中未包含pIds节点");

        JSONObject privilegeObj = JSONObject.parseObject(privilegeInfo);
        JSONArray pIds = privilegeObj.getJSONArray("pIds");
        int errorCount = 0;
        for (int pIndex = 0; pIndex < pIds.size(); pIndex++) {
            privilegeObj.put("pId", pIds.getJSONObject(pIndex).getString("pId"));
            try {
                validateData(privilegeObj);
                //根据权限组ID和商户ID查询是否有数据
                String pFlag = privilegeObj.getString("pFlag");//权限组
                privilegeObj.put("privilegeFlag", "1".equals(pFlag) ? "1" : "0");

                List<Map> privilegeGroups = privilegeDAOImpl.queryUserPrivilege(privilegeObj);
                Assert.listIsNull(privilegeGroups, "已经存在该权限无需多次添加" + privilegeInfo);
                if (!privilegeDAOImpl.addUserPrivilege(privilegeObj)) {
                    return new ResponseEntity<String>("添加权限失败", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } catch (Exception e) {
                logger.error("保存权限失败", e);
                errorCount++;
            }
        }

        JSONObject paramOut = new JSONObject();
        paramOut.put("success", pIds.size() - errorCount);
        paramOut.put("error", errorCount);
        return new ResponseEntity<String>(paramOut.toJSONString(), HttpStatus.OK);
    }

    /**
     * 删除员工权限
     *
     * @param privilegeInfo
     * @return
     */
    @Override
    public ResponseEntity<String> deleteStaffPrivilegeOrPrivilegeGroup(String privilegeInfo) {
        JSONObject privilegeObj = validateDeleteStaffPrivilegeData(privilegeInfo);
        //根据权限组ID和商户ID查询是否有数据
        String pFlag = privilegeObj.getString("pFlag");//权限组
        privilegeObj.put("privilegeFlag", "1".equals(pFlag) ? "1" : "0");
        List<Map> privilegeGroups = privilegeDAOImpl.queryUserPrivilege(privilegeObj);
        Assert.isNotNull(privilegeGroups, "不存在该权限" + privilegeInfo);

        if (!privilegeDAOImpl.deleteUserPrivilege(privilegeObj)) {
            return new ResponseEntity<String>("添加权限失败", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>("成功", HttpStatus.OK);
    }

    /**
     * 数据校验
     *
     * @param privilegeInfo
     * @return
     */
    private JSONObject validateDeleteStaffPrivilegeData(String privilegeInfo) {

        Assert.isJsonObject(privilegeInfo, "请求报文不是有效的json格式");

        Assert.jsonObjectHaveKey(privilegeInfo, "pId", "请求报文中未包含pId节点");

        Assert.jsonObjectHaveKey(privilegeInfo, "pFlag", "请求报文中未包含pFlag节点");

        Assert.jsonObjectHaveKey(privilegeInfo, "userId", "请求报文中未包含userId节点");

        Assert.jsonObjectHaveKey(privilegeInfo, "storeId", "请求报文中未包含storeId节点");

        Assert.jsonObjectHaveKey(privilegeInfo, "storeTypeCd", "请求报文中未包含storeTypeCd节点");

        JSONObject privilegeObj = JSONObject.parseObject(privilegeInfo);
        String pFlag = privilegeObj.getString("pFlag");//权限组
        if ("1".equals(pFlag)) {
            validatePrivilegeGroup(privilegeObj);
            return privilegeObj;
        }
        validatePrivilege(privilegeObj);
        return privilegeObj;
    }

    private JSONObject validateData(JSONObject privilegeObj) {

        Assert.jsonObjectHaveKey(privilegeObj, "pId", "请求报文中未包含pId节点");

        Assert.jsonObjectHaveKey(privilegeObj, "pFlag", "请求报文中未包含pFlag节点");

        Assert.jsonObjectHaveKey(privilegeObj, "userId", "请求报文中未包含userId节点");

        Assert.jsonObjectHaveKey(privilegeObj, "storeId", "请求报文中未包含storeId节点");

        Assert.jsonObjectHaveKey(privilegeObj, "storeTypeCd", "请求报文中未包含storeTypeCd节点");

        String pFlag = privilegeObj.getString("pFlag");//权限组
        if ("1".equals(pFlag)) {
            validatePrivilegeGroup(privilegeObj);
            return privilegeObj;
        }
        validatePrivilege(privilegeObj);
        return privilegeObj;
    }

    /**
     * 权限组数据校验
     *
     * @param privilegeObj
     */
    private void validatePrivilegeGroup(JSONObject privilegeObj) {

        //判断当前权限组是否隶属于 当前商户
        privilegeObj.put("pgId", privilegeObj.getString("pId"));
        List<Map> privilegeGroups = privilegeDAOImpl.queryPrivilegeGroup(privilegeObj);
        if (privilegeGroups == null || privilegeGroups.size() == 0) {
            throw new SMOException(1999, "当前没有权限操作该权限组" + privilegeGroups.toString());
        }
    }

    /**
     * 权限数据校验
     *
     * @param privilegeObj
     */
    private void validatePrivilege(JSONObject privilegeObj) {

        privilegeObj.put("domain", privilegeObj.getString("storeTypeCd"));
        List<Map> privileges = privilegeDAOImpl.queryPrivilege(privilegeObj);
        if (privileges == null || privileges.size() == 0) {
            throw new SMOException(1999, "当前没有权限操作该权限" + privileges.toString());
        }
    }


    public IPrivilegeDAO getPrivilegeDAOImpl() {
        return privilegeDAOImpl;
    }

    public void setPrivilegeDAOImpl(IPrivilegeDAO privilegeDAOImpl) {
        this.privilegeDAOImpl = privilegeDAOImpl;
    }
}
