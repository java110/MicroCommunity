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
import com.java110.user.dao.IMenuGroupCatalogV1ServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 类表述：
 * add by 吴学文 at 2022-02-26 10:18:54 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Service("menuGroupCatalogV1ServiceDaoImpl")
public class MenuGroupCatalogV1ServiceDaoImpl extends BaseServiceDao implements IMenuGroupCatalogV1ServiceDao {

    private static Logger logger = LoggerFactory.getLogger(MenuGroupCatalogV1ServiceDaoImpl.class);





    /**
     * 保存菜单目录组信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public int saveMenuGroupCatalogInfo(Map info) throws DAOException {
        logger.debug("保存 saveMenuGroupCatalogInfo 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("menuGroupCatalogV1ServiceDaoImpl.saveMenuGroupCatalogInfo",info);

        return saveFlag;
    }


    /**
     * 查询菜单目录组信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getMenuGroupCatalogInfo(Map info) throws DAOException {
        logger.debug("查询 getMenuGroupCatalogInfo 入参 info : {}",info);

        List<Map> businessMenuGroupCatalogInfos = sqlSessionTemplate.selectList("menuGroupCatalogV1ServiceDaoImpl.getMenuGroupCatalogInfo",info);

        return businessMenuGroupCatalogInfos;
    }


    /**
     * 修改菜单目录组信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public int updateMenuGroupCatalogInfo(Map info) throws DAOException {
        logger.debug("修改 updateMenuGroupCatalogInfo 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("menuGroupCatalogV1ServiceDaoImpl.updateMenuGroupCatalogInfo",info);

        return saveFlag;
    }

     /**
     * 查询菜单目录组数量
     * @param info 菜单目录组信息
     * @return 菜单目录组数量
     */
    @Override
    public int queryMenuGroupCatalogsCount(Map info) {
        logger.debug("查询 queryMenuGroupCatalogsCount 入参 info : {}",info);

        List<Map> businessMenuGroupCatalogInfos = sqlSessionTemplate.selectList("menuGroupCatalogV1ServiceDaoImpl.queryMenuGroupCatalogsCount", info);
        if (businessMenuGroupCatalogInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessMenuGroupCatalogInfos.get(0).get("count").toString());
    }


}
