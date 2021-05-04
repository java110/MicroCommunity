package com.java110.acct.api;

import com.java110.acct.bmo.account.IGetAccountBMO;
import com.java110.dto.account.AccountDto;
import com.java110.dto.shopVipAccountDetail.ShopVipAccountDetailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
                                                            @RequestParam(value = "page") int page,
                                                            @RequestParam(value = "row") int row) {
        AccountDto accountDto = new AccountDto();
        accountDto.setPage(page);
        accountDto.setRow(row);
        accountDto.setObjId(storeId);
        return getAccountBMOImpl.get(accountDto);
    }
}
