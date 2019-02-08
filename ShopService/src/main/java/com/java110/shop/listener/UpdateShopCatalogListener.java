package com.java110.shop.listener;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.BusinessTypeConstant;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.constant.StatusConstant;
import com.java110.common.exception.ListenerExecuteException;
import com.java110.common.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.shop.dao.IShopServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 修改商品目录 侦听
 *
 * 处理节点
 * 1、businessShopCatalog:{} 商品优惠信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E5%93%81%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateShopCatalogListener")
@Transactional
public class UpdateShopCatalogListener extends AbstractShopBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(UpdateShopCatalogListener.class);
    @Autowired
    IShopServiceDao shopServiceDaoImpl;

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_SHOP_CATALOG;
    }

    /**
     * business过程
     * @param dataFlowContext
     * @param business
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {

        JSONObject data = business.getDatas();

        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessShopCatalog 节点
        if(data.containsKey("businessShopCatalog")){
            JSONObject businessShopCatalog = data.getJSONObject("businessShopCatalog");
            doBusinessShopCatalog(business,businessShopCatalog);
        }
    }


    /**
     * business to instance 过程
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {

        //JSONObject data = business.getDatas();

        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_ADD);

        //商户优惠信息
        Map businessShopCatalog = shopServiceDaoImpl.getBusinessShopCatalog(info);
        if( businessShopCatalog != null && !businessShopCatalog.isEmpty()) {
            flushBusinessShopCatalog(businessShopCatalog,StatusConstant.STATUS_CD_VALID);
            shopServiceDaoImpl.updateShopCatalogInstance(businessShopCatalog);
            dataFlowContext.addParamOut("catalogId",businessShopCatalog.get("catalog_id"));
        }
    }

    /**
     * 撤单
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {

        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");
        Map info = new HashMap();
        info.put("bId",bId);
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        Map delInfo = new HashMap();
        delInfo.put("bId",business.getbId());
        delInfo.put("operate",StatusConstant.OPERATE_DEL);
        //商户信息
        Map shopCatalog = shopServiceDaoImpl.getShopCatalog(info);
        if(shopCatalog != null && !shopCatalog.isEmpty()){

            //商户信息
            Map businessShopCatalog = shopServiceDaoImpl.getBusinessShopCatalog(delInfo);
            //除非程序出错了，这里不会为空
            if(businessShopCatalog == null || businessShopCatalog.isEmpty()){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR,"撤单失败（shop_catalog），程序内部异常,请检查！ "+delInfo);
            }

            flushBusinessShopCatalog(businessShopCatalog,StatusConstant.STATUS_CD_VALID);
            shopServiceDaoImpl.updateShopCatalogInstance(businessShopCatalog);
            dataFlowContext.addParamOut("catalogId",businessShopCatalog.get("catalog_id"));
        }

    }



    /**
     * 处理 businessShopCatalog 节点
     * @param business 总的数据节点
     * @param businessShopCatalog 商户节点
     */
    private void doBusinessShopCatalog(Business business,JSONObject businessShopCatalog){

        Assert.jsonObjectHaveKey(businessShopCatalog,"catalogId","businessShop 节点下没有包含 catalogId 节点");

        if(businessShopCatalog.getString("catalogId").startsWith("-")){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"catalogId 错误，不能自动生成（必须已经存在的catalogId）"+businessShopCatalog);
        }
        //自动保存DEL
        autoSaveDelBusinessShopCatalog(business,businessShopCatalog);

        businessShopCatalog.put("bId",business.getbId());
        businessShopCatalog.put("operate", StatusConstant.OPERATE_ADD);
        //保存商户信息
        shopServiceDaoImpl.saveBusinessShopCatalog(businessShopCatalog);

    }

    @Override
    public IShopServiceDao getShopServiceDaoImpl() {
        return shopServiceDaoImpl;
    }

    public void setShopServiceDaoImpl(IShopServiceDao shopServiceDaoImpl) {
        this.shopServiceDaoImpl = shopServiceDaoImpl;
    }
}
