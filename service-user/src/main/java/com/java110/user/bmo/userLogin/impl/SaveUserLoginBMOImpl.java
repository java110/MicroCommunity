package com.java110.user.bmo.userLogin.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.user.IUserLoginInnerServiceSMO;
import com.java110.po.userLogin.UserLoginPo;
import com.java110.user.bmo.userLogin.ISaveUserLoginBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveUserLoginBMOImpl")
public class SaveUserLoginBMOImpl implements ISaveUserLoginBMO {

    @Autowired
    private IUserLoginInnerServiceSMO userLoginInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param userLoginPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(UserLoginPo userLoginPo) {

        userLoginPo.setLoginId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_loginId));
        int flag = userLoginInnerServiceSMOImpl.saveUserLogin(userLoginPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
