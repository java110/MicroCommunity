package com.java110.store.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.store.dao.IStoreServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 商户服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("storeServiceDaoImpl")
//@Transactional
public class StoreServiceDaoImpl extends BaseServiceDao implements IStoreServiceDao {

    private final static Logger logger = LoggerFactory.getLogger(StoreServiceDaoImpl.class);

    /**
     * 商户信息封装
     *
     * @param businessStoreInfo 商户信息 封装
     * @throws DAOException
     */
    @Override
    public void saveBusinessStoreInfo(Map businessStoreInfo) throws DAOException {
        businessStoreInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存商户信息 入参 businessStoreInfo : {}", businessStoreInfo);
        int saveFlag = sqlSessionTemplate.insert("storeServiceDaoImpl.saveBusinessStoreInfo", businessStoreInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存商户数据失败：" + JSONObject.toJSONString(businessStoreInfo));
        }
    }

    /**
     * 商户属性信息分装
     *
     * @param businessStoreAttr 商户属性信息封装
     * @throws DAOException
     */
    @Override
    public void saveBusinessStoreAttr(Map businessStoreAttr) throws DAOException {
        businessStoreAttr.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存商户属性信息 入参 businessStoreAttr : {}", businessStoreAttr);

        int saveFlag = sqlSessionTemplate.insert("storeServiceDaoImpl.saveBusinessStoreAttr", businessStoreAttr);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存商户属性数据失败：" + JSONObject.toJSONString(businessStoreAttr));
        }
    }

    /**
     * 保存商户照片信息
     *
     * @param businessStorePhoto 商户照片
     * @throws DAOException
     */
    @Override
    public void saveBusinessStorePhoto(Map businessStorePhoto) throws DAOException {
        businessStorePhoto.put("month", DateUtil.getCurrentMonth());
        logger.debug("保存商户照片信息 入参 businessStorePhoto : {}", businessStorePhoto);

        int saveFlag = sqlSessionTemplate.insert("storeServiceDaoImpl.saveBusinessStorePhoto", businessStorePhoto);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存商户照片数据失败：" + JSONObject.toJSONString(businessStorePhoto));
        }
    }

    /**
     * 保存商户证件信息
     *
     * @param businessStoreCerdentials 商户证件
     * @throws DAOException
     */
    @Override
    public void saveBusinessStoreCerdentials(Map businessStoreCerdentials) throws DAOException {
        businessStoreCerdentials.put("month", DateUtil.getCurrentMonth());
        logger.debug("保存商户证件信息 入参 businessStoreCerdentials : {}", businessStoreCerdentials);

        int saveFlag = sqlSessionTemplate.insert("storeServiceDaoImpl.saveBusinessStoreCerdentials", businessStoreCerdentials);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存商户证件数据失败：" + JSONObject.toJSONString(businessStoreCerdentials));
        }
    }

    /**
     * 查询商户信息
     *
     * @param info bId 信息
     * @return 商户信息
     * @throws DAOException
     */
    @Override
    public Map getBusinessStoreInfo(Map info) throws DAOException {

        logger.debug("查询商户信息 入参 info : {}", info);

        List<Map> businessStoreInfos = sqlSessionTemplate.selectList("storeServiceDaoImpl.getBusinessStoreInfo", info);
        if (businessStoreInfos == null) {
            return null;
        }
        if (businessStoreInfos.size() > 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "根据条件查询有多条数据,数据异常，请检查：businessStoreInfos，" + JSONObject.toJSONString(info));
        }

        if (businessStoreInfos == null || businessStoreInfos.size() < 1) {
            return null;
        }

        return businessStoreInfos.get(0);
    }

    /**
     * 查询商户属性
     *
     * @param info bId 信息
     * @return 商户属性
     * @throws DAOException
     */
    @Override
    public List<Map> getBusinessStoreAttrs(Map info) throws DAOException {
        logger.debug("查询商户属性信息 入参 info : {}", info);

        List<Map> businessStoreAttrs = sqlSessionTemplate.selectList("storeServiceDaoImpl.getBusinessStoreAttrs", info);

        return businessStoreAttrs;
    }

    /**
     * 查询商户照片
     *
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> getBusinessStorePhoto(Map info) throws DAOException {
        logger.debug("查询商户照片信息 入参 info : {}", info);

        List<Map> businessStorePhotos = sqlSessionTemplate.selectList("storeServiceDaoImpl.getBusinessStorePhoto", info);

        return businessStorePhotos;
    }

    /**
     * 查询商户证件
     *
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> getBusinessStoreCerdentials(Map info) throws DAOException {
        logger.debug("查询商户证件信息 入参 info : {}", info);

        List<Map> businessStoreCerdentialses = sqlSessionTemplate.selectList("storeServiceDaoImpl.getBusinessStoreCerdentials", info);

        return businessStoreCerdentialses;
    }

    /**
     * 保存商户信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException
     */
    @Override
    public void saveStoreInfoInstance(Map info) throws DAOException {
        logger.debug("保存商户信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("storeServiceDaoImpl.saveStoreInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存商户信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public void saveStoreAttrsInstance(Map info) throws DAOException {
        logger.debug("保存商户属性信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("storeServiceDaoImpl.saveStoreAttrsInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存商户属性信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public void saveStorePhotoInstance(Map info) throws DAOException {
        logger.debug("保存商户照片信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("storeServiceDaoImpl.saveStorePhotoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存商户照片信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public void saveStoreCerdentialsInstance(Map info) throws DAOException {
        logger.debug("保存商户证件信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("storeServiceDaoImpl.saveStoreCerdentialsInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存商户证件信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询商户信息（instance）
     *
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public Map getStoreInfo(Map info) throws DAOException {
        logger.debug("查询商户信息 入参 info : {}", info);

        List<Map> businessStoreInfos = sqlSessionTemplate.selectList("storeServiceDaoImpl.getStoreInfo", info);
        if (businessStoreInfos == null || businessStoreInfos.size() == 0) {
            return null;
        }
        if (businessStoreInfos.size() > 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "根据条件查询有多条数据,数据异常，请检查：getStoreInfo，" + JSONObject.toJSONString(info));
        }

        return businessStoreInfos.get(0);
    }

    /**
     * 商户属性查询（instance）
     *
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> getStoreAttrs(Map info) throws DAOException {
        logger.debug("查询商户属性信息 入参 info : {}", info);

        List<Map> storeAttrs = sqlSessionTemplate.selectList("storeServiceDaoImpl.getStoreAttrs", info);

        return storeAttrs;
    }

    /**
     * 商户照片查询（instance）
     *
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> getStorePhoto(Map info) throws DAOException {
        logger.debug("查询商户照片信息 入参 info : {}", info);

        List<Map> storePhotos = sqlSessionTemplate.selectList("storeServiceDaoImpl.getStorePhoto", info);

        return storePhotos;
    }

    /**
     * 商户证件查询（instance）
     *
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> getStoreCerdentials(Map info) throws DAOException {
        logger.debug("查询商户证件信息 入参 info : {}", info);

        List<Map> storeCerdentialses = sqlSessionTemplate.selectList("storeServiceDaoImpl.getStoreCerdentials", info);

        return storeCerdentialses;
    }

    /**
     * 修改商户信息
     *
     * @param info 修改信息
     * @throws DAOException
     */
    @Override
    public void updateStoreInfoInstance(Map info) throws DAOException {
        logger.debug("修改商户信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("storeServiceDaoImpl.updateStoreInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改商户信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 修改商户属性信息（instance）
     *
     * @param info 修改信息
     * @throws DAOException
     */
    @Override
    public void updateStoreAttrInstance(Map info) throws DAOException {
        logger.debug("修改商户属性信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("storeServiceDaoImpl.updateStoreAttrInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改商户属性信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 修改 商户照片信息
     *
     * @param info 修改信息
     * @throws DAOException
     */
    @Override
    public void updateStorePhotoInstance(Map info) throws DAOException {
        logger.debug("修改商户照片信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("storeServiceDaoImpl.updateStorePhotoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改商户照片信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 修改商户证件信息
     *
     * @param info 修改信息
     * @throws DAOException
     */
    @Override
    public void updateStoreCerdentailsInstance(Map info) throws DAOException {
        logger.debug("修改商户证件信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("storeServiceDaoImpl.updateStoreCerdentailsInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改商户证件信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 商户成员加入信息
     *
     * @param businessMemberStore 商户成员信息 封装
     * @throws DAOException 操作数据库异常
     */
    public void saveBusinessMemberStore(Map businessMemberStore) throws DAOException {
        logger.debug("商户成员加入 入参 businessMemberStore : {}", businessMemberStore);

        int saveFlag = sqlSessionTemplate.insert("storeServiceDaoImpl.saveBusinessMemberStore", businessMemberStore);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "商户成员加入失败：" + JSONObject.toJSONString(businessMemberStore));
        }
    }

    /**
     * 成员加入 保存信息至instance
     *
     * @param info
     * @throws DAOException
     */
    @Override
    public void saveMemberStoreInstance(Map info) throws DAOException {
        logger.debug("商户成员加入Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("storeServiceDaoImpl.saveMemberStoreInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存商户照片信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询商户成员加入信息（business过程）
     * 根据bId 查询商户信息
     *
     * @param info bId 信息
     * @return 商户信息
     * @throws DAOException
     */
    public Map getBusinessMemberStore(Map info) throws DAOException {
        logger.debug("查询商户成员加入信息 入参 info : {}", info);

        List<Map> businessMemberStores = sqlSessionTemplate.selectList("storeServiceDaoImpl.getBusinessMemberStore", info);
        if (businessMemberStores == null || businessMemberStores.size() == 0) {
            return null;
        }
        if (businessMemberStores.size() > 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "根据条件查询有多条数据,数据异常，请检查：businessMemberStore，" + JSONObject.toJSONString(info));
        }

        return businessMemberStores.get(0);
    }

    /**
     * 查询商户成员加入信息（instance过程）
     * 根据bId 查询商户信息
     *
     * @param info bId 信息
     * @return 商户信息
     * @throws DAOException
     */
    public Map getMemberStore(Map info) throws DAOException {
        logger.debug("查询商户成员加入信息 入参 info : {}", info);

        List<Map> memberStores = sqlSessionTemplate.selectList("storeServiceDaoImpl.getMemberStore", info);
        if (memberStores == null || memberStores.size() == 0) {
            return null;
        }
        if (memberStores.size() > 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "根据条件查询有多条数据,数据异常，请检查：getMemberStore，" + JSONObject.toJSONString(info));
        }

        return memberStores.get(0);
    }

    /**
     * 修改商户成员加入信息
     *
     * @param info 修改信息
     * @throws DAOException
     */
    public void updateMemberStoreInstance(Map info) throws DAOException {
        logger.debug("修改商户成员加入信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("storeServiceDaoImpl.updateMemberStoreInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改商户成员加入信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 保存物业用户信息
     *
     * @param info
     * @throws DAOException
     */
    public void saveBusinessStoreUser(Map info) throws DAOException {
        info.put("month", DateUtil.getCurrentMonth());
        logger.debug("保存物业用户信息入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("storeServiceDaoImpl.saveBusinessStoreUser", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存商户用户信息数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询物业用户信息
     *
     * @param info bId 信息
     * @return 物业照片
     * @throws DAOException
     */
    public List<Map> getBusinessStoreUser(Map info) throws DAOException {
        logger.debug("查询商户用户信息 入参 info : {}", info);

        List<Map> businessPropertyUsers = sqlSessionTemplate.selectList("storeServiceDaoImpl.getBusinessStoreUser", info);

        return businessPropertyUsers;
    }

    /**
     * 保存 物业用户信息 Business数据到 Instance中
     *
     * @param info
     * @throws DAOException
     */
    public void saveStoreUserInstance(Map info) throws DAOException {
        logger.debug("保存商户用户信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("storeServiceDaoImpl.saveStoreUserInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存商户用户信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询物业用户信息（instance 过程）
     *
     * @param info bId 信息
     * @return 物业照片
     * @throws DAOException
     */
    public List<Map> getStoreUser(Map info) throws DAOException {
        logger.debug("查询商户用户信息 入参 info : {}", info);

        List<Map> propertyUsers = sqlSessionTemplate.selectList("storeServiceDaoImpl.getStoreUser", info);

        return propertyUsers;
    }

    /**
     * 修改物业用户信息
     *
     * @param info 修改信息
     * @throws DAOException
     */
    public void updateStoreUserInstance(Map info) throws DAOException {
        logger.debug("修改商户用户信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("storeServiceDaoImpl.updateStoreUserInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改商户用户信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public List<Map> getStores(Map info) throws DAOException {
        logger.debug("查询商户信息 入参 info : {}", info);

        List<Map> propertyUsers = sqlSessionTemplate.selectList("storeServiceDaoImpl.getStores", info);

        return propertyUsers;
    }

    public int getStoreCount(Map info) throws DAOException {
        List<Map> stores = sqlSessionTemplate.selectList("storeServiceDaoImpl.getStoreCount", info);
        if (stores.size() < 1) {
            return 0;
        }
        return Integer.parseInt(stores.get(0).get("count").toString());
    }

    /**
     * 查询员工和商户信息表
     *
     * @param info
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> getStoreUserInfo(Map info) throws DAOException {
        List<Map> storeUserInfos = sqlSessionTemplate.selectList("storeServiceDaoImpl.getStoreUserInfo", info);
        return storeUserInfos;
    }

    @Override
    public List<Map> getStoreStaffs(Map info) throws DAOException {
        List<Map> storeUserInfos = sqlSessionTemplate.selectList("storeServiceDaoImpl.getStoreStaffs", info);
        return storeUserInfos;
    }

    @Override
    public int getStoreStaffCount(Map info) throws DAOException {
        List<Map> storeUserInfos = sqlSessionTemplate.selectList("storeServiceDaoImpl.getStoreStaffCount", info);
        if (storeUserInfos.size() < 1) {
            return 0;
        }
        return Integer.parseInt(storeUserInfos.get(0).get("count").toString());
    }

    /**
     * 修改 商户信息
     *
     * @param info
     * @return
     * @throws DAOException
     */
    @Override
    public int updateStore(Map info) throws DAOException {
        int saveFlag = sqlSessionTemplate.update("storeServiceDaoImpl.updateStore", info);
        return saveFlag;
    }
}
