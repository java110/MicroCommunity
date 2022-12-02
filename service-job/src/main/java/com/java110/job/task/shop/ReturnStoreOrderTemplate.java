package com.java110.job.task.shop;

import com.java110.dto.shop.ShopDto;
import com.java110.dto.task.TaskDto;
import com.java110.intf.mall.IShopInnerServiceSMO;
import com.java110.job.quartz.TaskSystemQuartz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 购买商品没有支付时 增加库存
 */
@Component
public class ReturnStoreOrderTemplate  extends TaskSystemQuartz {

    @Autowired(required = false)
    private IShopInnerServiceSMO shopInnerServiceSMOImpl;

    @Override
    protected void process(TaskDto taskDto) throws Exception {
        ShopDto shopDto = new ShopDto();
        int flag = shopInnerServiceSMOImpl.returnStoreOrder(shopDto);

        if(flag < 1){
            throw new IllegalArgumentException("退还库存");
        }
    }
}
