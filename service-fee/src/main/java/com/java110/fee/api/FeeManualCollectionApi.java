package com.java110.fee.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.fee.FeeManualCollectionDto;
import com.java110.dto.fee.FeeManualCollectionDetailDto;
import com.java110.fee.bmo.feeManualCollection.IDeleteFeeManualCollectionBMO;
import com.java110.fee.bmo.feeManualCollection.IGetFeeManualCollectionBMO;
import com.java110.fee.bmo.feeManualCollection.ISaveFeeManualCollectionBMO;
import com.java110.fee.bmo.feeManualCollection.IUpdateFeeManualCollectionBMO;
import com.java110.fee.bmo.feeManualCollectionDetail.IDeleteFeeManualCollectionDetailBMO;
import com.java110.fee.bmo.feeManualCollectionDetail.IGetExportCollectionBMO;
import com.java110.fee.bmo.feeManualCollectionDetail.IGetFeeManualCollectionDetailBMO;
import com.java110.fee.bmo.feeManualCollectionDetail.ISaveFeeManualCollectionDetailBMO;
import com.java110.fee.bmo.feeManualCollectionDetail.IUpdateFeeManualCollectionDetailBMO;
import com.java110.po.feeManualCollection.FeeManualCollectionPo;
import com.java110.po.feeManualCollectionDetail.FeeManualCollectionDetailPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/feeManualCollection")
public class FeeManualCollectionApi {

    @Autowired
    private ISaveFeeManualCollectionBMO saveFeeManualCollectionBMOImpl;
    @Autowired
    private IUpdateFeeManualCollectionBMO updateFeeManualCollectionBMOImpl;
    @Autowired
    private IDeleteFeeManualCollectionBMO deleteFeeManualCollectionBMOImpl;

    @Autowired
    private IGetFeeManualCollectionBMO getFeeManualCollectionBMOImpl;

    @Autowired
    private ISaveFeeManualCollectionDetailBMO saveFeeManualCollectionDetailBMOImpl;
    @Autowired
    private IUpdateFeeManualCollectionDetailBMO updateFeeManualCollectionDetailBMOImpl;
    @Autowired
    private IDeleteFeeManualCollectionDetailBMO deleteFeeManualCollectionDetailBMOImpl;

    @Autowired
    private IGetFeeManualCollectionDetailBMO getFeeManualCollectionDetailBMOImpl;

    @Autowired
    private IGetExportCollectionBMO getExportCollectionBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /feeManualCollection/saveFeeManualCollection
     * @path /app/feeManualCollection/saveFeeManualCollection
     */
    @RequestMapping(value = "/saveFeeManualCollection", method = RequestMethod.POST)
    public ResponseEntity<String> saveFeeManualCollection(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "roomId", "请求报文中未包含roomId");
        //Assert.hasKeyAndValue(reqJson, "ownerId", "请求报文中未包含ownerId");


