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
import com.java110.community.dao.IParkingSpaceV1ServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 类表述：
 * add by 吴学文 at 2022-04-11 13:21:22 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Service("parkingSpaceV1ServiceDaoImpl")
public class ParkingSpaceV1ServiceDaoImpl extends BaseServiceDao implements IParkingSpaceV1ServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ParkingSpaceV1ServiceDaoImpl.class);





    /**
     * 保存车位信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public int saveParkingSpaceInfo(Map info) throws DAOException {
        logger.debug("保存 saveParkingSpaceInfo 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("parkingSpaceV1ServiceDaoImpl.saveParkingSpaceInfo",info);

        return saveFlag;
    }


    /**
     * 查询车位信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getParkingSpaceInfo(Map info) throws DAOException {
        logger.debug("查询 getParkingSpaceInfo 入参 info : {}",info);

        List<Map> businessParkingSpaceInfos = sqlSessionTemplate.selectList("parkingSpaceV1ServiceDaoImpl.getParkingSpaceInfo",info);

        return businessParkingSpaceInfos;
    }


    /**
     * 修改车位信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public int updateParkingSpaceInfo(Map info) throws DAOException {
        logger.debug("修改 updateParkingSpaceInfo 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("parkingSpaceV1ServiceDaoImpl.updateParkingSpaceInfo",info);

        return saveFlag;
    }

     /**
     * 查询车位数量
     * @param info 车位信息
     * @return 车位数量
     */
    @Override
    public int queryParkingSpacesCount(Map info) {
        logger.debug("查询 queryParkingSpacesCount 入参 info : {}",info);

        List<Map> businessParkingSpaceInfos = sqlSessionTemplate.selectList("parkingSpaceV1ServiceDaoImpl.queryParkingSpacesCount", info);
        if (businessParkingSpaceInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessParkingSpaceInfos.get(0).get("count").toString());
    }

    /**
     * 保存车位信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public int saveParkingSpaceInfos(Map info) throws DAOException {
        logger.debug("保存 saveParkingSpaceInfos 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("parkingSpaceV1ServiceDaoImpl.saveParkingSpaceInfos",info);

        return saveFlag;
    }
}
