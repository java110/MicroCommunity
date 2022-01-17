package com.java110.store.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.po.purchase.PurchaseApplyDetailPo;
import com.java110.store.dao.IPurchaseApplyServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.vo.api.purchaseApply.PurchaseApplyDetailVo;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 采购申请服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("purchaseApplyServiceDaoImpl")
//@Transactional
public class PurchaseApplyServiceDaoImpl extends BaseServiceDao implements IPurchaseApplyServiceDao {

    private static Logger logger = LoggerFactory.getLogger(PurchaseApplyServiceDaoImpl.class);

    /**
     * 采购申请信息封装
     *
     * @param businessPurchaseApplyInfo 采购申请信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessPurchaseApplyInfo(Map businessPurchaseApplyInfo) throws DAOException {
        businessPurchaseApplyInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存采购申请信息 入参 businessPurchaseApplyInfo : {}", businessPurchaseApplyInfo);
        int saveFlag = sqlSessionTemplate.insert("purchaseApplyServiceDaoImpl.saveBusinessPurchaseApplyInfo", businessPurchaseApplyInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存采购申请数据失败：" + JSONObject.toJSONString(businessPurchaseApplyInfo));
        }
    }

    @Override
    public void saveBusinessPurchaseApplyDetailInfo(List<PurchaseApplyDetailVo> list) throws DAOException {
        logger.debug("保存采购申请明细信息buiness表 入参 list : {}", list);
        int saveFlag = sqlSessionTemplate.insert("purchaseApplyServiceDaoImpl.saveBusinessPurchaseApplyDetailInfo", list);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存采购申请数据失败：" + JSONObject.toJSONString(list));
        }
    }

    @Override
    public int savePurchaseApplyDetailInfo(List<PurchaseApplyDetailPo> list) throws DAOException {
        logger.debug("保存采购申请明细信息 入参 list : {}", list);
        int saveFlag = sqlSessionTemplate.insert("purchaseApplyServiceDaoImpl.savePurchaseApplyDetailInfo", list);

        return saveFlag;
    }


    /**
     * 查询采购申请信息
     *
     * @param info bId 信息
     * @return 采购申请信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessPurchaseApplyInfo(Map info) throws DAOException {

        logger.debug("查询采购申请信息 入参 info : {}", info);

        List<Map> businessPurchaseApplyInfos = sqlSessionTemplate.selectList("purchaseApplyServiceDaoImpl.getBusinessPurchaseApplyInfo", info);

        return businessPurchaseApplyInfos;
    }

    @Override
    public List<Map> getBusinessPurchaseApplyDetailInfo(Map info) throws DAOException {
        logger.debug("查询采购申请明细信息 入参 info : {}", info);
        List<Map> businessPurchaseApplyDetailInfos = sqlSessionTemplate.selectList("purchaseApplyServiceDaoImpl.getBusinessPurchaseApplyDetailInfo", info);
        return businessPurchaseApplyDetailInfos;
    }


    /**
     * 保存采购申请信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void savePurchaseApplyInfoInstance(Map info) throws DAOException {
        logger.debug("保存采购申请信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("purchaseApplyServiceDaoImpl.savePurchaseApplyInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存采购申请信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询采购申请信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getPurchaseApplyInfo(Map info) throws DAOException {
        logger.debug("查询采购申请信息 入参 info : {}", info);

        List<Map> businessPurchaseApplyInfos = sqlSessionTemplate.selectList("purchaseApplyServiceDaoImpl.getPurchaseApplyInfo", info);

        return businessPurchaseApplyInfos;
    }

    @Override
    public List<PurchaseApplyDto> getPurchaseApplyInfo2(Map info) throws DAOException {
        logger.debug("查询采购申请信息 入参 info : {}", info);

        List<PurchaseApplyDto> businessPurchaseApplyInfos = sqlSessionTemplate.selectList("purchaseApplyServiceDaoImpl.getPurchaseApplyInfo2", info);

        return businessPurchaseApplyInfos;
    }

    @Override
    public List<Map> getPurchaseApplyDetailInfo(Map info) throws DAOException {
        logger.debug("查询采购申请明细信息 入参 info : {}", info);

        List<Map> businessPurchaseApplyDetailInfos = sqlSessionTemplate.selectList("purchaseApplyServiceDaoImpl.getPurchaseApplyDetailInfo", info);

        return businessPurchaseApplyDetailInfos;
    }


    /**
     * 修改采购申请信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updatePurchaseApplyInfoInstance(Map info) throws DAOException {
        logger.debug("修改采购申请信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("purchaseApplyServiceDaoImpl.updatePurchaseApplyInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改采购申请信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询采购申请数量
     *
     * @param info 采购申请信息
     * @return 采购申请数量
     */
    @Override
    public int queryPurchaseApplysCount(Map info) {
        logger.debug("查询采购申请数据 入参 info : {}", info);

        List<Map> businessPurchaseApplyInfos = sqlSessionTemplate.selectList("purchaseApplyServiceDaoImpl.queryPurchaseApplysCount", info);
        if (businessPurchaseApplyInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessPurchaseApplyInfos.get(0).get("count").toString());
    }

    @Override
    public int savePurchaseApply(Map purchaseApplyInfo) throws DAOException {
        logger.debug("保存采购申请信息入参 info : {}", purchaseApplyInfo);

        int saveFlag = sqlSessionTemplate.insert("purchaseApplyServiceDaoImpl.savePurchaseApply", purchaseApplyInfo);

        return saveFlag;
    }

    /**
     * 获得下级处理人id
     *
     * @param info
     * @return
     */
    @Override
    public List<Map> getActRuTaskUserId(Map info) {
        logger.debug("获得下级处理人id信息入参 info : {}", info);
        List<Map> purchaseApplys = sqlSessionTemplate.selectList("purchaseApplyServiceDaoImpl.getActRuTaskUserId", info);
        return purchaseApplys;
    }

    /**
     * getActRuTaskId
     *
     * @param info
     * @return
     */
    @Override
    public List<Map> getActRuTaskId(Map info) {
        logger.debug("获得获取流程任务id信息入参 info : {}", info);
        List<Map> purchaseApplys = sqlSessionTemplate.selectList("purchaseApplyServiceDaoImpl.getActRuTaskId", info);
        return purchaseApplys;
    }

    /**
     * 修改流程任务信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateActRuTaskById(Map info) throws DAOException {
        logger.debug("修改流程任务信息 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("purchaseApplyServiceDaoImpl.updateActRuTaskById", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改流程任务信息数据失败：" + JSONObject.toJSONString(info));
        }
    }


}
