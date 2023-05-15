package com.java110.intf.store;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.contract.ContractDto;
import com.java110.po.contract.ContractPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * @ClassName IContractInnerServiceSMO
 * @Description 合同管理接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "store-service", configuration = {FeignConfiguration.class})
@RequestMapping("/contractApi")
public interface IContractInnerServiceSMO {


    @RequestMapping(value = "/saveContract", method = RequestMethod.POST)
    public int saveContract(@RequestBody ContractPo contractPo);

    @RequestMapping(value = "/updateContract", method = RequestMethod.POST)
    public int updateContract(@RequestBody ContractPo contractPo);

    @RequestMapping(value = "/deleteContract", method = RequestMethod.POST)
    public int deleteContract(@RequestBody ContractPo contractPo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param contractDto 数据对象分享
     * @return ContractDto 对象数据
     */
    @RequestMapping(value = "/queryContracts", method = RequestMethod.POST)
    List<ContractDto> queryContracts(@RequestBody ContractDto contractDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param contractDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryContractsCount", method = RequestMethod.POST)
    int queryContractsCount(@RequestBody ContractDto contractDto);

    /**
     * 查询业主合同
     * @param info
     * @return
     */
    @RequestMapping(value = "/queryContractsByOwnerIds", method = RequestMethod.POST)
    List<Map> queryContractsByOwnerIds(@RequestBody Map info);
}
