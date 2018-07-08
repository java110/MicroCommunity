package com.java110.shop.dao;


import com.java110.common.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 商品组件内部之间使用，没有给外围系统提供服务能力
 * 商品服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IShopServiceDao {

    /**
     * 保存 商品信息
     * @param businessShopInfo 商品信息 封装
     * @throws DAOException 操作数据库异常
     */
    public void saveBusinessShopInfo(Map businessShopInfo) throws DAOException;

    /**
     * 保存商品属性
     * @param businessShopAttr 商品属性信息封装
     * @throws DAOException 操作数据库异常
     */
    public void saveBusinessShopAttr(Map businessShopAttr) throws DAOException;

    /**
     * 保存 商品属性参数
     * @param businessShopAttrParam 商品属性参数信息封装
     * @throws DAOException 操作数据库异常
     */
    public void saveBusinessShopAttrParam(Map businessShopAttrParam) throws DAOException;


    /**
     * 保存商品照片信息
     * @param businessShopPhoto 商品照片
     * @throws DAOException 操作数据库异常
     */
    public void saveBusinessShopPhoto(Map businessShopPhoto) throws DAOException;

    /**
     * 保存商品证件信息
     * @param businessShopPreferential 商品证件
     * @throws DAOException 操作数据库异常
     */
    public void saveBusinessShopPreferential(Map businessShopPreferential) throws DAOException;

    /**
     * 保存商品 描述 信息
     * @param businessShopDesc 商品 描述信息
     * @throws DAOException
     */
    public void saveBusinessShopDesc(Map businessShopDesc) throws DAOException;

    /**
     * 保存商品目录信息 add by wuxw 2018-09-08
     * @param businessShopCatalog 商品目录
     * @throws DAOException
     */
    public void saveBusinessShopCatalog(Map businessShopCatalog) throws DAOException;


    /**
     * 查询商品信息（business过程）
     * 根据bId 查询商品信息
     * @param info bId 信息
     * @return 商品信息
     * @throws DAOException
     */
    public Map getBusinessShopInfo(Map info) throws DAOException;


    /**
     * 查询商品属性信息（business过程）
     * @param info bId 信息
     * @return 商品属性
     * @throws DAOException
     */
    public List<Map> getBusinessShopAttrs(Map info) throws DAOException;

    /**
     * 查询商品属性参数信息（business过程）
     * @param info bId 信息
     * @return 商品属性参数
     * @throws DAOException
     */
    public List<Map> getBusinessShopAttrParams(Map info) throws DAOException;


    /**
     * 查询商品照片
     * @param info bId 信息
     * @return 商品照片
     * @throws DAOException
     */
    public List<Map> getBusinessShopPhoto(Map info) throws DAOException;


    /**
     * 查询商品优惠信息
     * @param info bId 信息
     * @return 商品照片
     * @throws DAOException
     */
    public Map getBusinessShopPreferential(Map info) throws DAOException;

    /**
     * 查询商品描述信息
     * @param info bId 信息
     * @return 商品照片
     * @throws DAOException
     */
    public Map getBusinessShopDesc(Map info) throws DAOException;

    /**
     * 查询商品目录信息
     * @param info bId 信息
     * @return 商品目录
     * @throws DAOException
     */
    public Map getBusinessShopCatalog(Map info) throws DAOException;

    /**
     * 保存 商品信息 Business数据到 Instance中
     * @param info
     * @throws DAOException
     */
    public void saveShopInfoInstance(Map info) throws DAOException;


    /**
     * 保存 商品属性信息 Business数据到 Instance中
     * @param info
     * @throws DAOException
     */
    public void saveShopAttrsInstance(Map info) throws DAOException;

    /**
     * 保存 商户属性参数 business 数据到 Instance 中
     * @param info
     * @throws DAOException
     */
    public void saveShopAttrParamsInstance(Map info) throws DAOException;

    /**
     * 保存 商品照片信息 Business数据到 Instance中
     * @param info
     * @throws DAOException
     */
    public void saveShopPhotoInstance(Map info) throws DAOException;


    /**
     * 保存 商品证件信息 Business数据到 Instance中
     * @param info
     * @throws DAOException
     */
    public void saveShopPreferentialInstance(Map info) throws DAOException;

    /**
     * 保存 商品描述信息 Business数据到 Instance中
     * @param info
     * @throws DAOException
     */
    public void saveShopDescInstance(Map info) throws DAOException;

    /**
     * 保存 商品目录信息 Business数据到 Instance中
     * @param info
     * @throws DAOException
     */
    public void saveShopCatalogInstance(Map info) throws DAOException;

    /**
     * 保存购买记录
     * @param businessBuyShop
     * @throws DAOException
     */
    public void saveBuyShopInstance(Map businessBuyShop) throws DAOException;

    /**
     * 保存商品购买记录属性
     * @param businessBuyShopAttr
     * @throws DAOException
     */
    public void saveBuyShopAttrInstance(Map businessBuyShopAttr) throws DAOException;

    /**
     * 查询商品信息（instance过程）
     * 根据bId 查询商品信息
     * @param info bId 信息
     * @return 商品信息
     * @throws DAOException
     */
    public Map getShopInfo(Map info) throws DAOException;


    /**
     * 查询商品属性信息（instance过程）
     * @param info bId 信息
     * @return 商品属性
     * @throws DAOException
     */
    public List<Map> getShopAttrs(Map info) throws DAOException;

    /**
     * 查询商品属性参数信息 （instance过程）
     * @param info bId 信息
     * @return 商品属性参数
     * @throws DAOException
     */
    public List<Map> getShopAttrParams(Map info) throws DAOException;


    /**
     * 查询商品照片（instance 过程）
     * @param info bId 信息
     * @return 商品照片
     * @throws DAOException
     */
    public List<Map> getShopPhoto(Map info) throws DAOException;

    /**
     * 查询商品优惠信息（instance 过程）
     * @param info bId 信息
     * @return 商品照片
     * @throws DAOException
     */
    public Map getShopPreferential(Map info) throws DAOException;

    /**
     * 查询商品描述信息（instance 过程）
     * @param info bId 信息
     * @return 商品照片
     * @throws DAOException
     */
    public Map getShopDesc(Map info) throws DAOException;

    /**
     * 查询商品目录信息（instance 过程）
     * @param info bId 信息
     * @return 商品目录
     * @throws DAOException
     */
    public Map getShopCatalog(Map info) throws DAOException;

    /**
     * 商品购买查询（instance）
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    public Map getBuyShop(Map info) throws DAOException;

    /**
     * 商品属性查询（instance）
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    public List<Map> getBuyShopAttrs(Map info) throws DAOException;

    /**
     * 修改商品信息
     * @param info 修改信息
     * @throws DAOException
     */
    public void updateShopInfoInstance(Map info) throws DAOException;


    /**
     * 修改商品属性信息
     * @param info 修改信息
     * @throws DAOException
     */
    public void updateShopAttrInstance(Map info) throws DAOException;

    /**
     * 修改商品属性参数信息
     * @param info 修改信息
     * @throws DAOException
     */
    public void updateShopAttrParamInstance(Map info) throws DAOException;


    /**
     * 修改商品照片信息
     * @param info 修改信息
     * @throws DAOException
     */
    public void updateShopPhotoInstance(Map info) throws DAOException;

    /**
     * 修改商品优惠信息
     * @param info 修改信息
     * @throws DAOException
     */
    public void updateShopPreferentialInstance(Map info) throws DAOException;

    /**
     * 修改商品描述信息
     * @param info 修改信息
     * @throws DAOException
     */
    public void updateShopDescInstance(Map info) throws DAOException;

    /**
     * 修改商品目录信息
     * @param info 修改信息
     * @throws DAOException
     */
    public void updateShopCatalogInstance(Map info) throws DAOException;


    /**
     * 修改商品购买信息
     * @param info 修改信息
     * @throws DAOException
     */
    public void updateBuyShopInstance(Map info) throws DAOException;

    /**
     * 修改商品购买属性信息（instance）
     * @param info 修改信息
     * @throws DAOException
     */
    public void updateBuyShopAttrInstance(Map info) throws DAOException;
}