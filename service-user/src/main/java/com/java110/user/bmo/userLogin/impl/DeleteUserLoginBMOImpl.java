package com.java110.user.bmo.userLogin.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.dto.user.UserAttrDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.user.IUserAttrInnerServiceSMO;
import com.java110.intf.user.IUserLoginInnerServiceSMO;
import com.java110.po.userLogin.UserLoginPo;
import com.java110.user.bmo.userLogin.IDeleteUserLoginBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("deleteUserLoginBMOImpl")
public class DeleteUserLoginBMOImpl implements IDeleteUserLoginBMO {

    @Autowired
    private IUserLoginInnerServiceSMO userLoginInnerServiceSMOImpl;
    @Autowired
    private IUserAttrInnerServiceSMO userAttrInnerServiceSMOImpl;

    /**
     * @param userLoginPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(UserLoginPo userLoginPo) {

        int flag = userLoginInnerServiceSMOImpl.deleteUserLogin(userLoginPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }
    /**
     * @param userDto 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> deleteOpenId(UserDto userDto) {
        UserAttrDto userAttrDto = new UserAttrDto();
        userAttrDto.setUserId( userDto.getUserId() );
        userAttrDto.setSpecCd( "100201911001" );
        List<UserAttrDto> userAttrDtos = userAttrInnerServiceSMOImpl.queryUserAttrs( userAttrDto );
        if (userAttrDtos != null && userAttrDtos.size() > 0) {
            UserAttrDto userAttr = new UserAttrDto();
            userAttr.setAttrId( userAttrDtos.get( 0 ).getAttrId() );
            int flag = userAttrInnerServiceSMOImpl.deleteUserAttr(userAttr);
            if (flag < 1) {
                return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "清空openid失败");
            }
        }


        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
