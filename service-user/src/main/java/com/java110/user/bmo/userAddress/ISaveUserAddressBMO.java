package com.java110.user.bmo.userAddress;

import com.java110.po.user.UserAddressPo;
import org.springframework.http.ResponseEntity;
public interface ISaveUserAddressBMO {


    /**
     * 添加用户联系地址
     * add by wuxw
     * @param userAddressPo
     * @return
     */
    ResponseEntity<String> save(UserAddressPo userAddressPo);


}
