package com.java110.property.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.property.dao.IPropertyServiceDao;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.exception.DAOException;
import com.java110.common.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 物业服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("propertyServiceDaoImpl")
//@Transactional
public class PropertyServiceDaoImpl extends BaseServiceDao implements IPropertyServiceDao {

    private final static Logger logger = LoggerFactory.getLogger(PropertyServiceDaoImpl.class);

    /**
     * 物业信息封装
     * @param businessPropertyInfo 物业信息 封装
     * @throws DAOException
     */
    @Override
    public void saveBusinessPropertyInfo(Map businessPropertyInfo) throws DAOException {
        businessPropertyInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存物业信息 入参 businessPropertyInfo : {}",businessPropertyInfo);
        int saveFlag = sqlSessionTemplate.insert("propertyServiceDaoImpl.saveBusinessPropertyInfo",businessPropertyInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存物业数据失败："+ JSONObject.toJSONString(businessPropertyInfo));
        }
    }

    /**
     * 物业属性信息分装
     * @param businessPropertyAttr 物业属性信息封装
     * @throws DAOException
     */
    @Override
    public void saveBusinessPropertyAttr(Map businessPropertyAttr) throws DAOException {
        businessPropertyAttr.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存物业属性信息 入参 businessPropertyAttr : {}",businessPropertyAttr);

        int saveFlag = sqlSessionTemplate.insert("propertyServiceDaoImpl.saveBusinessPropertyAttr",businessPropertyAttr);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存物业属性数据失败："+ JSONObject.toJSONString(businessPropertyAttr));
        }
    }

    /**
     * 保存物业照片信息
     * @param businessPropertyPhoto 物业照片
     * @throws DAOException
     */
    @Override
    public void saveBusinessPropertyPhoto(Map businessPropertyPhoto) throws DAOException {
        businessPropertyPhoto.put("month", DateUtil.getCurrentMonth());
        logger.debug("保存物业照片信息 入参 businessPropertyPhoto : {}",businessPropertyPhoto);

        int saveFlag = sqlSessionTemplate.insert("propertyServiceDaoImpl.saveBusinessPropertyPhoto",businessPropertyPhoto);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存物业照片数据失败："+ JSONObject.toJSONString(businessPropertyPhoto));
        }
    }

    /**
     * 保存物业证件信息
     * @param businessPropertyCerdentials 物业证件
     * @throws DAOException
     */
    @Override
    public void saveBusinessPropertyCerdentials(Map businessPropertyCerdentials) throws DAOException {
        businessPropertyCerdentials.put("month", DateUtil.getCurrentMonth());
        logger.debug("保存物业证件信息 入参 businessPropertyCerdentials : {}",businessPropertyCerdentials);

        int saveFlag = sqlSessionTemplate.insert("propertyServiceDaoImpl.saveBusinessPropertyCerdentials",businessPropertyCerdentials);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存物业证件数据失败："+ JSONObject.toJSONString(businessPropertyCerdentials));
        }
    }

    /**
     * 查询物业信息
     * @param info bId 信息
     * @return 物业信息
     * @throws DAOException
     */
    @Override
    public Map getBusinessPropertyInfo(Map info) throws DAOException {

        logger.debug("查询物业信息 入参 info : {}",info);

        List<Map> businessPropertyInfos = sqlSessionTemplate.selectList("propertyServiceDaoImpl.getBusinessPropertyInfo",info);
        if(businessPropertyInfos == null){
            return null;
        }
        if(businessPropertyInfos.size() >1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"根据条件查询有多条数据,数据异常，请检查：businessPropertyInfos，"+ JSONObject.toJSONString(info));
        }

        return businessPropertyInfos.get(0);
    }

    /**
     * 查询物业属性
     * @param info bId 信息
     * @return 物业属性
     * @throws DAOException
     */
    @Override
    public List<Map> getBusinessPropertyAttrs(Map info) throws DAOException {
        logger.debug("查询物业属性信息 入参 info : {}",info);

        List<Map> businessPropertyAttrs = sqlSessionTemplate.selectList("propertyServiceDaoImpl.getBusinessPropertyAttrs",info);

        return businessPropertyAttrs;
    }

    /**
     * 查询物业照片
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> getBusinessPropertyPhoto(Map info) throws DAOException {
        logger.debug("查询物业照片信息 入参 info : {}",info);

        List<Map> businessPropertyPhotos = sqlSessionTemplate.selectList("propertyServiceDaoImpl.getBusinessPropertyPhoto",info);

        return businessPropertyPhotos;
    }

    /**
     * 查询物业证件
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> getBusinessPropertyCerdentials(Map info) throws DAOException {
        logger.debug("查询物业证件信息 入参 info : {}",info);

        List<Map> businessPropertyCerdentialses = sqlSessionTemplate.selectList("propertyServiceDaoImpl.getBusinessPropertyCerdentials",info);

        return businessPropertyCerdentialses;
    }

    /**
     * 保存物业信息 到 instance
     * @param info   bId 信息
     * @throws DAOException
     */
    @Override
    public void savePropertyInfoInstance(Map info) throws DAOException {
        logger.debug("保存物业信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("propertyServiceDaoImpl.savePropertyInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存物业信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    @Override
    public void savePropertyAttrsInstance(Map info) throws DAOException {
        logger.debug("保存物业属性信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("propertyServiceDaoImpl.savePropertyAttrsInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存物业属性信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    @Override
    public void savePropertyPhotoInstance(Map info) throws DAOException {
        logger.debug("保存物业照片信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("propertyServiceDaoImpl.savePropertyPhotoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存物业照片信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    @Override
    public void savePropertyCerdentialsInstance(Map info) throws DAOException {
        logger.debug("保存物业证件信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("propertyServiceDaoImpl.savePropertyCerdentialsInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存物业证件信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询物业信息（instance）
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public Map getPropertyInfo(Map info) throws DAOException {
        logger.debug("查询物业信息 入参 info : {}",info);

        List<Map> businessPropertyInfos = sqlSessionTemplate.selectList("propertyServiceDaoImpl.getPropertyInfo",info);
        if(businessPropertyInfos == null || businessPropertyInfos.size() == 0){
            return null;
        }
        if(businessPropertyInfos.size() >1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"根据条件查询有多条数据,数据异常，请检查：getPropertyInfo，"+ JSONObject.toJSONString(info));
        }

        return businessPropertyInfos.get(0);
    }

    /**
     * 物业属性查询（instance）
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> getPropertyAttrs(Map info) throws DAOException {
        logger.debug("查询物业属性信息 入参 info : {}",info);

        List<Map> propertyAttrs = sqlSessionTemplate.selectList("propertyServiceDaoImpl.getPropertyAttrs",info);

        return propertyAttrs;
    }

    /**
     * 物业照片查询（instance）
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> getPropertyPhoto(Map info) throws DAOException {
        logger.debug("查询物业照片信息 入参 info : {}",info);

        List<Map> propertyPhotos = sqlSessionTemplate.selectList("propertyServiceDaoImpl.getPropertyPhoto",info);

        return propertyPhotos;
    }

    /**
     * 物业证件查询（instance）
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> getPropertyCerdentials(Map info) throws DAOException {
        logger.debug("查询物业证件信息 入参 info : {}",info);

        List<Map> propertyCerdentialses = sqlSessionTemplate.selectList("propertyServiceDaoImpl.getPropertyCerdentials",info);

        return propertyCerdentialses;
    }

    /**
     * 修改物业信息
     * @param info 修改信息
     * @throws DAOException
     */
    @Override
    public void updatePropertyInfoInstance(Map info) throws DAOException {
        logger.debug("修改物业信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("propertyServiceDaoImpl.updatePropertyInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改物业信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    /**
     * 修改物业属性信息（instance）
     * @param info 修改信息
     * @throws DAOException
     */
    @Override
    public void updatePropertyAttrInstance(Map info) throws DAOException {
        logger.debug("修改物业属性信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("propertyServiceDaoImpl.updatePropertyAttrInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改物业属性信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    /**
     * 修改 物业照片信息
     * @param info 修改信息
     * @throws DAOException
     */
    @Override
    public void updatePropertyPhotoInstance(Map info) throws DAOException {
        logger.debug("修改物业照片信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("propertyServiceDaoImpl.updatePropertyPhotoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改物业照片信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    /**
     * 修改物业证件信息
     * @param info 修改信息
     * @throws DAOException
     */
    @Override
    public void updatePropertyCerdentailsInstance(Map info) throws DAOException {
        logger.debug("修改物业证件信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("propertyServiceDaoImpl.updatePropertyCerdentailsInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改物业证件信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    /**
     * 物业成员加入信息
     * @param businessMemberProperty 物业成员信息 封装
     * @throws DAOException 操作数据库异常
     */
    public void saveBusinessMemberProperty(Map businessMemberProperty) throws DAOException{
        logger.debug("物业成员加入 入参 businessMemberProperty : {}",businessMemberProperty);

        int saveFlag = sqlSessionTemplate.insert("propertyServiceDaoImpl.saveBusinessMemberProperty",businessMemberProperty);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"物业成员加入失败："+ JSONObject.toJSONString(businessMemberProperty));
        }
    }

    /**
     * 成员加入 保存信息至instance
     * @param info
     * @throws DAOException
     */
    @Override
    public void saveMemberPropertyInstance(Map info) throws DAOException {
        logger.debug("物业成员加入Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("propertyServiceDaoImpl.saveMemberPropertyInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存物业照片信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询物业成员加入信息（business过程）
     * 根据bId 查询物业信息
     * @param info bId 信息
     * @return 物业信息
     * @throws DAOException
     */
    public Map getBusinessMemberProperty(Map info) throws DAOException{
        logger.debug("查询物业成员加入信息 入参 info : {}",info);

        List<Map> businessMemberPropertys = sqlSessionTemplate.selectList("propertyServiceDaoImpl.getBusinessMemberProperty",info);
        if(businessMemberPropertys == null || businessMemberPropertys.size() == 0){
            return null;
        }
        if(businessMemberPropertys.size() >1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"根据条件查询有多条数据,数据异常，请检查：businessMemberProperty，"+ JSONObject.toJSONString(info));
        }

        return businessMemberPropertys.get(0);
    }

    /**
     * 查询物业成员加入信息（instance过程）
     * 根据bId 查询物业信息
     * @param info bId 信息
     * @return 物业信息
     * @throws DAOException
     */
    public Map getMemberProperty(Map info) throws DAOException{
        logger.debug("查询物业成员加入信息 入参 info : {}",info);

        List<Map> memberPropertys = sqlSessionTemplate.selectList("propertyServiceDaoImpl.getMemberProperty",info);
        if(memberPropertys == null || memberPropertys.size() == 0){
            return null;
        }
        if(memberPropertys.size() >1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"根据条件查询有多条数据,数据异常，请检查：getMemberProperty，"+ JSONObject.toJSONString(info));
        }

        return memberPropertys.get(0);
    }
    /**
     * 修改物业成员加入信息
     * @param info 修改信息
     * @throws DAOException
     */
    public void updateMemberPropertyInstance(Map info) throws DAOException{
        logger.debug("修改物业成员加入信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("propertyServiceDaoImpl.updateMemberPropertyInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改物业成员加入信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }
}
