package com.java110.order.smo;

import org.springframework.http.ResponseEntity;

/**
 * 权限业务处理类
 * Created by Administrator on 2019/4/1.
 */
public interface IPrivilegeSMO {

    /**
     * 保存用户默认权限
     * @param privilegeInfo 权限信息
     * @return
     */
    public ResponseEntity<String> saveUserDefaultPrivilege(String privilegeInfo);
}
