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
package com.java110.fee.dao.impl;

import com.java110.utils.exception.DAOException;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.fee.dao.IPayFeeDetailNewV1ServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 类表述：
 * add by 吴学文 at 2021-12-06 21:10:16 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Service("payFeeDetailNewV1ServiceDaoImpl")
public class PayFeeDetailNewV1ServiceDaoImpl extends BaseServiceDao implements IPayFeeDetailNewV1ServiceDao {

    private static Logger logger = LoggerFactory.getLogger(PayFeeDetailNewV1ServiceDaoImpl.class);

    /**
     * 保存费用明细信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public int savePayFeeDetailNewInfo(Map info) throws DAOException {
        logger.debug("保存 savePayFeeDetailNewInfo 入参 info : {}", info);
        int saveFlag = sqlSessionTemplate.insert("payFeeDetailNewV1ServiceDaoImpl.savePayFeeDetailNewInfo", info);
        return saveFlag;
    }

    /**
     * 查询费用明细信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getPayFeeDetailNewInfo(Map info) throws DAOException {
        logger.debug("查询 getPayFeeDetailNewInfo 入参 info : {}", info);
        List<Map> businessPayFeeDetailNewInfos = sqlSessionTemplate.selectList("payFeeDetailNewV1ServiceDaoImpl.getPayFeeDetailNewInfo", info);
        return businessPayFeeDetailNewInfos;
    }

    /**
     * 修改费用明细信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public int updatePayFeeDetailNewInfo(Map info) throws DAOException {
        logger.debug("修改 updatePayFeeDetailNewInfo 入参 info : {}", info);
        int saveFlag = sqlSessionTemplate.update("payFeeDetailNewV1ServiceDaoImpl.updatePayFeeDetailNewInfo", info);
        return saveFlag;
    }

    /**
     * 查询费用明细数量
     *
     * @param info 费用明细信息
     * @return 费用明细数量
     */
    @Override
    public int queryPayFeeDetailNewsCount(Map info) {
        logger.debug("查询 queryPayFeeDetailNewsCount 入参 info : {}", info);
        List<Map> businessPayFeeDetailNewInfos = sqlSessionTemplate.selectList("payFeeDetailNewV1ServiceDaoImpl.queryPayFeeDetailNewsCount", info);
        if (businessPayFeeDetailNewInfos.size() < 1) {
            return 0;
        }
        return Integer.parseInt(businessPayFeeDetailNewInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> queryPayFeeDetailNewInfo(Map info) {
        logger.debug("查询 queryPayFeeDetailNewInfo 入参 info : {}", info);
        List<Map> businessPayFeeDetailNewInfos = sqlSessionTemplate.selectList("payFeeDetailNewV1ServiceDaoImpl.queryPayFeeDetailNewInfo", info);
        return businessPayFeeDetailNewInfos;
    }

    @Override
    public int queryPayFeeDetailNewCount(Map info) {
        logger.debug("查询 queryPayFeeDetailNewCount 入参 info : {}", info);
        List<Map> businessPayFeeDetailNewInfos = sqlSessionTemplate.selectList("payFeeDetailNewV1ServiceDaoImpl.queryPayFeeDetailNewCount", info);
        if (businessPayFeeDetailNewInfos.size() < 1) {
            return 0;
        }
        return Integer.parseInt(businessPayFeeDetailNewInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> queryPayFeeDetailNewSumInfo(Map info) {
        logger.debug("查询 queryPayFeeDetailNewSumInfo 入参 info : {}", info);
        List<Map> businessPayFeeDetailNewInfos = sqlSessionTemplate.selectList("payFeeDetailNewV1ServiceDaoImpl.queryPayFeeDetailNewSumInfo", info);
        return businessPayFeeDetailNewInfos;
    }
}
