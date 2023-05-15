package com.java110.intf.acct;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.account.AccountBondDto;
import com.java110.po.accountBond.AccountBondPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IAccountBondInnerServiceSMO
 * @Description 保证金接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "acct-service", configuration = {FeignConfiguration.class})
@RequestMapping("/accountBondApi")
public interface IAccountBondInnerServiceSMO {


    @RequestMapping(value = "/saveAccountBond", method = RequestMethod.POST)
    public int saveAccountBond(@RequestBody AccountBondPo accountBondPo);

    @RequestMapping(value = "/updateAccountBond", method = RequestMethod.POST)
    public int updateAccountBond(@RequestBody  AccountBondPo accountBondPo);

    @RequestMapping(value = "/deleteAccountBond", method = RequestMethod.POST)
    public int deleteAccountBond(@RequestBody  AccountBondPo accountBondPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param accountBondDto 数据对象分享
     * @return AccountBondDto 对象数据
     */
    @RequestMapping(value = "/queryAccountBonds", method = RequestMethod.POST)
    List<AccountBondDto> queryAccountBonds(@RequestBody AccountBondDto accountBondDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param accountBondDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryAccountBondsCount", method = RequestMethod.POST)
    int queryAccountBondsCount(@RequestBody AccountBondDto accountBondDto);
}
