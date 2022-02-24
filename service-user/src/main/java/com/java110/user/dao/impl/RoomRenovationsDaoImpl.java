package com.java110.user.dao.impl;

import com.java110.core.base.dao.BaseServiceDao;
import com.java110.user.dao.IRoomRenovationsServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 房屋装修
 *
 * @author fqz
 * @date 2021-02-25 8:52
 */
@Service("roomRenovationsServiceDaoImpl")
public class RoomRenovationsDaoImpl extends BaseServiceDao implements IRoomRenovationsServiceDao {

    private static Logger logger = LoggerFactory.getLogger(RoomRenovationsDaoImpl.class);

    /**
     * 查询装修申请信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     */
    @Override
    public List<Map> getRoomRenovationInfo(Map info) {
        logger.debug("查询装修申请信息 入参 info : {}", info);

        List<Map> businessRoomRenovationInfos = sqlSessionTemplate.selectList("roomRenovationsServiceDaoImpl.getRoomRenovationInfo", info);

        return businessRoomRenovationInfos;
    }

    /**
     * 查询装修申请数量
     *
     * @param info 装修申请信息
     * @return 装修申请数量
     */
    @Override
    public int queryRoomRenovationsCount(Map info) {
        logger.debug("查询装修申请数据 入参 info : {}", info);

        List<Map> businessRoomRenovationInfos = sqlSessionTemplate.selectList("roomRenovationsServiceDaoImpl.queryRoomRenovationsCount", info);
        if (businessRoomRenovationInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessRoomRenovationInfos.get(0).get("count").toString());
    }
}
