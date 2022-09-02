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

import com.java110.core.base.dao.BaseServiceDao;
import com.java110.core.log.LoggerFactory;
import com.java110.user.dao.IRoleCommunityV1ServiceDao;
import com.java110.utils.exception.DAOException;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 类表述：
 * add by 吴学文 at 2022-07-25 17:12:49 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Service("roleCommunityV1ServiceDaoImpl")
public class RoleCommunityV1ServiceDaoImpl extends BaseServiceDao implements IRoleCommunityV1ServiceDao {

    private static Logger logger = LoggerFactory.getLogger(RoleCommunityV1ServiceDaoImpl.class);


    /**
     * 保存项目授权信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public int saveRoleCommunityInfo(Map info) throws DAOException {
        logger.debug("保存 saveRoleCommunityInfo 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("roleCommunityV1ServiceDaoImpl.saveRoleCommunityInfo", info);

        return saveFlag;
    }


    /**
     * 查询项目授权信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getRoleCommunityInfo(Map info) throws DAOException {
        logger.debug("查询 getRoleCommunityInfo 入参 info : {}", info);

        List<Map> businessRoleCommunityInfos = sqlSessionTemplate.selectList("roleCommunityV1ServiceDaoImpl.getRoleCommunityInfo", info);

        return businessRoleCommunityInfos;
    }


    /**
     * 修改项目授权信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public int updateRoleCommunityInfo(Map info) throws DAOException {
        logger.debug("修改 updateRoleCommunityInfo 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("roleCommunityV1ServiceDaoImpl.updateRoleCommunityInfo", info);

        return saveFlag;
    }

    /**
     * 查询项目授权数量
     *
     * @param info 项目授权信息
     * @return 项目授权数量
     */
    @Override
    public int queryRoleCommunitysCount(Map info) {
        logger.debug("查询 queryRoleCommunitysCount 入参 info : {}", info);

        List<Map> businessRoleCommunityInfos = sqlSessionTemplate.selectList("roleCommunityV1ServiceDaoImpl.queryRoleCommunitysCount", info);
        if (businessRoleCommunityInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessRoleCommunityInfos.get(0).get("count").toString());
    }

    @Override
    public int queryRoleCommunitysNameCount(Map info) {
        logger.debug("查询 queryRoleCommunitysNameCount 入参 info : {}", info);

        List<Map> businessRoleCommunityInfos = sqlSessionTemplate.selectList("roleCommunityV1ServiceDaoImpl.queryRoleCommunitysNameCount", info);
        if (businessRoleCommunityInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessRoleCommunityInfos.get(0).get("count").toString());    }

    @Override
    public List<Map> queryRoleCommunitysName(Map info) {
        logger.debug("查询 getRoleCommunityInfo 入参 info : {}", info);

        List<Map> businessRoleCommunityInfos = sqlSessionTemplate.selectList("roleCommunityV1ServiceDaoImpl.queryRoleCommunitysName", info);

        return businessRoleCommunityInfos;
    }


}
