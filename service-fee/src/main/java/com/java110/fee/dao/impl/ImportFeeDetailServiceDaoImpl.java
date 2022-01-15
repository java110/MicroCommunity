package com.java110.fee.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.fee.dao.IImportFeeDetailServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 费用导入明细服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("importFeeDetailServiceDaoImpl")
//@Transactional
public class ImportFeeDetailServiceDaoImpl extends BaseServiceDao implements IImportFeeDetailServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ImportFeeDetailServiceDaoImpl.class);


    /**
     * 保存费用导入明细信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveImportFeeDetailInfo(Map info) throws DAOException {
        logger.debug("保存费用导入明细信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("importFeeDetailServiceDaoImpl.saveImportFeeDetailInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存费用导入明细信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public void saveImportFeeDetails(Map info) throws DAOException {
        logger.debug("saveImportFeeDetails 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("importFeeDetailServiceDaoImpl.saveImportFeeDetails", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存费用导入明细信息数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询费用导入明细信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getImportFeeDetailInfo(Map info) throws DAOException {
        logger.debug("查询费用导入明细信息 入参 info : {}", info);

        List<Map> businessImportFeeDetailInfos = sqlSessionTemplate.selectList("importFeeDetailServiceDaoImpl.getImportFeeDetailInfo", info);

        return businessImportFeeDetailInfos;
    }


    /**
     * 修改费用导入明细信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateImportFeeDetailInfo(Map info) throws DAOException {
        logger.debug("修改费用导入明细信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("importFeeDetailServiceDaoImpl.updateImportFeeDetailInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改费用导入明细信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询费用导入明细数量
     *
     * @param info 费用导入明细信息
     * @return 费用导入明细数量
     */
    @Override
    public int queryImportFeeDetailsCount(Map info) {
        logger.debug("查询费用导入明细数据 入参 info : {}", info);

        List<Map> businessImportFeeDetailInfos = sqlSessionTemplate.selectList("importFeeDetailServiceDaoImpl.queryImportFeeDetailsCount", info);
        if (businessImportFeeDetailInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessImportFeeDetailInfos.get(0).get("count").toString());
    }


}
