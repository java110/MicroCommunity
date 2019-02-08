package com.java110.shop.listener;

import com.alibaba.fastjson.JSONArray;
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
import java.util.List;
import java.util.Map;

/**
 * 修改商户信息 侦听
 *
 * 处理节点
 * 1、businessShopAttr:{} 商品属性信息节点
 * 2、businessShopAttrParam:{} 商品属性参数节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E5%93%81%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateShopAttrAndAttrParamListener")
@Transactional
public class UpdateShopAttrAndAttrParamListener extends AbstractShopBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(UpdateShopAttrAndAttrParamListener.class);
    @Autowired
    IShopServiceDao shopServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_SHOP_INFO;
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

        //处理 businessShop 节点
        if(data.containsKey("businessShopAttr")){
            JSONArray businessShopAttrs = data.getJSONArray("businessShopAttr");
            doBusinessShopAttr(business,businessShopAttrs);
        }

        //处理 businessShop 节点
        if(data.containsKey("businessShopAttrParam")){
            JSONArray businessShopAttrParams = data.getJSONArray("businessShopAttrParam");
            doBusinessShopAttrParam(business,businessShopAttrParams);
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

        //商户属性信息
        List<Map> businessShopAttrs = shopServiceDaoImpl.getBusinessShopAttrs(info);
        if(businessShopAttrs != null && businessShopAttrs.size() > 0) {
            for(Map businessShopAttr : businessShopAttrs) {
                flushBusinessShopAttr(businessShopAttr,StatusConstant.STATUS_CD_VALID);
                shopServiceDaoImpl.updateShopAttrInstance(businessShopAttr);
            }
        }

        //商户属性参数信息
        List<Map> businessShopAttrParams = shopServiceDaoImpl.getBusinessShopAttrParams(info);
        if(businessShopAttrParams != null && businessShopAttrParams.size() > 0) {
            for(Map businessShopAttrParam : businessShopAttrParams) {
                flushBusinessShopAttrParam(businessShopAttrParam,StatusConstant.STATUS_CD_VALID);
                shopServiceDaoImpl.updateShopAttrParamInstance(businessShopAttrParam);
            }
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
        List<Map> shopAttrs = shopServiceDaoImpl.getShopAttrs(info);
        if(shopAttrs != null && shopAttrs.size()>0){

            List<Map> businessShopAttrs = shopServiceDaoImpl.getBusinessShopAttrs(delInfo);
            //除非程序出错了，这里不会为空
            if(businessShopAttrs == null || businessShopAttrs.size() ==0 ){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR,"撤单失败(shop_attr)，程序内部异常,请检查！ "+delInfo);
            }
            for(Map businessShopAttr : businessShopAttrs) {
                flushBusinessShopAttr(businessShopAttr,StatusConstant.STATUS_CD_VALID);
                shopServiceDaoImpl.updateShopAttrInstance(businessShopAttr);
            }
        }

        //商户属性参数信息
        List<Map> shopAttrParams = shopServiceDaoImpl.getShopAttrParams(info);
        if(shopAttrParams != null && shopAttrParams.size()>0){

            List<Map> businessShopAttrParams = shopServiceDaoImpl.getBusinessShopAttrParams(delInfo);
            //除非程序出错了，这里不会为空
            if(businessShopAttrParams == null || businessShopAttrParams.size() ==0 ){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR,"撤单失败(shop_attr_param)，程序内部异常,请检查！ "+delInfo);
            }
            for(Map businessShopAttrParam : businessShopAttrParams) {
                flushBusinessShopAttrParam(businessShopAttrParam,StatusConstant.STATUS_CD_VALID);
                shopServiceDaoImpl.updateShopAttrParamInstance(businessShopAttrParam);
            }
        }

    }



    /**
     * 处理 businessShopAttr 节点
     * @param business 总的数据节点
     * @param businessShopAttrs 商品属性节点
     */
    private void doBusinessShopAttr(Business business,JSONArray businessShopAttrs){
        JSONObject data = business.getDatas();


        for(int shopAttrIndex = 0 ; shopAttrIndex < businessShopAttrs.size();shopAttrIndex ++){
            JSONObject shopAttr = businessShopAttrs.getJSONObject(shopAttrIndex);
            Assert.jsonObjectHaveKey(shopAttr,"attrId","businessShopAttr 节点下没有包含 attrId 节点");
            if(shopAttr.getString("attrId").startsWith("-")){
                throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"attrId 错误，不能自动生成（必须已经存在的attrId）"+shopAttr);
            }
            //自动保存DEL数据
            autoSaveDelBusinessShopAttr(business,shopAttr);

            shopAttr.put("bId",business.getbId());
            shopAttr.put("shopId",shopAttr.getString("shopId"));
            shopAttr.put("operate", StatusConstant.OPERATE_ADD);

            shopServiceDaoImpl.saveBusinessShopAttr(shopAttr);
        }

    }

    /**
     * 处理 businessShopAttrParam 节点
     * @param business 总的数据节点
     * @param businessShopAttrParams 商品属性参数节点
     */
    private void doBusinessShopAttrParam(Business business,JSONArray businessShopAttrParams){
        JSONObject data = business.getDatas();


        for(int shopAttrParamIndex = 0 ; shopAttrParamIndex < businessShopAttrParams.size();shopAttrParamIndex ++){
            JSONObject shopAttrParam = businessShopAttrParams.getJSONObject(shopAttrParamIndex);
            Assert.jsonObjectHaveKey(shopAttrParam,"attrParamId","businessShopAttr 节点下没有包含 attrParamId 节点");
            if(shopAttrParam.getString("attrParamId").startsWith("-")){
                throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"attrParamId 错误，不能自动生成（必须已经存在的attrId）"+shopAttrParam);
            }
            //自动保存DEL数据
            autoSaveDelBusinessShopAttrParam(business,shopAttrParam);

            shopAttrParam.put("bId",business.getbId());
            shopAttrParam.put("shopId",shopAttrParam.getString("shopId"));
            shopAttrParam.put("operate", StatusConstant.OPERATE_ADD);

            shopServiceDaoImpl.saveBusinessShopAttrParam(shopAttrParam);
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
