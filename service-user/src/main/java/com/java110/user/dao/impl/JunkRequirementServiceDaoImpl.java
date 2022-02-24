package com.java110.user.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.user.dao.IJunkRequirementServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 旧货市场服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("junkRequirementServiceDaoImpl")
//@Transactional
public class JunkRequirementServiceDaoImpl extends BaseServiceDao implements IJunkRequirementServiceDao {

    private static Logger logger = LoggerFactory.getLogger(JunkRequirementServiceDaoImpl.class);

    /**
     * 旧货市场信息封装
     *
     * @param businessJunkRequirementInfo 旧货市场信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessJunkRequirementInfo(Map businessJunkRequirementInfo) throws DAOException {
        businessJunkRequirementInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存旧货市场信息 入参 businessJunkRequirementInfo : {}", businessJunkRequirementInfo);
        int saveFlag = sqlSessionTemplate.insert("junkRequirementServiceDaoImpl.saveBusinessJunkRequirementInfo", businessJunkRequirementInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存旧货市场数据失败：" + JSONObject.toJSONString(businessJunkRequirementInfo));
        }
    }


    /**
     * 查询旧货市场信息
     *
     * @param info bId 信息
     * @return 旧货市场信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessJunkRequirementInfo(Map info) throws DAOException {

        logger.debug("查询旧货市场信息 入参 info : {}", info);

        List<Map> businessJunkRequirementInfos = sqlSessionTemplate.selectList("junkRequirementServiceDaoImpl.getBusinessJunkRequirementInfo", info);

        return businessJunkRequirementInfos;
    }


    /**
     * 保存旧货市场信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveJunkRequirementInfoInstance(Map info) throws DAOException {
        logger.debug("保存旧货市场信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("junkRequirementServiceDaoImpl.saveJunkRequirementInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存旧货市场信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询旧货市场信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getJunkRequirementInfo(Map info) throws DAOException {
        logger.debug("查询旧货市场信息 入参 info : {}", info);

        List<Map> businessJunkRequirementInfos = sqlSessionTemplate.selectList("junkRequirementServiceDaoImpl.getJunkRequirementInfo", info);

        return businessJunkRequirementInfos;
    }


    /**
     * 修改旧货市场信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateJunkRequirementInfoInstance(Map info) throws DAOException {
        logger.debug("修改旧货市场信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("junkRequirementServiceDaoImpl.updateJunkRequirementInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改旧货市场信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询旧货市场数量
     *
     * @param info 旧货市场信息
     * @return 旧货市场数量
     */
    @Override
    public int queryJunkRequirementsCount(Map info) {
        logger.debug("查询旧货市场数据 入参 info : {}", info);

        List<Map> businessJunkRequirementInfos = sqlSessionTemplate.selectList("junkRequirementServiceDaoImpl.queryJunkRequirementsCount", info);
        if (businessJunkRequirementInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessJunkRequirementInfos.get(0).get("count").toString());
    }


}
