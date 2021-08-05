package com.java110.common.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.assetImportLog.IDeleteAssetImportLogBMO;
import com.java110.common.bmo.assetImportLog.IGetAssetImportLogBMO;
import com.java110.common.bmo.assetImportLog.ISaveAssetImportLogBMO;
import com.java110.common.bmo.assetImportLog.IUpdateAssetImportLogBMO;
import com.java110.dto.assetImportLog.AssetImportLogDto;
import com.java110.po.assetImportLog.AssetImportLogPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/assetImportLog")
public class AssetImportLogApi {

    @Autowired
    private ISaveAssetImportLogBMO saveAssetImportLogBMOImpl;
    @Autowired
    private IUpdateAssetImportLogBMO updateAssetImportLogBMOImpl;
    @Autowired
    private IDeleteAssetImportLogBMO deleteAssetImportLogBMOImpl;

    @Autowired
    private IGetAssetImportLogBMO getAssetImportLogBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJsonStr
     * @return
     * @serviceCode /assetImportLog/saveAssetImportLog
     * @path /app/assetImportLog/saveAssetImportLog
     */
    @RequestMapping(value = "/saveAssetImportLog", method = RequestMethod.POST)
    public ResponseEntity<String> saveAssetImportLog(@RequestBody String reqJsonStr) {
        JSONObject reqJson = JSONObject.parseObject(reqJsonStr);
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");

        return saveAssetImportLogBMOImpl.save(reqJson);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /assetImportLog/updateAssetImportLog
     * @path /app/assetImportLog/updateAssetImportLog
     */
    @RequestMapping(value = "/updateAssetImportLog", method = RequestMethod.POST)
    public ResponseEntity<String> updateAssetImportLog(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "logId", "logId不能为空");


        AssetImportLogPo assetImportLogPo = BeanConvertUtil.covertBean(reqJson, AssetImportLogPo.class);
        return updateAssetImportLogBMOImpl.update(assetImportLogPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /assetImportLog/deleteAssetImportLog
     * @path /app/assetImportLog/deleteAssetImportLog
     */
    @RequestMapping(value = "/deleteAssetImportLog", method = RequestMethod.POST)
    public ResponseEntity<String> deleteAssetImportLog(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "logId", "logId不能为空");


        AssetImportLogPo assetImportLogPo = BeanConvertUtil.covertBean(reqJson, AssetImportLogPo.class);
        return deleteAssetImportLogBMOImpl.delete(assetImportLogPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /assetImportLog/queryAssetImportLog
     * @path /app/assetImportLog/queryAssetImportLog
     */
    @RequestMapping(value = "/queryAssetImportLog", method = RequestMethod.GET)
    public ResponseEntity<String> queryAssetImportLog(@RequestParam(value = "communityId") String communityId,
                                                      @RequestParam(value = "page") int page,
                                                      @RequestParam(value = "row") int row) {
        AssetImportLogDto assetImportLogDto = new AssetImportLogDto();
        assetImportLogDto.setPage(page);
        assetImportLogDto.setRow(row);
        assetImportLogDto.setCommunityId(communityId);
        return getAssetImportLogBMOImpl.get(assetImportLogDto);
    }
}
