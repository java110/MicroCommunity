package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IUnitAttrServiceDao;
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
 * 单元属性服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("unitAttrServiceDaoImpl")
//@Transactional
public class UnitAttrServiceDaoImpl extends BaseServiceDao implements IUnitAttrServiceDao {

    private static Logger logger = LoggerFactory.getLogger(UnitAttrServiceDaoImpl.class);

    /**
     * 单元属性信息封装
     *
     * @param businessUnitAttrInfo 单元属性信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessUnitAttrInfo(Map businessUnitAttrInfo) throws DAOException {
        businessUnitAttrInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存单元属性信息 入参 businessUnitAttrInfo : {}", businessUnitAttrInfo);
        int saveFlag = sqlSessionTemplate.insert("unitAttrServiceDaoImpl.saveBusinessUnitAttrInfo", businessUnitAttrInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存单元属性数据失败：" + JSONObject.toJSONString(businessUnitAttrInfo));
        }
    }


    /**
     * 查询单元属性信息
     *
     * @param info bId 信息
     * @return 单元属性信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessUnitAttrInfo(Map info) throws DAOException {

        logger.debug("查询单元属性信息 入参 info : {}", info);

        List<Map> businessUnitAttrInfos = sqlSessionTemplate.selectList("unitAttrServiceDaoImpl.getBusinessUnitAttrInfo", info);

        return businessUnitAttrInfos;
    }


    /**
     * 保存单元属性信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveUnitAttrInfoInstance(Map info) throws DAOException {
        logger.debug("保存单元属性信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("unitAttrServiceDaoImpl.saveUnitAttrInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存单元属性信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询单元属性信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getUnitAttrInfo(Map info) throws DAOException {
        logger.debug("查询单元属性信息 入参 info : {}", info);

        List<Map> businessUnitAttrInfos = sqlSessionTemplate.selectList("unitAttrServiceDaoImpl.getUnitAttrInfo", info);

        return businessUnitAttrInfos;
    }


    /**
     * 修改单元属性信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateUnitAttrInfoInstance(Map info) throws DAOException {
        logger.debug("修改单元属性信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("unitAttrServiceDaoImpl.updateUnitAttrInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改单元属性信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询单元属性数量
     *
     * @param info 单元属性信息
     * @return 单元属性数量
     */
    @Override
    public int queryUnitAttrsCount(Map info) {
        logger.debug("查询单元属性数据 入参 info : {}", info);

        List<Map> businessUnitAttrInfos = sqlSessionTemplate.selectList("unitAttrServiceDaoImpl.queryUnitAttrsCount", info);
        if (businessUnitAttrInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessUnitAttrInfos.get(0).get("count").toString());
    }

    @Override
    public int saveUnitAttr(Map beanCovertMap) {

        int saveFlag = sqlSessionTemplate.update("unitAttrServiceDaoImpl.saveUnitAttr", beanCovertMap);

        return saveFlag;
    }


}
