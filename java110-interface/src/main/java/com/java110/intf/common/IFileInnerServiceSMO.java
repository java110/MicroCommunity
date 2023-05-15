package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.file.FileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 文件处理，将文件 存在数据库中（只有演示环境可以保存到数据库中，正式环境 应该对接到文件系统）
 */
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/fileApi")
public interface IFileInnerServiceSMO {

    /**
     * 保存文件
     * @param fileDto 文件数据封装
     * @return true 成功 false 失败
     */
    @RequestMapping(value = "/saveFile", method = RequestMethod.POST)
    String saveFile(@RequestBody FileDto fileDto);

    /**
     * 查询文件信息
     * @param fileDto 文件数据封装
     * @return 文件数据对象
     */
    @RequestMapping(value = "/queryFiles", method = RequestMethod.POST)
    List<FileDto> queryFiles(@RequestBody FileDto fileDto);
}
