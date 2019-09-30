package com.java110.comment.dao;


import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 商品组件内部之间使用，没有给外围系统提供服务能力
 * 商品服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface ICommentServiceDao {



    /**
     * 保存评论
     * @param comment 评论
     * @throws DAOException
     */
    public void saveCommentInstance(Map comment) throws DAOException;


    /**
     * 保存评论
     * @param subComment 评论内容
     * @throws DAOException
     */
    public void saveSubCommentInstance(Map subComment) throws DAOException;

    /**
     * 保存商品购买记录属性
     * @param subCommentAttr
     * @throws DAOException
     */
    public void saveSubCommentAttrInstance(Map subCommentAttr) throws DAOException;

    /**
     * 保存评论分数属性
     * @param subCommentScore
     * @throws DAOException
     */
    public void saveCommentScoreInstance(Map subCommentScore) throws DAOException;

    /**
     * 保存 评论照片
     * @param subCommentPhoto
     * @throws DAOException
     */
    public void saveSubCommentPhotoInstance(Map subCommentPhoto) throws DAOException;



    /**
     * 评论查询（instance）
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    public Map getComment(Map info) throws DAOException;

    /**
     * 评论内容查询（instance）
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    public Map getSubComment(Map info) throws DAOException;

    /**
     * 评论内容查询（instance）
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    public List<Map> getSubComments(Map info) throws DAOException;
    /**
     * 评论属性查询（instance）
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    public List<Map> getSubCommentAttrs(Map info) throws DAOException;

    /**
     * 评论照片查询（instance）
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    public List<Map> getSubCommentPhotos(Map info) throws DAOException;

    /**
     * 评论分数查询（instance）
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    public List<Map> getCommentScores(Map info) throws DAOException;


    /**
     * 修改评论信息
     * @param info 修改信息
     * @throws DAOException
     */
    public void updateCommentInstance(Map info) throws DAOException;

    /**
     * 修改评论内容信息
     * @param info 修改信息
     * @throws DAOException
     */
    public void updateSubCommentInstance(Map info) throws DAOException;

    /**
     * 修改评论属性信息（instance）
     * @param info 修改信息
     * @throws DAOException
     */
    public void updateSubCommentAttrInstance(Map info) throws DAOException;

    /**
     * 修改评论照片信息（instance）
     * @param info 修改信息
     * @throws DAOException
     */
    public void updateSubCommentPhotoInstance(Map info) throws DAOException;

    /**
     * 修改评论照片信息（instance）
     * @param info 修改信息
     * @throws DAOException
     */
    public void updateCommentScoreInstance(Map info) throws DAOException;
}