package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.UnitDto;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.community.dao.IFloorServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小区楼服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("floorServiceDaoImpl")
//@Transactional
public class FloorServiceDaoImpl extends BaseServiceDao implements IFloorServiceDao {

    private static Logger logger = LoggerFactory.getLogger(FloorServiceDaoImpl.class);

    /**
     * 小区楼信息封装
     *
     * @param businessFloorInfo 小区楼信息 封装
     * @throws DAOException
     */
    @Override
    public void saveBusinessFloorInfo(Map businessFloorInfo) throws DAOException {
        businessFloorInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存小区楼信息 入参 businessFloorInfo : {}", businessFloorInfo);
        int saveFlag = sqlSessionTemplate.insert("floorServiceDaoImpl.saveBusinessFloorInfo", businessFloorInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存小区楼数据失败：" + JSONObject.toJSONString(businessFloorInfo));
        }
    }


    /**
     * 查询小区楼信息
     *
     * @param info bId 信息
     * @return 小区楼信息
     * @throws DAOException
     */
    @Override
    public List<Map> getBusinessFloorInfo(Map info) throws DAOException {

        logger.debug("查询小区楼信息 入参 info : {}", info);

        List<Map> businessFloorInfos = sqlSessionTemplate.selectList("floorServiceDaoImpl.getBusinessFloorInfo", info);

        return businessFloorInfos;
    }


    /**
     * 保存小区楼信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException
     */
    @Override
    public void saveFloorInfoInstance(Map info) throws DAOException {
        logger.debug("保存小区楼信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("floorServiceDaoImpl.saveFloorInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存小区楼信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询小区楼信息（instance）
     *
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> getFloorInfo(Map info) throws DAOException {
        logger.debug("查询小区楼信息 入参 info : {}", info);

        List<Map> businessFloorInfos = sqlSessionTemplate.selectList("floorServiceDaoImpl.getFloorInfo", info);

        return businessFloorInfos;
    }


    /**
     * 修改小区楼信息
     *
     * @param info 修改信息
     * @throws DAOException
     */
    @Override
    public void updateFloorInfoInstance(Map info) throws DAOException {
        logger.debug("修改小区楼信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("floorServiceDaoImpl.updateFloorInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改小区楼信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public int queryFloorsCount(String communityId) throws DAOException {
        logger.debug("查询小区楼信息 入参 communityId : {}", communityId);
        Map info = new HashMap();
        info.put("communityId", communityId);
        List<Map> businessFloorInfos = sqlSessionTemplate.selectList("floorServiceDaoImpl.queryFloorsCount", info);
        if (businessFloorInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessFloorInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> queryFloors(Map floorMap) throws DAOException {
        logger.debug("查询小区楼信息 入参 floorMap : {}", floorMap);

        List<Map> businessFloorInfos = sqlSessionTemplate.selectList("floorServiceDaoImpl.queryFloors", floorMap);

        return businessFloorInfos;
    }

    @Override
    public int queryFloorsCount(Map info) throws DAOException {
        logger.debug("查询小区楼信息 入参 info : {}", info);

        List<Map> businessFloorInfos = sqlSessionTemplate.selectList("floorServiceDaoImpl.queryFloorsCount", info);
        if (businessFloorInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessFloorInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> queryFloorAndUnits(Map info) {
        logger.debug("查询queryFloorAndUnits信息 入参 floorMap : {}", info);

        List<Map> businessFloorInfos = sqlSessionTemplate.selectList("floorServiceDaoImpl.queryFloorAndUnits", info);

        return businessFloorInfos;
    }


}
