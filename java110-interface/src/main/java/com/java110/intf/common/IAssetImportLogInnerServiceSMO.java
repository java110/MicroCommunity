package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.assetImportLog.AssetImportLogDto;
import com.java110.po.assetImportLog.AssetImportLogPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IAssetImportLogInnerServiceSMO
 * @Description 批量操作日志接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/assetImportLogApi")
public interface IAssetImportLogInnerServiceSMO {


    @RequestMapping(value = "/saveAssetImportLog", method = RequestMethod.POST)
    public int saveAssetImportLog(@RequestBody AssetImportLogPo assetImportLogPo);

    @RequestMapping(value = "/updateAssetImportLog", method = RequestMethod.POST)
    public int updateAssetImportLog(@RequestBody  AssetImportLogPo assetImportLogPo);

    @RequestMapping(value = "/deleteAssetImportLog", method = RequestMethod.POST)
    public int deleteAssetImportLog(@RequestBody  AssetImportLogPo assetImportLogPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param assetImportLogDto 数据对象分享
     * @return AssetImportLogDto 对象数据
     */
    @RequestMapping(value = "/queryAssetImportLogs", method = RequestMethod.POST)
    List<AssetImportLogDto> queryAssetImportLogs(@RequestBody AssetImportLogDto assetImportLogDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param assetImportLogDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryAssetImportLogsCount", method = RequestMethod.POST)
    int queryAssetImportLogsCount(@RequestBody AssetImportLogDto assetImportLogDto);
}
