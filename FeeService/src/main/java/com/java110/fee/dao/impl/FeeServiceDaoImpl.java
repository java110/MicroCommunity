package com.java110.fee.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.exception.DAOException;
import com.java110.common.util.DateUtil;
import com.java110.fee.dao.IFeeServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 小区服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("feeServiceDaoImpl")
//@Transactional
public class FeeServiceDaoImpl extends BaseServiceDao implements IFeeServiceDao {

    private static Logger logger = LoggerFactory.getLogger(FeeServiceDaoImpl.class);

    /**
     * 小区信息封装
     *
     * @param businessFeeInfo 小区信息 封装
     * @throws DAOException
     */
    @Override
    public void saveBusinessFeeInfo(Map businessFeeInfo) throws DAOException {
        businessFeeInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存小区信息 入参 businessFeeInfo : {}", businessFeeInfo);
        int saveFlag = sqlSessionTemplate.insert("feeServiceDaoImpl.saveBusinessFeeInfo", businessFeeInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存小区数据失败：" + JSONObject.toJSONString(businessFeeInfo));
        }
    }

    /**
     * 小区属性信息分装
     *
     * @param businessFeeAttr 小区属性信息封装
     * @throws DAOException
     */
    @Override
    public void saveBusinessFeeAttr(Map businessFeeAttr) throws DAOException {
        businessFeeAttr.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存小区属性信息 入参 businessFeeAttr : {}", businessFeeAttr);

        int saveFlag = sqlSessionTemplate.insert("feeServiceDaoImpl.saveBusinessFeeAttr", businessFeeAttr);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存小区属性数据失败：" + JSONObject.toJSONString(businessFeeAttr));
        }
    }

    /**
     * 保存小区照片信息
     *
     * @param businessFeePhoto 小区照片
     * @throws DAOException
     */
    @Override
    public void saveBusinessFeePhoto(Map businessFeePhoto) throws DAOException {
        businessFeePhoto.put("month", DateUtil.getCurrentMonth());
        logger.debug("保存小区照片信息 入参 businessFeePhoto : {}", businessFeePhoto);

        int saveFlag = sqlSessionTemplate.insert("feeServiceDaoImpl.saveBusinessFeePhoto", businessFeePhoto);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存小区照片数据失败：" + JSONObject.toJSONString(businessFeePhoto));
        }
    }

    /**
     * 保存小区证件信息
     *
     * @param businessFeeCerdentials 小区证件
     * @throws DAOException
     */
    @Override
    public void saveBusinessFeeCerdentials(Map businessFeeCerdentials) throws DAOException {
        businessFeeCerdentials.put("month", DateUtil.getCurrentMonth());
        logger.debug("保存小区证件信息 入参 businessFeeCerdentials : {}", businessFeeCerdentials);

        int saveFlag = sqlSessionTemplate.insert("feeServiceDaoImpl.saveBusinessFeeCerdentials", businessFeeCerdentials);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存小区证件数据失败：" + JSONObject.toJSONString(businessFeeCerdentials));
        }
    }

    /**
     * 查询小区信息
     *
     * @param info bId 信息
     * @return 小区信息
     * @throws DAOException
     */
    @Override
    public Map getBusinessFeeInfo(Map info) throws DAOException {

        logger.debug("查询小区信息 入参 info : {}", info);

        List<Map> businessFeeInfos = sqlSessionTemplate.selectList("feeServiceDaoImpl.getBusinessFeeInfo", info);
        if (businessFeeInfos == null) {
            return null;
        }
        if (businessFeeInfos.size() > 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "根据条件查询有多条数据,数据异常，请检查：businessFeeInfos，" + JSONObject.toJSONString(info));
        }

        return businessFeeInfos.get(0);
    }

    /**
     * 查询小区属性
     *
     * @param info bId 信息
     * @return 小区属性
     * @throws DAOException
     */
    @Override
    public List<Map> getBusinessFeeAttrs(Map info) throws DAOException {
        logger.debug("查询小区属性信息 入参 info : {}", info);

        List<Map> businessFeeAttrs = sqlSessionTemplate.selectList("feeServiceDaoImpl.getBusinessFeeAttrs", info);

        return businessFeeAttrs;
    }

    /**
     * 查询小区照片
     *
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> getBusinessFeePhoto(Map info) throws DAOException {
        logger.debug("查询小区照片信息 入参 info : {}", info);

        List<Map> businessFeePhotos = sqlSessionTemplate.selectList("feeServiceDaoImpl.getBusinessFeePhoto", info);

        return businessFeePhotos;
    }

    /**
     * 查询小区证件
     *
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> getBusinessFeeCerdentials(Map info) throws DAOException {
        logger.debug("查询小区证件信息 入参 info : {}", info);

        List<Map> businessFeeCerdentialses = sqlSessionTemplate.selectList("feeServiceDaoImpl.getBusinessFeeCerdentials", info);

        return businessFeeCerdentialses;
    }

    /**
     * 保存小区信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException
     */
    @Override
    public void saveFeeInfoInstance(Map info) throws DAOException {
        logger.debug("保存小区信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("feeServiceDaoImpl.saveFeeInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存小区信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public void saveFeeAttrsInstance(Map info) throws DAOException {
        logger.debug("保存小区属性信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("feeServiceDaoImpl.saveFeeAttrsInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存小区属性信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public void saveFeePhotoInstance(Map info) throws DAOException {
        logger.debug("保存小区照片信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("feeServiceDaoImpl.saveFeePhotoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存小区照片信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询小区信息（instance）
     *
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public Map getFeeInfo(Map info) throws DAOException {
        logger.debug("查询小区信息 入参 info : {}", info);

        List<Map> businessFeeInfos = sqlSessionTemplate.selectList("feeServiceDaoImpl.getFeeInfo", info);
        if (businessFeeInfos == null || businessFeeInfos.size() == 0) {
            return null;
        }
        if (businessFeeInfos.size() > 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "根据条件查询有多条数据,数据异常，请检查：getFeeInfo，" + JSONObject.toJSONString(info));
        }

        return businessFeeInfos.get(0);
    }

    /**
     * 小区属性查询（instance）
     *
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> getFeeAttrs(Map info) throws DAOException {
        logger.debug("查询小区属性信息 入参 info : {}", info);

        List<Map> feeAttrs = sqlSessionTemplate.selectList("feeServiceDaoImpl.getFeeAttrs", info);

        return feeAttrs;
    }

    /**
     * 小区照片查询（instance）
     *
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> getFeePhoto(Map info) throws DAOException {
        logger.debug("查询小区照片信息 入参 info : {}", info);

        List<Map> feePhotos = sqlSessionTemplate.selectList("feeServiceDaoImpl.getFeePhoto", info);

        return feePhotos;
    }


    /**
     * 修改小区信息
     *
     * @param info 修改信息
     * @throws DAOException
     */
    @Override
    public void updateFeeInfoInstance(Map info) throws DAOException {
        logger.debug("修改小区信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("feeServiceDaoImpl.updateFeeInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改小区信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 修改小区属性信息（instance）
     *
     * @param info 修改信息
     * @throws DAOException
     */
    @Override
    public void updateFeeAttrInstance(Map info) throws DAOException {
        logger.debug("修改小区属性信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("feeServiceDaoImpl.updateFeeAttrInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改小区属性信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 修改 小区照片信息
     *
     * @param info 修改信息
     * @throws DAOException
     */
    @Override
    public void updateFeePhotoInstance(Map info) throws DAOException {
        logger.debug("修改小区照片信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("feeServiceDaoImpl.updateFeePhotoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改小区照片信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 小区成员加入信息
     *
     * @param businessFeeMember 小区成员信息 封装
     * @throws DAOException 操作数据库异常
     */
    public void saveBusinessFeeMember(Map businessFeeMember) throws DAOException {
        logger.debug("小区成员加入 入参 businessFeeMember : {}", businessFeeMember);
        businessFeeMember.put("month", DateUtil.getCurrentMonth());
        int saveFlag = sqlSessionTemplate.insert("feeServiceDaoImpl.saveBusinessFeeMember", businessFeeMember);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "小区成员加入失败：" + JSONObject.toJSONString(businessFeeMember));
        }
    }

    /**
     * 成员加入 保存信息至instance
     *
     * @param info
     * @throws DAOException
     */
    @Override
    public void saveFeeMemberInstance(Map info) throws DAOException {
        logger.debug("小区成员加入Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("feeServiceDaoImpl.saveFeeMemberInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存小区照片信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询小区成员加入信息（business过程）
     * 根据bId 查询小区信息
     *
     * @param info bId 信息
     * @return 小区信息
     * @throws DAOException
     */
    public Map getBusinessFeeMember(Map info) throws DAOException {
        logger.debug("查询小区成员加入信息 入参 info : {}", info);

        List<Map> businessFeeMembers = sqlSessionTemplate.selectList("feeServiceDaoImpl.getBusinessFeeMember", info);
        if (businessFeeMembers == null || businessFeeMembers.size() == 0) {
            return null;
        }
        if (businessFeeMembers.size() > 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "根据条件查询有多条数据,数据异常，请检查：businessFeeMember，" + JSONObject.toJSONString(info));
        }

        return businessFeeMembers.get(0);
    }

    /**
     * 查询小区成员加入信息（instance过程）
     * 根据bId 查询小区信息
     *
     * @param info bId 信息
     * @return 小区信息
     * @throws DAOException
     */
    public Map getFeeMember(Map info) throws DAOException {
        logger.debug("查询小区成员加入信息 入参 info : {}", info);

        List<Map> memberFees = sqlSessionTemplate.selectList("feeServiceDaoImpl.getFeeMember", info);
        if (memberFees == null || memberFees.size() == 0) {
            return null;
        }
        if (memberFees.size() > 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "根据条件查询有多条数据,数据异常，请检查：getFeeMember，" + JSONObject.toJSONString(info));
        }

        return memberFees.get(0);
    }

    /**
     * 修改小区成员加入信息
     *
     * @param info 修改信息
     * @throws DAOException
     */
    public void updateFeeMemberInstance(Map info) throws DAOException {
        logger.debug("修改小区成员加入信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("feeServiceDaoImpl.updateFeeMemberInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改小区成员加入信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> getFeeMembers(Map info) throws DAOException {
        logger.debug("查询小区成员加入信息 入参 info : {}", info);

        List<Map> memberFees = sqlSessionTemplate.selectList("feeServiceDaoImpl.getFeeMember", info);

        return memberFees;
    }

    @Override
    public int getFeeMemberCount(Map info) {
        logger.debug("查询小区成员加入信息 入参 info : {}", info);

        List<Map> memberFees = sqlSessionTemplate.selectList("feeServiceDaoImpl.getFeeMemberCount", info);

        if (memberFees.size() < 1) {
            return 0;
        }

        return Integer.parseInt(memberFees.get(0).get("count").toString());
    }
}
