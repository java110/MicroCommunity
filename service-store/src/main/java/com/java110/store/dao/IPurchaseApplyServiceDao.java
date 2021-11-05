package com.java110.store.dao;


import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.po.purchase.PurchaseApplyDetailPo;
import com.java110.utils.exception.DAOException;
import com.java110.vo.api.purchaseApply.PurchaseApplyDetailVo;

import java.util.List;
import java.util.Map;

/**
 * 采购申请组件内部之间使用，没有给外围系统提供服务能力
 * 采购申请服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 * <p>
 * Created by wuxw on 2016/12/27.
 */
public interface IPurchaseApplyServiceDao {

    /**
     * 保存 采购申请信息
     *
     * @param businessPurchaseApplyInfo 采购申请信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessPurchaseApplyInfo(Map businessPurchaseApplyInfo) throws DAOException;

    //保存采购明细
    void saveBusinessPurchaseApplyDetailInfo(List<PurchaseApplyDetailVo> list) throws DAOException;

    //保存采购明细
    int savePurchaseApplyDetailInfo(List<PurchaseApplyDetailPo> list) throws DAOException;


    /**
     * 查询采购申请信息（business过程）
     * 根据bId 查询采购申请信息
     *
     * @param info bId 信息
     * @return 采购申请信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessPurchaseApplyInfo(Map info) throws DAOException;


    //查询采购明细business表
    List<Map> getBusinessPurchaseApplyDetailInfo(Map info) throws DAOException;


    /**
     * 保存 采购申请信息 Business数据到 Instance中
     *
     * @param info
     * @throws DAOException DAO异常
     */
    void savePurchaseApplyInfoInstance(Map info) throws DAOException;


    /**
     * 查询采购申请信息（instance过程）
     * 根据bId 查询采购申请信息
     *
     * @param info bId 信息
     * @return 采购申请信息
     * @throws DAOException DAO异常
     */
    List<Map> getPurchaseApplyInfo(Map info) throws DAOException;

    List<PurchaseApplyDto> getPurchaseApplyInfo2(Map info) throws DAOException;


    //查询采购明细
    List<Map> getPurchaseApplyDetailInfo(Map info) throws DAOException;


    /**
     * 修改采购申请信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updatePurchaseApplyInfoInstance(Map info) throws DAOException;


    /**
     * 查询采购申请总数
     *
     * @param info 采购申请信息
     * @return 采购申请数量
     */
    int queryPurchaseApplysCount(Map info);


    /**
     * 保存 采购申请信息
     *
     * @param businessPurchaseApplyInfo 采购申请信息 封装
     * @throws DAOException 操作数据库异常
     */
    int savePurchaseApply(Map businessPurchaseApplyInfo) throws DAOException;

    /**
     * 获取下级处理人id
     */
    List<Map> getActRuTaskUserId(Map info);

    /**
     * 获取流程任务id
     */
    List<Map> getActRuTaskId(Map info);

    /**
     * 修改流程任务信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateActRuTaskById(Map info) throws DAOException;

}
