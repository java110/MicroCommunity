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
package com.java110.user.dao.impl;

import com.java110.utils.exception.DAOException;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.user.dao.IOwnerRoomRelV1ServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 类表述：
 * add by 吴学文 at 2021-09-14 18:57:35 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Service("ownerRoomRelV1ServiceDaoImpl")
public class OwnerRoomRelV1ServiceDaoImpl extends BaseServiceDao implements IOwnerRoomRelV1ServiceDao {

    private static Logger logger = LoggerFactory.getLogger(OwnerRoomRelV1ServiceDaoImpl.class);


    /**
     * 保存业主房屋关系表信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public int saveOwnerRoomRelInfo(Map info) throws DAOException {
        logger.debug("保存 saveOwnerRoomRelInfo 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("ownerRoomRelV1ServiceDaoImpl.saveOwnerRoomRelInfo", info);

        return saveFlag;
    }


    /**
     * 查询业主房屋关系表信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getOwnerRoomRelInfo(Map info) throws DAOException {
        logger.debug("查询 getOwnerRoomRelInfo 入参 info : {}", info);

        List<Map> businessOwnerRoomRelInfos = sqlSessionTemplate.selectList("ownerRoomRelV1ServiceDaoImpl.getOwnerRoomRelInfo", info);

        return businessOwnerRoomRelInfos;
    }


    /**
     * 修改业主房屋关系表信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public int updateOwnerRoomRelInfo(Map info) throws DAOException {
        logger.debug("修改 updateOwnerRoomRelInfo 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("ownerRoomRelV1ServiceDaoImpl.updateOwnerRoomRelInfo", info);

        return saveFlag;
    }

    /**
     * 查询业主房屋关系表数量
     *
     * @param info 业主房屋关系表信息
     * @return 业主房屋关系表数量
     */
    @Override
    public int queryOwnerRoomRelsCount(Map info) {
        logger.debug("查询 queryOwnerRoomRelsCount 入参 info : {}", info);

        List<Map> businessOwnerRoomRelInfos = sqlSessionTemplate.selectList("ownerRoomRelV1ServiceDaoImpl.queryOwnerRoomRelsCount", info);
        if (businessOwnerRoomRelInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessOwnerRoomRelInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> queryRoomCountByOwnerIds(Map info) {
        logger.debug("查询 queryRoomCountByOwnerIds 入参 info : {}", info);

        List<Map> result = sqlSessionTemplate.selectList("ownerRoomRelV1ServiceDaoImpl.queryRoomCountByOwnerIds", info);
        return result;
    }


}
