package com.java110.store.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.store.dao.IResourceSupplierServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 物品供应商服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("resourceSupplierServiceDaoImpl")
//@Transactional
public class ResourceSupplierServiceDaoImpl extends BaseServiceDao implements IResourceSupplierServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ResourceSupplierServiceDaoImpl.class);

    /**
     * 物品供应商信息封装
     *
     * @param businessResourceSupplierInfo 物品供应商信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessResourceSupplierInfo(Map businessResourceSupplierInfo) throws DAOException {
        businessResourceSupplierInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存物品供应商信息 入参 businessResourceSupplierInfo : {}", businessResourceSupplierInfo);
        int saveFlag = sqlSessionTemplate.insert("resourceSupplierServiceDaoImpl.saveBusinessResourceSupplierInfo", businessResourceSupplierInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存物品供应商数据失败：" + JSONObject.toJSONString(businessResourceSupplierInfo));
        }
    }


    /**
     * 查询物品供应商信息
     *
     * @param info bId 信息
     * @return 物品供应商信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessResourceSupplierInfo(Map info) throws DAOException {

        logger.debug("查询物品供应商信息 入参 info : {}", info);

        List<Map> businessResourceSupplierInfos = sqlSessionTemplate.selectList("resourceSupplierServiceDaoImpl.getBusinessResourceSupplierInfo", info);

        return businessResourceSupplierInfos;
    }


    /**
     * 保存物品供应商信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveResourceSupplierInfoInstance(Map info) throws DAOException {
        logger.debug("保存物品供应商信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("resourceSupplierServiceDaoImpl.saveResourceSupplierInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存物品供应商信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询物品供应商信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getResourceSupplierInfo(Map info) throws DAOException {
        logger.debug("查询物品供应商信息 入参 info : {}", info);

        List<Map> businessResourceSupplierInfos = sqlSessionTemplate.selectList("resourceSupplierServiceDaoImpl.getResourceSupplierInfo", info);

        return businessResourceSupplierInfos;
    }


    /**
     * 修改物品供应商信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateResourceSupplierInfoInstance(Map info) throws DAOException {
        logger.debug("修改物品供应商信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("resourceSupplierServiceDaoImpl.updateResourceSupplierInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改物品供应商信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询物品供应商数量
     *
     * @param info 物品供应商信息
     * @return 物品供应商数量
     */
    @Override
    public int queryResourceSuppliersCount(Map info) {
        logger.debug("查询物品供应商数据 入参 info : {}", info);

        List<Map> businessResourceSupplierInfos = sqlSessionTemplate.selectList("resourceSupplierServiceDaoImpl.queryResourceSuppliersCount", info);
        if (businessResourceSupplierInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessResourceSupplierInfos.get(0).get("count").toString());
    }


}
