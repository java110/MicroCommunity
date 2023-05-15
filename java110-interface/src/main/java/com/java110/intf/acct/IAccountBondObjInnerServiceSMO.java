package com.java110.intf.acct;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.account.AccountBondObjDto;
import com.java110.po.accountBondObj.AccountBondObjPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IAccountBondObjInnerServiceSMO
 * @Description 保证金对象接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "acct-service", configuration = {FeignConfiguration.class})
@RequestMapping("/accountBondObjApi")
public interface IAccountBondObjInnerServiceSMO {


    @RequestMapping(value = "/saveAccountBondObj", method = RequestMethod.POST)
    public int saveAccountBondObj(@RequestBody AccountBondObjPo accountBondObjPo);

    @RequestMapping(value = "/updateAccountBondObj", method = RequestMethod.POST)
    public int updateAccountBondObj(@RequestBody  AccountBondObjPo accountBondObjPo);

    @RequestMapping(value = "/deleteAccountBondObj", method = RequestMethod.POST)
    public int deleteAccountBondObj(@RequestBody  AccountBondObjPo accountBondObjPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param accountBondObjDto 数据对象分享
     * @return AccountBondObjDto 对象数据
     */
    @RequestMapping(value = "/queryAccountBondObjs", method = RequestMethod.POST)
    List<AccountBondObjDto> queryAccountBondObjs(@RequestBody AccountBondObjDto accountBondObjDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param accountBondObjDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryAccountBondObjsCount", method = RequestMethod.POST)
    int queryAccountBondObjsCount(@RequestBody AccountBondObjDto accountBondObjDto);
}
