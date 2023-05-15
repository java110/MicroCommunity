package com.java110.intf.store;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.contract.ContractCollectionPlanDto;
import com.java110.po.contractCollectionPlan.ContractCollectionPlanPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IContractCollectionPlanInnerServiceSMO
 * @Description 合同收款计划接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "store-service", configuration = {FeignConfiguration.class})
@RequestMapping("/contractCollectionPlanApi")
public interface IContractCollectionPlanInnerServiceSMO {


    @RequestMapping(value = "/saveContractCollectionPlan", method = RequestMethod.POST)
    public int saveContractCollectionPlan(@RequestBody ContractCollectionPlanPo contractCollectionPlanPo);

    @RequestMapping(value = "/updateContractCollectionPlan", method = RequestMethod.POST)
    public int updateContractCollectionPlan(@RequestBody  ContractCollectionPlanPo contractCollectionPlanPo);

    @RequestMapping(value = "/deleteContractCollectionPlan", method = RequestMethod.POST)
    public int deleteContractCollectionPlan(@RequestBody  ContractCollectionPlanPo contractCollectionPlanPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param contractCollectionPlanDto 数据对象分享
     * @return ContractCollectionPlanDto 对象数据
     */
    @RequestMapping(value = "/queryContractCollectionPlans", method = RequestMethod.POST)
    List<ContractCollectionPlanDto> queryContractCollectionPlans(@RequestBody ContractCollectionPlanDto contractCollectionPlanDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param contractCollectionPlanDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryContractCollectionPlansCount", method = RequestMethod.POST)
    int queryContractCollectionPlansCount(@RequestBody ContractCollectionPlanDto contractCollectionPlanDto);
}
