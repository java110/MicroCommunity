package com.java110.acct.bmo.accountWithdrawalApply.impl;

import com.java110.acct.bmo.accountWithdrawalApply.ISaveAccountWithdrawalApplyBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;

import com.java110.dto.user.UserDto;
import com.java110.intf.acct.IAccountWithdrawalApplyInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.accountWithdrawalApply.AccountWithdrawalApplyPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service("saveAccountWithdrawalApplyBMOImpl")
public class SaveAccountWithdrawalApplyBMOImpl implements ISaveAccountWithdrawalApplyBMO {

    @Autowired
    private IAccountWithdrawalApplyInnerServiceSMO accountWithdrawalApplyInnerServiceSMOImpl;
    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;
    /**
     * 添加小区信息
     *
     * @param accountWithdrawalApplyPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(AccountWithdrawalApplyPo accountWithdrawalApplyPo,String userId) {


        UserDto userDto = new UserDto();
        userDto.setUserId( userId );
        //根据登录用户查询用户详细信息
        List<UserDto> userDtoList = userInnerServiceSMOImpl.getUsers( userDto );
        if(null != userDtoList && userDtoList.size() > 0){

            accountWithdrawalApplyPo.setApplyUserName( userDtoList.get( 0 ).getUserName() );
            accountWithdrawalApplyPo.setApplyUserTel( userDtoList.get( 0 ).getTel() );
            accountWithdrawalApplyPo.setApplyUserId( userDtoList.get( 0 ).getUserId() );

            accountWithdrawalApplyPo.setApplyId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_applyId));
            int flag = accountWithdrawalApplyInnerServiceSMOImpl.saveAccountWithdrawalApply(accountWithdrawalApplyPo);

            if (flag > 0) {
                return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
            }
        }


        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败，检查用户信息是否完整");
    }

}
