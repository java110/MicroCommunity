package com.java110.acct.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.bmo.account.IGetAccountBMO;
import com.java110.acct.bmo.account.IOwnerPrestoreAccountBMO;
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
import com.java110.po.accountDetail.AccountDetailPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        if(!StringUtil.isEmpty(shopId)){
            accountDto.setObjId(shopId);
        }else {
            accountDto.setObjId(storeId);
        }
        return getAccountBMOImpl.get(accountDto);
    }

    /**
     * 查询业主账户
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /account/queryOwnerAccount
     * @path /app/account/queryOwnerAccount
     */
    @RequestMapping(value = "/queryOwnerAccount", method = RequestMethod.GET)
    public ResponseEntity<String> queryOwnerAccount(
            @RequestParam(value = "communityId") String communityId,
            @RequestParam(value = "ownerId", required = false) String ownerId,
            @RequestParam(value = "ownerName", required = false) String ownerName,
            @RequestParam(value = "feeId", required = false) String feeId,
            @RequestParam(value = "link", required = false) String link,
            @RequestParam(value = "idCard", required = false) String idCard,
            @RequestParam(value = "acctType", required = false) String acctType,
            @RequestParam(value = "acctId", required = false) String acctId,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "row") int row) {
        AccountDto accountDto = new AccountDto();
        accountDto.setPage(page);
        accountDto.setRow(row);
        if (!StringUtil.isEmpty(feeId)) {
            FeeDto feeDto = new FeeDto();
            feeDto.setFeeId(feeId);
            List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
            Assert.listOnlyOne(feeDtos, "查询费用信息错误！");
            //获取付费对象类型(3333 房屋 6666 是车位)
            String payerObjType = feeDtos.get(0).getPayerObjType();
            //获取付费对象id
            String payerObjId = feeDtos.get(0).getPayerObjId();
            if (FeeDto.PAYER_OBJ_TYPE_ROOM.equals(payerObjType)) { //房屋
                OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
                ownerRoomRelDto.setRoomId(payerObjId);
                List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);
                Assert.listOnlyOne(ownerRoomRelDtos, "查询业主房屋关系表错误！");
                ownerId = ownerRoomRelDtos.get(0).getOwnerId();
            } else if (FeeDto.PAYER_OBJ_TYPE_CAR.equals(payerObjType)) {
                OwnerCarDto ownerCarDto = new OwnerCarDto();
                ownerCarDto.setCarId(payerObjId);
                List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
//                Assert.listOnlyOne(ownerCarDtos, "查询业主车辆关系表错误！");
                ownerId = ownerCarDtos.get(0).getOwnerId();
            } else if (FeeDto.PAYER_OBJ_TYPE_CONTRACT.equals(payerObjType)) {
                ContractDto contractDto = new ContractDto();
                contractDto.setContractId(payerObjId);
                List<ContractDto> contractDtos = contractInnerServiceSMOImpl.queryContracts(contractDto);
//                Assert.listOnlyOne(ownerCarDtos, "查询业主车辆关系表错误！");
                ownerId = contractDtos.get(0).getObjId();
            } else {
                ownerId = "-1";
            }
            accountDto.setObjId(ownerId);
        } else {
            accountDto.setObjId(ownerId);
        }
        accountDto.setObjType(AccountDto.OBJ_TYPE_PERSON);
        accountDto.setAcctName(ownerName);
        accountDto.setPartId(communityId);
        accountDto.setAcctType(acctType);
        accountDto.setLink(link);
        accountDto.setAcctId(acctId);
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setOwnerId(ownerId);
        ownerDto.setCommunityId(communityId);
        ownerDto.setLink(link);
        ownerDto.setIdCard(idCard);
        return getAccountBMOImpl.queryOwnerAccount(accountDto, ownerDto);
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
                                                          @RequestParam(value = "row") int row) {
        AccountDetailDto accountDto = new AccountDetailDto();
        accountDto.setPage(page);
        accountDto.setRow(row);
        accountDto.setObjId(objId);
        accountDto.setAcctId(acctId);
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

    /**
     * 业主账户预存
     *
     * @param reqJson 小区ID
     * @return
     * @serviceCode /account/ownerPrestoreAccount
     * @path /app/account/ownerPrestoreAccount
     */
    @RequestMapping(value = "/ownerPrestoreAccount", method = RequestMethod.POST)
    public ResponseEntity<String> queryAccountDetail(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");
        Assert.hasKeyAndValue(reqJson, "ownerId", "业主不能为空");
        Assert.hasKeyAndValue(reqJson, "amount", "金额不能为空");
        Assert.hasKeyAndValue(reqJson, "acctType", "账户类型不能为空");
        Assert.hasKeyAndValue(reqJson, "primeRate", "未包含支付方式");

        AccountDetailPo accountDetailPo = new AccountDetailPo();
        accountDetailPo.setRemark(reqJson.getString("remark"));
        accountDetailPo.setObjId(reqJson.getString("ownerId"));
        accountDetailPo.setAmount(reqJson.getString("amount"));
        return ownerPrestoreAccountBMOImpl.prestore(accountDetailPo, reqJson);
    }
}
