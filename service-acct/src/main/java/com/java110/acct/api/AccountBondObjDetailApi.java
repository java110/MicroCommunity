package com.java110.acct.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.bmo.accountBondObjDetail.IDeleteAccountBondObjDetailBMO;
import com.java110.acct.bmo.accountBondObjDetail.IGetAccountBondObjDetailBMO;
import com.java110.acct.bmo.accountBondObjDetail.ISaveAccountBondObjDetailBMO;
import com.java110.acct.bmo.accountBondObjDetail.IUpdateAccountBondObjDetailBMO;
import com.java110.dto.account.AccountBondObjDetailDto;
import com.java110.po.accountBondObjDetail.AccountBondObjDetailPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/accountBondObjDetail")
public class AccountBondObjDetailApi {

    @Autowired
    private ISaveAccountBondObjDetailBMO saveAccountBondObjDetailBMOImpl;
    @Autowired
    private IUpdateAccountBondObjDetailBMO updateAccountBondObjDetailBMOImpl;
    @Autowired
    private IDeleteAccountBondObjDetailBMO deleteAccountBondObjDetailBMOImpl;

    @Autowired
    private IGetAccountBondObjDetailBMO getAccountBondObjDetailBMOImpl;

    /**
     * 微信保存消息模板
     * @serviceCode /accountBondObjDetail/saveAccountBondObjDetail
     * @path /app/accountBondObjDetail/saveAccountBondObjDetail
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/saveAccountBondObjDetail", method = RequestMethod.POST)
    public ResponseEntity<String> saveAccountBondObjDetail(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "bobjId", "请求报文中未包含bobjId");
        Assert.hasKeyAndValue(reqJson, "objId", "请求报文中未包含objId");
        Assert.hasKeyAndValue(reqJson, "bondType", "请求报文中未包含bondType");
        Assert.hasKeyAndValue(reqJson, "receivableAmount", "请求报文中未包含receivableAmount");
        Assert.hasKeyAndValue(reqJson, "receivedAmount", "请求报文中未包含receivedAmount");
        Assert.hasKeyAndValue(reqJson, "state", "请求报文中未包含state");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");


        AccountBondObjDetailPo accountBondObjDetailPo = BeanConvertUtil.covertBean(reqJson, AccountBondObjDetailPo.class);
        return saveAccountBondObjDetailBMOImpl.save(accountBondObjDetailPo);
    }

    /**
     * 微信修改消息模板
     * @serviceCode /accountBondObjDetail/updateAccountBondObjDetail
     * @path /app/accountBondObjDetail/updateAccountBondObjDetail
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/updateAccountBondObjDetail", method = RequestMethod.POST)
    public ResponseEntity<String> updateAccountBondObjDetail(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "bobjId", "请求报文中未包含bobjId");
        Assert.hasKeyAndValue(reqJson, "objId", "请求报文中未包含objId");
        Assert.hasKeyAndValue(reqJson, "bondType", "请求报文中未包含bondType");
        Assert.hasKeyAndValue(reqJson, "receivableAmount", "请求报文中未包含receivableAmount");
        Assert.hasKeyAndValue(reqJson, "receivedAmount", "请求报文中未包含receivedAmount");
        Assert.hasKeyAndValue(reqJson, "state", "请求报文中未包含state");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");
        Assert.hasKeyAndValue(reqJson, "detailId", "detailId不能为空");


        AccountBondObjDetailPo accountBondObjDetailPo = BeanConvertUtil.covertBean(reqJson, AccountBondObjDetailPo.class);
        return updateAccountBondObjDetailBMOImpl.update(accountBondObjDetailPo);
    }

    /**
     * 微信删除消息模板
     * @serviceCode /accountBondObjDetail/deleteAccountBondObjDetail
     * @path /app/accountBondObjDetail/deleteAccountBondObjDetail
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/deleteAccountBondObjDetail", method = RequestMethod.POST)
    public ResponseEntity<String> deleteAccountBondObjDetail(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "detailId", "detailId不能为空");
        Assert.hasKeyAndValue(reqJson, "objId", "objId不能为空");


        AccountBondObjDetailPo accountBondObjDetailPo = BeanConvertUtil.covertBean(reqJson, AccountBondObjDetailPo.class);
        return deleteAccountBondObjDetailBMOImpl.delete(accountBondObjDetailPo);
    }

    /**
     * 微信删除消息模板
     * @serviceCode /accountBondObjDetail/queryAccountBondObjDetail
     * @path /app/accountBondObjDetail/queryAccountBondObjDetail
     * @param
     * @return
     */
    @RequestMapping(value = "/queryAccountBondObjDetail", method = RequestMethod.GET)
    public ResponseEntity<String> queryAccountBondObjDetail(@RequestParam(value = "detailId" , required = false) String detailId,
                                                            @RequestParam(value = "bobjId" , required = false) String bobjId,
                                                            @RequestParam(value = "state" , required = false) String state,
                                                            @RequestParam(value = "objId" , required = false) String objId,
                                                      @RequestParam(value = "page") int page,
                                                      @RequestParam(value = "row") int row) {
        AccountBondObjDetailDto accountBondObjDetailDto = new AccountBondObjDetailDto();
        accountBondObjDetailDto.setPage(page);
        accountBondObjDetailDto.setRow(row);
        accountBondObjDetailDto.setDetailId( detailId );
        accountBondObjDetailDto.setBobjId( bobjId );
        accountBondObjDetailDto.setState( state );
        accountBondObjDetailDto.setObjId( objId );
        return getAccountBondObjDetailBMOImpl.get(accountBondObjDetailDto);
    }
}
