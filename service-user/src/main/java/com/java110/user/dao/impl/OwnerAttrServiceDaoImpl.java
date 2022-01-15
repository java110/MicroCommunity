package com.java110.user.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.user.dao.IOwnerAttrServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 业主属性服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("ownerAttrServiceDaoImpl")
//@Transactional
public class OwnerAttrServiceDaoImpl extends BaseServiceDao implements IOwnerAttrServiceDao {

    private static Logger logger = LoggerFactory.getLogger(OwnerAttrServiceDaoImpl.class);

    /**
     * 业主属性信息封装
     *
     * @param businessOwnerAttrInfo 业主属性信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessOwnerAttrInfo(Map businessOwnerAttrInfo) throws DAOException {
        businessOwnerAttrInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存业主属性信息 入参 businessOwnerAttrInfo : {}", businessOwnerAttrInfo);
        int saveFlag = sqlSessionTemplate.insert("ownerAttrServiceDaoImpl.saveBusinessOwnerAttrInfo", businessOwnerAttrInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存业主属性数据失败：" + JSONObject.toJSONString(businessOwnerAttrInfo));
        }
    }


    /**
     * 查询业主属性信息
     *
     * @param info bId 信息
     * @return 业主属性信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessOwnerAttrInfo(Map info) throws DAOException {

        logger.debug("查询业主属性信息 入参 info : {}", info);

        List<Map> businessOwnerAttrInfos = sqlSessionTemplate.selectList("ownerAttrServiceDaoImpl.getBusinessOwnerAttrInfo", info);

        return businessOwnerAttrInfos;
    }


    /**
     * 保存业主属性信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveOwnerAttrInfoInstance(Map info) throws DAOException {
        logger.debug("保存业主属性信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("ownerAttrServiceDaoImpl.saveOwnerAttrInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存业主属性信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询业主属性信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getOwnerAttrInfo(Map info) throws DAOException {
        logger.debug("查询业主属性信息 入参 info : {}", info);

        List<Map> businessOwnerAttrInfos = sqlSessionTemplate.selectList("ownerAttrServiceDaoImpl.getOwnerAttrInfo", info);

        return businessOwnerAttrInfos;
    }


    /**
     * 修改业主属性信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public int updateOwnerAttrInfoInstance(Map info) throws DAOException {
        logger.debug("修改业主属性信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("ownerAttrServiceDaoImpl.updateOwnerAttrInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改业主属性信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
        return saveFlag;
    }

    /**
     * 查询业主属性数量
     *
     * @param info 业主属性信息
     * @return 业主属性数量
     */
    @Override
    public int queryOwnerAttrsCount(Map info) {
        logger.debug("查询业主属性数据 入参 info : {}", info);

        List<Map> businessOwnerAttrInfos = sqlSessionTemplate.selectList("ownerAttrServiceDaoImpl.queryOwnerAttrsCount", info);
        if (businessOwnerAttrInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessOwnerAttrInfos.get(0).get("count").toString());
    }

    @Override
    public int saveOwnerAttr(Map beanCovertMap) {
        logger.debug("保存业主属性信息 入参 info : {}", beanCovertMap);

        int saveFlag = sqlSessionTemplate.insert("ownerAttrServiceDaoImpl.saveOwnerAttr", beanCovertMap);
        return saveFlag;
    }


}
