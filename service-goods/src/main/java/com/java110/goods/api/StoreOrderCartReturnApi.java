package com.java110.goods.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.storeOrderCartReturn.StoreOrderCartReturnDto;
import com.java110.dto.storeOrderCartReturnEvent.StoreOrderCartReturnEventDto;
import com.java110.goods.bmo.storeOrderCartReturn.IDeleteStoreOrderCartReturnBMO;
import com.java110.goods.bmo.storeOrderCartReturn.IGetStoreOrderCartReturnBMO;
import com.java110.goods.bmo.storeOrderCartReturn.ISaveStoreOrderCartReturnBMO;
import com.java110.goods.bmo.storeOrderCartReturn.IUpdateStoreOrderCartReturnBMO;
import com.java110.goods.bmo.storeOrderCartReturnEvent.IGetStoreOrderCartReturnEventBMO;
import com.java110.po.storeOrderCartReturn.StoreOrderCartReturnPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/storeOrderCartReturn")
public class StoreOrderCartReturnApi {

    @Autowired
    private ISaveStoreOrderCartReturnBMO saveStoreOrderCartReturnBMOImpl;
    @Autowired
    private IUpdateStoreOrderCartReturnBMO updateStoreOrderCartReturnBMOImpl;
    @Autowired
    private IDeleteStoreOrderCartReturnBMO deleteStoreOrderCartReturnBMOImpl;

    @Autowired
    private IGetStoreOrderCartReturnBMO getStoreOrderCartReturnBMOImpl;

    @Autowired
    private IGetStoreOrderCartReturnEventBMO getStoreOrderCartReturnEventBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /storeOrderCartReturn/saveStoreOrderCartReturn
     * @path /app/storeOrderCartReturn/saveStoreOrderCartReturn
     */
    @RequestMapping(value = "/saveStoreOrderCartReturn", method = RequestMethod.POST)
    public ResponseEntity<String> saveStoreOrderCartReturn(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "orderId", "请求报文中未包含orderId");
        Assert.hasKeyAndValue(reqJson, "cartId", "请求报文中未包含cartId");
        Assert.hasKeyAndValue(reqJson, "personIds", "请求报文中未包含personIds");
        Assert.hasKeyAndValue(reqJson, "returnReason", "请求报文中未包含returnReason");

        StoreOrderCartReturnPo storeOrderCartReturnPo = BeanConvertUtil.covertBean(reqJson, StoreOrderCartReturnPo.class);
        return saveStoreOrderCartReturnBMOImpl.save(storeOrderCartReturnPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /storeOrderCartReturn/updateStoreOrderCartReturn
     * @path /app/storeOrderCartReturn/updateStoreOrderCartReturn
     */
    @RequestMapping(value = "/updateStoreOrderCartReturn", method = RequestMethod.POST)
    public ResponseEntity<String> updateStoreOrderCartReturn(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "orderId", "请求报文中未包含orderId");
        Assert.hasKeyAndValue(reqJson, "cartId", "请求报文中未包含cartId");
        Assert.hasKeyAndValue(reqJson, "personIds", "请求报文中未包含personIds");
        Assert.hasKeyAndValue(reqJson, "returnReason", "请求报文中未包含returnReason");
        Assert.hasKeyAndValue(reqJson, "returnId", "returnId不能为空");


        StoreOrderCartReturnPo storeOrderCartReturnPo = BeanConvertUtil.covertBean(reqJson, StoreOrderCartReturnPo.class);
        return updateStoreOrderCartReturnBMOImpl.update(storeOrderCartReturnPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /storeOrderCartReturn/deleteStoreOrderCartReturn
     * @path /app/storeOrderCartReturn/deleteStoreOrderCartReturn
     */
    @RequestMapping(value = "/deleteStoreOrderCartReturn", method = RequestMethod.POST)
    public ResponseEntity<String> deleteStoreOrderCartReturn(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "returnId", "returnId不能为空");


        StoreOrderCartReturnPo storeOrderCartReturnPo = BeanConvertUtil.covertBean(reqJson, StoreOrderCartReturnPo.class);
        return deleteStoreOrderCartReturnBMOImpl.delete(storeOrderCartReturnPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param orderId 订单ID
     * @return
     * @serviceCode /storeOrderCartReturn/queryStoreOrderCartReturn
     * @path /app/storeOrderCartReturn/queryStoreOrderCartReturn
     */
    @RequestMapping(value = "/queryStoreOrderCartReturn", method = RequestMethod.GET)
    public ResponseEntity<String> queryStoreOrderCartReturn(@RequestParam(value = "cartId") String cartId,
                                                            @RequestParam(value = "orderId") String orderId,
                                                            @RequestParam(value = "page") int page,
                                                            @RequestParam(value = "row") int row) {
        StoreOrderCartReturnDto storeOrderCartReturnDto = new StoreOrderCartReturnDto();
        storeOrderCartReturnDto.setPage(page);
        storeOrderCartReturnDto.setRow(row);
        storeOrderCartReturnDto.setOrderId(orderId);
        storeOrderCartReturnDto.setOrderId(cartId);
        return getStoreOrderCartReturnBMOImpl.get(storeOrderCartReturnDto);
    }

    /**
     * 微信删除消息模板
     *
     * @param storeId 商户ID
     * @return
     * @serviceCode /storeOrderCartReturnEvent/queryStoreOrderCartReturnEvent
     * @path /app/storeOrderCartReturnEvent/queryStoreOrderCartReturnEvent
     */
    @RequestMapping(value = "/queryStoreOrderCartReturnEvent", method = RequestMethod.GET)
    public ResponseEntity<String> queryStoreOrderCartReturnEvent(@RequestParam(value = "returnId") String returnId,
                                                                 @RequestParam(value = "storeId") String storeId,
                                                                 @RequestParam(value = "page") int page,
                                                                 @RequestParam(value = "row") int row) {
        StoreOrderCartReturnEventDto storeOrderCartReturnEventDto = new StoreOrderCartReturnEventDto();
        storeOrderCartReturnEventDto.setPage(page);
        storeOrderCartReturnEventDto.setRow(row);
        storeOrderCartReturnEventDto.setReturnId(returnId);
        return getStoreOrderCartReturnEventBMOImpl.get(storeOrderCartReturnEventDto);
    }
}
