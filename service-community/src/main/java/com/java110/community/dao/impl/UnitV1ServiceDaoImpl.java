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
import com.java110.community.dao.IUnitV1ServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 类表述：
 * add by 吴学文 at 2021-09-14 18:42:46 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Service("unitV1ServiceDaoImpl")
public class UnitV1ServiceDaoImpl extends BaseServiceDao implements IUnitV1ServiceDao {

    private static Logger logger = LoggerFactory.getLogger(UnitV1ServiceDaoImpl.class);





    /**
     * 保存单元新信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public int saveUnitInfo(Map info) throws DAOException {
        logger.debug("保存 saveUnitInfo 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("unitV1ServiceDaoImpl.saveUnitInfo",info);

        return saveFlag;
    }


    /**
     * 查询单元新信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getUnitInfo(Map info) throws DAOException {
        logger.debug("查询 getUnitInfo 入参 info : {}",info);

        List<Map> businessUnitInfos = sqlSessionTemplate.selectList("unitV1ServiceDaoImpl.getUnitInfo",info);

        return businessUnitInfos;
    }


    /**
     * 修改单元新信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public int updateUnitInfo(Map info) throws DAOException {
        logger.debug("修改 updateUnitInfo 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("unitV1ServiceDaoImpl.updateUnitInfo",info);

        return saveFlag;
    }

     /**
     * 查询单元新数量
     * @param info 单元新信息
     * @return 单元新数量
     */
    @Override
    public int queryUnitsCount(Map info) {
        logger.debug("查询 queryUnitsCount 入参 info : {}",info);

        List<Map> businessUnitInfos = sqlSessionTemplate.selectList("unitV1ServiceDaoImpl.queryUnitsCount", info);
        if (businessUnitInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessUnitInfos.get(0).get("count").toString());
    }


}
