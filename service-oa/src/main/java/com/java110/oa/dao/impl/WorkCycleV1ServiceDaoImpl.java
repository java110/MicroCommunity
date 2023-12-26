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
package com.java110.oa.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.oa.dao.IWorkCycleV1ServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 类表述：
 * add by 吴学文 at 2023-12-25 15:38:11 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Service("workCycleV1ServiceDaoImpl")
public class WorkCycleV1ServiceDaoImpl extends BaseServiceDao implements IWorkCycleV1ServiceDao {

    private static Logger logger = LoggerFactory.getLogger(WorkCycleV1ServiceDaoImpl.class);





    /**
     * 保存工作单周期信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public int saveWorkCycleInfo(Map info) throws DAOException {
        logger.debug("保存 saveWorkCycleInfo 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("workCycleV1ServiceDaoImpl.saveWorkCycleInfo",info);

        return saveFlag;
    }


    /**
     * 查询工作单周期信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getWorkCycleInfo(Map info) throws DAOException {
        logger.debug("查询 getWorkCycleInfo 入参 info : {}",info);

        List<Map> infos = sqlSessionTemplate.selectList("workCycleV1ServiceDaoImpl.getWorkCycleInfo",info);

        return infos;
    }


    /**
     * 修改工作单周期信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public int updateWorkCycleInfo(Map info) throws DAOException {
        logger.debug("修改 updateWorkCycleInfo 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("workCycleV1ServiceDaoImpl.updateWorkCycleInfo",info);

        return saveFlag;
    }

     /**
     * 查询工作单周期数量
     * @param info 工作单周期信息
     * @return 工作单周期数量
     */
    @Override
    public int queryWorkCyclesCount(Map info) {
        logger.debug("查询 queryWorkCyclesCount 入参 info : {}",info);

        List<Map> infos = sqlSessionTemplate.selectList("workCycleV1ServiceDaoImpl.queryWorkCyclesCount", info);
        if (infos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(infos.get(0).get("count").toString());
    }


}
