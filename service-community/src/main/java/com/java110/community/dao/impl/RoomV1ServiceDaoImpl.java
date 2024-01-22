/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.community.dao.IRoomV1ServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 类表述：
 * add by 吴学文 at 2021-09-14 14:28:56 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Service("roomV1ServiceDaoImpl")
public class RoomV1ServiceDaoImpl extends BaseServiceDao implements IRoomV1ServiceDao {

    private static Logger logger = LoggerFactory.getLogger(RoomV1ServiceDaoImpl.class);





    /**
     * 保存房屋信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public int saveRoomInfo(Map info) throws DAOException {
        logger.debug("保存 saveRoomInfo 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("roomV1ServiceDaoImpl.saveRoomInfo",info);

        return saveFlag;
    }


    /**
     * 查询房屋信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getRoomInfo(Map info) throws DAOException {
        logger.debug("查询 getRoomInfo 入参 info : {}",info);

        List<Map> infos = sqlSessionTemplate.selectList("roomV1ServiceDaoImpl.getRoomInfo",info);

        return infos;
    }


    /**
     * 修改房屋信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public int updateRoomInfo(Map info) throws DAOException {
        logger.debug("修改 updateRoomInfo 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("roomV1ServiceDaoImpl.updateRoomInfo",info);

        return saveFlag;
    }

     /**
     * 查询房屋数量
     * @param info 房屋信息
     * @return 房屋数量
     */
    @Override
    public int queryRoomsCount(Map info) {
        logger.debug("查询 queryRoomsCount 入参 info : {}",info);

        List<Map> businessRoomInfos = sqlSessionTemplate.selectList("roomV1ServiceDaoImpl.queryRoomsCount", info);
        if (businessRoomInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessRoomInfos.get(0).get("count").toString());
    }


}
