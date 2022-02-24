package com.java110.fee.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.fee.dao.IInitializePayFeeServiceDao;
import com.java110.fee.dao.IPayFeeAuditServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 缴费审核服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("initializePayFeeServiceDaoImpl")
//@Transactional
public class InitializePayFeeServiceDaoImpl extends BaseServiceDao implements IInitializePayFeeServiceDao {

    private static Logger logger = LoggerFactory.getLogger(InitializePayFeeServiceDaoImpl.class);



    public int deletePayFee(Map info) throws DAOException {
        logger.debug("删除费用信息 入参 info : {}", info);

        int deleteFlag = sqlSessionTemplate.delete("initializePayFeeServiceDaoImpl.deletePayFee", info);

        /*if (deleteFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "building_owner初始化失败：" + JSONObject.toJSONString(info));
        }*/
        return deleteFlag;
    }

}
