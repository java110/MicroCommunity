package com.java110.goods.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.storeCart.StoreCartDto;
import com.java110.dto.storeOrder.StoreOrderDto;
import com.java110.dto.storeOrderAddress.StoreOrderAddressDto;
import com.java110.dto.storeOrderCart.StoreOrderCartDto;
import com.java110.goods.bmo.storeCart.IDeleteStoreCartBMO;
import com.java110.goods.bmo.storeCart.IGetStoreCartBMO;
import com.java110.goods.bmo.storeCart.ISaveStoreCartBMO;
import com.java110.goods.bmo.storeCart.IUpdateStoreCartBMO;
import com.java110.goods.bmo.storeOrder.IDeleteStoreOrderBMO;
import com.java110.goods.bmo.storeOrder.IGetStoreOrderBMO;
import com.java110.goods.bmo.storeOrder.ISaveStoreOrderBMO;
import com.java110.goods.bmo.storeOrder.IUpdateStoreOrderBMO;
import com.java110.goods.bmo.storeOrderAddress.IDeleteStoreOrderAddressBMO;
import com.java110.goods.bmo.storeOrderAddress.IGetStoreOrderAddressBMO;
import com.java110.goods.bmo.storeOrderAddress.ISaveStoreOrderAddressBMO;
import com.java110.goods.bmo.storeOrderAddress.IUpdateStoreOrderAddressBMO;
import com.java110.goods.bmo.storeOrderCart.IDeleteStoreOrderCartBMO;
import com.java110.goods.bmo.storeOrderCart.IGetStoreOrderCartBMO;
import com.java110.goods.bmo.storeOrderCart.ISaveStoreOrderCartBMO;
import com.java110.goods.bmo.storeOrderCart.IUpdateStoreOrderCartBMO;
import com.java110.po.storeCart.StoreCartPo;
import com.java110.po.storeOrder.StoreOrderPo;
import com.java110.po.storeOrderAddress.StoreOrderAddressPo;
import com.java110.po.storeOrderCart.StoreOrderCartPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商户订单
 * add by wuxw 2020-11-15
 */
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


    @Autowired
    private ISaveStoreOrderAddressBMO saveStoreOrderAddressBMOImpl;
    @Autowired
    private IUpdateStoreOrderAddressBMO updateStoreOrderAddressBMOImpl;
    @Autowired
    private IDeleteStoreOrderAddressBMO deleteStoreOrderAddressBMOImpl;

    @Autowired
    private IGetStoreOrderAddressBMO getStoreOrderAddressBMOImpl;

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
        Assert.hasKeyAndValue(reqJson, "addressId", "请求报文中未包含收货人信息");
        Assert.hasKey(reqJson, "goodsList", "未包含商品信息");

        StoreOrderPo storeOrderPo = BeanConvertUtil.covertBean(reqJson, StoreOrderPo.class);
        return saveStoreOrderBMOImpl.save(storeOrderPo, reqJson.getJSONArray("goodsList"),
                reqJson.getString("addressId"));
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

        Assert.hasKeyAndValue(reqJson, "orderId", "请求报文中未包含orderId");

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
     * @serviceCode /storeOrder/saveStoreCart
     * @path /app/storeOrder/saveStoreCart
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
     * @serviceCode /storeOrder/updateStoreCart
     * @path /app/storeOrder/updateStoreCart
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
     * @serviceCode /storeOrder/deleteStoreCart
     * @path /app/storeOrder/deleteStoreCart
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
     * @return
     * @serviceCode /storeOrder/queryStoreCart
     * @path /app/storeOrder/queryStoreCart
     */
    @RequestMapping(value = "/queryStoreCart", method = RequestMethod.GET)
    public ResponseEntity<String> queryStoreCart(@RequestParam(value = "personId", required = false) String personId,
                                                 @RequestParam(value = "page") int page,
                                                 @RequestParam(value = "row") int row) {
        StoreCartDto storeCartDto = new StoreCartDto();
        storeCartDto.setPage(page);
        storeCartDto.setRow(row);
        storeCartDto.setPersonId(personId);
        return getStoreCartBMOImpl.get(storeCartDto);
    }


    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /storeOrder/saveStoreOrderCart
     * @path /app/storeOrder/saveStoreOrderCart
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
     * @serviceCode /storeOrder/updateStoreOrderCart
     * @path /app/storeOrder/updateStoreOrderCart
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
     * @serviceCode /storeOrder/deleteStoreOrderCart
     * @path /app/storeOrder/deleteStoreOrderCart
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
     * @serviceCode /storeOrder/queryStoreOrderCart
     * @path /app/storeOrder/queryStoreOrderCart
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


    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /storeOrder/saveStoreOrderAddress
     * @path /app/storeOrder/saveStoreOrderAddress
     */
    @RequestMapping(value = "/saveStoreOrderAddress", method = RequestMethod.POST)
    public ResponseEntity<String> saveStoreOrderAddress(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "orderId", "请求报文中未包含orderId");
        Assert.hasKeyAndValue(reqJson, "areaCode", "请求报文中未包含areaCode");
        Assert.hasKeyAndValue(reqJson, "tel", "请求报文中未包含tel");
        Assert.hasKeyAndValue(reqJson, "address", "请求报文中未包含address");
        Assert.hasKeyAndValue(reqJson, "username", "请求报文中未包含username");


        StoreOrderAddressPo storeOrderAddressPo = BeanConvertUtil.covertBean(reqJson, StoreOrderAddressPo.class);
        return saveStoreOrderAddressBMOImpl.save(storeOrderAddressPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /storeOrder/updateStoreOrderAddress
     * @path /app/storeOrder/updateStoreOrderAddress
     */
    @RequestMapping(value = "/updateStoreOrderAddress", method = RequestMethod.POST)
    public ResponseEntity<String> updateStoreOrderAddress(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "orderId", "请求报文中未包含orderId");
        Assert.hasKeyAndValue(reqJson, "areaCode", "请求报文中未包含areaCode");
        Assert.hasKeyAndValue(reqJson, "tel", "请求报文中未包含tel");
        Assert.hasKeyAndValue(reqJson, "address", "请求报文中未包含address");
        Assert.hasKeyAndValue(reqJson, "username", "请求报文中未包含username");
        Assert.hasKeyAndValue(reqJson, "oaId", "oaId不能为空");


        StoreOrderAddressPo storeOrderAddressPo = BeanConvertUtil.covertBean(reqJson, StoreOrderAddressPo.class);
        return updateStoreOrderAddressBMOImpl.update(storeOrderAddressPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /storeOrder/deleteStoreOrderAddress
     * @path /app/storeOrder/deleteStoreOrderAddress
     */
    @RequestMapping(value = "/deleteStoreOrderAddress", method = RequestMethod.POST)
    public ResponseEntity<String> deleteStoreOrderAddress(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "oaId", "oaId不能为空");


        StoreOrderAddressPo storeOrderAddressPo = BeanConvertUtil.covertBean(reqJson, StoreOrderAddressPo.class);
        return deleteStoreOrderAddressBMOImpl.delete(storeOrderAddressPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param orderId 订单ID
     * @return
     * @serviceCode /storeOrder/queryStoreOrderAddress
     * @path /app/storeOrder/queryStoreOrderAddress
     */
    @RequestMapping(value = "/queryStoreOrderAddress", method = RequestMethod.GET)
    public ResponseEntity<String> queryStoreOrderAddress(@RequestParam(value = "orderId") String orderId,
                                                         @RequestParam(value = "page") int page,
                                                         @RequestParam(value = "row") int row) {
        StoreOrderAddressDto storeOrderAddressDto = new StoreOrderAddressDto();
        storeOrderAddressDto.setPage(page);
        storeOrderAddressDto.setRow(row);
        storeOrderAddressDto.setOrderId(orderId);
        return getStoreOrderAddressBMOImpl.get(storeOrderAddressDto);
    }
}
