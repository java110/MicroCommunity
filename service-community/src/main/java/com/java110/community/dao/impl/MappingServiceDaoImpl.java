package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.community.dao.IMappingServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 映射服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("mappingServiceDaoImpl")
//@Transactional
public class MappingServiceDaoImpl extends BaseServiceDao implements IMappingServiceDao {

    private static Logger logger = LoggerFactory.getLogger(MappingServiceDaoImpl.class);

    /**
     * 映射信息封装
     *
     * @param businessMappingInfo 映射信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public int saveMappingInfo(Map businessMappingInfo) throws DAOException {
        businessMappingInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存映射信息 入参 businessMappingInfo : {}", businessMappingInfo);
        int saveFlag = sqlSessionTemplate.insert("mappingServiceDaoImpl.saveMappingInfo", businessMappingInfo);
        return saveFlag;
    }


    /**
     * 查询映射信息
     *
     * @param info bId 信息
     * @return 映射信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessMappingInfo(Map info) throws DAOException {

        logger.debug("查询映射信息 入参 info : {}", info);

        List<Map> businessMappingInfos = sqlSessionTemplate.selectList("mappingServiceDaoImpl.getBusinessMappingInfo", info);

        return businessMappingInfos;
    }


    /**
     * 保存映射信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveMappingInfoInstance(Map info) throws DAOException {
        logger.debug("保存映射信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("mappingServiceDaoImpl.saveMappingInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存映射信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询映射信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getMappingInfo(Map info) throws DAOException {
        logger.debug("查询映射信息 入参 info : {}", info);

        List<Map> businessMappingInfos = sqlSessionTemplate.selectList("mappingServiceDaoImpl.getMappingInfo", info);

        return businessMappingInfos;
    }


    /**
     * 修改映射信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public int updateMappingInfo(Map info) throws DAOException {
        logger.debug("修改映射信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("mappingServiceDaoImpl.updateMappingInfo", info);

       return saveFlag;
    }

    /**
     * 查询映射数量
     *
     * @param info 映射信息
     * @return 映射数量
     */
    @Override
    public int queryMappingsCount(Map info) {
        logger.debug("查询映射数据 入参 info : {}", info);

        List<Map> businessMappingInfos = sqlSessionTemplate.selectList("mappingServiceDaoImpl.queryMappingsCount", info);
        if (businessMappingInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessMappingInfos.get(0).get("count").toString());
    }


}
