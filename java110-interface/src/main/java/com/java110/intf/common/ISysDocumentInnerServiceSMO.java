package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.sysDocument.SysDocumentDto;
import com.java110.po.sysDocument.SysDocumentPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName ISysDocumentInnerServiceSMO
 * @Description 系统文档接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/sysDocumentApi")
public interface ISysDocumentInnerServiceSMO {


    @RequestMapping(value = "/saveSysDocument", method = RequestMethod.POST)
    public int saveSysDocument(@RequestBody SysDocumentPo sysDocumentPo);

    @RequestMapping(value = "/updateSysDocument", method = RequestMethod.POST)
    public int updateSysDocument(@RequestBody SysDocumentPo sysDocumentPo);

    @RequestMapping(value = "/deleteSysDocument", method = RequestMethod.POST)
    public int deleteSysDocument(@RequestBody SysDocumentPo sysDocumentPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param sysDocumentDto 数据对象分享
     * @return SysDocumentDto 对象数据
     */
    @RequestMapping(value = "/querySysDocuments", method = RequestMethod.POST)
    List<SysDocumentDto> querySysDocuments(@RequestBody SysDocumentDto sysDocumentDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param sysDocumentDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/querySysDocumentsCount", method = RequestMethod.POST)
    int querySysDocumentsCount(@RequestBody SysDocumentDto sysDocumentDto);
}
