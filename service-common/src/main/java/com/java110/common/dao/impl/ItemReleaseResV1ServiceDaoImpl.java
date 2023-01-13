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
package com.java110.common.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.common.dao.IItemReleaseResV1ServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 类表述：
 * add by 吴学文 at 2023-01-11 15:49:03 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Service("itemReleaseResV1ServiceDaoImpl")
public class ItemReleaseResV1ServiceDaoImpl extends BaseServiceDao implements IItemReleaseResV1ServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ItemReleaseResV1ServiceDaoImpl.class);





    /**
     * 保存放行物品信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public int saveItemReleaseResInfo(Map info) throws DAOException {
        logger.debug("保存 saveItemReleaseResInfo 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("itemReleaseResV1ServiceDaoImpl.saveItemReleaseResInfo",info);

        return saveFlag;
    }


    @Override
    public int saveItemReleaseResesInfo(Map info) {
        logger.debug("保存 saveItemReleaseResesInfo 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("itemReleaseResV1ServiceDaoImpl.saveItemReleaseResesInfo",info);

        return saveFlag;
    }



    /**
     * 查询放行物品信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getItemReleaseResInfo(Map info) throws DAOException {
        logger.debug("查询 getItemReleaseResInfo 入参 info : {}",info);

        List<Map> businessItemReleaseResInfos = sqlSessionTemplate.selectList("itemReleaseResV1ServiceDaoImpl.getItemReleaseResInfo",info);

        return businessItemReleaseResInfos;
    }


    /**
     * 修改放行物品信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public int updateItemReleaseResInfo(Map info) throws DAOException {
        logger.debug("修改 updateItemReleaseResInfo 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("itemReleaseResV1ServiceDaoImpl.updateItemReleaseResInfo",info);

        return saveFlag;
    }

     /**
     * 查询放行物品数量
     * @param info 放行物品信息
     * @return 放行物品数量
     */
    @Override
    public int queryItemReleaseRessCount(Map info) {
        logger.debug("查询 queryItemReleaseRessCount 入参 info : {}",info);

        List<Map> businessItemReleaseResInfos = sqlSessionTemplate.selectList("itemReleaseResV1ServiceDaoImpl.queryItemReleaseRessCount", info);
        if (businessItemReleaseResInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessItemReleaseResInfos.get(0).get("count").toString());
    }


}
