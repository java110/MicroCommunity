package com.java110.code.rest;

import com.java110.code.smo.ICommonServiceSmo;
import com.java110.core.base.controller.BaseController;
import com.java110.entity.mapping.CodeMapping;
import com.java110.feign.base.ICommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by wuxw on 2017/7/25.
 */
//@RestController
public class CommonServiceRest extends BaseController implements ICommonService{

    @Autowired
    ICommonServiceSmo commonServiceSmoImpl;

    @Override
    @RequestMapping("/commonService/getCodeMappingAll")
    public List<CodeMapping> getCodeMappingAll()  throws Exception{
        return commonServiceSmoImpl.getCodeMappingAll();
    }

    @Override
    @RequestMapping("/commonService/getCodeMappingByDomain")
    public List<CodeMapping> getCodeMappingByDomain(@RequestParam("domain") String domain)  throws Exception{
        CodeMapping codeMapping = new CodeMapping();
        codeMapping.setDomain(domain);
        return commonServiceSmoImpl.getCodeMappingByDomain(codeMapping);
    }

    @Override
    @RequestMapping("/commonService/getCodeMappingByHCode")
    public List<CodeMapping> getCodeMappingByHCode(@RequestParam("hCode") String hCode)  throws Exception{
        CodeMapping codeMapping = new CodeMapping();
        codeMapping.setH_code(hCode);
        return commonServiceSmoImpl.getCodeMappingByHCode(codeMapping);
    }

    @Override
    @RequestMapping("/commonService/getCodeMappingByPCode")
    public List<CodeMapping> getCodeMappingByPCode(@RequestParam("pCode") String pCode)  throws Exception{
        CodeMapping codeMapping = new CodeMapping();
        codeMapping.setP_code(pCode);
        return commonServiceSmoImpl.getCodeMappingByPCode(codeMapping);
    }

    @Override
    @RequestMapping("/commonService/getCodeMappingByDomainAndHCode")
    public List<CodeMapping> getCodeMappingByDomainAndHCode(@RequestParam("hCode") String hCode,@RequestParam("pCode") String pCode)  throws Exception{
        CodeMapping codeMapping = new CodeMapping();
        codeMapping.setH_code(hCode);
        codeMapping.setP_code(pCode);
        return commonServiceSmoImpl.getCodeMappingByDomainAndHCode(codeMapping);
    }

    @Override
    @RequestMapping("/commonService/getCodeMappingByDomainAndPCode")
    public List<CodeMapping> getCodeMappingByDomainAndPCode(@RequestParam("domain") String domain, @RequestParam("pCode") String pCode)  throws Exception{
        CodeMapping codeMapping = new CodeMapping();
        codeMapping.setDomain(domain);
        codeMapping.setP_code(pCode);
        return commonServiceSmoImpl.getCodeMappingByDomainAndPCode(codeMapping);
    }


    /**
     * 常量域和h_code 查询
     * @param hCode
     * @return
     * @throws Exception
     */
    @RequestMapping("/commonService/getDynamicConstantValueByHCode")
    public List<CodeMapping> getDynamicConstantValueByHCode(@RequestParam("hCode") String hCode) throws Exception{
        return getCodeMappingByDomainAndHCode("DynamicConstant",hCode);
    }

    /**
     * 常量域和h_code 查询
     * @param pCode
     * @return
     * @throws Exception
     */
    @RequestMapping("/commonService/getDynamicConstantValueByPCode")
    public List<CodeMapping> getDynamicConstantValueByPCode(@RequestParam("pCode") String pCode) throws Exception{
        return getCodeMappingByDomainAndPCode("DynamicConstant",pCode);
    }

    public ICommonServiceSmo getCommonServiceSmoImpl() {
        return commonServiceSmoImpl;
    }

    public void setCommonServiceSmoImpl(ICommonServiceSmo commonServiceSmoImpl) {
        this.commonServiceSmoImpl = commonServiceSmoImpl;
    }
}
