package com.java110.intf.store;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.contract.ContractPartyaDto;
import com.java110.po.contractPartya.ContractPartyaPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IContractPartyaInnerServiceSMO
 * @Description 合同房屋接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "store-service", configuration = {FeignConfiguration.class})
@RequestMapping("/contractPartyaApi")
public interface IContractPartyaInnerServiceSMO {


    @RequestMapping(value = "/saveContractPartya", method = RequestMethod.POST)
    public int saveContractPartya(@RequestBody ContractPartyaPo contractPartyaPo);

    @RequestMapping(value = "/updateContractPartya", method = RequestMethod.POST)
    public int updateContractPartya(@RequestBody  ContractPartyaPo contractPartyaPo);

    @RequestMapping(value = "/deleteContractPartya", method = RequestMethod.POST)
    public int deleteContractPartya(@RequestBody  ContractPartyaPo contractPartyaPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param contractPartyaDto 数据对象分享
     * @return ContractPartyaDto 对象数据
     */
    @RequestMapping(value = "/queryContractPartyas", method = RequestMethod.POST)
    List<ContractPartyaDto> queryContractPartyas(@RequestBody ContractPartyaDto contractPartyaDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param contractPartyaDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryContractPartyasCount", method = RequestMethod.POST)
    int queryContractPartyasCount(@RequestBody ContractPartyaDto contractPartyaDto);
}
