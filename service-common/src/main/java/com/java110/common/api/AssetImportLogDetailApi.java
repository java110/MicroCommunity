package com.java110.common.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.assetImportLogDetail.IDeleteAssetImportLogDetailBMO;
import com.java110.common.bmo.assetImportLogDetail.IGetAssetImportLogDetailBMO;
import com.java110.common.bmo.assetImportLogDetail.ISaveAssetImportLogDetailBMO;
import com.java110.common.bmo.assetImportLogDetail.IUpdateAssetImportLogDetailBMO;
import com.java110.dto.assetImportLog.AssetImportLogDetailDto;
import com.java110.po.assetImportLogDetail.AssetImportLogDetailPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/assetImportLogDetail")
public class AssetImportLogDetailApi {

    @Autowired
    private ISaveAssetImportLogDetailBMO saveAssetImportLogDetailBMOImpl;
    @Autowired
    private IUpdateAssetImportLogDetailBMO updateAssetImportLogDetailBMOImpl;
    @Autowired
    private IDeleteAssetImportLogDetailBMO deleteAssetImportLogDetailBMOImpl;

    @Autowired
    private IGetAssetImportLogDetailBMO getAssetImportLogDetailBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /assetImportLogDetail/saveAssetImportLogDetail
     * @path /app/assetImportLogDetail/saveAssetImportLogDetail
     */
    @RequestMapping(value = "/saveAssetImportLogDetail", method = RequestMethod.POST)
    public ResponseEntity<String> saveAssetImportLogDetail(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");


        AssetImportLogDetailPo assetImportLogDetailPo = BeanConvertUtil.covertBean(reqJson, AssetImportLogDetailPo.class);
        return saveAssetImportLogDetailBMOImpl.save(assetImportLogDetailPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /assetImportLogDetail/updateAssetImportLogDetail
     * @path /app/assetImportLogDetail/updateAssetImportLogDetail
     */
    @RequestMapping(value = "/updateAssetImportLogDetail", method = RequestMethod.POST)
    public ResponseEntity<String> updateAssetImportLogDetail(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "detailId", "detailId不能为空");


        AssetImportLogDetailPo assetImportLogDetailPo = BeanConvertUtil.covertBean(reqJson, AssetImportLogDetailPo.class);
        return updateAssetImportLogDetailBMOImpl.update(assetImportLogDetailPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /assetImportLogDetail/deleteAssetImportLogDetail
     * @path /app/assetImportLogDetail/deleteAssetImportLogDetail
     */
    @RequestMapping(value = "/deleteAssetImportLogDetail", method = RequestMethod.POST)
    public ResponseEntity<String> deleteAssetImportLogDetail(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "detailId", "detailId不能为空");


        AssetImportLogDetailPo assetImportLogDetailPo = BeanConvertUtil.covertBean(reqJson, AssetImportLogDetailPo.class);
        return deleteAssetImportLogDetailBMOImpl.delete(assetImportLogDetailPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /assetImportLogDetail/queryAssetImportLogDetail
     * @path /app/assetImportLogDetail/queryAssetImportLogDetail
     */
    @RequestMapping(value = "/queryAssetImportLogDetail", method = RequestMethod.GET)
    public ResponseEntity<String> queryAssetImportLogDetail(@RequestParam(value = "communityId") String communityId,
                                                            @RequestParam(value = "logId",required = false) String logId,
                                                            @RequestParam(value = "page") int page,
                                                            @RequestParam(value = "row") int row) {
        AssetImportLogDetailDto assetImportLogDetailDto = new AssetImportLogDetailDto();
        assetImportLogDetailDto.setPage(page);
        assetImportLogDetailDto.setRow(row);
        assetImportLogDetailDto.setCommunityId(communityId);
        assetImportLogDetailDto.setLogId(logId);
        return getAssetImportLogDetailBMOImpl.get(assetImportLogDetailDto);
    }
}
