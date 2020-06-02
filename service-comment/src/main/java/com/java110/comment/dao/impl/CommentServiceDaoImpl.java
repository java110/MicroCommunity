package com.java110.comment.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.comment.dao.ICommentServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 商品服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("commentServiceDaoImpl")
//@Transactional
public class CommentServiceDaoImpl extends BaseServiceDao implements ICommentServiceDao {

    private final static Logger logger = LoggerFactory.getLogger(CommentServiceDaoImpl.class);






    /**
     * 保存评论信息
     * @param comment
     * @throws DAOException
     */
    @Override
    public void saveCommentInstance(Map comment) throws DAOException {
        comment.put("month", DateUtil.getCurrentMonth());
        logger.debug("保存评论记录信息 入参 comment : {}",comment);

        int saveFlag = sqlSessionTemplate.insert("commentServiceDaoImpl.saveCommentInstance",comment);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存评论数据失败："+ JSONObject.toJSONString(comment));
        }
    }

    /**
     * 保存评论信息
     * @param subComment
     * @throws DAOException
     */
    @Override
    public void saveSubCommentInstance(Map subComment) throws DAOException {
        subComment.put("month", DateUtil.getCurrentMonth());
        logger.debug("保存评论记录信息 入参 subComment : {}",subComment);

        int saveFlag = sqlSessionTemplate.insert("commentServiceDaoImpl.saveSubCommentInstance",subComment);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存评论数据失败："+ JSONObject.toJSONString(subComment));
        }
    }


    /**
     * 购买商品属性保存
     * @param subCommentAttr
     * @throws DAOException
     */
    @Override
    public void saveSubCommentAttrInstance(Map subCommentAttr) throws DAOException {
        subCommentAttr.put("month", DateUtil.getCurrentMonth());
        logger.debug("保存评论属性信息 入参 subCommentAttr : {}",subCommentAttr);

        int saveFlag = sqlSessionTemplate.insert("commentServiceDaoImpl.saveSubCommentAttrInstance",subCommentAttr);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存评论属性数据失败："+ JSONObject.toJSONString(subCommentAttr));
        }
    }


    /**
     * 评论查询（instance）
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public Map getComment(Map info) throws DAOException {
        logger.debug("查询评论信息 入参 info : {}",info);

        List<Map> comments = sqlSessionTemplate.selectList("commentServiceDaoImpl.getComment",info);
        if(comments == null || comments.size() == 0){
            return null;
        }
        if(comments.size() >1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"根据条件查询有多条数据,数据异常，请检查：getShopCatalog，"+ JSONObject.toJSONString(info));
        }
        return comments.get(0);
    }

    /**
     * 评论内容查询（instance）
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public Map getSubComment(Map info) throws DAOException {
        logger.debug("查询评论信息 入参 info : {}",info);

        List<Map> subComments = sqlSessionTemplate.selectList("commentServiceDaoImpl.getSubComment",info);
        if(subComments == null || subComments.size() == 0){
            return null;
        }
        return subComments.get(0);
    }

    /**
     * 评论内容查询（instance）
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> getSubComments(Map info) throws DAOException {
        logger.debug("查询评论信息 入参 info : {}",info);

        List<Map> subComments = sqlSessionTemplate.selectList("commentServiceDaoImpl.getSubComment",info);

        return subComments;
    }

    /**
     * 商品属性查询（instance）
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> getSubCommentAttrs(Map info) throws DAOException {
        logger.debug("查询评论属性信息 入参 info : {}",info);

        List<Map> subCommentAttrs = sqlSessionTemplate.selectList("commentServiceDaoImpl.getSubCommentAttrs",info);

        return subCommentAttrs;
    }
    /**
     * 修改评论信息
     * @param info 修改信息
     * @throws DAOException
     */
    @Override
    public void updateCommentInstance(Map info) throws DAOException {
        logger.debug("修改评论信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("commentServiceDaoImpl.updateCommentInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改评论信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    /**
     * 修改评论信息
     * @param info 修改信息
     * @throws DAOException
     */
    @Override
    public void updateSubCommentInstance(Map info) throws DAOException {
        logger.debug("修改评论信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("commentServiceDaoImpl.updateSubCommentInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改评论信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    /**
     * 修改商品购买属性信息（instance）
     * @param info 修改信息
     * @throws DAOException
     */
    @Override
    public void updateSubCommentAttrInstance(Map info) throws DAOException {
        logger.debug("修改评价属性信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("commentServiceDaoImpl.updateSubCommentAttrInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改评价属性信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    @Override
    public void saveSubCommentPhotoInstance(Map subCommentPhoto) throws DAOException {
        subCommentPhoto.put("month", DateUtil.getCurrentMonth());
        logger.debug("保存评论照片信息 入参 subCommentPhoto : {}",subCommentPhoto);

        int saveFlag = sqlSessionTemplate.insert("commentServiceDaoImpl.saveSubCommentPhotoInstance",subCommentPhoto);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存评论照片数据失败："+ JSONObject.toJSONString(subCommentPhoto));
        }
    }

    @Override
    public List<Map> getSubCommentPhotos(Map info) throws DAOException {
        logger.debug("查询评论照片信息 入参 info : {}",info);

        List<Map> subCommentPhotos = sqlSessionTemplate.selectList("commentServiceDaoImpl.getSubCommentPhotos",info);

        return subCommentPhotos;
    }

    @Override
    public void updateSubCommentPhotoInstance(Map info) throws DAOException {
        logger.debug("修改评价照片信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("commentServiceDaoImpl.updateSubCommentPhotoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改评价照片信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    @Override
    public void saveCommentScoreInstance(Map commentScore) throws DAOException {
        commentScore.put("month", DateUtil.getCurrentMonth());
        logger.debug("保存评论分数信息 入参 subCommentScore : {}",commentScore);

        int saveFlag = sqlSessionTemplate.insert("commentServiceDaoImpl.saveCommentScoreInstance",commentScore);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存评论分数数据失败："+ JSONObject.toJSONString(commentScore));
        }
    }

    @Override
    public List<Map> getCommentScores(Map info) throws DAOException {
        logger.debug("查询评论分数信息 入参 info : {}",info);

        List<Map> subCommentScores = sqlSessionTemplate.selectList("commentServiceDaoImpl.getCommentScores",info);

        return subCommentScores;
    }

    @Override
    public void updateCommentScoreInstance(Map info) throws DAOException {
        logger.debug("修改评价分数信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("commentServiceDaoImpl.updateCommentScoreInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改评价分数信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }
}
