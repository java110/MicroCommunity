package com.java110.intf.acct;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.account.AccountBondObjDetailDto;
import com.java110.po.accountBondObjDetail.AccountBondObjDetailPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IAccountBondObjDetailInnerServiceSMO
 * @Description 保证金明细接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "acct-service", configuration = {FeignConfiguration.class})
@RequestMapping("/accountBondObjDetailApi")
public interface IAccountBondObjDetailInnerServiceSMO {


    @RequestMapping(value = "/saveAccountBondObjDetail", method = RequestMethod.POST)
    public int saveAccountBondObjDetail(@RequestBody AccountBondObjDetailPo accountBondObjDetailPo);

    @RequestMapping(value = "/updateAccountBondObjDetail", method = RequestMethod.POST)
    public int updateAccountBondObjDetail(@RequestBody  AccountBondObjDetailPo accountBondObjDetailPo);

    @RequestMapping(value = "/deleteAccountBondObjDetail", method = RequestMethod.POST)
    public int deleteAccountBondObjDetail(@RequestBody  AccountBondObjDetailPo accountBondObjDetailPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param accountBondObjDetailDto 数据对象分享
     * @return AccountBondObjDetailDto 对象数据
     */
    @RequestMapping(value = "/queryAccountBondObjDetails", method = RequestMethod.POST)
    List<AccountBondObjDetailDto> queryAccountBondObjDetails(@RequestBody AccountBondObjDetailDto accountBondObjDetailDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param accountBondObjDetailDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryAccountBondObjDetailsCount", method = RequestMethod.POST)
    int queryAccountBondObjDetailsCount(@RequestBody AccountBondObjDetailDto accountBondObjDetailDto);
}