        FeeManualCollectionPo feeManualCollectionPo = BeanConvertUtil.covertBean(reqJson, FeeManualCollectionPo.class);
        return saveFeeManualCollectionBMOImpl.save(feeManualCollectionPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /feeManualCollection/updateFeeManualCollection
     * @path /app/feeManualCollection/updateFeeManualCollection
     */
    @RequestMapping(value = "/updateFeeManualCollection", method = RequestMethod.POST)
    public ResponseEntity<String> updateFeeManualCollection(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "roomId", "请求报文中未包含roomId");
        Assert.hasKeyAndValue(reqJson, "ownerId", "请求报文中未包含ownerId");
        Assert.hasKeyAndValue(reqJson, "receiptId", "receiptId不能为空");


        FeeManualCollectionPo feeManualCollectionPo = BeanConvertUtil.covertBean(reqJson, FeeManualCollectionPo.class);
        return updateFeeManualCollectionBMOImpl.update(feeManualCollectionPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /feeManualCollection/deleteFeeManualCollection
     * @path /app/feeManualCollection/deleteFeeManualCollection
     */
    @RequestMapping(value = "/deleteFeeManualCollection", method = RequestMethod.POST)
    public ResponseEntity<String> deleteFeeManualCollection(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "collectionId", "collectionId不能为空");


        FeeManualCollectionPo feeManualCollectionPo = BeanConvertUtil.covertBean(reqJson, FeeManualCollectionPo.class);
        return deleteFeeManualCollectionBMOImpl.delete(feeManualCollectionPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /feeManualCollection/queryFeeManualCollection
     * @path /app/feeManualCollection/queryFeeManualCollection
     */
    @RequestMapping(value = "/queryFeeManualCollection", method = RequestMethod.GET)
    public ResponseEntity<String> queryFeeManualCollection(@RequestParam(value = "communityId") String communityId,
                                                           @RequestParam(value = "page") int page,
                                                           @RequestParam(value = "row") int row) {
        FeeManualCollectionDto feeManualCollectionDto = new FeeManualCollectionDto();
        feeManualCollectionDto.setPage(page);
        feeManualCollectionDto.setRow(row);
        feeManualCollectionDto.setCommunityId(communityId);
        return getFeeManualCollectionBMOImpl.get(feeManualCollectionDto);
    }

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /feeManualCollection/saveFeeManualCollectionDetail
     * @path /app/feeManualCollection/saveFeeManualCollectionDetail
     */
    @RequestMapping(value = "/saveFeeManualCollectionDetail", method = RequestMethod.POST)
    public ResponseEntity<String> saveFeeManualCollectionDetail(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "collectionId", "请求报文中未包含collectionId");
        Assert.hasKeyAndValue(reqJson, "feeId", "请求报文中未包含feeId");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");
        Assert.hasKeyAndValue(reqJson, "amount", "请求报文中未包含amount");


        FeeManualCollectionDetailPo feeManualCollectionDetailPo = BeanConvertUtil.covertBean(reqJson, FeeManualCollectionDetailPo.class);
        return saveFeeManualCollectionDetailBMOImpl.save(feeManualCollectionDetailPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /feeManualCollection/updateFeeManualCollectionDetail
     * @path /app/feeManualCollection/updateFeeManualCollectionDetail
     */
    @RequestMapping(value = "/updateFeeManualCollectionDetail", method = RequestMethod.POST)
    public ResponseEntity<String> updateFeeManualCollectionDetail(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "collectionId", "请求报文中未包含collectionId");
        Assert.hasKeyAndValue(reqJson, "feeId", "请求报文中未包含feeId");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");
        Assert.hasKeyAndValue(reqJson, "amount", "请求报文中未包含amount");
        Assert.hasKeyAndValue(reqJson, "detailId", "detailId不能为空");


        FeeManualCollectionDetailPo feeManualCollectionDetailPo = BeanConvertUtil.covertBean(reqJson, FeeManualCollectionDetailPo.class);
        return updateFeeManualCollectionDetailBMOImpl.update(feeManualCollectionDetailPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /feeManualCollection/deleteCollectionDetail
     * @path /app/feeManualCollection/deleteCollectionDetail
     */
    @RequestMapping(value = "/deleteCollectionDetail", method = RequestMethod.POST)
    public ResponseEntity<String> deleteCollectionDetail(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "detailId", "detailId不能为空");


        FeeManualCollectionDetailPo feeManualCollectionDetailPo = BeanConvertUtil.covertBean(reqJson, FeeManualCollectionDetailPo.class);
        return deleteFeeManualCollectionDetailBMOImpl.delete(feeManualCollectionDetailPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /feeManualCollection/queryCollectionDetail
     * @path /app/feeManualCollection/queryCollectionDetail
     */
    @RequestMapping(value = "/queryCollectionDetail", method = RequestMethod.GET)
    public ResponseEntity<String> queryCollectionDetail(@RequestParam(value = "communityId") String communityId,
                                                        @RequestParam(value = "collectionId", required = false) String collectionId,
                                                        @RequestParam(value = "page") int page,
                                                        @RequestParam(value = "row") int row) {
        FeeManualCollectionDetailDto feeManualCollectionDetailDto = new FeeManualCollectionDetailDto();
        feeManualCollectionDetailDto.setPage(page);
        feeManualCollectionDetailDto.setRow(row);
        feeManualCollectionDetailDto.setCommunityId(communityId);
        feeManualCollectionDetailDto.setCollectionId(collectionId);
        return getFeeManualCollectionDetailBMOImpl.get(feeManualCollectionDetailDto);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /feeManualCollection/queryExportCollections
     * @path /app/feeManualCollection/queryExportCollections
     */
    @RequestMapping(value = "/queryExportCollections", method = RequestMethod.GET)
    public ResponseEntity<String> queryExportCollections(@RequestParam(value = "communityId") String communityId) {
        FeeManualCollectionDetailDto feeManualCollectionDetailDto = new FeeManualCollectionDetailDto();
        feeManualCollectionDetailDto.setCommunityId(communityId);

        return getExportCollectionBMOImpl.get(feeManualCollectionDetailDto);
    }
}
