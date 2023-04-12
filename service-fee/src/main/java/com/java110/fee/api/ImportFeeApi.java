package com.java110.fee.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.importFee.ImportFeeDto;
import com.java110.dto.importFee.ImportFeeDetailDto;
import com.java110.fee.bmo.importFee.IFeeSharingBMO;
import com.java110.fee.bmo.importFee.IGetImportFeeBMO;
import com.java110.fee.bmo.importFeeDetail.IDeleteImportFeeDetailBMO;
import com.java110.fee.bmo.importFeeDetail.IGetImportFeeDetailBMO;
import com.java110.fee.bmo.importFeeDetail.ISaveImportFeeDetailBMO;
import com.java110.fee.bmo.importFeeDetail.IUpdateImportFeeDetailBMO;
import com.java110.po.importFeeDetail.ImportFeeDetailPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/importFee")
public class ImportFeeApi {

    @Autowired
    private ISaveImportFeeDetailBMO saveImportFeeDetailBMOImpl;
    @Autowired
    private IUpdateImportFeeDetailBMO updateImportFeeDetailBMOImpl;
    @Autowired
    private IDeleteImportFeeDetailBMO deleteImportFeeDetailBMOImpl;

    @Autowired
    private IGetImportFeeDetailBMO getImportFeeDetailBMOImpl;


    @Autowired
    private IGetImportFeeBMO getImportFeeBMOImpl;

    @Autowired
    private IFeeSharingBMO feeSharingBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /importFee/saveImportFeeDetail
     * @path /app/importFee/saveImportFeeDetail
     */
    @RequestMapping(value = "/saveImportFeeDetail", method = RequestMethod.POST)
    public ResponseEntity<String> saveImportFeeDetail(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "ifdId", "请求报文中未包含ifdId");


        ImportFeeDetailPo importFeeDetailPo = BeanConvertUtil.covertBean(reqJson, ImportFeeDetailPo.class);
        return saveImportFeeDetailBMOImpl.save(importFeeDetailPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /importFee/updateImportFeeDetail
     * @path /app/importFee/updateImportFeeDetail
     */
    @RequestMapping(value = "/updateImportFeeDetail", method = RequestMethod.POST)
    public ResponseEntity<String> updateImportFeeDetail(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "ifdId", "请求报文中未包含ifdId");
        Assert.hasKeyAndValue(reqJson, "ifdId", "ifdId不能为空");


        ImportFeeDetailPo importFeeDetailPo = BeanConvertUtil.covertBean(reqJson, ImportFeeDetailPo.class);
        return updateImportFeeDetailBMOImpl.update(importFeeDetailPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /importFee/deleteImportFeeDetail
     * @path /app/importFee/deleteImportFeeDetail
     */
    @RequestMapping(value = "/deleteImportFeeDetail", method = RequestMethod.POST)
    public ResponseEntity<String> deleteImportFeeDetail(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "ifdId", "ifdId不能为空");


        ImportFeeDetailPo importFeeDetailPo = BeanConvertUtil.covertBean(reqJson, ImportFeeDetailPo.class);
        return deleteImportFeeDetailBMOImpl.delete(importFeeDetailPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /importFee/queryImportFeeDetail
     * @path /app/importFee/queryImportFeeDetail
     */
    @RequestMapping(value = "/queryImportFeeDetail", method = RequestMethod.GET)
    public ResponseEntity<String> queryImportFeeDetail(@RequestParam(value = "communityId") String communityId,
                                                       @RequestParam(value = "page") int page,
                                                       @RequestParam(value = "row") int row,
                                                       @RequestParam(value = "floorNum", required = false) String floorNum,
                                                       @RequestParam(value = "unitNum", required = false) String unitNum,
                                                       @RequestParam(value = "roomNum", required = false) String roomNum,
                                                       @RequestParam(value = "feeId", required = false) String feeId,
                                                       @RequestParam(value = "importFeeId", required = false) String importFeeId) {
        ImportFeeDetailDto importFeeDetailDto = new ImportFeeDetailDto();
        importFeeDetailDto.setPage(page);
        importFeeDetailDto.setRow(row);
        importFeeDetailDto.setCommunityId(communityId);
        importFeeDetailDto.setImportFeeId(importFeeId);
        importFeeDetailDto.setFloorNum(floorNum);
        importFeeDetailDto.setUnitNum(unitNum);
        importFeeDetailDto.setRoomNum(roomNum);
        importFeeDetailDto.setFeeId(feeId);
        return getImportFeeDetailBMOImpl.get(importFeeDetailDto);
    }


    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /importFee/queryImportFee
     * @path /app/importFee/queryImportFee
     */
    @RequestMapping(value = "/queryImportFee", method = RequestMethod.GET)
    public ResponseEntity<String> queryImportFee(@RequestParam(value = "communityId") String communityId,
                                                 @RequestParam(value = "page") int page,
                                                 @RequestParam(value = "importFeeId", required = false) String importFeeId,
                                                 @RequestParam(value = "feeTypeCd", required = false) String feeTypeCd,

                                                 @RequestParam(value = "row") int row) {
        ImportFeeDto importFeeDto = new ImportFeeDto();
        importFeeDto.setPage(page);
        importFeeDto.setRow(row);
        importFeeDto.setImportFeeId(importFeeId);
        importFeeDto.setFeeTypeCd(feeTypeCd);
        importFeeDto.setCommunityId(communityId);
        return getImportFeeBMOImpl.get(importFeeDto);
    }

    /**
     * 费用公摊
     *
     * @param reqJson 公摊信息
     * @return
     * @serviceCode /importFee/feeSharing
     * @path /app/importFee/feeSharing
     */
    @RequestMapping(value = "/feeSharing", method = RequestMethod.POST)
    public ResponseEntity<String> feeSharing(@RequestBody String reqJson,
                                             @RequestHeader(value = "store-id") String storeId,
                                             @RequestHeader(value = "user-id") String userId) {
        JSONObject reqObj = JSONObject.parseObject(reqJson);
        Assert.hasKeyAndValue(reqObj, "communityId", "未包含小区ID");
        Assert.hasKeyAndValue(reqObj, "totalDegrees", "未包含使用量");
        Assert.hasKeyAndValue(reqObj, "scope", "未包含公摊范围");
        Assert.hasKeyAndValue(reqObj, "formulaId", "未包含公摊公式");
        Assert.hasKeyAndValue(reqObj, "feeName", "未包含费用名称");
        Assert.hasKeyAndValue(reqObj, "startTime", "未包含开始时间");
        Assert.hasKeyAndValue(reqObj, "endTime", "未包含结束时间");
        Assert.hasKeyAndValue(reqObj, "objId", "未包含公摊对象");
        Assert.hasKeyAndValue(reqObj, "feeTypeCd", "未包含费用类型");
        Assert.hasKeyAndValue(reqObj, "roomType", "未包含房屋类型");
        reqObj.put("storeId", storeId);
        reqObj.put("userId", userId);

        return feeSharingBMOImpl.share(reqObj);
    }
}
