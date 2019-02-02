package com.java110.shop.listener;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.constant.StatusConstant;
import com.java110.common.exception.ListenerExecuteException;
import com.java110.entity.center.Business;
import com.java110.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.shop.dao.IShopServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 商户 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractShopBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener{
    private final static Logger logger = LoggerFactory.getLogger(AbstractShopBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     * @return
     */
    public abstract IShopServiceDao getShopServiceDaoImpl();

    /**
     * 刷新 businessShopInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     * @param businessShopInfo
     */
    protected void flushBusinessShopInfo(Map businessShopInfo,String statusCd){
        businessShopInfo.put("newBId",businessShopInfo.get("b_id"));
        businessShopInfo.put("shopId",businessShopInfo.get("shop_id"));
        businessShopInfo.put("storeId",businessShopInfo.get("store_id"));
        businessShopInfo.put("catalogId",businessShopInfo.get("catalog_id"));
        businessShopInfo.put("hotBuy",businessShopInfo.get("hot_buy"));
        businessShopInfo.put("salePrice",businessShopInfo.get("sale_price"));
        businessShopInfo.put("openShopCount",businessShopInfo.get("open_shop_count"));
        businessShopInfo.put("shopCount",businessShopInfo.get("shop_count"));
        businessShopInfo.put("startDate",businessShopInfo.get("start_date"));
        businessShopInfo.put("endDate",businessShopInfo.get("end_date"));
        businessShopInfo.put("statusCd", statusCd);
    }

    /**
        刷新 businessShopAttr 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     * @param businessShopAttr
     * @param statusCd
     */
    protected void flushBusinessShopAttr(Map businessShopAttr,String statusCd){
        businessShopAttr.put("attrId",businessShopAttr.get("attr_id"));
        businessShopAttr.put("specCd",businessShopAttr.get("spec_cd"));
        businessShopAttr.put("shopId",businessShopAttr.get("shop_id"));
        businessShopAttr.put("newBId",businessShopAttr.get("b_id"));
        businessShopAttr.put("statusCd",statusCd);
    }

    /**
     * 刷新 businessShopAttrParam 数据
     * @param businessShopAttrParam
     * @param statusCd
     */
    protected void flushBusinessShopAttrParam(Map businessShopAttrParam,String statusCd){
        businessShopAttrParam.put("newBId",businessShopAttrParam.get("b_id"));
        businessShopAttrParam.put("attrParamId",businessShopAttrParam.get("attr_param_id"));
        businessShopAttrParam.put("shopId",businessShopAttrParam.get("shop_id"));
        businessShopAttrParam.put("specCd",businessShopAttrParam.get("spec_cd"));
        businessShopAttrParam.put("statusCd",statusCd);
    }

    /**
     * 刷新 businessShopPhoto 数据
     * @param businessShopPhoto
     * @param statusCd
     */
    protected void flushBusinessShopPhoto(Map businessShopPhoto,String statusCd){
        businessShopPhoto.put("shopId",businessShopPhoto.get("shop_id"));
        businessShopPhoto.put("shopPhotoId",businessShopPhoto.get("shop_photo_id"));
        businessShopPhoto.put("shopPhotoTypeCd",businessShopPhoto.get("shop_photo_type_cd"));
        businessShopPhoto.put("newBId",businessShopPhoto.get("b_id"));
        businessShopPhoto.put("statusCd",statusCd);
    }

    /**
     * 刷新 businessShopPreferential 数据
     * @param businessShopPreferential
     * @param statusCd
     */
    protected void flushBusinessShopPreferential(Map businessShopPreferential ,String statusCd){
        businessShopPreferential.put("shopPreferentialId",businessShopPreferential.get("shop_preferential_id"));
        businessShopPreferential.put("shopId",businessShopPreferential.get("shop_id"));
        businessShopPreferential.put("originalPrice",businessShopPreferential.get("original_price"));
        businessShopPreferential.put("discountRate",businessShopPreferential.get("discount_rate"));
        businessShopPreferential.put("showOriginalPrice",businessShopPreferential.get("show_original_price"));
        businessShopPreferential.put("preferentialStartDate",businessShopPreferential.get("preferential_start_date"));
        businessShopPreferential.put("preferentialEndDate",businessShopPreferential.get("preferential_end_date"));
        businessShopPreferential.put("newBId",businessShopPreferential.get("b_id"));
        businessShopPreferential.put("statusCd",statusCd);
    }


    /**
     * 刷新 businessShopDesc 数据
     * @param businessShopDesc
     * @param statusCd
     */
    protected void flushBusinessShopDesc(Map businessShopDesc ,String statusCd){
        businessShopDesc.put("shopDescId",businessShopDesc.get("shop_desc_id"));
        businessShopDesc.put("shopId",businessShopDesc.get("shop_id"));
        businessShopDesc.put("shopDescribe",businessShopDesc.get("shop_describe"));
        businessShopDesc.put("newBId",businessShopDesc.get("b_id"));
        businessShopDesc.put("statusCd",statusCd);
    }

    /**
     * 刷新 businessShopCatalog 数据
     * @param businessShopCatalog
     * @param statusCd
     */
    protected void flushBusinessShopCatalog(Map businessShopCatalog ,String statusCd){
        businessShopCatalog.put("catalogId",businessShopCatalog.get("catalog_id"));
        businessShopCatalog.put("storeId",businessShopCatalog.get("store_id"));
        businessShopCatalog.put("parentCatalogId",businessShopCatalog.get("parent_catalog_id"));
        businessShopCatalog.put("newBId",businessShopCatalog.get("b_id"));
        businessShopCatalog.put("statusCd",statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     * @param businessShop 商户信息
     */
    protected void autoSaveDelBusinessShop(Business business, JSONObject businessShop){
//自动插入DEL
        Map info = new HashMap();
        info.put("shopId",businessShop.getString("shopId"));
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        Map currentShopInfo = getShopServiceDaoImpl().getShopInfo(info);
        if(currentShopInfo == null || currentShopInfo.isEmpty()){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"未找到需要修改数据信息，入参错误或数据有问题，请检查"+info);
        }
        currentShopInfo.put("bId",business.getbId());
        currentShopInfo.put("shopId",currentShopInfo.get("shop_id"));
        currentShopInfo.put("storeId",currentShopInfo.get("store_id"));
        currentShopInfo.put("catalogId",currentShopInfo.get("catalog_id"));
        currentShopInfo.put("hotBuy",currentShopInfo.get("hot_buy"));
        currentShopInfo.put("salePrice",currentShopInfo.get("sale_price"));
        currentShopInfo.put("openShopCount",currentShopInfo.get("open_shop_count"));
        currentShopInfo.put("shopCount",currentShopInfo.get("shop_count"));
        currentShopInfo.put("startDate",currentShopInfo.get("start_date"));
        currentShopInfo.put("endDate",currentShopInfo.get("end_date"));
        currentShopInfo.put("operate",StatusConstant.OPERATE_DEL);
        getShopServiceDaoImpl().saveBusinessShopInfo(currentShopInfo);
    }

    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     * @param business 当前业务
     * @param shopAttr 商户属性
     */
    protected void autoSaveDelBusinessShopAttr(Business business, JSONObject shopAttr){
        Map info = new HashMap();
        info.put("attrId",shopAttr.getString("attrId"));
        info.put("shopId",shopAttr.getString("shopId"));
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        List<Map> currentShopAttrs = getShopServiceDaoImpl().getShopAttrs(info);
        if(currentShopAttrs == null || currentShopAttrs.size() != 1){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"未找到需要修改数据信息，入参错误或数据有问题，请检查"+info);
        }
        Map currentShopAttr = currentShopAttrs.get(0);
        currentShopAttr.put("bId",business.getbId());
        currentShopAttr.put("attrId",currentShopAttr.get("attr_id"));
        currentShopAttr.put("shopId",currentShopAttr.get("shop_id"));
        currentShopAttr.put("specCd",currentShopAttr.get("spec_cd"));
        currentShopAttr.put("operate",StatusConstant.OPERATE_DEL);
        getShopServiceDaoImpl().saveBusinessShopAttr(currentShopAttr);
    }

    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     * @param business 当前业务
     * @param shopAttrParam 商品属性参数
     */
    protected void autoSaveDelBusinessShopAttrParam(Business business,JSONObject shopAttrParam){
        Map info = new HashMap();
        info.put("attrParamId",shopAttrParam.getString("attrParamId"));
        info.put("shopId",shopAttrParam.getString("shopId"));
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        List<Map> currentShopAttrParams = getShopServiceDaoImpl().getShopAttrParams(info);
        if(currentShopAttrParams == null || currentShopAttrParams.size() != 1){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"未找到需要修改数据信息，入参错误或数据有问题，请检查"+info);
        }
        Map currentShopAttrParam = currentShopAttrParams.get(0);
        currentShopAttrParam.put("bId",business.getbId());
        currentShopAttrParam.put("attrParamId",currentShopAttrParam.get("attr_param_id"));
        currentShopAttrParam.put("shopId",currentShopAttrParam.get("shop_id"));
        currentShopAttrParam.put("specCd",currentShopAttrParam.get("spec_cd"));
        currentShopAttrParam.put("operate",StatusConstant.OPERATE_DEL);
        getShopServiceDaoImpl().saveBusinessShopAttrParam(currentShopAttrParam);
    }
    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     * @param business
     * @param businessShopPhoto 商户照片
     */
    protected void autoSaveDelBusinessShopPhoto(Business business,JSONObject businessShopPhoto){
       Map info = new HashMap();
        info.put("shopPhotoId",businessShopPhoto.getString("shopPhotoId"));
        info.put("shopId",businessShopPhoto.getString("shopId"));
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        List<Map> currentShopPhotos = getShopServiceDaoImpl().getShopPhoto(info);
        if(currentShopPhotos == null || currentShopPhotos.size() != 1){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"未找到需要修改数据信息，入参错误或数据有问题，请检查"+info);
        }
        Map currentShopPhoto = currentShopPhotos.get(0);
        currentShopPhoto.put("bId",business.getbId());
        currentShopPhoto.put("shopPhotoId",currentShopPhoto.get("shop_photo_id"));
        currentShopPhoto.put("shopId",currentShopPhoto.get("shop_id"));
        currentShopPhoto.put("shopPhotoTypeCd",currentShopPhoto.get("shop_photo_type_cd"));
        currentShopPhoto.put("operate",StatusConstant.OPERATE_DEL);
        getShopServiceDaoImpl().saveBusinessShopPhoto(currentShopPhoto);
    }

    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     * @param business
     * @param businessShopPreferentials 商品优惠
     */
    protected void autoSaveDelBusinessShopPreferential(Business business,JSONObject businessShopPreferentials){
        Map info = new HashMap();
        info.put("shopPreferentialId",businessShopPreferentials.getString("shopPreferentialId"));
        info.put("shopId",businessShopPreferentials.getString("shopId"));
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        Map currentShopPreferential = getShopServiceDaoImpl().getShopPreferential(info);
        if(currentShopPreferential == null || currentShopPreferential.isEmpty()){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"未找到需要修改数据信息，入参错误或数据有问题，请检查"+info);
        }

        currentShopPreferential.put("bId",business.getbId());
        currentShopPreferential.put("shopPreferentialId",currentShopPreferential.get("shop_preferential_id"));
        currentShopPreferential.put("shopId",currentShopPreferential.get("shop_id"));
        currentShopPreferential.put("originalPrice",currentShopPreferential.get("original_price"));
        currentShopPreferential.put("discountRate",currentShopPreferential.get("discount_rate"));
        currentShopPreferential.put("showOriginalPrice",currentShopPreferential.get("show_original_price"));
        currentShopPreferential.put("preferentialStartDate",currentShopPreferential.get("preferential_start_date"));
        currentShopPreferential.put("preferentialEndDate",currentShopPreferential.get("preferential_end_date"));
        currentShopPreferential.put("operate",StatusConstant.OPERATE_DEL);
        getShopServiceDaoImpl().saveBusinessShopPreferential(currentShopPreferential);
    }

    /**
     *
     * @param business
     * @param businessShopDesc
     */
    protected void autoSaveDelBusinessShopDesc(Business business,JSONObject businessShopDesc){
        Map info = new HashMap();
        info.put("shopDescId",businessShopDesc.getString("shopDescId"));
        info.put("shopId",businessShopDesc.getString("shopId"));
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        Map currentShopDesc = getShopServiceDaoImpl().getShopDesc(info);
        if(currentShopDesc == null || currentShopDesc.isEmpty()){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"未找到需要修改数据信息，入参错误或数据有问题，请检查"+info);
        }

        currentShopDesc.put("bId",business.getbId());
        currentShopDesc.put("shopDescId",currentShopDesc.get("shop_desc_id"));
        currentShopDesc.put("shopId",currentShopDesc.get("shop_id"));
        currentShopDesc.put("shopDescribe",currentShopDesc.get("shop_describe"));
        currentShopDesc.put("operate",StatusConstant.OPERATE_DEL);
        getShopServiceDaoImpl().saveBusinessShopDesc(currentShopDesc);
    }

    /**
     * 商品目录 自动刷 DEL数据
     * @param business
     * @param businessShopCalalog
     */
    protected void autoSaveDelBusinessShopCatalog(Business business,JSONObject businessShopCalalog){
        Map info = new HashMap();
        info.put("catalogId",businessShopCalalog.getString("catalogId"));
        info.put("storeId",businessShopCalalog.getString("storeId"));
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        Map currentShopCatalog = getShopServiceDaoImpl().getShopCatalog(info);
        if(currentShopCatalog == null || currentShopCatalog.isEmpty()){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"未找到需要修改数据信息，入参错误或数据有问题，请检查"+info);
        }

        currentShopCatalog.put("bId",business.getbId());
        currentShopCatalog.put("catalogId",currentShopCatalog.get("catalog_id"));
        currentShopCatalog.put("storeId",currentShopCatalog.get("store_id"));
        currentShopCatalog.put("parentCatalogId",currentShopCatalog.get("parent_catalog_id"));
        currentShopCatalog.put("operate",StatusConstant.OPERATE_DEL);
        getShopServiceDaoImpl().saveBusinessShopCatalog(currentShopCatalog);
    }
}
