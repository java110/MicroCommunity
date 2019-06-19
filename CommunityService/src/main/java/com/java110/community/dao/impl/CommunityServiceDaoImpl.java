package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.exception.DAOException;
import com.java110.common.util.DateUtil;
import com.java110.community.dao.ICommunityServiceDao;
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
@Service("communityServiceDaoImpl")
//@Transactional
public class CommunityServiceDaoImpl extends BaseServiceDao implements ICommunityServiceDao {

    private static Logger logger = LoggerFactory.getLogger(CommunityServiceDaoImpl.class);

    /**
     * 小区信息封装
     *
     * @param businessCommunityInfo 小区信息 封装
     * @throws DAOException
     */
    @Override
    public void saveBusinessCommunityInfo(Map businessCommunityInfo) throws DAOException {
        businessCommunityInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存小区信息 入参 businessCommunityInfo : {}", businessCommunityInfo);
        int saveFlag = sqlSessionTemplate.insert("communityServiceDaoImpl.saveBusinessCommunityInfo", businessCommunityInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存小区数据失败：" + JSONObject.toJSONString(businessCommunityInfo));
        }
    }

    /**
     * 小区属性信息分装
     *
     * @param businessCommunityAttr 小区属性信息封装
     * @throws DAOException
     */
    @Override
    public void saveBusinessCommunityAttr(Map businessCommunityAttr) throws DAOException {
        businessCommunityAttr.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存小区属性信息 入参 businessCommunityAttr : {}", businessCommunityAttr);

        int saveFlag = sqlSessionTemplate.insert("communityServiceDaoImpl.saveBusinessCommunityAttr", businessCommunityAttr);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存小区属性数据失败：" + JSONObject.toJSONString(businessCommunityAttr));
        }
    }

    /**
     * 保存小区照片信息
     *
     * @param businessCommunityPhoto 小区照片
     * @throws DAOException
     */
    @Override
    public void saveBusinessCommunityPhoto(Map businessCommunityPhoto) throws DAOException {
        businessCommunityPhoto.put("month", DateUtil.getCurrentMonth());
        logger.debug("保存小区照片信息 入参 businessCommunityPhoto : {}", businessCommunityPhoto);

        int saveFlag = sqlSessionTemplate.insert("communityServiceDaoImpl.saveBusinessCommunityPhoto", businessCommunityPhoto);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存小区照片数据失败：" + JSONObject.toJSONString(businessCommunityPhoto));
        }
    }

    /**
     * 保存小区证件信息
     *
     * @param businessCommunityCerdentials 小区证件
     * @throws DAOException
     */
    @Override
    public void saveBusinessCommunityCerdentials(Map businessCommunityCerdentials) throws DAOException {
        businessCommunityCerdentials.put("month", DateUtil.getCurrentMonth());
        logger.debug("保存小区证件信息 入参 businessCommunityCerdentials : {}", businessCommunityCerdentials);

        int saveFlag = sqlSessionTemplate.insert("communityServiceDaoImpl.saveBusinessCommunityCerdentials", businessCommunityCerdentials);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存小区证件数据失败：" + JSONObject.toJSONString(businessCommunityCerdentials));
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
    public Map getBusinessCommunityInfo(Map info) throws DAOException {

        logger.debug("查询小区信息 入参 info : {}", info);

        List<Map> businessCommunityInfos = sqlSessionTemplate.selectList("communityServiceDaoImpl.getBusinessCommunityInfo", info);
        if (businessCommunityInfos == null) {
            return null;
        }
        if (businessCommunityInfos.size() > 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "根据条件查询有多条数据,数据异常，请检查：businessCommunityInfos，" + JSONObject.toJSONString(info));
        }

        return businessCommunityInfos.get(0);
    }

    /**
     * 查询小区属性
     *
     * @param info bId 信息
     * @return 小区属性
     * @throws DAOException
     */
    @Override
    public List<Map> getBusinessCommunityAttrs(Map info) throws DAOException {
        logger.debug("查询小区属性信息 入参 info : {}", info);

        List<Map> businessCommunityAttrs = sqlSessionTemplate.selectList("communityServiceDaoImpl.getBusinessCommunityAttrs", info);

        return businessCommunityAttrs;
    }

    /**
     * 查询小区照片
     *
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> getBusinessCommunityPhoto(Map info) throws DAOException {
        logger.debug("查询小区照片信息 入参 info : {}", info);

        List<Map> businessCommunityPhotos = sqlSessionTemplate.selectList("communityServiceDaoImpl.getBusinessCommunityPhoto", info);

        return businessCommunityPhotos;
    }

    /**
     * 查询小区证件
     *
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> getBusinessCommunityCerdentials(Map info) throws DAOException {
        logger.debug("查询小区证件信息 入参 info : {}", info);

        List<Map> businessCommunityCerdentialses = sqlSessionTemplate.selectList("communityServiceDaoImpl.getBusinessCommunityCerdentials", info);

        return businessCommunityCerdentialses;
    }

    /**
     * 保存小区信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException
     */
    @Override
    public void saveCommunityInfoInstance(Map info) throws DAOException {
        logger.debug("保存小区信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("communityServiceDaoImpl.saveCommunityInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存小区信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public void saveCommunityAttrsInstance(Map info) throws DAOException {
        logger.debug("保存小区属性信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("communityServiceDaoImpl.saveCommunityAttrsInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存小区属性信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public void saveCommunityPhotoInstance(Map info) throws DAOException {
        logger.debug("保存小区照片信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("communityServiceDaoImpl.saveCommunityPhotoInstance", info);

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
    public Map getCommunityInfo(Map info) throws DAOException {
        logger.debug("查询小区信息 入参 info : {}", info);

        List<Map> businessCommunityInfos = sqlSessionTemplate.selectList("communityServiceDaoImpl.getCommunityInfo", info);
        if (businessCommunityInfos == null || businessCommunityInfos.size() == 0) {
            return null;
        }
        if (businessCommunityInfos.size() > 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "根据条件查询有多条数据,数据异常，请检查：getCommunityInfo，" + JSONObject.toJSONString(info));
        }

        return businessCommunityInfos.get(0);
    }

    /**
     * 小区属性查询（instance）
     *
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> getCommunityAttrs(Map info) throws DAOException {
        logger.debug("查询小区属性信息 入参 info : {}", info);

        List<Map> communityAttrs = sqlSessionTemplate.selectList("communityServiceDaoImpl.getCommunityAttrs", info);

        return communityAttrs;
    }

    /**
     * 小区照片查询（instance）
     *
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> getCommunityPhoto(Map info) throws DAOException {
        logger.debug("查询小区照片信息 入参 info : {}", info);

        List<Map> communityPhotos = sqlSessionTemplate.selectList("communityServiceDaoImpl.getCommunityPhoto", info);

        return communityPhotos;
    }


    /**
     * 修改小区信息
     *
     * @param info 修改信息
     * @throws DAOException
     */
    @Override
    public void updateCommunityInfoInstance(Map info) throws DAOException {
        logger.debug("修改小区信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("communityServiceDaoImpl.updateCommunityInfoInstance", info);

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
    public void updateCommunityAttrInstance(Map info) throws DAOException {
        logger.debug("修改小区属性信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("communityServiceDaoImpl.updateCommunityAttrInstance", info);

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
    public void updateCommunityPhotoInstance(Map info) throws DAOException {
        logger.debug("修改小区照片信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("communityServiceDaoImpl.updateCommunityPhotoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改小区照片信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 小区成员加入信息
     *
     * @param businessCommunityMember 小区成员信息 封装
     * @throws DAOException 操作数据库异常
     */
    public void saveBusinessCommunityMember(Map businessCommunityMember) throws DAOException {
        logger.debug("小区成员加入 入参 businessCommunityMember : {}", businessCommunityMember);
        businessCommunityMember.put("month", DateUtil.getCurrentMonth());
        int saveFlag = sqlSessionTemplate.insert("communityServiceDaoImpl.saveBusinessCommunityMember", businessCommunityMember);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "小区成员加入失败：" + JSONObject.toJSONString(businessCommunityMember));
        }
    }

    /**
     * 成员加入 保存信息至instance
     *
     * @param info
     * @throws DAOException
     */
    @Override
    public void saveCommunityMemberInstance(Map info) throws DAOException {
        logger.debug("小区成员加入Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("communityServiceDaoImpl.saveCommunityMemberInstance", info);

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
    public Map getBusinessCommunityMember(Map info) throws DAOException {
        logger.debug("查询小区成员加入信息 入参 info : {}", info);

        List<Map> businessCommunityMembers = sqlSessionTemplate.selectList("communityServiceDaoImpl.getBusinessCommunityMember", info);
        if (businessCommunityMembers == null || businessCommunityMembers.size() == 0) {
            return null;
        }
        if (businessCommunityMembers.size() > 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "根据条件查询有多条数据,数据异常，请检查：businessCommunityMember，" + JSONObject.toJSONString(info));
        }

        return businessCommunityMembers.get(0);
    }

    /**
     * 查询小区成员加入信息（instance过程）
     * 根据bId 查询小区信息
     *
     * @param info bId 信息
     * @return 小区信息
     * @throws DAOException
     */
    public Map getCommunityMember(Map info) throws DAOException {
        logger.debug("查询小区成员加入信息 入参 info : {}", info);

        List<Map> memberCommunitys = sqlSessionTemplate.selectList("communityServiceDaoImpl.getCommunityMember", info);
        if (memberCommunitys == null || memberCommunitys.size() == 0) {
            return null;
        }
        if (memberCommunitys.size() > 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "根据条件查询有多条数据,数据异常，请检查：getCommunityMember，" + JSONObject.toJSONString(info));
        }

        return memberCommunitys.get(0);
    }

    /**
     * 修改小区成员加入信息
     *
     * @param info 修改信息
     * @throws DAOException
     */
    public void updateCommunityMemberInstance(Map info) throws DAOException {
        logger.debug("修改小区成员加入信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("communityServiceDaoImpl.updateCommunityMemberInstance", info);

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
    public List<Map> getCommunityMembers(Map info) throws DAOException {
        logger.debug("查询小区成员加入信息 入参 info : {}", info);

        List<Map> memberCommunitys = sqlSessionTemplate.selectList("communityServiceDaoImpl.getCommunityMember", info);

        return memberCommunitys;
    }

    @Override
    public int getCommunityMemberCount(Map info) {
        logger.debug("查询小区成员加入信息 入参 info : {}", info);

        List<Map> memberCommunitys = sqlSessionTemplate.selectList("communityServiceDaoImpl.getCommunityMemberCount", info);

        if (memberCommunitys.size() < 1) {
            return 0;
        }

        return Integer.parseInt(memberCommunitys.get(0).get("count").toString());
    }
}
