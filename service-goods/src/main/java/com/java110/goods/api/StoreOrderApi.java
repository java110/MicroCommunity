package com.java110.goods.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.storeCart.StoreCartDto;
import com.java110.dto.storeOrder.StoreOrderDto;
import com.java110.dto.storeOrderCart.StoreOrderCartDto;
import com.java110.goods.bmo.storeCart.IDeleteStoreCartBMO;
import com.java110.goods.bmo.storeCart.IGetStoreCartBMO;
import com.java110.goods.bmo.storeCart.ISaveStoreCartBMO;
import com.java110.goods.bmo.storeCart.IUpdateStoreCartBMO;
import com.java110.goods.bmo.storeOrder.IDeleteStoreOrderBMO;
import com.java110.goods.bmo.storeOrder.IGetStoreOrderBMO;
import com.java110.goods.bmo.storeOrder.ISaveStoreOrderBMO;
import com.java110.goods.bmo.storeOrder.IUpdateStoreOrderBMO;
import com.java110.goods.bmo.storeOrderCart.IDeleteStoreOrderCartBMO;
import com.java110.goods.bmo.storeOrderCart.IGetStoreOrderCartBMO;
import com.java110.goods.bmo.storeOrderCart.ISaveStoreOrderCartBMO;
import com.java110.goods.bmo.storeOrderCart.IUpdateStoreOrderCartBMO;
import com.java110.po.storeCart.StoreCartPo;
import com.java110.po.storeOrder.StoreOrderPo;
import com.java110.po.storeOrderCart.StoreOrderCartPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/storeOrder")
public class StoreOrderApi {


    @Autowired
    private ISaveStoreCartBMO saveStoreCartBMOImpl;
    @Autowired
    private IUpdateStoreCartBMO updateStoreCartBMOImpl;
    @Autowired
    private IDeleteStoreCartBMO deleteStoreCartBMOImpl;

    @Autowired
    private IGetStoreCartBMO getStoreCartBMOImpl;

    @Autowired
    private ISaveStoreOrderBMO saveStoreOrderBMOImpl;
    @Autowired
    private IUpdateStoreOrderBMO updateStoreOrderBMOImpl;
    @Autowired
    private IDeleteStoreOrderBMO deleteStoreOrderBMOImpl;

    @Autowired
    private IGetStoreOrderBMO getStoreOrderBMOImpl;


    @Autowired
    private ISaveStoreOrderCartBMO saveStoreOrderCartBMOImpl;
    @Autowired
    private IUpdateStoreOrderCartBMO updateStoreOrderCartBMOImpl;
    @Autowired
    private IDeleteStoreOrderCartBMO deleteStoreOrderCartBMOImpl;

    @Autowired
    private IGetStoreOrderCartBMO getStoreOrderCartBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /storeOrder/saveStoreOrder
     * @path /app/storeOrder/saveStoreOrder
     */
    @RequestMapping(value = "/saveStoreOrder", method = RequestMethod.POST)
    public ResponseEntity<String> saveStoreOrder(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "personId", "请求报文中未包含personId");
        Assert.hasKeyAndValue(reqJson, "personName", "请求报文中未包含personName");
        Assert.hasKeyAndValue(reqJson, "totalPrice", "请求报文中未包含totalPrice");
        Assert.hasKeyAndValue(reqJson, "payPrice", "请求报文中未包含payPrice");


        StoreOrderPo storeOrderPo = BeanConvertUtil.covertBean(reqJson, StoreOrderPo.class);
        return saveStoreOrderBMOImpl.save(storeOrderPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /storeOrder/updateStoreOrder
     * @path /app/storeOrder/updateStoreOrder
     */
    @RequestMapping(value = "/updateStoreOrder", method = RequestMethod.POST)
    public ResponseEntity<String> updateStoreOrder(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "personId", "请求报文中未包含personId");
        Assert.hasKeyAndValue(reqJson, "personName", "请求报文中未包含personName");
        Assert.hasKeyAndValue(reqJson, "totalPrice", "请求报文中未包含totalPrice");
        Assert.hasKeyAndValue(reqJson, "payPrice", "请求报文中未包含payPrice");
        Assert.hasKeyAndValue(reqJson, "orderId", "orderId不能为空");


        StoreOrderPo storeOrderPo = BeanConvertUtil.covertBean(reqJson, StoreOrderPo.class);
        return updateStoreOrderBMOImpl.update(storeOrderPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /storeOrder/deleteStoreOrder
     * @path /app/storeOrder/deleteStoreOrder
     */
    @RequestMapping(value = "/deleteStoreOrder", method = RequestMethod.POST)
    public ResponseEntity<String> deleteStoreOrder(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "orderId", "orderId不能为空");


        StoreOrderPo storeOrderPo = BeanConvertUtil.covertBean(reqJson, StoreOrderPo.class);
        return deleteStoreOrderBMOImpl.delete(storeOrderPo);
    }

    /**
     * 微信删除消息模板
     *
     * @return
     * @serviceCode /storeOrder/queryStoreOrder
     * @path /app/storeOrder/queryStoreOrder
     */
    @RequestMapping(value = "/queryStoreOrder", method = RequestMethod.GET)
    public ResponseEntity<String> queryStoreOrder(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "row") int row) {
        StoreOrderDto storeOrderDto = new StoreOrderDto();
        storeOrderDto.setPage(page);
        storeOrderDto.setRow(row);
        return getStoreOrderBMOImpl.get(storeOrderDto);
    }


    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /storeCart/saveStoreCart
     * @path /app/storeCart/saveStoreCart
     */
    @RequestMapping(value = "/saveStoreCart", method = RequestMethod.POST)
    public ResponseEntity<String> saveStoreCart(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "productId", "请求报文中未包含productId");
        Assert.hasKeyAndValue(reqJson, "valueId", "请求报文中未包含valueId");
        Assert.hasKeyAndValue(reqJson, "personId", "请求报文中未包含personId");
        Assert.hasKeyAndValue(reqJson, "cartNum", "请求报文中未包含cartNum");


