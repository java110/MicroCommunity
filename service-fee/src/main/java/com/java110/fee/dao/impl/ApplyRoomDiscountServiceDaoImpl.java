package com.java110.fee.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.fee.dao.IApplyRoomDiscountServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 房屋折扣申请服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("applyRoomDiscountServiceDaoImpl")
//@Transactional
public class ApplyRoomDiscountServiceDaoImpl extends BaseServiceDao implements IApplyRoomDiscountServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ApplyRoomDiscountServiceDaoImpl.class);


    /**
     * 保存房屋折扣申请信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveApplyRoomDiscountInfo(Map info) throws DAOException {
        logger.debug("保存房屋折扣申请信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("applyRoomDiscountServiceDaoImpl.saveApplyRoomDiscountInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存房屋折扣申请信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询房屋折扣申请信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getApplyRoomDiscountInfo(Map info) throws DAOException {
        logger.debug("查询房屋折扣申请信息 入参 info : {}", info);

        List<Map> businessApplyRoomDiscountInfos = sqlSessionTemplate.selectList("applyRoomDiscountServiceDaoImpl.getApplyRoomDiscountInfo", info);

        return businessApplyRoomDiscountInfos;
    }

    /**
     * 查询最新的符合条件的优惠申请信息
     *
     * @param info
     * @return
     */
    @Override
    public List<Map> queryFirstApplyRoomDiscounts(Map info) {
        logger.debug("查询房屋折扣申请信息 入参 info : {}", info);

        List<Map> businessApplyRoomDiscountInfos = sqlSessionTemplate.selectList("applyRoomDiscountServiceDaoImpl.getFirstApplyRoomDiscounts", info);

        return businessApplyRoomDiscountInfos;
    }


    /**
     * 修改房屋折扣申请信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateApplyRoomDiscountInfo(Map info) throws DAOException {
        logger.debug("修改房屋折扣申请信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("applyRoomDiscountServiceDaoImpl.updateApplyRoomDiscountInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改房屋折扣申请信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询房屋折扣申请数量
     *
     * @param info 房屋折扣申请信息
     * @return 房屋折扣申请数量
     */
    @Override
    public int queryApplyRoomDiscountsCount(Map info) {
        logger.debug("查询房屋折扣申请数据 入参 info : {}", info);

        List<Map> businessApplyRoomDiscountInfos = sqlSessionTemplate.selectList("applyRoomDiscountServiceDaoImpl.queryApplyRoomDiscountsCount", info);
        if (businessApplyRoomDiscountInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessApplyRoomDiscountInfos.get(0).get("count").toString());
    }


}
