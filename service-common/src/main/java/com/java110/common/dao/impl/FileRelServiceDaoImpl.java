package com.java110.common.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IFileRelServiceDao;
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
 * 文件存放服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("fileRelServiceDaoImpl")
//@Transactional
public class FileRelServiceDaoImpl extends BaseServiceDao implements IFileRelServiceDao {

    private static Logger logger = LoggerFactory.getLogger(FileRelServiceDaoImpl.class);

    /**
     * 文件存放信息封装
     *
     * @param businessFileRelInfo 文件存放信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessFileRelInfo(Map businessFileRelInfo) throws DAOException {
        businessFileRelInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存文件存放信息 入参 businessFileRelInfo : {}", businessFileRelInfo);
        int saveFlag = sqlSessionTemplate.insert("fileRelServiceDaoImpl.saveBusinessFileRelInfo", businessFileRelInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存文件存放数据失败：" + JSONObject.toJSONString(businessFileRelInfo));
        }
    }


    /**
     * 查询文件存放信息
     *
     * @param info bId 信息
     * @return 文件存放信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessFileRelInfo(Map info) throws DAOException {

        logger.debug("查询文件存放信息 入参 info : {}", info);

        List<Map> businessFileRelInfos = sqlSessionTemplate.selectList("fileRelServiceDaoImpl.getBusinessFileRelInfo", info);

        return businessFileRelInfos;
    }


    /**
     * 保存文件存放信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveFileRelInfoInstance(Map info) throws DAOException {
        logger.debug("保存文件存放信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("fileRelServiceDaoImpl.saveFileRelInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存文件存放信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public int saveFileRel(Map info) {
        int saveFlag = sqlSessionTemplate.insert("fileRelServiceDaoImpl.saveFileRel", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存文件存放信息Instance数据失败：" + JSONObject.toJSONString(info));
        }

        return saveFlag;
    }

    @Override
    public int deleteFileRel(Map info) {
        int deleteFlag = sqlSessionTemplate.insert("fileRelServiceDaoImpl.deleteFileRel", info);

        if (deleteFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "删除文件存放信息Instance数据失败：" + JSONObject.toJSONString(info));
        }

        return deleteFlag;
    }


    /**
     * 查询文件存放信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getFileRelInfo(Map info) throws DAOException {
        logger.debug("查询文件存放信息 入参 info : {}", info);

        List<Map> businessFileRelInfos = sqlSessionTemplate.selectList("fileRelServiceDaoImpl.getFileRelInfo", info);

        return businessFileRelInfos;
    }


    /**
     * 修改文件存放信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateFileRelInfoInstance(Map info) throws DAOException {
        logger.debug("修改文件存放信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("fileRelServiceDaoImpl.updateFileRelInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改文件存放信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询文件存放数量
     *
     * @param info 文件存放信息
     * @return 文件存放数量
     */
    @Override
    public int queryFileRelsCount(Map info) {
        logger.debug("查询文件存放数据 入参 info : {}", info);

        List<Map> businessFileRelInfos = sqlSessionTemplate.selectList("fileRelServiceDaoImpl.queryFileRelsCount", info);
        if (businessFileRelInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessFileRelInfos.get(0).get("count").toString());
    }


}
