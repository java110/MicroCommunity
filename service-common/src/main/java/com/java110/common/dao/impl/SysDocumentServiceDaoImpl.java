package com.java110.common.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.common.dao.ISysDocumentServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 系统文档服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("sysDocumentServiceDaoImpl")
//@Transactional
public class SysDocumentServiceDaoImpl extends BaseServiceDao implements ISysDocumentServiceDao {

    private static Logger logger = LoggerFactory.getLogger(SysDocumentServiceDaoImpl.class);





    /**
     * 保存系统文档信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveSysDocumentInfo(Map info) throws DAOException {
        logger.debug("保存系统文档信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("sysDocumentServiceDaoImpl.saveSysDocumentInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存系统文档信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询系统文档信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getSysDocumentInfo(Map info) throws DAOException {
        logger.debug("查询系统文档信息 入参 info : {}",info);

        List<Map> businessSysDocumentInfos = sqlSessionTemplate.selectList("sysDocumentServiceDaoImpl.getSysDocumentInfo",info);

        return businessSysDocumentInfos;
    }


    /**
     * 修改系统文档信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateSysDocumentInfo(Map info) throws DAOException {
        logger.debug("修改系统文档信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("sysDocumentServiceDaoImpl.updateSysDocumentInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改系统文档信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询系统文档数量
     * @param info 系统文档信息
     * @return 系统文档数量
     */
    @Override
    public int querySysDocumentsCount(Map info) {
        logger.debug("查询系统文档数据 入参 info : {}",info);

        List<Map> businessSysDocumentInfos = sqlSessionTemplate.selectList("sysDocumentServiceDaoImpl.querySysDocumentsCount", info);
        if (businessSysDocumentInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessSysDocumentInfos.get(0).get("count").toString());
    }


}
