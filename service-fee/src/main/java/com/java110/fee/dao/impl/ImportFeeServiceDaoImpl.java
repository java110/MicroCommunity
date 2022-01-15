package com.java110.fee.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.fee.dao.IImportFeeServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 费用导入服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("importFeeServiceDaoImpl")
//@Transactional
public class ImportFeeServiceDaoImpl extends BaseServiceDao implements IImportFeeServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ImportFeeServiceDaoImpl.class);





    /**
     * 保存费用导入信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveImportFeeInfo(Map info) throws DAOException {
        logger.debug("保存费用导入信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("importFeeServiceDaoImpl.saveImportFeeInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存费用导入信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询费用导入信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getImportFeeInfo(Map info) throws DAOException {
        logger.debug("查询费用导入信息 入参 info : {}",info);

        List<Map> businessImportFeeInfos = sqlSessionTemplate.selectList("importFeeServiceDaoImpl.getImportFeeInfo",info);

        return businessImportFeeInfos;
    }


    /**
     * 修改费用导入信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateImportFeeInfo(Map info) throws DAOException {
        logger.debug("修改费用导入信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("importFeeServiceDaoImpl.updateImportFeeInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改费用导入信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询费用导入数量
     * @param info 费用导入信息
     * @return 费用导入数量
     */
    @Override
    public int queryImportFeesCount(Map info) {
        logger.debug("查询费用导入数据 入参 info : {}",info);

        List<Map> businessImportFeeInfos = sqlSessionTemplate.selectList("importFeeServiceDaoImpl.queryImportFeesCount", info);
        if (businessImportFeeInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessImportFeeInfos.get(0).get("count").toString());
    }


}
