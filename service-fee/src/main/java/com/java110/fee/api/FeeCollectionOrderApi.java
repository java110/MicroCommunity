package com.java110.fee.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.fee.FeeCollectionOrderDto;
import com.java110.fee.bmo.feeCollectionOrder.IDeleteFeeCollectionOrderBMO;
import com.java110.fee.bmo.feeCollectionOrder.IGetFeeCollectionOrderBMO;
import com.java110.fee.bmo.feeCollectionOrder.ISaveFeeCollectionOrderBMO;
import com.java110.fee.bmo.feeCollectionOrder.IUpdateFeeCollectionOrderBMO;
import com.java110.po.feeCollectionOrder.FeeCollectionOrderPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/feeCollectionOrder")
public class FeeCollectionOrderApi {

    @Autowired
    private ISaveFeeCollectionOrderBMO saveFeeCollectionOrderBMOImpl;
    @Autowired
    private IUpdateFeeCollectionOrderBMO updateFeeCollectionOrderBMOImpl;
    @Autowired
    private IDeleteFeeCollectionOrderBMO deleteFeeCollectionOrderBMOImpl;

    @Autowired
    private IGetFeeCollectionOrderBMO getFeeCollectionOrderBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /feeCollectionOrder/saveFeeCollectionOrder
     * @path /app/feeCollectionOrder/saveFeeCollectionOrder
     */
    @RequestMapping(value = "/saveFeeCollectionOrder", method = RequestMethod.POST)
    public ResponseEntity<String> saveFeeCollectionOrder(
            @RequestHeader(value = "user-id") String userId,
            @RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "collectionWay", "请求报文中未包含collectionWay");
        reqJson.put("staffId", userId);

        FeeCollectionOrderPo feeCollectionOrderPo = BeanConvertUtil.covertBean(reqJson, FeeCollectionOrderPo.class);
        return saveFeeCollectionOrderBMOImpl.save(feeCollectionOrderPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /feeCollectionOrder/updateFeeCollectionOrder
     * @path /app/feeCollectionOrder/updateFeeCollectionOrder
     */
    @RequestMapping(value = "/updateFeeCollectionOrder", method = RequestMethod.POST)
    public ResponseEntity<String> updateFeeCollectionOrder(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "collectionWay", "请求报文中未包含collectionWay");
        Assert.hasKeyAndValue(reqJson, "orderId", "orderId不能为空");


        FeeCollectionOrderPo feeCollectionOrderPo = BeanConvertUtil.covertBean(reqJson, FeeCollectionOrderPo.class);
        return updateFeeCollectionOrderBMOImpl.update(feeCollectionOrderPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /feeCollectionOrder/deleteFeeCollectionOrder
     * @path /app/feeCollectionOrder/deleteFeeCollectionOrder
     */
    @RequestMapping(value = "/deleteFeeCollectionOrder", method = RequestMethod.POST)
    public ResponseEntity<String> deleteFeeCollectionOrder(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "orderId", "orderId不能为空");


        FeeCollectionOrderPo feeCollectionOrderPo = BeanConvertUtil.covertBean(reqJson, FeeCollectionOrderPo.class);
        return deleteFeeCollectionOrderBMOImpl.delete(feeCollectionOrderPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /feeCollectionOrder/queryFeeCollectionOrder
     * @path /app/feeCollectionOrder/queryFeeCollectionOrder
     */
    @RequestMapping(value = "/queryFeeCollectionOrder", method = RequestMethod.GET)
    public ResponseEntity<String> queryFeeCollectionOrder(@RequestParam(value = "communityId") String communityId,
                                                          @RequestParam(value = "state",required = false) String state,
                                                          @RequestParam(value = "staffName",required = false) String staffName,
                                                          @RequestParam(value = "collectionWay",required = false) String collectionWay,
                                                          @RequestParam(value = "page") int page,
                                                          @RequestParam(value = "row") int row) {
        FeeCollectionOrderDto feeCollectionOrderDto = new FeeCollectionOrderDto();
        feeCollectionOrderDto.setPage(page);
        feeCollectionOrderDto.setRow(row);
        feeCollectionOrderDto.setCommunityId(communityId);
        feeCollectionOrderDto.setState(state);
        feeCollectionOrderDto.setStaffName(staffName);
        feeCollectionOrderDto.setCollectionWay(collectionWay);
        return getFeeCollectionOrderBMOImpl.get(feeCollectionOrderDto);
    }
}
