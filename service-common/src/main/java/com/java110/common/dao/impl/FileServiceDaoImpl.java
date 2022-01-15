package com.java110.common.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.common.dao.IFileServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 应用服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("fileServiceDaoImpl")
//@Transactional
public class FileServiceDaoImpl extends BaseServiceDao implements IFileServiceDao {

    private static Logger logger = LoggerFactory.getLogger(FileServiceDaoImpl.class);

    /**
     * 应用信息封装
     * @param fileInfo 应用信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public int saveFile(Map fileInfo) throws DAOException {
        // 查询business_user 数据是否已经存在
        logger.debug("保存应用信息 入参 fileInfo : {}",fileInfo);
        int saveFlag = sqlSessionTemplate.insert("fileServiceDaoImpl.saveFile",fileInfo);

        return saveFlag;
    }


    /**
     * 查询文件信息
     * @param info bId 信息
     * @return 应用信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getFiles(Map info) throws DAOException {

        logger.debug("查询应用信息 入参 info : {}",info);

        List<Map> businessAppInfos = sqlSessionTemplate.selectList("fileServiceDaoImpl.getFiles",info);

        return businessAppInfos;
    }



}
