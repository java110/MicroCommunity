package com.java110.intf.store;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.contract.ContractChangePlanDetailDto;
import com.java110.po.contractChangePlanDetail.ContractChangePlanDetailPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IContractChangePlanDetailInnerServiceSMO
 * @Description 合同变更明细接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "store-service", configuration = {FeignConfiguration.class})
@RequestMapping("/contractChangePlanDetailApi")
public interface IContractChangePlanDetailInnerServiceSMO {


    @RequestMapping(value = "/saveContractChangePlanDetail", method = RequestMethod.POST)
    public int saveContractChangePlanDetail(@RequestBody ContractChangePlanDetailPo contractChangePlanDetailPo);

    @RequestMapping(value = "/updateContractChangePlanDetail", method = RequestMethod.POST)
    public int updateContractChangePlanDetail(@RequestBody  ContractChangePlanDetailPo contractChangePlanDetailPo);

    @RequestMapping(value = "/deleteContractChangePlanDetail", method = RequestMethod.POST)
    public int deleteContractChangePlanDetail(@RequestBody  ContractChangePlanDetailPo contractChangePlanDetailPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param contractChangePlanDetailDto 数据对象分享
     * @return ContractChangePlanDetailDto 对象数据
     */
    @RequestMapping(value = "/queryContractChangePlanDetails", method = RequestMethod.POST)
    List<ContractChangePlanDetailDto> queryContractChangePlanDetails(@RequestBody ContractChangePlanDetailDto contractChangePlanDetailDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param contractChangePlanDetailDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryContractChangePlanDetailsCount", method = RequestMethod.POST)
    int queryContractChangePlanDetailsCount(@RequestBody ContractChangePlanDetailDto contractChangePlanDetailDto);
}
