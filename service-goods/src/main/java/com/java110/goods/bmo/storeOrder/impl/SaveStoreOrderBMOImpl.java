package com.java110.goods.bmo.storeOrder.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.factory.Java110TransactionalFactory;
import com.java110.dto.groupBuyProductSpec.GroupBuyProductSpecDto;
import com.java110.dto.productSpecValue.ProductSpecValueDto;
import com.java110.dto.storeOrder.StoreOrderDto;
import com.java110.dto.storeOrderCart.StoreOrderCartDto;
import com.java110.dto.userAddress.UserAddressDto;
import com.java110.goods.bmo.storeOrder.ISaveStoreOrderBMO;
import com.java110.intf.goods.IStoreOrderAddressInnerServiceSMO;
import com.java110.intf.goods.IStoreOrderCartInnerServiceSMO;
import com.java110.intf.goods.IStoreOrderInnerServiceSMO;
import com.java110.intf.user.IUserAddressInnerServiceSMO;
import com.java110.intf.goods.IGroupBuyProductSpecInnerServiceSMO;
import com.java110.intf.goods.IProductInnerServiceSMO;
import com.java110.intf.goods.IProductSpecValueInnerServiceSMO;
import com.java110.po.groupBuyProductSpec.GroupBuyProductSpecPo;
import com.java110.po.productSpecValue.ProductSpecValuePo;
import com.java110.po.storeOrder.StoreOrderPo;
import com.java110.po.storeOrderAddress.StoreOrderAddressPo;
import com.java110.po.storeOrderCart.StoreOrderCartPo;
import com.java110.utils.lock.DistributedLock;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service("saveStoreOrderBMOImpl")
public class SaveStoreOrderBMOImpl implements ISaveStoreOrderBMO {

    @Autowired
    private IStoreOrderInnerServiceSMO storeOrderInnerServiceSMOImpl;

    @Autowired
    private IStoreOrderCartInnerServiceSMO storeOrderCartInnerServiceSMOImpl;

    @Autowired
    private IProductInnerServiceSMO productInnerServiceSMOImpl;

    @Autowired
    private IProductSpecValueInnerServiceSMO productSpecValueInnerServiceSMOImpl;

    @Autowired
    private IGroupBuyProductSpecInnerServiceSMO groupBuyProductSpecInnerServiceSMOImpl;

    @Autowired
    private IUserAddressInnerServiceSMO userAddressInnerServiceSMOImpl;

