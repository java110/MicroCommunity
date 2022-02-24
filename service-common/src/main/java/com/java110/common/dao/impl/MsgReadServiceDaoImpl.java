package com.java110.common.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IMsgReadServiceDao;
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
 * 消息阅读服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("msgReadServiceDaoImpl")
//@Transactional
public class MsgReadServiceDaoImpl extends BaseServiceDao implements IMsgReadServiceDao {

    private static Logger logger = LoggerFactory.getLogger(MsgReadServiceDaoImpl.class);

    /**
     * 消息阅读信息封装
     *
     * @param businessMsgReadInfo 消息阅读信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessMsgReadInfo(Map businessMsgReadInfo) throws DAOException {
        businessMsgReadInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存消息阅读信息 入参 businessMsgReadInfo : {}", businessMsgReadInfo);
        int saveFlag = sqlSessionTemplate.insert("msgReadServiceDaoImpl.saveBusinessMsgReadInfo", businessMsgReadInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存消息阅读数据失败：" + JSONObject.toJSONString(businessMsgReadInfo));
        }
    }


    /**
     * 查询消息阅读信息
     *
     * @param info bId 信息
     * @return 消息阅读信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessMsgReadInfo(Map info) throws DAOException {

        logger.debug("查询消息阅读信息 入参 info : {}", info);

        List<Map> businessMsgReadInfos = sqlSessionTemplate.selectList("msgReadServiceDaoImpl.getBusinessMsgReadInfo", info);

        return businessMsgReadInfos;
    }


    /**
     * 保存消息阅读信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveMsgReadInfoInstance(Map info) throws DAOException {
        logger.debug("保存消息阅读信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("msgReadServiceDaoImpl.saveMsgReadInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存消息阅读信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询消息阅读信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getMsgReadInfo(Map info) throws DAOException {
        logger.debug("查询消息阅读信息 入参 info : {}", info);

        List<Map> businessMsgReadInfos = sqlSessionTemplate.selectList("msgReadServiceDaoImpl.getMsgReadInfo", info);

        return businessMsgReadInfos;
    }


    /**
     * 修改消息阅读信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateMsgReadInfoInstance(Map info) throws DAOException {
        logger.debug("修改消息阅读信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("msgReadServiceDaoImpl.updateMsgReadInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改消息阅读信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询消息阅读数量
     *
     * @param info 消息阅读信息
     * @return 消息阅读数量
     */
    @Override
    public int queryMsgReadsCount(Map info) {
        logger.debug("查询消息阅读数据 入参 info : {}", info);

        List<Map> businessMsgReadInfos = sqlSessionTemplate.selectList("msgReadServiceDaoImpl.queryMsgReadsCount", info);
        if (businessMsgReadInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessMsgReadInfos.get(0).get("count").toString());
    }


}
