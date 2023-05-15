package com.java110.intf.store;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.contract.ContractTypeDto;
import com.java110.po.contractType.ContractTypePo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IContractTypeInnerServiceSMO
 * @Description 合同类型接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "store-service", configuration = {FeignConfiguration.class})
@RequestMapping("/contractTypeApi")
public interface IContractTypeInnerServiceSMO {


    @RequestMapping(value = "/saveContractType", method = RequestMethod.POST)
    public int saveContractType(@RequestBody ContractTypePo contractTypePo);

    @RequestMapping(value = "/updateContractType", method = RequestMethod.POST)
    public int updateContractType(@RequestBody ContractTypePo contractTypePo);

    @RequestMapping(value = "/deleteContractType", method = RequestMethod.POST)
    public int deleteContractType(@RequestBody ContractTypePo contractTypePo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param contractTypeDto 数据对象分享
     * @return ContractTypeDto 对象数据
     */
    @RequestMapping(value = "/queryContractTypes", method = RequestMethod.POST)
    List<ContractTypeDto> queryContractTypes(@RequestBody ContractTypeDto contractTypeDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param contractTypeDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryContractTypesCount", method = RequestMethod.POST)
    int queryContractTypesCount(@RequestBody ContractTypeDto contractTypeDto);
}
