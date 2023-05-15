package com.java110.intf.store;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.contract.ContractAttrDto;
import com.java110.po.contractAttr.ContractAttrPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IContractAttrInnerServiceSMO
 * @Description 合同属性接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/contractAttrApi")
public interface IContractAttrInnerServiceSMO {


    @RequestMapping(value = "/saveContractAttr", method = RequestMethod.POST)
    public int saveContractAttr(@RequestBody ContractAttrPo contractAttrPo);

    @RequestMapping(value = "/updateContractAttr", method = RequestMethod.POST)
    public int updateContractAttr(@RequestBody ContractAttrPo contractAttrPo);

    @RequestMapping(value = "/deleteContractAttr", method = RequestMethod.POST)
    public int deleteContractAttr(@RequestBody ContractAttrPo contractAttrPo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param contractAttrDto 数据对象分享
     * @return ContractAttrDto 对象数据
     */
    @RequestMapping(value = "/queryContractAttrs", method = RequestMethod.POST)
    List<ContractAttrDto> queryContractAttrs(@RequestBody ContractAttrDto contractAttrDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param contractAttrDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryContractAttrsCount", method = RequestMethod.POST)
    int queryContractAttrsCount(@RequestBody ContractAttrDto contractAttrDto);
}
