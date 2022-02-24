package com.java110.common.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IMsgServiceDao;
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
 * 消息服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("msgServiceDaoImpl")
//@Transactional
public class MsgServiceDaoImpl extends BaseServiceDao implements IMsgServiceDao {

    private static Logger logger = LoggerFactory.getLogger(MsgServiceDaoImpl.class);

    /**
     * 消息信息封装
     *
     * @param businessMsgInfo 消息信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessMsgInfo(Map businessMsgInfo) throws DAOException {
        businessMsgInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存消息信息 入参 businessMsgInfo : {}", businessMsgInfo);
        int saveFlag = sqlSessionTemplate.insert("msgServiceDaoImpl.saveBusinessMsgInfo", businessMsgInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存消息数据失败：" + JSONObject.toJSONString(businessMsgInfo));
        }
    }


    /**
     * 查询消息信息
     *
     * @param info bId 信息
     * @return 消息信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessMsgInfo(Map info) throws DAOException {

        logger.debug("查询消息信息 入参 info : {}", info);

        List<Map> businessMsgInfos = sqlSessionTemplate.selectList("msgServiceDaoImpl.getBusinessMsgInfo", info);

        return businessMsgInfos;
    }


    /**
     * 保存消息信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveMsgInfoInstance(Map info) throws DAOException {
        logger.debug("保存消息信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("msgServiceDaoImpl.saveMsgInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存消息信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询消息信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getMsgInfo(Map info) throws DAOException {
        logger.debug("查询消息信息 入参 info : {}", info);

        List<Map> businessMsgInfos = sqlSessionTemplate.selectList("msgServiceDaoImpl.getMsgInfo", info);

        return businessMsgInfos;
    }


    /**
     * 修改消息信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateMsgInfoInstance(Map info) throws DAOException {
        logger.debug("修改消息信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("msgServiceDaoImpl.updateMsgInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改消息信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询消息数量
     *
     * @param info 消息信息
     * @return 消息数量
     */
    @Override
    public int queryMsgsCount(Map info) {
        logger.debug("查询消息数据 入参 info : {}", info);

        List<Map> businessMsgInfos = sqlSessionTemplate.selectList("msgServiceDaoImpl.queryMsgsCount", info);
        if (businessMsgInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessMsgInfos.get(0).get("count").toString());
    }


}
