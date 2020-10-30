package com.java110.goods.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.storeCart.StoreCartDto;
import com.java110.goods.bmo.storeCart.IDeleteStoreCartBMO;
import com.java110.goods.bmo.storeCart.IGetStoreCartBMO;
import com.java110.goods.bmo.storeCart.ISaveStoreCartBMO;
import com.java110.goods.bmo.storeCart.IUpdateStoreCartBMO;
import com.java110.po.storeCart.StoreCartPo;
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
@RequestMapping(value = "/storeCart")
public class StoreCartApi {

    @Autowired
    private ISaveStoreCartBMO saveStoreCartBMOImpl;
    @Autowired
    private IUpdateStoreCartBMO updateStoreCartBMOImpl;
    @Autowired
    private IDeleteStoreCartBMO deleteStoreCartBMOImpl;

    @Autowired
    private IGetStoreCartBMO getStoreCartBMOImpl;

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
}
