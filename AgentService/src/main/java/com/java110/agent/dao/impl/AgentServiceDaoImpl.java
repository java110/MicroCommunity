package com.java110.agent.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.exception.DAOException;
import com.java110.common.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.agent.dao.IAgentServiceDao;
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
     * 代理商成员加入信息
     * @param businessMemberAgent 代理商成员信息 封装
     * @throws DAOException 操作数据库异常
     */
    public void saveBusinessMemberAgent(Map businessMemberAgent) throws DAOException{
        logger.debug("代理商成员加入 入参 businessMemberAgent : {}",businessMemberAgent);

        int saveFlag = sqlSessionTemplate.insert("agentServiceDaoImpl.saveBusinessMemberAgent",businessMemberAgent);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"代理商成员加入失败："+ JSONObject.toJSONString(businessMemberAgent));
        }
    }

    /**
     * 成员加入 保存信息至instance
     * @param info
     * @throws DAOException
     */
    @Override
    public void saveMemberAgentInstance(Map info) throws DAOException {
        logger.debug("代理商成员加入Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("agentServiceDaoImpl.saveMemberAgentInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存代理商照片信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询代理商成员加入信息（business过程）
     * 根据bId 查询代理商信息
     * @param info bId 信息
     * @return 代理商信息
     * @throws DAOException
     */
    public Map getBusinessMemberAgent(Map info) throws DAOException{
        logger.debug("查询代理商成员加入信息 入参 info : {}",info);

        List<Map> businessMemberAgents = sqlSessionTemplate.selectList("agentServiceDaoImpl.getBusinessMemberAgent",info);
        if(businessMemberAgents == null || businessMemberAgents.size() == 0){
            return null;
        }
        if(businessMemberAgents.size() >1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"根据条件查询有多条数据,数据异常，请检查：businessMemberAgent，"+ JSONObject.toJSONString(info));
        }

        return businessMemberAgents.get(0);
    }

    /**
     * 查询代理商成员加入信息（instance过程）
     * 根据bId 查询代理商信息
     * @param info bId 信息
     * @return 代理商信息
     * @throws DAOException
     */
    public Map getMemberAgent(Map info) throws DAOException{
        logger.debug("查询代理商成员加入信息 入参 info : {}",info);

        List<Map> memberAgents = sqlSessionTemplate.selectList("agentServiceDaoImpl.getMemberAgent",info);
        if(memberAgents == null || memberAgents.size() == 0){
            return null;
        }
        if(memberAgents.size() >1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"根据条件查询有多条数据,数据异常，请检查：getMemberAgent，"+ JSONObject.toJSONString(info));
        }

        return memberAgents.get(0);
    }
    /**
     * 修改代理商成员加入信息
     * @param info 修改信息
     * @throws DAOException
     */
    public void updateMemberAgentInstance(Map info) throws DAOException{
        logger.debug("修改代理商成员加入信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("agentServiceDaoImpl.updateMemberAgentInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改代理商成员加入信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }
}
