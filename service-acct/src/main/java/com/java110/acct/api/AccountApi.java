package com.java110.acct.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.bmo.account.IGetAccountBMO;
import com.java110.acct.bmo.account.IOwnerPrestoreAccountBMO;
import com.java110.core.smo.IOwnerGetDataCheck;
import com.java110.dto.account.AccountDto;
import com.java110.dto.account.AccountDetailDto;
import com.java110.dto.contract.ContractDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.store.IContractInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.po.account.AccountDetailPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName AccountApi
 * @Description TODO
 * @Author wuxw
 * @Date 2021/5/4 12:44
 * @Version 1.0
 * add by wuxw 2021/5/4
 **/

@RestController
@RequestMapping(value = "/account")
public class AccountApi {

    @Autowired
    private IGetAccountBMO getAccountBMOImpl;

    @Autowired
    private IOwnerPrestoreAccountBMO ownerPrestoreAccountBMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IContractInnerServiceSMO contractInnerServiceSMOImpl;

    @Autowired
    private IOwnerGetDataCheck ownerGetDataCheckImpl;

    /**
     * 微信删除消息模板
     *
     * @param storeId 小区ID
     * @return
     * @serviceCode /account/queryAccount
     * @path /app/account/queryAccount
     */
    @RequestMapping(value = "/queryAccount", method = RequestMethod.GET)
    public ResponseEntity<String> queryAccount(@RequestHeader(value = "store-id", required = false) String storeId,
                                               @RequestParam(value = "shopId", required = false) String shopId,
                                               @RequestParam(value = "page") int page,
                                               @RequestParam(value = "row") int row) {
        AccountDto accountDto = new AccountDto();
        accountDto.setPage(page);
        accountDto.setRow(row);
        if (!StringUtil.isEmpty(shopId)) {
            accountDto.setObjId(shopId);
        } else {
            accountDto.setObjId(storeId);
        }
        return getAccountBMOImpl.get(accountDto);
    }

    /**
     * 查询业主账户明细
     *
     * @param objId 小区ID
     * @return
     * @serviceCode /account/queryOwnerAccountDetail
     * @path /app/account/queryOwnerAccountDetail
     */
    @RequestMapping(value = "/queryOwnerAccountDetail", method = RequestMethod.GET)
    public ResponseEntity<String> queryOwnerAccountDetail(@RequestParam(value = "objId", required = false) String objId,
                                                          @RequestParam(value = "acctId", required = false) String acctId,
                                                          @RequestParam(value = "page") int page,
                                                          @RequestParam(value = "row") int row,
                                                          @RequestHeader(value = "user-id") String userId,
                                                          @RequestHeader(value = "app-id") String appId) {
        AccountDetailDto accountDto = new AccountDetailDto();
        accountDto.setPage(page);
        accountDto.setRow(row);
        accountDto.setObjId(objId);
        accountDto.setAcctId(acctId);
        //todo 业主账户安全性校验
        ownerGetDataCheckImpl.checkOwnerAccount(appId, userId, BeanConvertUtil.beanCovertJson(accountDto));
        return getAccountBMOImpl.getDetail(accountDto);
    }

    /**
     * 查询账户明细
     *
     * @param storeId 小区ID
     * @return
     * @serviceCode /account/queryAccountDetail
     * @path /app/account/queryAccountDetail
     */
    @RequestMapping(value = "/queryAccountDetail", method = RequestMethod.GET)
    public ResponseEntity<String> queryAccountDetail(@RequestHeader(value = "store-id", required = false) String storeId,
                                                     @RequestParam(value = "acctId", required = false) String acctId,
                                                     @RequestParam(value = "detailType", required = false) String detailType,
                                                     @RequestParam(value = "orderId", required = false) String orderId,
                                                     @RequestParam(value = "page") int page,
                                                     @RequestParam(value = "row") int row) {
        AccountDetailDto accountDto = new AccountDetailDto();
        accountDto.setPage(page);
        accountDto.setRow(row);
        accountDto.setObjId(storeId);
        accountDto.setAcctId(acctId);
        accountDto.setDetailType(detailType);
        accountDto.setOrderId(orderId);
        return getAccountBMOImpl.getDetail(accountDto);
    }


}
