package com.java110.store.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.store.dao.IComplaintServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 投诉建议服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("complaintServiceDaoImpl")
//@Transactional
public class ComplaintServiceDaoImpl extends BaseServiceDao implements IComplaintServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ComplaintServiceDaoImpl.class);

    /**
     * 投诉建议信息封装
     *
     * @param businessComplaintInfo 投诉建议信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessComplaintInfo(Map businessComplaintInfo) throws DAOException {
        businessComplaintInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存投诉建议信息 入参 businessComplaintInfo : {}", businessComplaintInfo);
        int saveFlag = sqlSessionTemplate.insert("complaintServiceDaoImpl.saveBusinessComplaintInfo", businessComplaintInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存投诉建议数据失败：" + JSONObject.toJSONString(businessComplaintInfo));
        }
    }


    /**
     * 查询投诉建议信息
     *
     * @param info bId 信息
     * @return 投诉建议信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessComplaintInfo(Map info) throws DAOException {

        logger.debug("查询投诉建议信息 入参 info : {}", info);

        List<Map> businessComplaintInfos = sqlSessionTemplate.selectList("complaintServiceDaoImpl.getBusinessComplaintInfo", info);

        return businessComplaintInfos;
    }


    /**
     * 保存投诉建议信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveComplaintInfoInstance(Map info) throws DAOException {
        logger.debug("保存投诉建议信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("complaintServiceDaoImpl.saveComplaintInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存投诉建议信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询投诉建议信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getComplaintInfo(Map info) throws DAOException {
        logger.debug("查询投诉建议信息 入参 info : {}", info);

        List<Map> businessComplaintInfos = sqlSessionTemplate.selectList("complaintServiceDaoImpl.getComplaintInfo", info);

        return businessComplaintInfos;
    }


    /**
     * 修改投诉建议信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateComplaintInfoInstance(Map info) throws DAOException {
        logger.debug("修改投诉建议信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("complaintServiceDaoImpl.updateComplaintInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改投诉建议信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询投诉建议数量
     *
     * @param info 投诉建议信息
     * @return 投诉建议数量
     */
    @Override
    public int queryComplaintsCount(Map info) {
        logger.debug("查询投诉建议数据 入参 info : {}", info);

        List<Map> businessComplaintInfos = sqlSessionTemplate.selectList("complaintServiceDaoImpl.queryComplaintsCount", info);
        if (businessComplaintInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessComplaintInfos.get(0).get("count").toString());
    }


}