        StoreCartPo storeCartPo = BeanConvertUtil.covertBean(reqJson, StoreCartPo.class);
        return saveStoreCartBMOImpl.save(storeCartPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /storeCart/updateStoreCart
     * @path /app/storeCart/updateStoreCart
     */
    @RequestMapping(value = "/updateStoreCart", method = RequestMethod.POST)
    public ResponseEntity<String> updateStoreCart(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "productId", "请求报文中未包含productId");
        Assert.hasKeyAndValue(reqJson, "valueId", "请求报文中未包含valueId");
        Assert.hasKeyAndValue(reqJson, "personId", "请求报文中未包含personId");
        Assert.hasKeyAndValue(reqJson, "cartNum", "请求报文中未包含cartNum");
        Assert.hasKeyAndValue(reqJson, "cartId", "cartId不能为空");


        StoreCartPo storeCartPo = BeanConvertUtil.covertBean(reqJson, StoreCartPo.class);
        return updateStoreCartBMOImpl.update(storeCartPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /storeCart/deleteStoreCart
     * @path /app/storeCart/deleteStoreCart
     */
    @RequestMapping(value = "/deleteStoreCart", method = RequestMethod.POST)
    public ResponseEntity<String> deleteStoreCart(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "cartId", "cartId不能为空");


        StoreCartPo storeCartPo = BeanConvertUtil.covertBean(reqJson, StoreCartPo.class);
        return deleteStoreCartBMOImpl.delete(storeCartPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /storeCart/queryStoreCart
     * @path /app/storeCart/queryStoreCart
     */
    @RequestMapping(value = "/queryStoreCart", method = RequestMethod.GET)
    public ResponseEntity<String> queryStoreCart(@RequestParam(value = "communityId") String communityId,
                                                 @RequestParam(value = "page") int page,
                                                 @RequestParam(value = "row") int row) {
        StoreCartDto storeCartDto = new StoreCartDto();
        storeCartDto.setPage(page);
        storeCartDto.setRow(row);
        //storeCartDto.setCommunityId(communityId);
        return getStoreCartBMOImpl.get(storeCartDto);
    }


    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /storeOrderCart/saveStoreOrderCart
     * @path /app/storeOrderCart/saveStoreOrderCart
     */
    @RequestMapping(value = "/saveStoreOrderCart", method = RequestMethod.POST)
    public ResponseEntity<String> saveStoreOrderCart(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "orderId", "请求报文中未包含orderId");
        Assert.hasKeyAndValue(reqJson, "productId", "请求报文中未包含productId");
        Assert.hasKeyAndValue(reqJson, "valueId", "请求报文中未包含valueId");
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含storeId");


        StoreOrderCartPo storeOrderCartPo = BeanConvertUtil.covertBean(reqJson, StoreOrderCartPo.class);
        return saveStoreOrderCartBMOImpl.save(storeOrderCartPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /storeOrderCart/updateStoreOrderCart
     * @path /app/storeOrderCart/updateStoreOrderCart
     */
    @RequestMapping(value = "/updateStoreOrderCart", method = RequestMethod.POST)
    public ResponseEntity<String> updateStoreOrderCart(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "orderId", "请求报文中未包含orderId");
        Assert.hasKeyAndValue(reqJson, "productId", "请求报文中未包含productId");
        Assert.hasKeyAndValue(reqJson, "valueId", "请求报文中未包含valueId");
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含storeId");
        Assert.hasKeyAndValue(reqJson, "cartId", "cartId不能为空");


        StoreOrderCartPo storeOrderCartPo = BeanConvertUtil.covertBean(reqJson, StoreOrderCartPo.class);
        return updateStoreOrderCartBMOImpl.update(storeOrderCartPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /storeOrderCart/deleteStoreOrderCart
     * @path /app/storeOrderCart/deleteStoreOrderCart
     */
    @RequestMapping(value = "/deleteStoreOrderCart", method = RequestMethod.POST)
    public ResponseEntity<String> deleteStoreOrderCart(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "cartId", "cartId不能为空");


        StoreOrderCartPo storeOrderCartPo = BeanConvertUtil.covertBean(reqJson, StoreOrderCartPo.class);
        return deleteStoreOrderCartBMOImpl.delete(storeOrderCartPo);
    }

    /**
     * 微信删除消息模板
     *
     * @return
     * @serviceCode /storeOrderCart/queryStoreOrderCart
     * @path /app/storeOrderCart/queryStoreOrderCart
     */
    @RequestMapping(value = "/queryStoreOrderCart", method = RequestMethod.GET)
    public ResponseEntity<String> queryStoreOrderCart(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "row") int row) {
        StoreOrderCartDto storeOrderCartDto = new StoreOrderCartDto();
        storeOrderCartDto.setPage(page);
        storeOrderCartDto.setRow(row);
        return getStoreOrderCartBMOImpl.get(storeOrderCartDto);
    }
}
