package com.java110.agent.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.agent.dao.IAgentServiceDao;
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
 * 代理商服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("agentServiceDaoImpl")
//@Transactional
public class AgentServiceDaoImpl extends BaseServiceDao implements IAgentServiceDao {

    private final static Logger logger = LoggerFactory.getLogger(AgentServiceDaoImpl.class);

    /**
     * 代理商信息封装
     * @param businessAgentInfo 代理商信息 封装
     * @throws DAOException
     */
    @Override
    public void saveBusinessAgentInfo(Map businessAgentInfo) throws DAOException {
        businessAgentInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存代理商信息 入参 businessAgentInfo : {}",businessAgentInfo);
        int saveFlag = sqlSessionTemplate.insert("agentServiceDaoImpl.saveBusinessAgentInfo",businessAgentInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存代理商数据失败："+ JSONObject.toJSONString(businessAgentInfo));
        }
    }

    /**
     * 代理商属性信息分装
     * @param businessAgentAttr 代理商属性信息封装
     * @throws DAOException
     */
    @Override
    public void saveBusinessAgentAttr(Map businessAgentAttr) throws DAOException {
        businessAgentAttr.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存代理商属性信息 入参 businessAgentAttr : {}",businessAgentAttr);

        int saveFlag = sqlSessionTemplate.insert("agentServiceDaoImpl.saveBusinessAgentAttr",businessAgentAttr);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存代理商属性数据失败："+ JSONObject.toJSONString(businessAgentAttr));
        }
    }

    /**
     * 保存代理商照片信息
     * @param businessAgentPhoto 代理商照片
     * @throws DAOException
     */
    @Override
    public void saveBusinessAgentPhoto(Map businessAgentPhoto) throws DAOException {
        businessAgentPhoto.put("month", DateUtil.getCurrentMonth());
        logger.debug("保存代理商照片信息 入参 businessAgentPhoto : {}",businessAgentPhoto);

        int saveFlag = sqlSessionTemplate.insert("agentServiceDaoImpl.saveBusinessAgentPhoto",businessAgentPhoto);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存代理商照片数据失败："+ JSONObject.toJSONString(businessAgentPhoto));
        }
    }

    /**
     * 保存代理商证件信息
     * @param businessAgentCerdentials 代理商证件
     * @throws DAOException
     */
    @Override
    public void saveBusinessAgentCerdentials(Map businessAgentCerdentials) throws DAOException {
        businessAgentCerdentials.put("month", DateUtil.getCurrentMonth());
        logger.debug("保存代理商证件信息 入参 businessAgentCerdentials : {}",businessAgentCerdentials);

        int saveFlag = sqlSessionTemplate.insert("agentServiceDaoImpl.saveBusinessAgentCerdentials",businessAgentCerdentials);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存代理商证件数据失败："+ JSONObject.toJSONString(businessAgentCerdentials));
        }
    }

    /**
     * 保存代理商用户信息
     * @param info
     * @throws DAOException
     */
    public void saveBusinessAgentUser(Map info) throws DAOException{
        info.put("month", DateUtil.getCurrentMonth());
        logger.debug("保存代理商用户信息入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("agentServiceDaoImpl.saveBusinessAgentUser",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存代理商用户信息数据失败："+ JSONObject.toJSONString(info));
        }
    }

    /**
     * 保存代理商费用信息
     * @param info
     * @throws DAOException
     */
    public void saveBusinessAgentFee(Map info) throws DAOException{
        info.put("month", DateUtil.getCurrentMonth());
        logger.debug("保存代理商费用信息入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("agentServiceDaoImpl.saveBusinessAgentFee",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存代理商费用信息数据失败："+ JSONObject.toJSONString(info));
        }
    }

    /**
     * 保存 住户信息
     * @param businessAgentHouse 住户信息 封装
     * @throws DAOException 操作数据库异常
     */
    public void saveBusinessAgentHouse(Map businessAgentHouse) throws DAOException{
        businessAgentHouse.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存住户信息 入参 businessAgentHouse : {}",businessAgentHouse);
        int saveFlag = sqlSessionTemplate.insert("agentServiceDaoImpl.saveBusinessAgentHouse",businessAgentHouse);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存住户数据失败："+ JSONObject.toJSONString(businessAgentHouse));
        }
    }

    /**
     * 保存住户属性
     * @param businessAgentHouseAttr 住户信息封装
     * @throws DAOException 操作数据库异常
     */
    public void saveBusinessAgentHouseAttr(Map businessAgentHouseAttr) throws DAOException{
        businessAgentHouseAttr.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存住户属性信息 入参 businessAgentHouseAttr : {}",businessAgentHouseAttr);

        int saveFlag = sqlSessionTemplate.insert("agentServiceDaoImpl.saveBusinessAgentHouseAttr",businessAgentHouseAttr);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存住户属性数据失败："+ JSONObject.toJSONString(businessAgentHouseAttr));
        }
    }

    /**
     * 查询代理商信息
     * @param info bId 信息
     * @return 代理商信息
     * @throws DAOException
     */
    @Override
    public Map getBusinessAgentInfo(Map info) throws DAOException {

        logger.debug("查询代理商信息 入参 info : {}",info);

        List<Map> businessAgentInfos = sqlSessionTemplate.selectList("agentServiceDaoImpl.getBusinessAgentInfo",info);
        if(businessAgentInfos == null){
            return null;
        }
        if(businessAgentInfos.size() >1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"根据条件查询有多条数据,数据异常，请检查：businessAgentInfos，"+ JSONObject.toJSONString(info));
        }

        return businessAgentInfos.get(0);
    }

    /**
     * 查询代理商属性
     * @param info bId 信息
     * @return 代理商属性
     * @throws DAOException
     */
    @Override
    public List<Map> getBusinessAgentAttrs(Map info) throws DAOException {
        logger.debug("查询代理商属性信息 入参 info : {}",info);

        List<Map> businessAgentAttrs = sqlSessionTemplate.selectList("agentServiceDaoImpl.getBusinessAgentAttrs",info);

        return businessAgentAttrs;
    }

    /**
     * 查询住户信息（business过程）
     * 根据bId 查询代理商信息
     * @param info bId 信息
     * @return 代理商信息
     * @throws DAOException
     */
    public Map getBusinessAgentHouse(Map info) throws DAOException{
        logger.debug("查询住户信息 入参 info : {}",info);

        List<Map> businessAgentHouses = sqlSessionTemplate.selectList("agentServiceDaoImpl.getBusinessAgentHouse",info);
        if(businessAgentHouses == null){
            return null;
        }
        if(businessAgentHouses.size() >1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"根据条件查询有多条数据,数据异常，请检查：businessAgentHouse，"+ JSONObject.toJSONString(info));
        }

        return businessAgentHouses.get(0);
    }


    /**
     * 查询住户属性信息（business过程）
     * @param info bId 信息
     * @return 代理商属性
     * @throws DAOException
     */
    public List<Map> getBusinessAgentHouseAttrs(Map info) throws DAOException{
        logger.debug("查询住户属性信息 入参 info : {}",info);

        List<Map> businessAgentHouseAttrs = sqlSessionTemplate.selectList("agentServiceDaoImpl.getBusinessAgentHouseAttrs",info);

        return businessAgentHouseAttrs;
    }

    /**
     * 查询代理商照片
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> getBusinessAgentPhoto(Map info) throws DAOException {
        logger.debug("查询代理商照片信息 入参 info : {}",info);

        List<Map> businessAgentPhotos = sqlSessionTemplate.selectList("agentServiceDaoImpl.getBusinessAgentPhoto",info);

        return businessAgentPhotos;
    }

    /**
     * 查询代理商证件
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> getBusinessAgentCerdentials(Map info) throws DAOException {
        logger.debug("查询代理商证件信息 入参 info : {}",info);

        List<Map> businessAgentCerdentialses = sqlSessionTemplate.selectList("agentServiceDaoImpl.getBusinessAgentCerdentials",info);

        return businessAgentCerdentialses;
    }

    /**
     * 查询代理商用户信息
     * @param info bId 信息
     * @return 代理商照片
     * @throws DAOException
     */
    public List<Map> getBusinessAgentUser(Map info) throws DAOException{
        logger.debug("查询代理商用户信息 入参 info : {}",info);

        List<Map> businessAgentUsers = sqlSessionTemplate.selectList("agentServiceDaoImpl.getBusinessAgentUser",info);

        return businessAgentUsers;
    }

    /**
     * 查询代理商费用信息
     * @param info bId 信息
     * @return 代理商照片
     * @throws DAOException
     */
    public List<Map> getBusinessAgentFee(Map info) throws DAOException{
        logger.debug("查询代理商用户信息 入参 info : {}",info);

        List<Map> businessAgentUsers = sqlSessionTemplate.selectList("agentServiceDaoImpl.getBusinessAgentFee",info);

        return businessAgentUsers;
    }


    /**
     * 保存代理商信息 到 instance
     * @param info   bId 信息
     * @throws DAOException
     */
    @Override
    public void saveAgentInfoInstance(Map info) throws DAOException {
        logger.debug("保存代理商信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("agentServiceDaoImpl.saveAgentInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存代理商信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    @Override
    public void saveAgentAttrsInstance(Map info) throws DAOException {
        logger.debug("保存代理商属性信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("agentServiceDaoImpl.saveAgentAttrsInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存代理商属性信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    /**
     * 保存 住户信息 Business数据到 Instance中
     * @param info
     * @throws DAOException
     */
    public void saveAgentHouseInstance(Map info) throws DAOException{
        logger.debug("保存住户信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("agentServiceDaoImpl.saveAgentHouseInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存住户信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 保存 住户属性信息 Business数据到 Instance中
     * @param info
     * @throws DAOException
     */
    public void saveAgentHouseAttrsInstance(Map info) throws DAOException{
        logger.debug("保存住户属性信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("agentServiceDaoImpl.saveAgentHouseAttrsInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存住户属性信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    @Override
    public void saveAgentPhotoInstance(Map info) throws DAOException {
        logger.debug("保存代理商照片信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("agentServiceDaoImpl.saveAgentPhotoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存代理商照片信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    @Override
    public void saveAgentCerdentialsInstance(Map info) throws DAOException {
        logger.debug("保存代理商证件信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("agentServiceDaoImpl.saveAgentCerdentialsInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存代理商证件信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    /**
     * 保存 代理商用户信息 Business数据到 Instance中
     * @param info
     * @throws DAOException
     */
    public void saveAgentUserInstance(Map info) throws DAOException{
        logger.debug("保存代理商用户信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("agentServiceDaoImpl.saveAgentUserInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存代理商用户信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    /**
     * 保存 代理商费用信息 Business数据到 Instance中
     * @param info
     * @throws DAOException
     */
    public void saveAgentFeeInstance(Map info) throws DAOException{
        logger.debug("保存代理商费用信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("agentServiceDaoImpl.saveAgentFeeInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存代理商费用信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询代理商信息（instance）
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public Map getAgentInfo(Map info) throws DAOException {
        logger.debug("查询代理商信息 入参 info : {}",info);

        List<Map> businessAgentInfos = sqlSessionTemplate.selectList("agentServiceDaoImpl.getAgentInfo",info);
        if(businessAgentInfos == null || businessAgentInfos.size() == 0){
            return null;
        }
        if(businessAgentInfos.size() >1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"根据条件查询有多条数据,数据异常，请检查：getAgentInfo，"+ JSONObject.toJSONString(info));
        }

        return businessAgentInfos.get(0);
    }

    /**
     * 代理商属性查询（instance）
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> getAgentAttrs(Map info) throws DAOException {
        logger.debug("查询代理商属性信息 入参 info : {}",info);

        List<Map> agentAttrs = sqlSessionTemplate.selectList("agentServiceDaoImpl.getAgentAttrs",info);

        return agentAttrs;
    }

    /**
     * 查询住户信息（instance过程）
     * 根据bId 查询代理商信息
     * @param info bId 信息
     * @return 代理商信息
     * @throws DAOException
     */
    public Map getAgentHouse(Map info) throws DAOException{
        logger.debug("查询住户信息 入参 info : {}",info);

        List<Map> businessAgentInfos = sqlSessionTemplate.selectList("agentServiceDaoImpl.getAgentHouse",info);
        if(businessAgentInfos == null || businessAgentInfos.size() == 0){
            return null;
        }
        if(businessAgentInfos.size() >1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"根据条件查询有多条数据,数据异常，请检查：getAgentHouse，"+ JSONObject.toJSONString(info));
        }

        return businessAgentInfos.get(0);
    }


    /**
     * 查询住户属性信息（instance过程）
     * @param info bId 信息
     * @return 代理商属性
     * @throws DAOException
     */
    public List<Map> getAgentHouseAttrs(Map info) throws DAOException{
        logger.debug("查询住户属性信息 入参 info : {}",info);

        List<Map> agentAttrs = sqlSessionTemplate.selectList("agentServiceDaoImpl.getAgentHouseAttrs",info);

        return agentAttrs;
    }

    /**
     * 代理商照片查询（instance）
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> getAgentPhoto(Map info) throws DAOException {
        logger.debug("查询代理商照片信息 入参 info : {}",info);

        List<Map> agentPhotos = sqlSessionTemplate.selectList("agentServiceDaoImpl.getAgentPhoto",info);

        return agentPhotos;
    }

    /**
     * 代理商证件查询（instance）
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> getAgentCerdentials(Map info) throws DAOException {
        logger.debug("查询代理商证件信息 入参 info : {}",info);

        List<Map> agentCerdentialses = sqlSessionTemplate.selectList("agentServiceDaoImpl.getAgentCerdentials",info);

        return agentCerdentialses;
    }

    /**
     * 查询代理商用户信息（instance 过程）
     * @param info bId 信息
     * @return 代理商照片
     * @throws DAOException
     */
    public List<Map> getAgentUser(Map info) throws DAOException{
        logger.debug("查询代理商用户信息 入参 info : {}",info);

        List<Map> agentUsers = sqlSessionTemplate.selectList("agentServiceDaoImpl.getAgentUser",info);

        return agentUsers;
    }

    public List<Map> getAgentFee(Map info) throws DAOException{
        logger.debug("查询代理商费用信息 入参 info : {}",info);
        List<Map> agentFees = sqlSessionTemplate.selectList("agentServiceDaoImpl.getAgentFee",info);
        return agentFees;
    }

    /**
     * 修改代理商信息
     * @param info 修改信息
     * @throws DAOException
     */
    @Override
    public void updateAgentInfoInstance(Map info) throws DAOException {
        logger.debug("修改代理商信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("agentServiceDaoImpl.updateAgentInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改代理商信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    /**
     * 修改代理商属性信息（instance）
     * @param info 修改信息
     * @throws DAOException
     */
    @Override
    public void updateAgentAttrInstance(Map info) throws DAOException {
        logger.debug("修改代理商属性信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("agentServiceDaoImpl.updateAgentAttrInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改代理商属性信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    /**
     * 修改住户信息
     * @param info 修改信息
     * @throws DAOException
     */
    public void updateAgentHouseInstance(Map info) throws DAOException{
        logger.debug("修改住户信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("agentServiceDaoImpl.updateAgentHouseInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改住户信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 修改住户属性信息
     * @param info 修改信息
     * @throws DAOException
     */
    public void updateAgentHouseAttrInstance(Map info) throws DAOException{
        logger.debug("修改住户属性信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("agentServiceDaoImpl.updateAgentHouseAttrInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改住户属性信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    /**
     * 修改 代理商照片信息
     * @param info 修改信息
     * @throws DAOException
     */
    @Override
    public void updateAgentPhotoInstance(Map info) throws DAOException {
        logger.debug("修改代理商照片信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("agentServiceDaoImpl.updateAgentPhotoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改代理商照片信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    /**
     * 修改代理商证件信息
     * @param info 修改信息
     * @throws DAOException
     */
    @Override
    public void updateAgentCerdentailsInstance(Map info) throws DAOException {
        logger.debug("修改代理商证件信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("agentServiceDaoImpl.updateAgentCerdentailsInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改代理商证件信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    /**
     * 修改代理商用户信息
     * @param info 修改信息
     * @throws DAOException
     */
    public void updateAgentUserInstance(Map info) throws DAOException{
        logger.debug("修改代理商用户信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("agentServiceDaoImpl.updateAgentUserInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改代理商用户信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }



    /**
     * 修改代理商费用信息
     * @param info 修改信息
     * @throws DAOException
     */
    public void updateAgentFeeInstance(Map info) throws DAOException{
        logger.debug("修改代理商费用信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("agentServiceDaoImpl.updateAgentFeeInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改代理商费用信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

}
