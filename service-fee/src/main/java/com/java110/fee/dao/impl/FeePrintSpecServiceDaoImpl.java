package com.java110.fee.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.fee.dao.IFeePrintSpecServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 打印说明服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("feePrintSpecServiceDaoImpl")
//@Transactional
public class FeePrintSpecServiceDaoImpl extends BaseServiceDao implements IFeePrintSpecServiceDao {

    private static Logger logger = LoggerFactory.getLogger(FeePrintSpecServiceDaoImpl.class);





    /**
     * 保存打印说明信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveFeePrintSpecInfo(Map info) throws DAOException {
        logger.debug("保存打印说明信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("feePrintSpecServiceDaoImpl.saveFeePrintSpecInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存打印说明信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询打印说明信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getFeePrintSpecInfo(Map info) throws DAOException {
        logger.debug("查询打印说明信息 入参 info : {}",info);

        List<Map> businessFeePrintSpecInfos = sqlSessionTemplate.selectList("feePrintSpecServiceDaoImpl.getFeePrintSpecInfo",info);

        return businessFeePrintSpecInfos;
    }


    /**
     * 修改打印说明信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateFeePrintSpecInfo(Map info) throws DAOException {
        logger.debug("修改打印说明信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("feePrintSpecServiceDaoImpl.updateFeePrintSpecInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改打印说明信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询打印说明数量
     * @param info 打印说明信息
     * @return 打印说明数量
     */
    @Override
    public int queryFeePrintSpecsCount(Map info) {
        logger.debug("查询打印说明数据 入参 info : {}",info);

        List<Map> businessFeePrintSpecInfos = sqlSessionTemplate.selectList("feePrintSpecServiceDaoImpl.queryFeePrintSpecsCount", info);
        if (businessFeePrintSpecInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessFeePrintSpecInfos.get(0).get("count").toString());
    }


}
