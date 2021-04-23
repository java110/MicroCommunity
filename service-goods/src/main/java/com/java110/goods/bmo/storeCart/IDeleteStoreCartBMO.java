package com.java110.goods.bmo.storeCart;
import com.java110.po.storeCart.StoreCartPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteStoreCartBMO {


    /**
     * 修改购物车
     * add by wuxw
     * @param storeCartPo
     * @return
     */
    ResponseEntity<String> delete(StoreCartPo storeCartPo);


}
