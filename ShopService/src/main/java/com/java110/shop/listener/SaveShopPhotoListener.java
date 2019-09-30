package com.java110.shop.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.shop.dao.IShopServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理节点 businessShopPhoto
 * Created by wuxw on 2018/7/7.
 */
@Java110Listener(name = "saveShopPhotoListener")
@Transactional
public class SaveShopPhotoListener extends AbstractShopBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(SaveShopPhotoListener.class);

    @Autowired
    IShopServiceDao shopServiceDaoImpl;


    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_SHOP_INFO;
    }



    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");
        //处理商品属性
        if(data.containsKey("businessShopPhoto")){
            JSONArray businessShopPhotos = data.getJSONArray("businessShopPhoto");
            doBusinessShopPhoto(business,businessShopPhotos);
        }
    }

    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_ADD);
        //商品属性
        List<Map> businessShopPhotos = shopServiceDaoImpl.getBusinessShopPhoto(info);
        if(businessShopPhotos != null && businessShopPhotos.size() > 0) {
            shopServiceDaoImpl.saveShopPhotoInstance(info);
        }
    }

    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        Map info = new HashMap();
        info.put("bId",bId);
        Map paramIn = new HashMap();
        paramIn.put("bId",bId);
        paramIn.put("statusCd",StatusConstant.STATUS_CD_INVALID);
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        //商品属性
        List<Map> shopPhotos = shopServiceDaoImpl.getShopPhoto(info);
        if(shopPhotos != null && shopPhotos.size()>0){
            shopServiceDaoImpl.updateShopPhotoInstance(paramIn);
        }
    }


    /**
     * 处理商品 属性
     * @param business 当前业务
     * @param businessShopPhotos 商品照片
     */
    private void doBusinessShopPhoto(Business business, JSONArray businessShopPhotos) {

        for(int shopPhotoIndex = 0 ; shopPhotoIndex < businessShopPhotos.size();shopPhotoIndex ++){
            JSONObject shopPhoto = businessShopPhotos.getJSONObject(shopPhotoIndex);
            Assert.jsonObjectHaveKey(shopPhoto,"shopPhotoId","businessShopPhoto 节点下没有包含 shopPhotoId 节点");
            shopPhoto.put("bId",business.getbId());
            shopPhoto.put("operate", StatusConstant.OPERATE_ADD);
            shopServiceDaoImpl.saveBusinessShopPhoto(shopPhoto);
        }
    }



    @Override
    public IShopServiceDao getShopServiceDaoImpl() {
        return shopServiceDaoImpl;
    }

    public void setShopServiceDaoImpl(IShopServiceDao shopServiceDaoImpl) {
        this.shopServiceDaoImpl = shopServiceDaoImpl;
    }
}