    @Autowired
    private IStoreOrderAddressInnerServiceSMO storeOrderAddressInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param storeOrderPo
     * @param goodsList    [
     *                     {
     *                     cartId:"123",//没有写-1
     *                     productId:"产品ID",
     *                     valueId:"产品规格ID",
     *                     cartNum:1,//购买数量
     *                     storeId:"产品商户ID",
     *                     <p>
     *                     }
     *                     ]
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(StoreOrderPo storeOrderPo, JSONArray goodsList, String addressId) {

        storeOrderPo.setOrderId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_orderId));
        storeOrderPo.setPayPrice("0");
        storeOrderPo.setTotalPrice("0");
        storeOrderPo.setFreightPrice("0");
        storeOrderPo.setState(StoreOrderDto.STATE_WAIT_PAY);
        storeOrderPo.setOId(StringUtil.isEmpty(Java110TransactionalFactory.getOId()) ? "-1" : Java110TransactionalFactory.getOId());
        JSONObject goods = null;
        for (int goodsIndex = 0; goodsIndex < goodsList.size(); goodsIndex++) {
            goods = goodsList.getJSONObject(goodsIndex);

            saveStoreOrderCart(goods, storeOrderPo);
        }
        int flag = storeOrderInnerServiceSMOImpl.saveStoreOrder(storeOrderPo);

        if (flag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
        }

        //保存收货人信息
        saveOrderAddress(storeOrderPo, addressId);

        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功", storeOrderPo);

    }

    private void saveOrderAddress(StoreOrderPo storeOrderPo, String addressId) {

        UserAddressDto userAddressDto = new UserAddressDto();
        userAddressDto.setAddressId(addressId);
        userAddressDto.setUserId(storeOrderPo.getPersonId());
        List<UserAddressDto> userAddressDtos = userAddressInnerServiceSMOImpl.queryUserAddresss(userAddressDto);

        Assert.listOnlyOne(userAddressDtos, "未找到收货人信息");

        userAddressDto = userAddressDtos.get(0);

        StoreOrderAddressPo storeOrderAddressPo = new StoreOrderAddressPo();
        storeOrderAddressPo.setAddress(userAddressDto.getAddress());
        storeOrderAddressPo.setAddressId(userAddressDto.getAddressId());
        storeOrderAddressPo.setAreaCode(userAddressDto.getAreaCode());
        storeOrderAddressPo.setOaId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_oaId));
        storeOrderAddressPo.setOrderId(storeOrderPo.getOrderId());
        storeOrderAddressPo.setTel(userAddressDto.getTel());
        storeOrderAddressPo.setUsername(userAddressDto.getUsername());

        int flag = storeOrderAddressInnerServiceSMOImpl.saveStoreOrderAddress(storeOrderAddressPo);

        if (flag < 1) {
            throw new IllegalArgumentException("保存收货人信息失败");
        }

    }

    /**
     * 保存购物车
     *
     * @param goods        商品
     * @param storeOrderPo
     */
    private void saveStoreOrderCart(JSONObject goods, StoreOrderPo storeOrderPo) {

        Assert.hasKeyAndValue(goods, "cartId", "未包含购物车ID");
        Assert.hasKeyAndValue(goods, "productId", "未包含商品");
        Assert.hasKeyAndValue(goods, "valueId", "未包含商品规格");
        Assert.hasKeyAndValue(goods, "cartNum", "未包含商品数量");
        Assert.hasKeyAndValue(goods, "storeId", "未包含商户");

        int flag = 0;
        ProductSpecValueDto productSpecValueDto = null;
        GroupBuyProductSpecDto groupBuyProductSpecDto = null;
        //开始锁代码
        String requestId = DistributedLock.getLockUUID();
        String key = this.getClass().getSimpleName() + goods.getString("productId") + goods.getString("valueId");
        try {
            DistributedLock.waitGetDistributedLock(key, requestId);
            productSpecValueDto = getProductSpecValueDto(goods);
            //查询是否 拼团产品
            groupBuyProductSpecDto = getGroupBuyProduct(productSpecValueDto);
        } finally {
            DistributedLock.releaseDistributedLock(requestId, key);
        }

        if (goods.getString("carId").startsWith("-")) {
            goods.put("carId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_cartId));
        }

        String payPrice = groupBuyProductSpecDto == null ? productSpecValueDto.getPrice() : groupBuyProductSpecDto.getGroupPrice();

        StoreOrderCartPo storeOrderCartPo = new StoreOrderCartPo();
        storeOrderCartPo.setCartId(goods.getString("carId"));
        storeOrderCartPo.setCartNum(goods.getString("cartNum"));
        storeOrderCartPo.setFreightPrice("0");
        storeOrderCartPo.setOrderId(storeOrderPo.getOrderId());
        storeOrderCartPo.setPayPrice(payPrice);
        storeOrderCartPo.setPersonId(storeOrderPo.getPersonId());
        storeOrderCartPo.setProductId(goods.getString("productId"));
        storeOrderCartPo.setState(StoreOrderCartDto.STATE_WAIT_BUY);
        storeOrderCartPo.setStoreId(goods.getString("storeId"));
        storeOrderCartPo.setValueId(goods.getString("valueId"));
        storeOrderCartPo.setPrice(productSpecValueDto.getPrice());

        flag = storeOrderCartInnerServiceSMOImpl.saveStoreOrderCart(storeOrderCartPo);

        if (flag < 1) {
            throw new IllegalArgumentException("保存购物车失败");
        }

        BigDecimal orderPayPrice = new BigDecimal(Double.parseDouble(storeOrderPo.getPayPrice()));

        double oPayPrice = orderPayPrice.add(new BigDecimal(Double.parseDouble(payPrice))).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        storeOrderPo.setPayPrice(oPayPrice + "");

        BigDecimal orderPrice = new BigDecimal(Double.parseDouble(storeOrderPo.getTotalPrice()));

        double oPrice = orderPrice.add(new BigDecimal(Double.parseDouble(storeOrderCartPo.getPrice()))).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        storeOrderPo.setPayPrice(oPrice + "");

    }

    private GroupBuyProductSpecDto getGroupBuyProduct(ProductSpecValueDto productSpecValueDto) {
        GroupBuyProductSpecDto groupBuyProductSpecDto;
        int flag;
        groupBuyProductSpecDto = new GroupBuyProductSpecDto();
        groupBuyProductSpecDto.setStoreId(productSpecValueDto.getStoreId());
        groupBuyProductSpecDto.setProductId(productSpecValueDto.getProductId());
        groupBuyProductSpecDto.setValueId(productSpecValueDto.getValueId());
        List<GroupBuyProductSpecDto> groupBuyProductSpecDtos = groupBuyProductSpecInnerServiceSMOImpl.queryGroupBuyProductSpecs(groupBuyProductSpecDto);

        if (groupBuyProductSpecDtos == null || groupBuyProductSpecDtos.size() < 1) {
            return null;
        }

        groupBuyProductSpecDto = groupBuyProductSpecDtos.get(0);

        int groupStock = Integer.parseInt(groupBuyProductSpecDto.getStock());
        if (groupStock < 1) {
            return null;
        }
        GroupBuyProductSpecPo groupBuyProductSpecPo = new GroupBuyProductSpecPo();
        groupBuyProductSpecPo.setStoreId(productSpecValueDto.getStoreId());
        groupBuyProductSpecPo.setProductId(productSpecValueDto.getProductId());
        groupBuyProductSpecPo.setValueId(productSpecValueDto.getValueId());
        groupBuyProductSpecPo.setGroupStock((groupStock - 1) + "");
        groupBuyProductSpecPo.setGroupSales((Integer.parseInt(groupBuyProductSpecDto.getGroupSales()) + 1) + "");
        flag = groupBuyProductSpecInnerServiceSMOImpl.updateGroupBuyProductSpec(groupBuyProductSpecPo);
        if (flag < 1) {
            throw new IllegalArgumentException("减库存失败");
        }
        return groupBuyProductSpecDto;
    }

    private ProductSpecValueDto getProductSpecValueDto(JSONObject goods) {
        ProductSpecValueDto productSpecValueDto;
        int flag;//查询 产品 及价格 并且 减库存
        productSpecValueDto = new ProductSpecValueDto();
        productSpecValueDto.setProductId(goods.getString("productId"));
        productSpecValueDto.setValueId(goods.getString("valueId"));
        productSpecValueDto.setStoreId(goods.getString("storeId"));
        List<ProductSpecValueDto> productSpecValueDtos = productSpecValueInnerServiceSMOImpl.queryProductSpecValues(productSpecValueDto);
        Assert.listOnlyOne(productSpecValueDtos, "不存在 该产品信息");

        productSpecValueDto = productSpecValueDtos.get(0);

        int stock = Integer.parseInt(productSpecValueDto.getStock());

        if (stock < 1) {
            throw new IllegalArgumentException("库存不足");
        }
        ProductSpecValuePo productSpecValuePo = new ProductSpecValuePo();
        productSpecValuePo.setValueId(productSpecValueDto.getValueId());
        productSpecValuePo.setProductId(productSpecValueDto.getProductId());
        productSpecValuePo.setStoreId(productSpecValueDto.getStoreId());
        productSpecValuePo.setSales((Integer.parseInt(productSpecValueDto.getSales()) + 1) + "");
        productSpecValuePo.setStock((stock - 1) + "");
        flag = productSpecValueInnerServiceSMOImpl.updateProductSpecValue(productSpecValuePo);
        if (flag < 1) {
            throw new IllegalArgumentException("减库存失败");
        }
        return productSpecValueDto;
    }

}
