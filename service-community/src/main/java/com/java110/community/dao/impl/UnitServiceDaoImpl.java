package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.community.dao.IUnitServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 小区单元服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("unitServiceDaoImpl")
//@Transactional
public class UnitServiceDaoImpl extends BaseServiceDao implements IUnitServiceDao {

    private  static Logger logger = LoggerFactory.getLogger(UnitServiceDaoImpl.class);

    /**
     * 小区单元信息封装
     *
     * @param businessUnitInfo 小区单元信息 封装
     * @throws DAOException
     */
    @Override
    public void saveBusinessUnitInfo(Map businessUnitInfo) throws DAOException {
        businessUnitInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存小区单元信息 入参 businessUnitInfo : {}", businessUnitInfo);
        int saveFlag = sqlSessionTemplate.insert("unitServiceDaoImpl.saveBusinessUnitInfo", businessUnitInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存小区单元数据失败：" + JSONObject.toJSONString(businessUnitInfo));
        }
    }


    /**
     * 查询小区单元信息
     *
     * @param info bId 信息
     * @return 小区单元信息
     * @throws DAOException
     */
    @Override
    public List<Map> getBusinessUnitInfo(Map info) throws DAOException {

        logger.debug("查询小区单元信息 入参 info : {}", info);

        List<Map> businessUnitInfos = sqlSessionTemplate.selectList("unitServiceDaoImpl.getBusinessUnitInfo", info);

        return businessUnitInfos;
    }


    /**
     * 保存小区单元信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException
     */
    @Override
    public void saveUnitInfoInstance(Map info) throws DAOException {
        logger.debug("保存小区单元信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("unitServiceDaoImpl.saveUnitInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存小区单元信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询小区单元信息（instance）
     *
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> getUnitInfo(Map info) throws DAOException {
        logger.debug("查询小区单元信息 入参 info : {}", info);

        List<Map> businessUnitInfos = sqlSessionTemplate.selectList("unitServiceDaoImpl.getUnitInfo", info);

        return businessUnitInfos;
    }


    /**
     * 修改小区单元信息
     *
     * @param info 修改信息
     * @throws DAOException
     */
    @Override
    public void updateUnitInfoInstance(Map info) throws DAOException {
        logger.debug("修改小区单元信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("unitServiceDaoImpl.updateUnitInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改小区单元信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询小区单元数量
     *
     * @param info 小区单元信息
     * @return 小区单元数量
     */
    @Override
    public int queryUnitsCount(Map info) {
        logger.debug("查询小区单元数据 入参 info : {}", info);

        List<Map> businessUnitInfos = sqlSessionTemplate.selectList("unitServiceDaoImpl.queryUnitsCount", info);
        if (businessUnitInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessUnitInfos.get(0).get("count").toString());
    }


    @Override
    public List<Map> queryUnitsByCommunityId(Map info) {
        logger.debug("查询queryUnitsByCommunityId数据 入参 info : {}", info);

        List<Map> units = sqlSessionTemplate.selectList("unitServiceDaoImpl.queryUnitsByCommunityId", info);

        return units;
    }

    /**
     * 查询小区单元信息（instance）
     *
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> getFloorAndUnitInfo(Map info) throws DAOException {
        logger.debug("查询小区单元信息 入参 info : {}", info);

        List<Map> businessUnitInfos = sqlSessionTemplate.selectList("unitServiceDaoImpl.getFloorAndUnitInfo", info);

        return businessUnitInfos;
    }


}
