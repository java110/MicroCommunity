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
import com.java110.user.dao.IOwnerV1ServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 类表述：
 * add by 吴学文 at 2021-09-14 18:56:16 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Service("ownerV1ServiceDaoImpl")
public class OwnerV1ServiceDaoImpl extends BaseServiceDao implements IOwnerV1ServiceDao {

    private static Logger logger = LoggerFactory.getLogger(OwnerV1ServiceDaoImpl.class);





    /**
     * 保存业主信息信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public int saveOwnerInfo(Map info) throws DAOException {
        logger.debug("保存 saveOwnerInfo 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("ownerV1ServiceDaoImpl.saveOwnerInfo",info);

        return saveFlag;
    }


    /**
     * 查询业主信息信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getOwnerInfo(Map info) throws DAOException {
        logger.debug("查询 getOwnerInfo 入参 info : {}",info);

        List<Map> businessOwnerInfos = sqlSessionTemplate.selectList("ownerV1ServiceDaoImpl.getOwnerInfo",info);

        return businessOwnerInfos;
    }


    /**
     * 修改业主信息信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public int updateOwnerInfo(Map info) throws DAOException {
        logger.debug("修改 updateOwnerInfo 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("ownerV1ServiceDaoImpl.updateOwnerInfo",info);

        return saveFlag;
    }

     /**
     * 查询业主信息数量
     * @param info 业主信息信息
     * @return 业主信息数量
     */
    @Override
    public int queryOwnersCount(Map info) {
        logger.debug("查询 queryOwnersCount 入参 info : {}",info);

        List<Map> businessOwnerInfos = sqlSessionTemplate.selectList("ownerV1ServiceDaoImpl.queryOwnersCount", info);
        if (businessOwnerInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessOwnerInfos.get(0).get("count").toString());
    }

    @Override
    public int queryOwnersBindCount(Map info) {
        logger.debug("查询 queryOwnersBindCount 入参 info : {}",info);

        List<Map> businessOwnerInfos = sqlSessionTemplate.selectList("ownerV1ServiceDaoImpl.queryOwnersBindCount", info);
        if (businessOwnerInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessOwnerInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> queryOwnerMembersCount(Map info) {
        logger.debug("查询 queryOwnerMembersCount 入参 info : {}",info);

        List<Map> result = sqlSessionTemplate.selectList("ownerV1ServiceDaoImpl.queryOwnerMembersCount", info);
        return result;
    }


}
