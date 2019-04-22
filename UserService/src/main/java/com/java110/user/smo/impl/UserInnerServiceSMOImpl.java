package com.java110.user.smo.impl;

import com.java110.core.smo.user.IUserInnerServiceSMO;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserInnerServiceSMOImpl implements IUserInnerServiceSMO {
    @Override
    public String getUserServiceVersion(@RequestParam("code") String code) {
        return code + " 0.0.6";
    }
}
