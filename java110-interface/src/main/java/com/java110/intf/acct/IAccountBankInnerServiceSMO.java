package com.java110.intf.acct;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.account.AccountBankDto;
import com.java110.po.accountBank.AccountBankPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IAccountBankInnerServiceSMO
 * @Description 开户行接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "acct-service", configuration = {FeignConfiguration.class})
@RequestMapping("/accountBankApi")
public interface IAccountBankInnerServiceSMO {


    @RequestMapping(value = "/saveAccountBank", method = RequestMethod.POST)
    public int saveAccountBank(@RequestBody AccountBankPo accountBankPo);

    @RequestMapping(value = "/updateAccountBank", method = RequestMethod.POST)
    public int updateAccountBank(@RequestBody  AccountBankPo accountBankPo);

    @RequestMapping(value = "/deleteAccountBank", method = RequestMethod.POST)
    public int deleteAccountBank(@RequestBody  AccountBankPo accountBankPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param accountBankDto 数据对象分享
     * @return AccountBankDto 对象数据
     */
    @RequestMapping(value = "/queryAccountBanks", method = RequestMethod.POST)
    List<AccountBankDto> queryAccountBanks(@RequestBody AccountBankDto accountBankDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param accountBankDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryAccountBanksCount", method = RequestMethod.POST)
    int queryAccountBanksCount(@RequestBody AccountBankDto accountBankDto);
}
