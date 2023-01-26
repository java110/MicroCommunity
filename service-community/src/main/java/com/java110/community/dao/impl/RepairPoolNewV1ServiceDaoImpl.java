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

import com.java110.utils.exception.DAOException;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.community.dao.IRepairPoolNewV1ServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 类表述：
 * add by 吴学文 at 2021-12-06 22:28:18 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Service("repairPoolNewV1ServiceDaoImpl")
public class RepairPoolNewV1ServiceDaoImpl extends BaseServiceDao implements IRepairPoolNewV1ServiceDao {

    private static Logger logger = LoggerFactory.getLogger(RepairPoolNewV1ServiceDaoImpl.class);





    /**
     * 保存费用明细信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public int saveRepairPoolNewInfo(Map info) throws DAOException {
        logger.debug("保存 saveRepairPoolNewInfo 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("repairPoolNewV1ServiceDaoImpl.saveRepairPoolNewInfo",info);

        return saveFlag;
    }


    /**
     * 查询费用明细信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getRepairPoolNewInfo(Map info) throws DAOException {
        logger.debug("查询 getRepairPoolNewInfo 入参 info : {}",info);

        List<Map> businessRepairPoolNewInfos = sqlSessionTemplate.selectList("repairPoolNewV1ServiceDaoImpl.getRepairPoolNewInfo",info);

        return businessRepairPoolNewInfos;
    }


    /**
     * 修改费用明细信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public int updateRepairPoolNewInfo(Map info) throws DAOException {
        logger.debug("修改 updateRepairPoolNewInfo 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("repairPoolNewV1ServiceDaoImpl.updateRepairPoolNewInfo",info);

        return saveFlag;
    }

     /**
     * 查询费用明细数量
     * @param info 费用明细信息
     * @return 费用明细数量
     */
    @Override
    public int queryRepairPoolNewsCount(Map info) {
        logger.debug("查询 queryRepairPoolNewsCount 入参 info : {}",info);

        List<Map> businessRepairPoolNewInfos = sqlSessionTemplate.selectList("repairPoolNewV1ServiceDaoImpl.queryRepairPoolNewsCount", info);
        if (businessRepairPoolNewInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessRepairPoolNewInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> queryRepairCountByOwnerTels(Map info) {
        logger.debug("查询 queryRepairCountByOwnerTels 入参 info : {}",info);

        List<Map> result = sqlSessionTemplate.selectList("repairPoolNewV1ServiceDaoImpl.queryRepairCountByOwnerTels", info);
        return result;
    }


}
