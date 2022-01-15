package com.java110.order.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.dto.businessDatabus.BusinessDatabusDto;
import com.java110.entity.mapping.Mapping;
import com.java110.order.dao.ICenterServiceDAO;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 中心服务 数据操作类
 * Created by wuxw on 2018/4/14.
 */
@Service("centerServiceDAOImpl")
@Transactional
public class CenterServiceDAOImpl extends BaseServiceDao implements ICenterServiceDAO {

    protected final static Logger logger = LoggerFactory.getLogger(CenterServiceDAOImpl.class);

    @Override
    public Map getOrder(Map order) throws DAOException {
        List<Map> orders = sqlSessionTemplate.selectList("centerServiceDAOImpl.getOrder", order);
        if (orders != null && orders.size() > 0) {
            return orders.get(0);
        }
        return null;
    }

    @Override
    public List<Map> getOrderItems(Map orderItem) throws DAOException {
        List<Map> orderItems = sqlSessionTemplate.selectList("centerServiceDAOImpl.getOrderItems", orderItem);

        return orderItems;
    }

    @Override
    public void saveOrderItem(Map orderItem) throws DAOException {
        logger.debug("----【CenterServiceDAOImpl.saveOrderItem】保存数据入参 : " + JSONObject.toJSONString(orderItem));

        int saveFlag = sqlSessionTemplate.insert("centerServiceDAOImpl.saveOrderItem", orderItem);
        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_CODE_INNER_ERROR, "保存订单项信息失败：" + JSONObject.toJSONString(orderItem));
        }




        if (!orderItem.containsKey("logText") || StringUtil.isEmpty(orderItem.get("logText") + "")) {
            return;
        }

        saveFlag = sqlSessionTemplate.insert("centerServiceDAOImpl.saveUnItemLog", orderItem);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_CODE_INNER_ERROR, "保存回滚日志失败：" + JSONObject.toJSONString(orderItem));
        }


    }

    @Override
    public void updateOrderItem(Map orderItem) throws DAOException {
        int saveFlag = sqlSessionTemplate.update("centerServiceDAOImpl.updateOrderItem", orderItem);
//        if (saveFlag < 1) {
//            throw new DAOException(ResponseConstant.RESULT_CODE_INNER_ERROR, "修改订单项失败：" + JSONObject.toJSONString(orderItem));
//        }
    }

    @Override
    public void deleteUnItemLog(Map unItemLog) throws DAOException {
        int saveFlag = sqlSessionTemplate.delete("centerServiceDAOImpl.deleteUnItemLog", unItemLog);
        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_CODE_INNER_ERROR, "删除事务日志失败：" + JSONObject.toJSONString(unItemLog));
        }
    }


    /**
     * 保存订单信息
     *
     * @param order 订单信息
     * @return
     */
    @Override
    public void saveOrder(Map order) throws DAOException {

        logger.debug("----【CenterServiceDAOImpl.saveOrder】保存数据入参 : " + JSONObject.toJSONString(order));

        int saveFlag = sqlSessionTemplate.insert("centerServiceDAOImpl.saveOrder", order);
        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_CODE_INNER_ERROR, "保存订单信息失败：" + JSONObject.toJSONString(order));
        }

    }

    /**
     * 保存属性信息
     *
     * @param orderAttrs
     * @return
     */
    @Override
    public void saveOrderAttrs(List<Map> orderAttrs) throws DAOException {

        logger.debug("----【CenterServiceDAOImpl.saveOrderAttrs】保存数据入参 : " + JSONObject.toJSONString(orderAttrs));

        for (Map orderAttr : orderAttrs) {
            int saveFlag = sqlSessionTemplate.insert("centerServiceDAOImpl.saveOrderAttrs", orderAttr);
            if (saveFlag < 1) {
                throw new DAOException(ResponseConstant.RESULT_CODE_INNER_ERROR, "保存订单属性信息失败：" + JSONObject.toJSONString(orderAttr));
            }
        }
    }

    /**
     * 保存订单项信息
     *
     * @param business 订单项信息
     */
    @Override
    public void saveBusiness(Map business) throws DAOException {

        logger.debug("----【CenterServiceDAOImpl.saveBusiness】保存数据入参 : " + JSONObject.toJSONString(business));
        int saveFlag = sqlSessionTemplate.insert("centerServiceDAOImpl.saveBusiness", business);
        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_CODE_INNER_ERROR, "保存订单项信息失败：" + JSONObject.toJSONString(business));
        }
    }

    /**
     * 保存订单项信息
     *
     * @param businesses 订单项信息
     */
    @Override
    public void saveBusiness(List<Map> businesses) throws DAOException {

        logger.debug("----【CenterServiceDAOImpl.saveBusiness】保存数据入参 : " + JSONObject.toJSONString(businesses));
        for (Map business : businesses) {
            int saveFlag = sqlSessionTemplate.insert("centerServiceDAOImpl.saveBusiness", business);
            if (saveFlag < 1) {
                throw new DAOException(ResponseConstant.RESULT_CODE_INNER_ERROR, "保存订单项信息失败：" + JSONObject.toJSONString(business));
            }
        }
    }

    /**
     * 保存属性信息
     *
     * @param businessAttrs
     */
    @Override
    public void saveBusinessAttrs(List<Map> businessAttrs) throws DAOException {

        logger.debug("----【CenterServiceDAOImpl.saveBusinessAttrs】保存数据入参 : " + JSONObject.toJSONString(businessAttrs));

        for (Map businessAttr : businessAttrs) {
            int saveFlag = sqlSessionTemplate.insert("centerServiceDAOImpl.saveBusinessAttrs", businessAttr);
            if (saveFlag < 1) {
                throw new DAOException(ResponseConstant.RESULT_CODE_INNER_ERROR, "保存订单项属性信息失败：" + JSONObject.toJSONString(businessAttr));
            }
        }
    }

    /**
     * 更新订单信息（一般就更新订单状态）
     *
     * @param order
     * @throws DAOException
     */
    @Override
    public void updateOrder(Map order) throws DAOException {
        logger.debug("----【CenterServiceDAOImpl.updateOrder】保存数据入参 : " + JSONObject.toJSONString(order));

        int saveFlag = sqlSessionTemplate.update("centerServiceDAOImpl.updateOrder", order);
        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_CODE_INNER_ERROR, "更新订单信息失败：" + JSONObject.toJSONString(order));
        }
    }

    /**
     * 更新订单项信息（一般就更新订单项状态）
     *
     * @param order
     * @throws DAOException
     */
    @Override
    public void updateBusiness(Map order) throws DAOException {
        logger.debug("----【CenterServiceDAOImpl.updateBusiness】保存数据入参 : " + JSONObject.toJSONString(order));

         sqlSessionTemplate.update("centerServiceDAOImpl.updateBusiness", order);
//        if (saveFlag < 1) {
//            throw new DAOException(ResponseConstant.RESULT_CODE_INNER_ERROR, "更新订单项信息失败：" + JSONObject.toJSONString(order));
//        }
    }

    /**
     * 根据bId 修改业务项信息
     *
     * @param business
     * @throws DAOException
     */
    public void updateBusinessByBId(Map business) throws DAOException {
        logger.debug("----【CenterServiceDAOImpl.updateBusinessByBId】保存数据入参 : " + JSONObject.toJSONString(business));

        int saveFlag = sqlSessionTemplate.update("centerServiceDAOImpl.updateBusinessByBId", business);
        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_CODE_INNER_ERROR, "更新订单项信息失败：" + JSONObject.toJSONString(business));
        }
    }

    /**
     * 当所有业务动作是否都是C，将订单信息改为 C
     *
     * @param bId
     * @return
     * @throws DAOException
     */
    public void completeOrderByBId(String bId) throws DAOException {
        logger.debug("----【CenterServiceDAOImpl.completeOrderByBId】数据入参 : " + bId);

        int updateFlag = sqlSessionTemplate.update("centerServiceDAOImpl.completeOrderByBId", bId);

        if (updateFlag < 1) {
            //throw new DAOException(ResponseConstant.RESULT_CODE_INNER_ERROR,"当前业务还没有全完成（C）："+ bId);
            logger.debug("当前业务还没有全完成（C）:" + bId);
        }
    }

    /**
     * 当所有业务动作是否都是C，将订单信息改为 C
     *
     * @param oId
     * @return
     * @throws DAOException
     */
    public void completeOrderByOId(String oId) throws DAOException {
        logger.debug("----【CenterServiceDAOImpl.completeOrderByOId】数据入参 : " + oId);

        int updateFlag = sqlSessionTemplate.update("centerServiceDAOImpl.completeOrderByOId", oId);

        if (updateFlag < 1) {
            //throw new DAOException(ResponseConstant.RESULT_CODE_INNER_ERROR,"当前业务还没有全完成（C）："+ bId);
            logger.debug("当前业务还没有全完成（C）:" + oId);
        }
    }

    /**
     * 判断 business 过程是否完成 1 表示完成 0表示未完成
     *
     * @param oId
     * @return
     * @throws DAOException
     */
    public int judgeAllBusinessCompleted(String oId, String statusCd) throws DAOException {
        logger.debug("----【CenterServiceDAOImpl.judgeAllBusinessCompleted】数据入参 :oId= " + oId + ",statusCd = " + statusCd);
        Map paramIn = new HashMap();
        paramIn.put("oId", oId);
        paramIn.put("statusCd", statusCd);

        List<Map> paramOuts = sqlSessionTemplate.selectList("centerServiceDAOImpl.judgeAllBusinessCompleted", paramIn);
        if (paramOuts == null || paramOuts.size() == 0) {
            return 0;
        }
        return 1;
    }

    /**
     * 判断 business 过程是否 满足撤单条件
     *
     * @param oId
     * @return
     * @throws DAOException
     */
    public int judgeAllBusinessDeleteOrder(String oId, String statusCd) throws DAOException {
        logger.debug("----【CenterServiceDAOImpl.judgeAllBusinessDeleteOrder】数据入参 :oId= " + oId + ",statusCd = " + statusCd);
        Map paramIn = new HashMap();
        paramIn.put("oId", oId);
        paramIn.put("statusCd", statusCd);

        List<Map> paramOuts = sqlSessionTemplate.selectList("centerServiceDAOImpl.judgeAllBusinessDeleteOrder", paramIn);
        if (paramOuts == null || paramOuts.size() == 0) {
            return 0;
        }
        return 1;
    }

    /**
     * 根据bId查询订单信息
     *
     * @param bId
     * @return
     * @throws DAOException
     */
    public Map getOrderInfoByBId(String bId) throws DAOException {
        List<Map> orders = sqlSessionTemplate.selectList("centerServiceDAOImpl.getOrderInfoByBId", bId);
        if (orders != null && orders.size() > 0) {
            return orders.get(0);
        }
        return null;
    }

    /**
     * 根据oId查询订单信息
     *
     * @param oId
     * @return
     * @throws DAOException
     */
    public Map getDeleteOrderBusinessByOId(String oId) throws DAOException {
        List<Map> orders = sqlSessionTemplate.selectList("centerServiceDAOImpl.getDeleteOrderBusinessByOId", oId);
        if (orders != null && orders.size() > 0) {
            return orders.get(0);
        }
        return null;
    }


    /**
     * 获取同个订单中已经完成的订单项
     *
     * @param bId
     * @return
     * @throws DAOException
     */
    public List<Map> getCommonOrderCompledBusinessByBId(String bId) throws DAOException {
        logger.debug("----【CenterServiceDAOImpl.getCommonOrderCompledBusinessByBId】数据入参 : " + bId);
        return sqlSessionTemplate.selectList("centerServiceDAOImpl.getCommonOrderCompledBusinessByBId", bId);
    }

    @Override
    public List<Map> getAppRouteAndServiceInfoAll() {
        return sqlSessionTemplate.selectList("centerServiceDAOImpl.getAppRouteAndServiceInfoAll");
    }


    /**
     * 查询映射表
     *
     * @return
     */
    @Override
    public List<Mapping> getMappingInfoAll() {
        return sqlSessionTemplate.selectList("centerServiceDAOImpl.getMappingInfoAll");
    }

    @Override
    public List<BasePrivilegeDto> getPrivilegeAll() {
        return sqlSessionTemplate.selectList("centerServiceDAOImpl.getPrivilegeAll");
    }

    @Override
    public List<BusinessDatabusDto> getDatabusAll() {
        return sqlSessionTemplate.selectList("centerServiceDAOImpl.getDatabusAll");
    }

    /**
     * 查询业主订单
     *
     * @param info
     * @return
     */
    @Override
    public List<Map> queryOwenrOrders(Map info) {
        return sqlSessionTemplate.selectList("centerServiceDAOImpl.queryOwenrOrders");
    }


    /**
     * 查询业主订单
     *
     * @param info
     * @return
     */
    @Override
    public List<Map> queryOrderByBusinessType(Map info) {
        return sqlSessionTemplate.selectList("centerServiceDAOImpl.queryOrderByBusinessType", info);
    }

    /**
     * 查询业主订单
     *
     * @param info
     * @return
     */
    @Override
    public List<Map> queryOrderByBId(Map info) {
        return sqlSessionTemplate.selectList("centerServiceDAOImpl.queryOrderByBId", info);
    }

    public int updateBusinessStatusCd(Map info) {
        return sqlSessionTemplate.update("centerServiceDAOImpl.updateBusinessStatusCd", info);
    }

    /**
     * 查询业主订单
     *
     * @param info
     * @return
     */
    @Override
    public List<Map> queryManchineOrders(Map info) {
        return sqlSessionTemplate.selectList("centerServiceDAOImpl.queryManchineOrders");
    }

    /**
     * 查询申请钥匙订单
     *
     * @param info
     * @return
     */
    @Override
    public List<Map> queryApplicationKeyOrders(Map info) {
        return sqlSessionTemplate.selectList("centerServiceDAOImpl.queryApplicationKeyOrders");
    }

    /**
     * 根据oId 查询Business
     *
     * @param info
     * @return
     * @throws DAOException
     */
    public List<Map> getBusinessByOId(Map info) throws DAOException {
        logger.debug("----【CenterServiceDAOImpl.getBusinessByOId】保存数据入参 : " + JSONObject.toJSONString(info));

        return sqlSessionTemplate.selectList("centerServiceDAOImpl.getBusinessByOId", info);
    }

    /**
     * 查询同订单 订单项
     *
     * @param info
     * @return
     * @throws DAOException
     */
    public List<Map> querySameOrderBusiness(Map info) throws DAOException {
        logger.debug("----【CenterServiceDAOImpl.querySameOrderBusiness】数据入参 : " + JSONObject.toJSONString(info));

        return sqlSessionTemplate.selectList("centerServiceDAOImpl.querySameOrderBusiness", info);
    }

}
