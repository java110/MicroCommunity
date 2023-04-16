package com.java110.acct.bmo.accountWithdrawalApply.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.bmo.accountWithdrawalApply.ISaveAccountWithdrawalApplyBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;

import com.java110.dto.user.UserDto;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.intf.acct.IAccountWithdrawalApplyInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.accountDetail.AccountDetailPo;
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

    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;
    /**
     * 添加小区信息
     *
     * @param accountWithdrawalApplyPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(AccountWithdrawalApplyPo accountWithdrawalApplyPo, String userId,JSONObject reqJson) {


        UserDto userDto = new UserDto();
        userDto.setUserId( userId );
        //根据登录用户查询用户详细信息
        List<UserDto> userDtoList = userInnerServiceSMOImpl.getUsers( userDto );
        if(null != userDtoList && userDtoList.size() > 0){

            accountWithdrawalApplyPo.setApplyUserName( userDtoList.get( 0 ).getUserName() );
            accountWithdrawalApplyPo.setApplyUserTel( userDtoList.get( 0 ).getTel() );
            accountWithdrawalApplyPo.setApplyUserId( userDtoList.get( 0 ).getUserId() );
            accountWithdrawalApplyPo.setApplyId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_applyId));

            AccountDetailPo accountDetailPo = new AccountDetailPo();
            accountDetailPo.setAcctId( accountWithdrawalApplyPo.getAcctId() );
            accountDetailPo.setAmount( accountWithdrawalApplyPo.getAmount() );
            accountDetailPo.setRemark( accountWithdrawalApplyPo.getContext() );
            accountDetailPo.setObjId( reqJson.getString( "acctObjId" ) );
            //商户类型
            accountDetailPo.setObjType( "7007");
            //调用扣款接口进行扣款
            int acctflag = accountInnerServiceSMOImpl.withholdAccount( accountDetailPo );
            if (acctflag < 1) {
                return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "扣款失败");
            }
            //报存提现信息
            int flag = accountWithdrawalApplyInnerServiceSMOImpl.saveAccountWithdrawalApply(accountWithdrawalApplyPo);
            if (flag < 1) {
                return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存提现失败");
            }

            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "提现成功");
        }


        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败，检查用户信息是否完整");
    }

}
