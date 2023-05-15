package com.java110.intf.store;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.contract.ContractTypeSpecDto;
import com.java110.po.contractTypeSpec.ContractTypeSpecPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IContractTypeSpecInnerServiceSMO
 * @Description 合同类型规格接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "store-service", configuration = {FeignConfiguration.class})
@RequestMapping("/contractTypeSpecApi")
public interface IContractTypeSpecInnerServiceSMO {


    @RequestMapping(value = "/saveContractTypeSpec", method = RequestMethod.POST)
    public int saveContractTypeSpec(@RequestBody ContractTypeSpecPo contractTypeSpecPo);

    @RequestMapping(value = "/updateContractTypeSpec", method = RequestMethod.POST)
    public int updateContractTypeSpec(@RequestBody ContractTypeSpecPo contractTypeSpecPo);

    @RequestMapping(value = "/deleteContractTypeSpec", method = RequestMethod.POST)
    public int deleteContractTypeSpec(@RequestBody ContractTypeSpecPo contractTypeSpecPo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param contractTypeSpecDto 数据对象分享
     * @return ContractTypeSpecDto 对象数据
     */
    @RequestMapping(value = "/queryContractTypeSpecs", method = RequestMethod.POST)
    List<ContractTypeSpecDto> queryContractTypeSpecs(@RequestBody ContractTypeSpecDto contractTypeSpecDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param contractTypeSpecDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryContractTypeSpecsCount", method = RequestMethod.POST)
    int queryContractTypeSpecsCount(@RequestBody ContractTypeSpecDto contractTypeSpecDto);
}
