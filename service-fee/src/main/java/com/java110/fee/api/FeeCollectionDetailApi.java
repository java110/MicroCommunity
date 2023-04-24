package com.java110.fee.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.fee.FeeCollectionDetailDto;
import com.java110.fee.bmo.feeCollectionDetail.IDeleteFeeCollectionDetailBMO;
import com.java110.fee.bmo.feeCollectionDetail.IGetFeeCollectionDetailBMO;
import com.java110.fee.bmo.feeCollectionDetail.ISaveFeeCollectionDetailBMO;
import com.java110.fee.bmo.feeCollectionDetail.IUpdateFeeCollectionDetailBMO;
import com.java110.po.feeCollectionDetail.FeeCollectionDetailPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/feeCollectionDetail")
public class FeeCollectionDetailApi {

    @Autowired
    private ISaveFeeCollectionDetailBMO saveFeeCollectionDetailBMOImpl;
    @Autowired
    private IUpdateFeeCollectionDetailBMO updateFeeCollectionDetailBMOImpl;
    @Autowired
    private IDeleteFeeCollectionDetailBMO deleteFeeCollectionDetailBMOImpl;

    @Autowired
    private IGetFeeCollectionDetailBMO getFeeCollectionDetailBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /feeCollectionDetail/saveFeeCollectionDetail
     * @path /app/feeCollectionDetail/saveFeeCollectionDetail
     */
    @RequestMapping(value = "/saveFeeCollectionDetail", method = RequestMethod.POST)
    public ResponseEntity<String> saveFeeCollectionDetail(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "collectionWay", "请求报文中未包含collectionWay");
        Assert.hasKeyAndValue(reqJson, "oweAmount", "请求报文中未包含oweAmount");


        FeeCollectionDetailPo feeCollectionDetailPo = BeanConvertUtil.covertBean(reqJson, FeeCollectionDetailPo.class);
        return saveFeeCollectionDetailBMOImpl.save(feeCollectionDetailPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /feeCollectionDetail/updateFeeCollectionDetail
     * @path /app/feeCollectionDetail/updateFeeCollectionDetail
     */
    @RequestMapping(value = "/updateFeeCollectionDetail", method = RequestMethod.POST)
    public ResponseEntity<String> updateFeeCollectionDetail(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "collectionWay", "请求报文中未包含collectionWay");
        Assert.hasKeyAndValue(reqJson, "oweAmount", "请求报文中未包含oweAmount");
        Assert.hasKeyAndValue(reqJson, "detailId", "detailId不能为空");


        FeeCollectionDetailPo feeCollectionDetailPo = BeanConvertUtil.covertBean(reqJson, FeeCollectionDetailPo.class);
        return updateFeeCollectionDetailBMOImpl.update(feeCollectionDetailPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /feeCollectionDetail/deleteFeeCollectionDetail
     * @path /app/feeCollectionDetail/deleteFeeCollectionDetail
     */
    @RequestMapping(value = "/deleteFeeCollectionDetail", method = RequestMethod.POST)
    public ResponseEntity<String> deleteFeeCollectionDetail(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "detailId", "detailId不能为空");


        FeeCollectionDetailPo feeCollectionDetailPo = BeanConvertUtil.covertBean(reqJson, FeeCollectionDetailPo.class);
        return deleteFeeCollectionDetailBMOImpl.delete(feeCollectionDetailPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /feeCollectionDetail/queryFeeCollectionDetail
     * @path /app/feeCollectionDetail/queryFeeCollectionDetail
     */
    @RequestMapping(value = "/queryFeeCollectionDetail", method = RequestMethod.GET)
    public ResponseEntity<String> queryFeeCollectionDetail(@RequestParam(value = "communityId") String communityId,
                                                           @RequestParam(value = "orderId",required = false) String orderId,
                                                           @RequestParam(value = "ownerName",required = false) String ownerName,
                                                           @RequestParam(value = "payerObjName",required = false) String payerObjName,
                                                           @RequestParam(value = "collectionWay",required = false) String collectionWay,
                                                           @RequestParam(value = "page") int page,
                                                           @RequestParam(value = "row") int row) {
        FeeCollectionDetailDto feeCollectionDetailDto = new FeeCollectionDetailDto();
        feeCollectionDetailDto.setPage(page);
        feeCollectionDetailDto.setRow(row);
        feeCollectionDetailDto.setCommunityId(communityId);
        feeCollectionDetailDto.setOrderId(orderId);
        feeCollectionDetailDto.setOwnerName(ownerName);
        feeCollectionDetailDto.setPayerObjName(payerObjName);
        feeCollectionDetailDto.setCollectionWay(collectionWay);
        return getFeeCollectionDetailBMOImpl.get(feeCollectionDetailDto);
    }
}
