package com.java110.intf.store;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.contract.ContractTypeTemplateDto;
import com.java110.po.contractTypeTemplate.ContractTypeTemplatePo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IContractTypeTemplateInnerServiceSMO
 * @Description 合同属性接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "store-service", configuration = {FeignConfiguration.class})
@RequestMapping("/contractTypeTemplateApi")
public interface IContractTypeTemplateInnerServiceSMO {


    @RequestMapping(value = "/saveContractTypeTemplate", method = RequestMethod.POST)
    public int saveContractTypeTemplate(@RequestBody ContractTypeTemplatePo contractTypeTemplatePo);

    @RequestMapping(value = "/updateContractTypeTemplate", method = RequestMethod.POST)
    public int updateContractTypeTemplate(@RequestBody ContractTypeTemplatePo contractTypeTemplatePo);

    @RequestMapping(value = "/deleteContractTypeTemplate", method = RequestMethod.POST)
    public int deleteContractTypeTemplate(@RequestBody ContractTypeTemplatePo contractTypeTemplatePo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param contractTypeTemplateDto 数据对象分享
     * @return ContractTypeTemplateDto 对象数据
     */
    @RequestMapping(value = "/queryContractTypeTemplates", method = RequestMethod.POST)
    List<ContractTypeTemplateDto> queryContractTypeTemplates(@RequestBody ContractTypeTemplateDto contractTypeTemplateDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param contractTypeTemplateDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryContractTypeTemplatesCount", method = RequestMethod.POST)
    int queryContractTypeTemplatesCount(@RequestBody ContractTypeTemplateDto contractTypeTemplateDto);
}
