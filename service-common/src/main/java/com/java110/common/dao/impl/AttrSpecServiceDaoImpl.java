package com.java110.common.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.common.dao.IAttrSpecServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 属性规格表服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("attrSpecServiceDaoImpl")
//@Transactional
public class AttrSpecServiceDaoImpl extends BaseServiceDao implements IAttrSpecServiceDao {

    private static Logger logger = LoggerFactory.getLogger(AttrSpecServiceDaoImpl.class);





    /**
     * 保存属性规格表信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveAttrSpecInfo(Map info) throws DAOException {
        logger.debug("保存属性规格表信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("attrSpecServiceDaoImpl.saveAttrSpecInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存属性规格表信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询属性规格表信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getAttrSpecInfo(Map info) throws DAOException {
        logger.debug("查询属性规格表信息 入参 info : {}",info);

        List<Map> businessAttrSpecInfos = sqlSessionTemplate.selectList("attrSpecServiceDaoImpl.getAttrSpecInfo",info);

        return businessAttrSpecInfos;
    }


    /**
     * 修改属性规格表信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateAttrSpecInfo(Map info) throws DAOException {
        logger.debug("修改属性规格表信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("attrSpecServiceDaoImpl.updateAttrSpecInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改属性规格表信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询属性规格表数量
     * @param info 属性规格表信息
     * @return 属性规格表数量
     */
    @Override
    public int queryAttrSpecsCount(Map info) {
        logger.debug("查询属性规格表数据 入参 info : {}",info);

        List<Map> businessAttrSpecInfos = sqlSessionTemplate.selectList("attrSpecServiceDaoImpl.queryAttrSpecsCount", info);
        if (businessAttrSpecInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessAttrSpecInfos.get(0).get("count").toString());
    }


}
