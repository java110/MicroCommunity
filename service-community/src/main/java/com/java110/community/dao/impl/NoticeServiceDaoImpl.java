package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.community.dao.INoticeServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 通知服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("noticeServiceDaoImpl")
//@Transactional
public class NoticeServiceDaoImpl extends BaseServiceDao implements INoticeServiceDao {

    private static Logger logger = LoggerFactory.getLogger(NoticeServiceDaoImpl.class);

    /**
     * 通知信息封装
     * @param businessNoticeInfo 通知信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessNoticeInfo(Map businessNoticeInfo) throws DAOException {
        businessNoticeInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存通知信息 入参 businessNoticeInfo : {}",businessNoticeInfo);
        int saveFlag = sqlSessionTemplate.insert("noticeServiceDaoImpl.saveBusinessNoticeInfo",businessNoticeInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存通知数据失败："+ JSONObject.toJSONString(businessNoticeInfo));
        }
    }


    /**
     * 查询通知信息
     * @param info bId 信息
     * @return 通知信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessNoticeInfo(Map info) throws DAOException {

        logger.debug("查询通知信息 入参 info : {}",info);

        List<Map> businessNoticeInfos = sqlSessionTemplate.selectList("noticeServiceDaoImpl.getBusinessNoticeInfo",info);

        return businessNoticeInfos;
    }



    /**
     * 保存通知信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveNoticeInfoInstance(Map info) throws DAOException {
        logger.debug("保存通知信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("noticeServiceDaoImpl.saveNoticeInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存通知信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询通知信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getNoticeInfo(Map info) throws DAOException {
        logger.debug("查询通知信息 入参 info : {}",info);

        List<Map> businessNoticeInfos = sqlSessionTemplate.selectList("noticeServiceDaoImpl.getNoticeInfo",info);

        return businessNoticeInfos;
    }


    /**
     * 修改通知信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateNoticeInfoInstance(Map info) throws DAOException {
        logger.debug("修改通知信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("noticeServiceDaoImpl.updateNoticeInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改通知信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询通知数量
     * @param info 通知信息
     * @return 通知数量
     */
    @Override
    public int queryNoticesCount(Map info) {
        logger.debug("查询通知数据 入参 info : {}",info);

        List<Map> businessNoticeInfos = sqlSessionTemplate.selectList("noticeServiceDaoImpl.queryNoticesCount", info);
        if (businessNoticeInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessNoticeInfos.get(0).get("count").toString());
    }


}
