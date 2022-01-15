package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IFloorAttrServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 考勤班组属性服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("floorAttrServiceDaoImpl")
//@Transactional
public class FloorAttrServiceDaoImpl extends BaseServiceDao implements IFloorAttrServiceDao {

    private static Logger logger = LoggerFactory.getLogger(FloorAttrServiceDaoImpl.class);

    /**
     * 考勤班组属性信息封装
     *
     * @param businessFloorAttrInfo 考勤班组属性信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessFloorAttrInfo(Map businessFloorAttrInfo) throws DAOException {
        businessFloorAttrInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存考勤班组属性信息 入参 businessFloorAttrInfo : {}", businessFloorAttrInfo);
        int saveFlag = sqlSessionTemplate.insert("floorAttrServiceDaoImpl.saveBusinessFloorAttrInfo", businessFloorAttrInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存考勤班组属性数据失败：" + JSONObject.toJSONString(businessFloorAttrInfo));
        }
    }


    /**
     * 查询考勤班组属性信息
     *
     * @param info bId 信息
     * @return 考勤班组属性信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessFloorAttrInfo(Map info) throws DAOException {

        logger.debug("查询考勤班组属性信息 入参 info : {}", info);

        List<Map> businessFloorAttrInfos = sqlSessionTemplate.selectList("floorAttrServiceDaoImpl.getBusinessFloorAttrInfo", info);

        return businessFloorAttrInfos;
    }


    /**
     * 保存考勤班组属性信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveFloorAttrInfoInstance(Map info) throws DAOException {
        logger.debug("保存考勤班组属性信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("floorAttrServiceDaoImpl.saveFloorAttrInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存考勤班组属性信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询考勤班组属性信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getFloorAttrInfo(Map info) throws DAOException {
        logger.debug("查询考勤班组属性信息 入参 info : {}", info);

        List<Map> businessFloorAttrInfos = sqlSessionTemplate.selectList("floorAttrServiceDaoImpl.getFloorAttrInfo", info);

        return businessFloorAttrInfos;
    }


    /**
     * 修改考勤班组属性信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public int updateFloorAttrInfoInstance(Map info) throws DAOException {
        logger.debug("修改考勤班组属性信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("floorAttrServiceDaoImpl.updateFloorAttrInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改考勤班组属性信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
        return saveFlag;
    }

    /**
     * 查询考勤班组属性数量
     *
     * @param info 考勤班组属性信息
     * @return 考勤班组属性数量
     */
    @Override
    public int queryFloorAttrsCount(Map info) {
        logger.debug("查询考勤班组属性数据 入参 info : {}", info);

        List<Map> businessFloorAttrInfos = sqlSessionTemplate.selectList("floorAttrServiceDaoImpl.queryFloorAttrsCount", info);
        if (businessFloorAttrInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessFloorAttrInfos.get(0).get("count").toString());
    }

    @Override
    public int saveFloorAttr(Map info) {
        logger.debug("保存saveFloorAttr 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("floorAttrServiceDaoImpl.saveFloorAttr", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存saveFloorAttr数据失败：" + JSONObject.toJSONString(info));
        }

        return saveFlag;
    }


}
