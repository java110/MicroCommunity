package com.java110.intf.store;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.contract.ContractChangePlanDto;
import com.java110.po.contractChangePlan.ContractChangePlanPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IContractChangePlanInnerServiceSMO
 * @Description 合同变更计划接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "store-service", configuration = {FeignConfiguration.class})
@RequestMapping("/contractChangePlanApi")
public interface IContractChangePlanInnerServiceSMO {


    @RequestMapping(value = "/saveContractChangePlan", method = RequestMethod.POST)
    public int saveContractChangePlan(@RequestBody ContractChangePlanPo contractChangePlanPo);

    @RequestMapping(value = "/updateContractChangePlan", method = RequestMethod.POST)
    public int updateContractChangePlan(@RequestBody  ContractChangePlanPo contractChangePlanPo);

    @RequestMapping(value = "/deleteContractChangePlan", method = RequestMethod.POST)
    public int deleteContractChangePlan(@RequestBody  ContractChangePlanPo contractChangePlanPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param contractChangePlanDto 数据对象分享
     * @return ContractChangePlanDto 对象数据
     */
    @RequestMapping(value = "/queryContractChangePlans", method = RequestMethod.POST)
    List<ContractChangePlanDto> queryContractChangePlans(@RequestBody ContractChangePlanDto contractChangePlanDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param contractChangePlanDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryContractChangePlansCount", method = RequestMethod.POST)
    int queryContractChangePlansCount(@RequestBody ContractChangePlanDto contractChangePlanDto);
}
