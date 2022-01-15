package com.java110.store.dao.impl.demo;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.store.dao.IDemoServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * demo服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("demoServiceDaoImpl")
//@Transactional
public class DemoServiceDaoImpl extends BaseServiceDao implements IDemoServiceDao {

    private static Logger logger = LoggerFactory.getLogger(DemoServiceDaoImpl.class);

    /**
     * demo信息封装
     * @param businessDemoInfo demo信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessDemoInfo(Map businessDemoInfo) throws DAOException {
        businessDemoInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存demo信息 入参 businessDemoInfo : {}",businessDemoInfo);
        int saveFlag = sqlSessionTemplate.insert("demoServiceDaoImpl.saveBusinessDemoInfo",businessDemoInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存demo数据失败："+ JSONObject.toJSONString(businessDemoInfo));
        }
    }


    /**
     * 查询demo信息
     * @param info bId 信息
     * @return demo信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessDemoInfo(Map info) throws DAOException {

        logger.debug("查询demo信息 入参 info : {}",info);

        List<Map> businessDemoInfos = sqlSessionTemplate.selectList("demoServiceDaoImpl.getBusinessDemoInfo",info);

        return businessDemoInfos;
    }



    /**
     * 保存demo信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveDemoInfoInstance(Map info) throws DAOException {
        logger.debug("保存demo信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("demoServiceDaoImpl.saveDemoInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存demo信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询demo信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getDemoInfo(Map info) throws DAOException {
        logger.debug("查询demo信息 入参 info : {}",info);

        List<Map> businessDemoInfos = sqlSessionTemplate.selectList("demoServiceDaoImpl.getDemoInfo",info);

        return businessDemoInfos;
    }


    /**
     * 修改demo信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateDemoInfoInstance(Map info) throws DAOException {
        logger.debug("修改demo信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("demoServiceDaoImpl.updateDemoInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改demo信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询demo数量
     * @param info demo信息
     * @return demo数量
     */
    @Override
    public int queryDemosCount(Map info) {
        logger.debug("查询demo数据 入参 info : {}",info);

        List<Map> businessDemoInfos = sqlSessionTemplate.selectList("demoServiceDaoImpl.queryDemosCount", info);
        if (businessDemoInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessDemoInfos.get(0).get("count").toString());
    }


}
