package com.java110.order.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.cache.MappingCache;
import com.java110.common.constant.MappingConstant;
import com.java110.common.util.Assert;
import com.java110.order.dao.IPrivilegeDAO;
import com.java110.order.smo.IPrivilegeSMO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

        Assert.isJsonObject(privilegeInfo,"请求报文不是有效的json格式");

        Assert.jsonObjectHaveKey(privilegeInfo,"userId","请求报文中未包含userId节点");

        Assert.jsonObjectHaveKey(privilegeInfo,"storeTypeCd","请求报文中未包含storeTypeCd节点");


        JSONObject privilegeObj = JSONObject.parseObject(privilegeInfo);

        String storeTypeCd = privilegeObj.getString("storeTypeCd");

        String defaultPrivilege = MappingCache.getValue(MappingConstant.DOMAIN_DEFAULT_PRIVILEGE_ADMIN,storeTypeCd);

        Assert.hasLength(defaultPrivilege,"在c_mapping 表中未配置商户类型为"+storeTypeCd+" 的默认权限组");
        privilegeObj.put("pId",defaultPrivilege);

        if(privilegeDAOImpl.saveUserDefaultPrivilege(privilegeObj)){
            return new ResponseEntity<String>("成功", HttpStatus.OK);
        }

        return new ResponseEntity<String>("未知异常", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    public IPrivilegeDAO getPrivilegeDAOImpl() {
        return privilegeDAOImpl;
    }

    public void setPrivilegeDAOImpl(IPrivilegeDAO privilegeDAOImpl) {
        this.privilegeDAOImpl = privilegeDAOImpl;
    }
}
