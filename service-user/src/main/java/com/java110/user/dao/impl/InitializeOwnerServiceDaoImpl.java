package com.java110.user.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.user.dao.IInitializeOwneServiceDao;
import com.java110.user.dao.IOwnerServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 业主服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("initializeOwnerServiceDaoImpl")
//@Transactional
public class InitializeOwnerServiceDaoImpl extends BaseServiceDao implements IInitializeOwneServiceDao {

    private static Logger logger = LoggerFactory.getLogger(InitializeOwnerServiceDaoImpl.class);



    public int deleteBuildingOwner(Map info) throws DAOException {
        logger.debug("删除业主信息 入参 info : {}", info);

        int deleteFlag = sqlSessionTemplate.delete("initializeOwnerServiceDaoImpl.deleteBuildingOwner", info);

    /*    if (deleteFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "building_owner初始化失败：" + JSONObject.toJSONString(info));
        }*/
        return deleteFlag;
    }
}
