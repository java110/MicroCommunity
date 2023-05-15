package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.file.FileRelDto;
import com.java110.po.file.FileRelPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IFileRelInnerServiceSMO
 * @Description 文件存放接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/fileRelApi")
public interface IFileRelInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param fileRelDto 数据对象分享
     * @return FileRelDto 对象数据
     */
    @RequestMapping(value = "/queryFileRels", method = RequestMethod.POST)
    List<FileRelDto> queryFileRels(@RequestBody FileRelDto fileRelDto);


    @RequestMapping(value = "/saveFileRel", method = RequestMethod.POST)
    public int saveFileRel(@RequestBody FileRelPo fileRelPo);

    @RequestMapping(value = "/updateFileRel", method = RequestMethod.POST)
    public int updateFileRel(@RequestBody FileRelPo fileRelPo);

    @RequestMapping(value = "/deleteFileRel", method = RequestMethod.POST)
    public int deleteFileRel(@RequestBody FileRelPo fileRelPo);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param fileRelDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryFileRelsCount", method = RequestMethod.POST)
    int queryFileRelsCount(@RequestBody FileRelDto fileRelDto);
}
