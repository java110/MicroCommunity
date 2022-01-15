package com.java110.store.dao.businesstype;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.store.dao.ICbusinesstypeServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * cbusinesstype服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("cbusinesstypeServiceDaoImpl")
//@Transactional
public class CbusinesstypeServiceDaoImpl extends BaseServiceDao implements ICbusinesstypeServiceDao {

    private static Logger logger = LoggerFactory.getLogger(CbusinesstypeServiceDaoImpl.class);

    /**
     * cbusinesstype信息封装
     * @param businessCbusinesstypeInfo cbusinesstype信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessCbusinesstypeInfo(Map businessCbusinesstypeInfo) throws DAOException {
        businessCbusinesstypeInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存cbusinesstype信息 入参 businessCbusinesstypeInfo : {}",businessCbusinesstypeInfo);
        int saveFlag = sqlSessionTemplate.insert("cbusinesstypeServiceDaoImpl.saveBusinessCbusinesstypeInfo",businessCbusinesstypeInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存cbusinesstype数据失败："+ JSONObject.toJSONString(businessCbusinesstypeInfo));
        }
    }


    /**
     * 查询cbusinesstype信息
     * @param info bId 信息
     * @return cbusinesstype信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessCbusinesstypeInfo(Map info) throws DAOException {

        logger.debug("查询cbusinesstype信息 入参 info : {}",info);

        List<Map> businessCbusinesstypeInfos = sqlSessionTemplate.selectList("cbusinesstypeServiceDaoImpl.getBusinessCbusinesstypeInfo",info);

        return businessCbusinesstypeInfos;
    }



    /**
     * 保存cbusinesstype信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveCbusinesstypeInfoInstance(Map info) throws DAOException {
        logger.debug("保存cbusinesstype信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("cbusinesstypeServiceDaoImpl.saveCbusinesstypeInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存cbusinesstype信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询cbusinesstype信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getCbusinesstypeInfo(Map info) throws DAOException {
        logger.debug("查询cbusinesstype信息 入参 info : {}",info);

        List<Map> businessCbusinesstypeInfos = sqlSessionTemplate.selectList("cbusinesstypeServiceDaoImpl.getCbusinesstypeInfo",info);

        return businessCbusinesstypeInfos;
    }


    /**
     * 修改cbusinesstype信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateCbusinesstypeInfoInstance(Map info) throws DAOException {
        logger.debug("修改cbusinesstype信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("cbusinesstypeServiceDaoImpl.updateCbusinesstypeInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改cbusinesstype信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询cbusinesstype数量
     * @param info cbusinesstype信息
     * @return cbusinesstype数量
     */
    @Override
    public int queryCbusinesstypesCount(Map info) {
        logger.debug("查询cbusinesstype数据 入参 info : {}",info);

        List<Map> businessCbusinesstypeInfos = sqlSessionTemplate.selectList("cbusinesstypeServiceDaoImpl.queryCbusinesstypesCount", info);
        if (businessCbusinesstypeInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessCbusinesstypeInfos.get(0).get("count").toString());
    }


}
