package com.java110.user.bmo.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.po.owner.OwnerPo;
import org.springframework.http.ResponseEntity;

public interface IChangeOwnerPhone {

    /**
     * 修改业主手机号
     * @param ownerPo
     * @return
     */
    ResponseEntity<String> change(OwnerPo ownerPo);
}
