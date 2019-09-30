package com.java110.shop.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.shop.dao.IShopServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 商品服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("shopServiceDaoImpl")
//@Transactional
public class ShopServiceDaoImpl extends BaseServiceDao implements IShopServiceDao {

    private final static Logger logger = LoggerFactory.getLogger(ShopServiceDaoImpl.class);

    /**
     * 商品信息封装
     * @param businessShopInfo 商品信息 封装
     * @throws DAOException
     */
    @Override
    public void saveBusinessShopInfo(Map businessShopInfo) throws DAOException {
        businessShopInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存商品信息 入参 businessShopInfo : {}",businessShopInfo);
        int saveFlag = sqlSessionTemplate.insert("shopServiceDaoImpl.saveBusinessShopInfo",businessShopInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存商品数据失败："+ JSONObject.toJSONString(businessShopInfo));
        }
    }

    /**
     * 商品属性信息分装
     * @param businessShopAttr 商品属性信息封装
     * @throws DAOException
     */
    @Override
    public void saveBusinessShopAttr(Map businessShopAttr) throws DAOException {
        businessShopAttr.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存商品属性信息 入参 businessShopAttr : {}",businessShopAttr);

        int saveFlag = sqlSessionTemplate.insert("shopServiceDaoImpl.saveBusinessShopAttr",businessShopAttr);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存商品属性数据失败："+ JSONObject.toJSONString(businessShopAttr));
        }
    }

    /**
     * 商品属性参数保存
     * @param businessShopAttrParam 商品属性参数信息封装
     * @throws DAOException
     */
    @Override
    public void saveBusinessShopAttrParam(Map businessShopAttrParam) throws DAOException {
        businessShopAttrParam.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存商品属性参数信息 入参 businessShopAttr : {}",businessShopAttrParam);

        int saveFlag = sqlSessionTemplate.insert("shopServiceDaoImpl.saveBusinessShopAttrParam",businessShopAttrParam);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存商品属性参数数据失败："+ JSONObject.toJSONString(businessShopAttrParam));
        }
    }

    /**
     * 保存商品照片信息
     * @param businessShopPhoto 商品照片
     * @throws DAOException
     */
    @Override
    public void saveBusinessShopPhoto(Map businessShopPhoto) throws DAOException {
        businessShopPhoto.put("month", DateUtil.getCurrentMonth());
        logger.debug("保存商品照片信息 入参 businessShopPhoto : {}",businessShopPhoto);

        int saveFlag = sqlSessionTemplate.insert("shopServiceDaoImpl.saveBusinessShopPhoto",businessShopPhoto);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存商品照片数据失败："+ JSONObject.toJSONString(businessShopPhoto));
        }
    }

    /**
     * 保存商品证件信息
     * @param businessShopPreferential 商品证件
     * @throws DAOException
     */
    @Override
    public void saveBusinessShopPreferential(Map businessShopPreferential) throws DAOException {
        businessShopPreferential.put("month", DateUtil.getCurrentMonth());
        logger.debug("保存商品优惠信息 入参 businessShopPreferential : {}",businessShopPreferential);

        int saveFlag = sqlSessionTemplate.insert("shopServiceDaoImpl.saveBusinessShopPreferential",businessShopPreferential);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存商品证件数据失败："+ JSONObject.toJSONString(businessShopPreferential));
        }
    }

    /**
     * 保存 商品描述信息
     * @param businessShopDesc 商品 描述信息
     * @throws DAOException
     */
    @Override
    public void saveBusinessShopDesc(Map businessShopDesc) throws DAOException {
        businessShopDesc.put("month", DateUtil.getCurrentMonth());
        logger.debug("保存商品描述信息 入参 businessShopDesc : {}",businessShopDesc);

        int saveFlag = sqlSessionTemplate.insert("shopServiceDaoImpl.saveBusinessShopDesc",businessShopDesc);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存商品描述数据失败："+ JSONObject.toJSONString(businessShopDesc));
        }
    }

    /**
     * 保存商品目录
     * @param businessShopCatalog 商品目录
     * @throws DAOException
     */
    @Override
    public void saveBusinessShopCatalog(Map businessShopCatalog) throws DAOException {
        businessShopCatalog.put("month", DateUtil.getCurrentMonth());
        logger.debug("保存商品目录信息 入参 businessShopCatalog : {}",businessShopCatalog);

        int saveFlag = sqlSessionTemplate.insert("shopServiceDaoImpl.saveBusinessShopCatalog",businessShopCatalog);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存商品目录数据失败："+ JSONObject.toJSONString(businessShopCatalog));
        }
    }

    /**
     * 查询商品信息
     * @param info bId 信息
     * @return 商品信息
     * @throws DAOException
     */
    @Override
    public Map getBusinessShopInfo(Map info) throws DAOException {

        logger.debug("查询商品信息 入参 info : {}",info);

        List<Map> businessShopInfos = sqlSessionTemplate.selectList("shopServiceDaoImpl.getBusinessShopInfo",info);
        if(businessShopInfos == null){
            return null;
        }
        if(businessShopInfos.size() >1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"根据条件查询有多条数据,数据异常，请检查：businessShopInfos，"+ JSONObject.toJSONString(info));
        }

        return businessShopInfos.get(0);
    }

    /**
     * 查询商品属性
     * @param info bId 信息
     * @return 商品属性
     * @throws DAOException
     */
    @Override
    public List<Map> getBusinessShopAttrs(Map info) throws DAOException {
        logger.debug("查询商品属性信息 入参 info : {}",info);

        List<Map> businessShopAttrs = sqlSessionTemplate.selectList("shopServiceDaoImpl.getBusinessShopAttrs",info);

        return businessShopAttrs;
    }

    /**
     * 查询商品属性参数信息
     * @param info bId 信息
     * @return 商品属性参数信息
     * @throws DAOException
     */
    @Override
    public List<Map> getBusinessShopAttrParams(Map info) throws DAOException {
        logger.debug("查询商品属性参数信息 入参 info : {}",info);

        List<Map> businessShopAttrParams = sqlSessionTemplate.selectList("shopServiceDaoImpl.getBusinessShopAttrParams",info);

        return businessShopAttrParams;
    }

    /**
     * 查询商品照片
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> getBusinessShopPhoto(Map info) throws DAOException {
        logger.debug("查询商品照片信息 入参 info : {}",info);

        List<Map> businessShopPhotos = sqlSessionTemplate.selectList("shopServiceDaoImpl.getBusinessShopPhoto",info);

        return businessShopPhotos;
    }

    /**
     * 查询商品优惠
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public Map getBusinessShopPreferential(Map info) throws DAOException {
        logger.debug("查询商品证件信息 入参 info : {}",info);

        List<Map> businessShopPreferentiales = sqlSessionTemplate.selectList("shopServiceDaoImpl.getBusinessShopPreferential",info);
        if(businessShopPreferentiales == null){
            return null;
        }
        if(businessShopPreferentiales.size() >1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"根据条件查询有多条数据,数据异常，请检查：businessShopPreferentiales，"+ JSONObject.toJSONString(info));
        }

        return businessShopPreferentiales.get(0);
    }

    /**
     * 查询商品描述
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public Map getBusinessShopDesc(Map info) throws DAOException {
        logger.debug("查询商品证件信息 入参 info : {}",info);

        List<Map> businessShopDesces = sqlSessionTemplate.selectList("shopServiceDaoImpl.getBusinessShopDesc",info);
        if(businessShopDesces == null){
            return null;
        }
        if(businessShopDesces.size() >1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"根据条件查询有多条数据,数据异常，请检查：businessShopDesces，"+ JSONObject.toJSONString(info));
        }

        return businessShopDesces.get(0);
    }

    /**
     * 查询商品目录
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public Map getBusinessShopCatalog(Map info) throws DAOException {
        logger.debug("查询商品证件信息 入参 info : {}",info);

        List<Map> businessShopCatalogs = sqlSessionTemplate.selectList("shopServiceDaoImpl.getBusinessShopCatalog",info);
        if(businessShopCatalogs == null){
            return null;
        }
        if(businessShopCatalogs.size() >1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"根据条件查询有多条数据,数据异常，请检查：businessShopCatalogs，"+ JSONObject.toJSONString(info));
        }

        return businessShopCatalogs.get(0);
    }

    /**
     * 保存商品信息 到 instance
     * @param info   bId 信息
     * @throws DAOException
     */
    @Override
    public void saveShopInfoInstance(Map info) throws DAOException {
        logger.debug("保存商品信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("shopServiceDaoImpl.saveShopInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存商品信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    @Override
    public void saveShopAttrsInstance(Map info) throws DAOException {
        logger.debug("保存商品属性信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("shopServiceDaoImpl.saveShopAttrsInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存商品属性信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    /**
     * 保存 商品属性 参数 至 business
     * @param info b_id
     * @throws DAOException
     */
    @Override
    public void saveShopAttrParamsInstance(Map info) throws DAOException {
        logger.debug("保存商品属性参数信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("shopServiceDaoImpl.saveShopAttrParamsInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存商品属性参数信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    @Override
    public void saveShopPhotoInstance(Map info) throws DAOException {
        logger.debug("保存商品照片信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("shopServiceDaoImpl.saveShopPhotoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存商品照片信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    @Override
    public void saveShopPreferentialInstance(Map info) throws DAOException {
        logger.debug("保存商品证件信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("shopServiceDaoImpl.saveShopPreferentialInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存商品证件信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    @Override
    public void saveShopDescInstance(Map info) throws DAOException {
        logger.debug("保存商品描述信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("shopServiceDaoImpl.saveShopDescInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存商品证件信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    @Override
    public void saveShopCatalogInstance(Map info) throws DAOException {
        logger.debug("保存商品目录信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("shopServiceDaoImpl.saveShopCatalogInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存商品目录信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 保存购买记录
     * @param businessBuyShop
     * @throws DAOException
     */
    @Override
    public void saveBuyShopInstance(Map businessBuyShop) throws DAOException {
        businessBuyShop.put("month", DateUtil.getCurrentMonth());
        logger.debug("保存商品购买记录信息 入参 businessBuyShop : {}",businessBuyShop);

        int saveFlag = sqlSessionTemplate.insert("shopServiceDaoImpl.saveBuyShopInstance",businessBuyShop);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存商品购买记录数据失败："+ JSONObject.toJSONString(businessBuyShop));
        }
    }

    /**
     * 购买商品属性保存
     * @param businessBuyShopAttr
     * @throws DAOException
     */
    @Override
    public void saveBuyShopAttrInstance(Map businessBuyShopAttr) throws DAOException {
        businessBuyShopAttr.put("month", DateUtil.getCurrentMonth());
        logger.debug("保存商品购买记录属性信息 入参 businessBuyShopAttr : {}",businessBuyShopAttr);

        int saveFlag = sqlSessionTemplate.insert("shopServiceDaoImpl.saveBuyShopAttrInstance",businessBuyShopAttr);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存商品购买记录属性数据失败："+ JSONObject.toJSONString(businessBuyShopAttr));
        }
    }

    /**
     * 查询商品信息（instance）
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public Map getShopInfo(Map info) throws DAOException {
        logger.debug("查询商品信息 入参 info : {}",info);

        List<Map> businessShopInfos = sqlSessionTemplate.selectList("shopServiceDaoImpl.getShopInfo",info);
        if(businessShopInfos == null || businessShopInfos.size() == 0){
            return null;
        }
        if(businessShopInfos.size() >1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"根据条件查询有多条数据,数据异常，请检查：getShopInfo，"+ JSONObject.toJSONString(info));
        }

        return businessShopInfos.get(0);
    }

    /**
     * 商品属性查询（instance）
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> getShopAttrs(Map info) throws DAOException {
        logger.debug("查询商品属性信息 入参 info : {}",info);

        List<Map> shopAttrs = sqlSessionTemplate.selectList("shopServiceDaoImpl.getShopAttrs",info);

        return shopAttrs;
    }

    /**
     * 商品属性参数查询（instance）
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> getShopAttrParams(Map info) throws DAOException {
        logger.debug("查询商品属性参数信息 入参 info : {}",info);

        List<Map> shopAttrParams = sqlSessionTemplate.selectList("shopServiceDaoImpl.getShopAttrParams",info);

        return shopAttrParams;
    }

    /**
     * 商品照片查询（instance）
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> getShopPhoto(Map info) throws DAOException {
        logger.debug("查询商品照片信息 入参 info : {}",info);

        List<Map> shopPhotos = sqlSessionTemplate.selectList("shopServiceDaoImpl.getShopPhoto",info);

        return shopPhotos;
    }

    /**
     * 商品优惠查询（instance）
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public Map getShopPreferential(Map info) throws DAOException {
        logger.debug("查询商品证件信息 入参 info : {}",info);

        List<Map> shopPreferentiales = sqlSessionTemplate.selectList("shopServiceDaoImpl.getShopPreferential",info);
        if(shopPreferentiales == null || shopPreferentiales.size() == 0){
            return null;
        }
        if(shopPreferentiales.size() >1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"根据条件查询有多条数据,数据异常，请检查：getShopInfo，"+ JSONObject.toJSONString(info));
        }
        return shopPreferentiales.get(0);
    }

    /**
     * 商品描述查询（instance）
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public Map getShopDesc(Map info) throws DAOException {
        logger.debug("查询商品证件信息 入参 info : {}",info);

        List<Map> shopDesces = sqlSessionTemplate.selectList("shopServiceDaoImpl.getShopDesc",info);
        if(shopDesces == null || shopDesces.size() == 0){
            return null;
        }
        if(shopDesces.size() >1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"根据条件查询有多条数据,数据异常，请检查：getShopInfo，"+ JSONObject.toJSONString(info));
        }
        return shopDesces.get(0);
    }

    /**
     * 商品目录查询（instance）
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public Map getShopCatalog(Map info) throws DAOException {
        logger.debug("查询商品目录信息 入参 info : {}",info);

        List<Map> shopCatalogs = sqlSessionTemplate.selectList("shopServiceDaoImpl.getShopCatalog",info);
        if(shopCatalogs == null || shopCatalogs.size() == 0){
            return null;
        }
        if(shopCatalogs.size() >1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"根据条件查询有多条数据,数据异常，请检查：getShopCatalog，"+ JSONObject.toJSONString(info));
        }
        return shopCatalogs.get(0);
    }

    /**
     * 商品描述查询（instance）
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public Map getBuyShop(Map info) throws DAOException {
        logger.debug("查询商品购买信息 入参 info : {}",info);

        List<Map> getBuyShops = sqlSessionTemplate.selectList("shopServiceDaoImpl.getBuyShop",info);
        if(getBuyShops == null || getBuyShops.size() == 0){
            return null;
        }
        if(getBuyShops.size() >1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"根据条件查询有多条数据,数据异常，请检查：getShopCatalog，"+ JSONObject.toJSONString(info));
        }
        return getBuyShops.get(0);
    }

    /**
     * 商品属性查询（instance）
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> getBuyShopAttrs(Map info) throws DAOException {
        logger.debug("查询商品购买属性信息 入参 info : {}",info);

        List<Map> buyShopAttrs = sqlSessionTemplate.selectList("shopServiceDaoImpl.getBuyShopAttrs",info);

        return buyShopAttrs;
    }

    /**
     * 修改商品信息
     * @param info 修改信息
     * @throws DAOException
     */
    @Override
    public void updateShopInfoInstance(Map info) throws DAOException {
        logger.debug("修改商品信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("shopServiceDaoImpl.updateShopInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改商品信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    /**
     * 修改商品属性信息（instance）
     * @param info 修改信息
     * @throws DAOException
     */
    @Override
    public void updateShopAttrInstance(Map info) throws DAOException {
        logger.debug("修改商品属性信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("shopServiceDaoImpl.updateShopAttrInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改商品属性信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    /**
     * 修改商品属性参数（instance）
     * @param info 修改信息
     * @throws DAOException
     */
    @Override
    public void updateShopAttrParamInstance(Map info) throws DAOException {
        logger.debug("修改商品属性参数信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("shopServiceDaoImpl.updateShopAttrParamInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改商品属性参数信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    /**
     * 修改 商品照片信息
     * @param info 修改信息
     * @throws DAOException
     */
    @Override
    public void updateShopPhotoInstance(Map info) throws DAOException {
        logger.debug("修改商品照片信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("shopServiceDaoImpl.updateShopPhotoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改商品照片信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    /**
     * 修改商品证件信息
     * @param info 修改信息
     * @throws DAOException
     */
    @Override
    public void updateShopPreferentialInstance(Map info) throws DAOException {
        logger.debug("修改商品证件信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("shopServiceDaoImpl.updateShopPreferentialInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改商品证件信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    /**
     * 修改商品描述信息
     * @param info 修改信息
     * @throws DAOException
     */
    @Override
    public void updateShopDescInstance(Map info) throws DAOException {
        logger.debug("修改商品描述信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("shopServiceDaoImpl.updateShopDescInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改商品描述信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    /**
     * 修改商品描述信息
     * @param info 修改信息
     * @throws DAOException
     */
    @Override
    public void updateShopCatalogInstance(Map info) throws DAOException {
        logger.debug("修改商品目录信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("shopServiceDaoImpl.updateShopCatalogInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改商品目录信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    /**
     * 修改商品购买信息
     * @param info 修改信息
     * @throws DAOException
     */
    @Override
    public void updateBuyShopInstance(Map info) throws DAOException {
        logger.debug("修改商品购买信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("shopServiceDaoImpl.updateBuyShopInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改商品购买信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    /**
     * 修改商品购买属性信息（instance）
     * @param info 修改信息
     * @throws DAOException
     */
    @Override
    public void updateBuyShopAttrInstance(Map info) throws DAOException {
        logger.debug("修改商品购买属性信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("shopServiceDaoImpl.updateBuyShopAttrInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改商品购买属性信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }
}
