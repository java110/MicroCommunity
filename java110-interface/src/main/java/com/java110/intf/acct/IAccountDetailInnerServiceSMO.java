package com.java110.intf.acct;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.account.AccountDetailDto;
import com.java110.po.accountDetail.AccountDetailPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IAccountDetailInnerServiceSMO
 * @Description 账户交易接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "acct-service", configuration = {FeignConfiguration.class})
@RequestMapping("/accountDetailApi")
public interface IAccountDetailInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param accountDetailDto 数据对象分享
     * @return AccountDetailDto 对象数据
     */
    @RequestMapping(value = "/queryAccountDetails", method = RequestMethod.POST)
    List<AccountDetailDto> queryAccountDetails(@RequestBody AccountDetailDto accountDetailDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param accountDetailDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryAccountDetailsCount", method = RequestMethod.POST)
    int queryAccountDetailsCount(@RequestBody AccountDetailDto accountDetailDto);


    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param accountDetailPo 数据对象分享
     * @return AccountDetailDto 对象数据
     */
    @RequestMapping(value = "/saveAccountDetails", method = RequestMethod.POST)
    int saveAccountDetails(@RequestBody AccountDetailPo accountDetailPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param accountDetailPo 数据对象分享
     * @return AccountDetailDto 对象数据
     */
    @RequestMapping(value = "/updateAccountDetails", method = RequestMethod.POST)
    int updateAccountDetails(@RequestBody AccountDetailPo accountDetailPo);
}
