package com.java110.dto.storeOrder;

import com.java110.dto.PageDto;
import com.java110.dto.product.ProductSpecDetailDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 订单购物车数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class StoreOrderCartDto extends PageDto implements Serializable {

    public static final String STATE_WAIT_BUY = "1001";//未购买
    public static final String STATE_WAIT_SEND = "2002";//待发货
    public static final String STATE_SENDING = "3003";//待收货
    public static final String STATE_WAIT_REPAIR = "4004";//待评价
    public static final String STATE_REQ_RETURN = "5005";//申请退款
    public static final String STATE_RETURN_SUCCESS = "6006";//退货成功
    public static final String STATE_RETURN_MONEY = "7007";//已退款
    public static final String STATE_FINISH = "8008";//已评价


    private String valueId;
    private String productId;
    private String prodName;
    private String orderId;
    private String cartId;
    private String remark;
    private String storeId;
    private String cartNum;
    private String price;
    private String payPrice;
    private String totalPrice;
    private String personId;
    private String personName;
    private String state;
    private String stateName;
    private String freightPrice;
    private String specId;


    private Date createTime;

    private String statusCd = "0";

    private List<ProductSpecDetailDto> productSpecDetails;


    public String getValueId() {
        return valueId;
    }

    public void setValueId(String valueId) {
        this.valueId = valueId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getCartNum() {
        return cartNum;
    }

    public void setCartNum(String cartNum) {
        this.cartNum = cartNum;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(String payPrice) {
        this.payPrice = payPrice;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFreightPrice() {
        return freightPrice;
    }

    public void setFreightPrice(String freightPrice) {
        this.freightPrice = freightPrice;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getSpecId() {
        return specId;
    }

    public void setSpecId(String specId) {
        this.specId = specId;
    }

    public List<ProductSpecDetailDto> getProductSpecDetails() {
        return productSpecDetails;
    }

    public void setProductSpecDetails(List<ProductSpecDetailDto> productSpecDetails) {
        this.productSpecDetails = productSpecDetails;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
