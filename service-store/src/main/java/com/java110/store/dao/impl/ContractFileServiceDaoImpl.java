package com.java110.store.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.store.dao.IContractFileServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 合同附件服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("contractFileServiceDaoImpl")
//@Transactional
public class ContractFileServiceDaoImpl extends BaseServiceDao implements IContractFileServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ContractFileServiceDaoImpl.class);





    /**
     * 保存合同附件信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveContractFileInfo(Map info) throws DAOException {
        logger.debug("保存合同附件信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("contractFileServiceDaoImpl.saveContractFileInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存合同附件信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询合同附件信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getContractFileInfo(Map info) throws DAOException {
        logger.debug("查询合同附件信息 入参 info : {}",info);

        List<Map> businessContractFileInfos = sqlSessionTemplate.selectList("contractFileServiceDaoImpl.getContractFileInfo",info);

        return businessContractFileInfos;
    }


    /**
     * 修改合同附件信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateContractFileInfo(Map info) throws DAOException {
        logger.debug("修改合同附件信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("contractFileServiceDaoImpl.updateContractFileInfo",info);

/*        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改合同附件信息Instance数据失败："+ JSONObject.toJSONString(info));
        }*/
    }

     /**
     * 查询合同附件数量
     * @param info 合同附件信息
     * @return 合同附件数量
     */
    @Override
    public int queryContractFilesCount(Map info) {
        logger.debug("查询合同附件数据 入参 info : {}",info);

        List<Map> businessContractFileInfos = sqlSessionTemplate.selectList("contractFileServiceDaoImpl.queryContractFilesCount", info);
        if (businessContractFileInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessContractFileInfos.get(0).get("count").toString());
    }


}
