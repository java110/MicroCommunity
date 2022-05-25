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

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.fee.dao.IFeeComboV1ServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 类表述：
 * add by 吴学文 at 2022-05-07 15:31:18 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Service("feeComboV1ServiceDaoImpl")
public class FeeComboV1ServiceDaoImpl extends BaseServiceDao implements IFeeComboV1ServiceDao {

    private static Logger logger = LoggerFactory.getLogger(FeeComboV1ServiceDaoImpl.class);





    /**
     * 保存费用套餐信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public int saveFeeComboInfo(Map info) throws DAOException {
        logger.debug("保存 saveFeeComboInfo 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("feeComboV1ServiceDaoImpl.saveFeeComboInfo",info);

        return saveFlag;
    }


    /**
     * 查询费用套餐信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getFeeComboInfo(Map info) throws DAOException {
        logger.debug("查询 getFeeComboInfo 入参 info : {}",info);

        List<Map> businessFeeComboInfos = sqlSessionTemplate.selectList("feeComboV1ServiceDaoImpl.getFeeComboInfo",info);

        return businessFeeComboInfos;
    }


    /**
     * 修改费用套餐信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public int updateFeeComboInfo(Map info) throws DAOException {
        logger.debug("修改 updateFeeComboInfo 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("feeComboV1ServiceDaoImpl.updateFeeComboInfo",info);

        return saveFlag;
    }

     /**
     * 查询费用套餐数量
     * @param info 费用套餐信息
     * @return 费用套餐数量
     */
    @Override
    public int queryFeeCombosCount(Map info) {
        logger.debug("查询 queryFeeCombosCount 入参 info : {}",info);

        List<Map> businessFeeComboInfos = sqlSessionTemplate.selectList("feeComboV1ServiceDaoImpl.queryFeeCombosCount", info);
        if (businessFeeComboInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessFeeComboInfos.get(0).get("count").toString());
    }


}
