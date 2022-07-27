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

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.user.dao.IPrivilegeUserV1ServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 类表述：
 * add by 吴学文 at 2022-02-28 17:59:22 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Service("privilegeUserV1ServiceDaoImpl")
public class PrivilegeUserV1ServiceDaoImpl extends BaseServiceDao implements IPrivilegeUserV1ServiceDao {

    private static Logger logger = LoggerFactory.getLogger(PrivilegeUserV1ServiceDaoImpl.class);





    /**
     * 保存用户权限信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public int savePrivilegeUserInfo(Map info) throws DAOException {
        logger.debug("保存 savePrivilegeUserInfo 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("privilegeUserV1ServiceDaoImpl.savePrivilegeUserInfo",info);

        return saveFlag;
    }


    /**
     * 查询用户权限信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getPrivilegeUserInfo(Map info) throws DAOException {
        logger.debug("查询 getPrivilegeUserInfo 入参 info : {}",info);

        List<Map> businessPrivilegeUserInfos = sqlSessionTemplate.selectList("privilegeUserV1ServiceDaoImpl.getPrivilegeUserInfo",info);

        return businessPrivilegeUserInfos;
    }


    /**
     * 修改用户权限信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public int updatePrivilegeUserInfo(Map info) throws DAOException {
        logger.debug("修改 updatePrivilegeUserInfo 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("privilegeUserV1ServiceDaoImpl.updatePrivilegeUserInfo",info);

        return saveFlag;
    }

     /**
     * 查询用户权限数量
     * @param info 用户权限信息
     * @return 用户权限数量
     */
    @Override
    public int queryPrivilegeUsersCount(Map info) {
        logger.debug("查询 queryPrivilegeUsersCount 入参 info : {}",info);

        List<Map> businessPrivilegeUserInfos = sqlSessionTemplate.selectList("privilegeUserV1ServiceDaoImpl.queryPrivilegeUsersCount", info);
        if (businessPrivilegeUserInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessPrivilegeUserInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> queryPrivilegeUserInfos(Map info) {
        logger.debug("查询 getPrivilegeUserInfo 入参 info : {}",info);

        List<Map> businessPrivilegeUserInfos = sqlSessionTemplate.selectList("privilegeUserV1ServiceDaoImpl.queryPrivilegeUserInfos",info);

        return businessPrivilegeUserInfos;
    }

    @Override
    public int queryPrivilegeUserInfoCount(Map info) {
        logger.debug("查询 queryPrivilegeUsersCount 入参 info : {}",info);

        List<Map> businessPrivilegeUserInfos = sqlSessionTemplate.selectList("privilegeUserV1ServiceDaoImpl.queryPrivilegeUserInfoCount", info);
        if (businessPrivilegeUserInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessPrivilegeUserInfos.get(0).get("count").toString());
    }

    @Override
    public int queryStaffsNoRoleCount(Map info) {
        logger.debug("查询 queryStaffsNoRoleCount 入参 info : {}",info);

        List<Map> businessPrivilegeUserInfos = sqlSessionTemplate.selectList("privilegeUserV1ServiceDaoImpl.queryStaffsNoRoleCount", info);
        if (businessPrivilegeUserInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessPrivilegeUserInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> queryStaffsNoRoleInfos(Map info) {
        logger.debug("查询 queryStaffsNoRoleInfos 入参 info : {}",info);

        List<Map> businessPrivilegeUserInfos = sqlSessionTemplate.selectList("privilegeUserV1ServiceDaoImpl.queryStaffsNoRoleInfos",info);

        return businessPrivilegeUserInfos;
    }


}
