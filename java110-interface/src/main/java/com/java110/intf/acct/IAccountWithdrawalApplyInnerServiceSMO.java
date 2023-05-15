package com.java110.intf.acct;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.account.AccountWithdrawalApplyDto;
import com.java110.po.accountWithdrawalApply.AccountWithdrawalApplyPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @ClassName IAccountWithdrawalApplyInnerServiceSMO
 * @Description 账户提现接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/accountWithdrawalApplyApi")
public interface IAccountWithdrawalApplyInnerServiceSMO {


    @RequestMapping(value = "/saveAccountWithdrawalApply", method = RequestMethod.POST)
    public int saveAccountWithdrawalApply(@RequestBody AccountWithdrawalApplyPo accountWithdrawalApplyPo);

    @RequestMapping(value = "/updateAccountWithdrawalApply", method = RequestMethod.POST)
    public int updateAccountWithdrawalApply(@RequestBody  AccountWithdrawalApplyPo accountWithdrawalApplyPo);

    @RequestMapping(value = "/deleteAccountWithdrawalApply", method = RequestMethod.POST)
    public int deleteAccountWithdrawalApply(@RequestBody  AccountWithdrawalApplyPo accountWithdrawalApplyPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param accountWithdrawalApplyDto 数据对象分享
     * @return AccountWithdrawalApplyDto 对象数据
     */
    @RequestMapping(value = "/queryAccountWithdrawalApplys", method = RequestMethod.POST)
    List<AccountWithdrawalApplyDto> queryAccountWithdrawalApplys(@RequestBody AccountWithdrawalApplyDto accountWithdrawalApplyDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param accountWithdrawalApplyDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryAccountWithdrawalApplysCount", method = RequestMethod.POST)
    int queryAccountWithdrawalApplysCount(@RequestBody AccountWithdrawalApplyDto accountWithdrawalApplyDto);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param states 数据对象分享
     * @return AccountWithdrawalApplyDto 对象数据
     */
    @RequestMapping(value = "/listStateWithdrawalApplys", method = RequestMethod.POST)
    List<AccountWithdrawalApplyDto> listStateWithdrawalApplys(@RequestParam String [] states);
    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param states 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/listStateWithdrawalApplysCount", method = RequestMethod.POST)
    int listStateWithdrawalApplysCount(@RequestParam String [] states);
}
