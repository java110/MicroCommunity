package com.java110.intf.acct;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.account.AccountDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IAccountInnerServiceSMO
 * @Description 账户接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "acct-service", configuration = {FeignConfiguration.class})
@RequestMapping("/accountApi")
public interface IAccountInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param accountDto 数据对象分享
     * @return AccountDto 对象数据
     */
    @RequestMapping(value = "/queryAccounts", method = RequestMethod.POST)
    List<AccountDto> queryAccounts(@RequestBody AccountDto accountDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param accountDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryAccountsCount", method = RequestMethod.POST)
    int queryAccountsCount(@RequestBody AccountDto accountDto);
}
