package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.assetImportLog.AssetImportLogDetailDto;
import com.java110.po.assetImportLogDetail.AssetImportLogDetailPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IAssetImportLogDetailInnerServiceSMO
 * @Description 批量操作日志详情接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/assetImportLogDetailApi")
public interface IAssetImportLogDetailInnerServiceSMO {


    @RequestMapping(value = "/saveAssetImportLogDetail", method = RequestMethod.POST)
    public int saveAssetImportLogDetail(@RequestBody AssetImportLogDetailPo assetImportLogDetailPo);

    @RequestMapping(value = "/updateAssetImportLogDetail", method = RequestMethod.POST)
    public int updateAssetImportLogDetail(@RequestBody  AssetImportLogDetailPo assetImportLogDetailPo);

    @RequestMapping(value = "/deleteAssetImportLogDetail", method = RequestMethod.POST)
    public int deleteAssetImportLogDetail(@RequestBody  AssetImportLogDetailPo assetImportLogDetailPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param assetImportLogDetailDto 数据对象分享
     * @return AssetImportLogDetailDto 对象数据
     */
    @RequestMapping(value = "/queryAssetImportLogDetails", method = RequestMethod.POST)
    List<AssetImportLogDetailDto> queryAssetImportLogDetails(@RequestBody AssetImportLogDetailDto assetImportLogDetailDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param assetImportLogDetailDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryAssetImportLogDetailsCount", method = RequestMethod.POST)
    int queryAssetImportLogDetailsCount(@RequestBody AssetImportLogDetailDto assetImportLogDetailDto);

    @RequestMapping(value = "/saveAssetImportLogDetails", method = RequestMethod.POST)
    int saveAssetImportLogDetails(@RequestBody List<AssetImportLogDetailPo> assetImportLogDetailPos);
}
