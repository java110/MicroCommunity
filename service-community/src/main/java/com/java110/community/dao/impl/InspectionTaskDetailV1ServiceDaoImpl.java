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
import com.java110.community.dao.IInspectionTaskDetailV1ServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 类表述：
 * add by 吴学文 at 2022-07-04 09:27:52 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Service("inspectionTaskDetailV1ServiceDaoImpl")
public class InspectionTaskDetailV1ServiceDaoImpl extends BaseServiceDao implements IInspectionTaskDetailV1ServiceDao {

    private static Logger logger = LoggerFactory.getLogger(InspectionTaskDetailV1ServiceDaoImpl.class);





    /**
     * 保存巡检明细信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public int saveInspectionTaskDetailInfo(Map info) throws DAOException {
        logger.debug("保存 saveInspectionTaskDetailInfo 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("inspectionTaskDetailV1ServiceDaoImpl.saveInspectionTaskDetailInfo",info);

        return saveFlag;
    }


    /**
     * 查询巡检明细信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getInspectionTaskDetailInfo(Map info) throws DAOException {
        logger.debug("查询 getInspectionTaskDetailInfo 入参 info : {}",info);

        List<Map> businessInspectionTaskDetailInfos = sqlSessionTemplate.selectList("inspectionTaskDetailV1ServiceDaoImpl.getInspectionTaskDetailInfo",info);

        return businessInspectionTaskDetailInfos;
    }


    /**
     * 修改巡检明细信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public int updateInspectionTaskDetailInfo(Map info) throws DAOException {
        logger.debug("修改 updateInspectionTaskDetailInfo 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("inspectionTaskDetailV1ServiceDaoImpl.updateInspectionTaskDetailInfo",info);

        return saveFlag;
    }

     /**
     * 查询巡检明细数量
     * @param info 巡检明细信息
     * @return 巡检明细数量
     */
    @Override
    public int queryInspectionTaskDetailsCount(Map info) {
        logger.debug("查询 queryInspectionTaskDetailsCount 入参 info : {}",info);

        List<Map> businessInspectionTaskDetailInfos = sqlSessionTemplate.selectList("inspectionTaskDetailV1ServiceDaoImpl.queryInspectionTaskDetailsCount", info);
        if (businessInspectionTaskDetailInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessInspectionTaskDetailInfos.get(0).get("count").toString());
    }


}
