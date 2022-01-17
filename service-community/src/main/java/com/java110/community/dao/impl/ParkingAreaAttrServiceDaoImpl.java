package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.community.dao.IParkingAreaAttrServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 单元属性服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("parkingAreaAttrServiceDaoImpl")
//@Transactional
public class ParkingAreaAttrServiceDaoImpl extends BaseServiceDao implements IParkingAreaAttrServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ParkingAreaAttrServiceDaoImpl.class);

    /**
     * 单元属性信息封装
     * @param businessParkingAreaAttrInfo 单元属性信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessParkingAreaAttrInfo(Map businessParkingAreaAttrInfo) throws DAOException {
        businessParkingAreaAttrInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存单元属性信息 入参 businessParkingAreaAttrInfo : {}",businessParkingAreaAttrInfo);
        int saveFlag = sqlSessionTemplate.insert("parkingAreaAttrServiceDaoImpl.saveBusinessParkingAreaAttrInfo",businessParkingAreaAttrInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存单元属性数据失败："+ JSONObject.toJSONString(businessParkingAreaAttrInfo));
        }
    }


    /**
     * 查询单元属性信息
     * @param info bId 信息
     * @return 单元属性信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessParkingAreaAttrInfo(Map info) throws DAOException {

        logger.debug("查询单元属性信息 入参 info : {}",info);

        List<Map> businessParkingAreaAttrInfos = sqlSessionTemplate.selectList("parkingAreaAttrServiceDaoImpl.getBusinessParkingAreaAttrInfo",info);

        return businessParkingAreaAttrInfos;
    }



    /**
     * 保存单元属性信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveParkingAreaAttrInfoInstance(Map info) throws DAOException {
        logger.debug("保存单元属性信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("parkingAreaAttrServiceDaoImpl.saveParkingAreaAttrInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存单元属性信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询单元属性信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getParkingAreaAttrInfo(Map info) throws DAOException {
        logger.debug("查询单元属性信息 入参 info : {}",info);

        List<Map> businessParkingAreaAttrInfos = sqlSessionTemplate.selectList("parkingAreaAttrServiceDaoImpl.getParkingAreaAttrInfo",info);

        return businessParkingAreaAttrInfos;
    }


    /**
     * 修改单元属性信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateParkingAreaAttrInfoInstance(Map info) throws DAOException {
        logger.debug("修改单元属性信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("parkingAreaAttrServiceDaoImpl.updateParkingAreaAttrInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改单元属性信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询单元属性数量
     * @param info 单元属性信息
     * @return 单元属性数量
     */
    @Override
    public int queryParkingAreaAttrsCount(Map info) {
        logger.debug("查询单元属性数据 入参 info : {}",info);

        List<Map> businessParkingAreaAttrInfos = sqlSessionTemplate.selectList("parkingAreaAttrServiceDaoImpl.queryParkingAreaAttrsCount", info);
        if (businessParkingAreaAttrInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessParkingAreaAttrInfos.get(0).get("count").toString());
    }

    @Override
    public int saveParkingAreaAttr(Map beanCovertMap) {
        int saveFlag = sqlSessionTemplate.insert("parkingAreaAttrServiceDaoImpl.saveParkingAreaAttr",beanCovertMap);
        return 0;
    }


}
