package com.java110.intf.store;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.contract.ContractChangePlanDetailAttrDto;
import com.java110.po.contractChangePlanDetailAttr.ContractChangePlanDetailAttrPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IContractChangePlanDetailAttrInnerServiceSMO
 * @Description 合同变更属性接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "store-service", configuration = {FeignConfiguration.class})
@RequestMapping("/contractChangePlanDetailAttrApi")
public interface IContractChangePlanDetailAttrInnerServiceSMO {


    @RequestMapping(value = "/saveContractChangePlanDetailAttr", method = RequestMethod.POST)
    public int saveContractChangePlanDetailAttr(@RequestBody ContractChangePlanDetailAttrPo contractChangePlanDetailAttrPo);

    @RequestMapping(value = "/updateContractChangePlanDetailAttr", method = RequestMethod.POST)
    public int updateContractChangePlanDetailAttr(@RequestBody  ContractChangePlanDetailAttrPo contractChangePlanDetailAttrPo);

    @RequestMapping(value = "/deleteContractChangePlanDetailAttr", method = RequestMethod.POST)
    public int deleteContractChangePlanDetailAttr(@RequestBody  ContractChangePlanDetailAttrPo contractChangePlanDetailAttrPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param contractChangePlanDetailAttrDto 数据对象分享
     * @return ContractChangePlanDetailAttrDto 对象数据
     */
    @RequestMapping(value = "/queryContractChangePlanDetailAttrs", method = RequestMethod.POST)
    List<ContractChangePlanDetailAttrDto> queryContractChangePlanDetailAttrs(@RequestBody ContractChangePlanDetailAttrDto contractChangePlanDetailAttrDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param contractChangePlanDetailAttrDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryContractChangePlanDetailAttrsCount", method = RequestMethod.POST)
    int queryContractChangePlanDetailAttrsCount(@RequestBody ContractChangePlanDetailAttrDto contractChangePlanDetailAttrDto);
}
