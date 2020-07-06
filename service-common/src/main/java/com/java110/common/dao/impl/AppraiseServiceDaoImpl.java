package com.java110.common.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IAppraiseServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 评价服务 数据操作层
 */
@Service("appraiseServiceDaoImpl")
public class AppraiseServiceDaoImpl extends BaseServiceDao implements IAppraiseServiceDao {
    @Override
    public int saveAppraise(Map info) {
        int saveFlag = sqlSessionTemplate.insert("appraiseServiceDaoImpl.saveAppraise", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存评价数据失败：" + JSONObject.toJSONString(info));
        }
        return saveFlag;
    }
}
