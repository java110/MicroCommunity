package com.java110.feign.base;

import com.java110.entity.mapping.CodeMapping;
import feign.Param;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by wuxw on 2017/7/25.
 */
@FeignClient(name = "base-service", fallback = CommonServiceFallback.class)
public interface ICommonService {

    /**
     * 查询所有有效的映射数据
     *
     * @return
     */
    @RequestMapping("/commonService/getCodeMappingAll")
    public List<CodeMapping> getCodeMappingAll()  throws Exception;

    /**
     * 根据域查询对应的映射关系
     *
     * @param domain
     * @return
     */
    @RequestMapping("/commonService/getCodeMappingByDomain")
    public List<CodeMapping> getCodeMappingByDomain(@RequestParam("domain") String domain)  throws Exception;

    /**
     * 根据HCode查询映射关系
     *
     * @param hCode
     * @return
     */
    @RequestMapping("/commonService/getCodeMappingByHCode")
    public List<CodeMapping> getCodeMappingByHCode(@RequestParam("hCode") String hCode)  throws Exception;


    /**
     * 根据PCode查询映射关系
     *
     * @param pCode
     * @return
     */
    @RequestMapping("/commonService/getCodeMappingByHCode")
    public List<CodeMapping> getCodeMappingByPCode(@RequestParam("pCode") String pCode)  throws Exception;

    /**
     * 根据domain 和 hcode 查询映射关系
     *
     * @param pCode
     * @return
     */
    @RequestMapping("/commonService/getCodeMappingByDomainAndHCode")
    public List<CodeMapping> getCodeMappingByDomainAndHCode(@RequestParam("hCode") String hCode,@RequestParam("pCode") String pCode)  throws Exception;

    /**
     * 根据domain 和 pcode 查询映射关系
     *
     * @param domain
     * @return
     */
    @RequestMapping("/commonService/getCodeMappingByDomainAndPCode")
    public List<CodeMapping> getCodeMappingByDomainAndPCode(@RequestParam("domain") String domain,@RequestParam("pCode") String pCode)  throws Exception;

    /**
     * 常量域和h_code 查询
     * @param hCode
     * @return
     * @throws Exception
     */
    @RequestMapping("/commonService/getDynamicConstantValueByHCode")
    public List<CodeMapping> getDynamicConstantValueByHCode(@RequestParam("hCode") String hCode) throws Exception;

    /**
     * 常量域和h_code 查询
     * @param pCode
     * @return
     * @throws Exception
     */
    @RequestMapping("/commonService/getDynamicConstantValueByPCode")
    public List<CodeMapping> getDynamicConstantValueByPCode(@RequestParam("pCode") String pCode) throws Exception;

}


