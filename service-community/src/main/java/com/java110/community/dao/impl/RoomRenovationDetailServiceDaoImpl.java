package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.community.dao.IRoomRenovationDetailServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 装修明细服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("roomRenovationDetailServiceDaoImpl")
//@Transactional
public class RoomRenovationDetailServiceDaoImpl extends BaseServiceDao implements IRoomRenovationDetailServiceDao {

    private static Logger logger = LoggerFactory.getLogger(RoomRenovationDetailServiceDaoImpl.class);

    /**
     * 保存装修明细信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveRoomRenovationDetailInfo(Map info) throws DAOException {
        logger.debug("保存装修明细信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("roomRenovationDetailServiceDaoImpl.saveRoomRenovationDetailInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存装修明细信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询装修明细信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getRoomRenovationDetailInfo(Map info) throws DAOException {
        logger.debug("查询装修明细信息 入参 info : {}", info);

        List<Map> businessRoomRenovationDetailInfos = sqlSessionTemplate.selectList("roomRenovationDetailServiceDaoImpl.getRoomRenovationDetailInfo", info);

        return businessRoomRenovationDetailInfos;
    }


    /**
     * 修改装修明细信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateRoomRenovationDetailInfo(Map info) throws DAOException {
        logger.debug("修改装修明细信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("roomRenovationDetailServiceDaoImpl.updateRoomRenovationDetailInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改装修明细信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询装修明细数量
     *
     * @param info 装修明细信息
     * @return 装修明细数量
     */
    @Override
    public int queryRoomRenovationDetailsCount(Map info) {
        logger.debug("查询装修明细数据 入参 info : {}", info);

        List<Map> businessRoomRenovationDetailInfos = sqlSessionTemplate.selectList("roomRenovationDetailServiceDaoImpl.queryRoomRenovationDetailsCount", info);
        if (businessRoomRenovationDetailInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessRoomRenovationDetailInfos.get(0).get("count").toString());
    }


}
