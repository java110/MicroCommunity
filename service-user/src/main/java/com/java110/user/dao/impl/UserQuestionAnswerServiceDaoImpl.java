package com.java110.user.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.user.dao.IUserQuestionAnswerServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 答卷服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("userQuestionAnswerServiceDaoImpl")
//@Transactional
public class UserQuestionAnswerServiceDaoImpl extends BaseServiceDao implements IUserQuestionAnswerServiceDao {

    private static Logger logger = LoggerFactory.getLogger(UserQuestionAnswerServiceDaoImpl.class);





    /**
     * 保存答卷信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveUserQuestionAnswerInfo(Map info) throws DAOException {
        logger.debug("保存答卷信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("userQuestionAnswerServiceDaoImpl.saveUserQuestionAnswerInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存答卷信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询答卷信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getUserQuestionAnswerInfo(Map info) throws DAOException {
        logger.debug("查询答卷信息 入参 info : {}",info);

        List<Map> businessUserQuestionAnswerInfos = sqlSessionTemplate.selectList("userQuestionAnswerServiceDaoImpl.getUserQuestionAnswerInfo",info);

        return businessUserQuestionAnswerInfos;
    }


    /**
     * 修改答卷信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateUserQuestionAnswerInfo(Map info) throws DAOException {
        logger.debug("修改答卷信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("userQuestionAnswerServiceDaoImpl.updateUserQuestionAnswerInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改答卷信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询答卷数量
     * @param info 答卷信息
     * @return 答卷数量
     */
    @Override
    public int queryUserQuestionAnswersCount(Map info) {
        logger.debug("查询答卷数据 入参 info : {}",info);

        List<Map> businessUserQuestionAnswerInfos = sqlSessionTemplate.selectList("userQuestionAnswerServiceDaoImpl.queryUserQuestionAnswersCount", info);
        if (businessUserQuestionAnswerInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessUserQuestionAnswerInfos.get(0).get("count").toString());
    }


}
