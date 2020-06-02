package com.java110.comment.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.comment.dao.ICommentServiceDao;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 如果commentId填写的值为-1,则重新生成
 * Created by wuxw on 2018/7/12.
 */
@Java110Listener(name="flushAboutCommentIdListener")
public class FlushAboutCommentIdListener extends AbstractCommentBusinessServiceDataFlowListener {

    protected final static Logger logger = LoggerFactory.getLogger(FlushAboutCommentIdListener.class);


    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_COMMENT_INFO;
    }

    @Override
    public ICommentServiceDao getCommentServiceDaoImpl() {
        return null;
    }

    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");
        //刷新commentId
        if(data.containsKey("comment")){
            JSONObject comment = data.getJSONObject("comment");
            if(!comment.containsKey("commentId") || comment.getString("commentId").startsWith("-")){
                flushCommentId(data);
            }
        }
        //刷新 subCommentId
        if(data.containsKey("subComment")){
            JSONObject subComment = data.getJSONObject("subComment");
            if(!subComment.containsKey("subCommentId") || subComment.getString("subCommentId").startsWith("-")){
                flushSubCommentId(data);
            }
        }
        //刷新 attrId
        if(data.containsKey("subCommentAttr")){
            JSONArray subCommentAttrs = data.getJSONArray("subCommentAttr");
            for (int subCommentAttrIndex = 0;subCommentAttrIndex < subCommentAttrs.size();subCommentAttrIndex ++){
                JSONObject subCommentAttr = subCommentAttrs.getJSONObject(subCommentAttrIndex);
                if(subCommentAttr.containsKey("attrId") && !subCommentAttr.getString("attrId").startsWith("-")){
                    continue;
                }
                subCommentAttr.put("attrId",GenerateCodeFactory.getSubCommentAttrId());
            }
        }
        //刷新 commentPhotoId
        if(data.containsKey("subCommentPhoto")){
            JSONArray subCommentPhotos = data.getJSONArray("subCommentPhoto");
            for (int subCommentPhotoIndex = 0;subCommentPhotoIndex < subCommentPhotos.size();subCommentPhotoIndex ++){
                JSONObject subCommentPhoto = subCommentPhotos.getJSONObject(subCommentPhotoIndex);
                if(subCommentPhoto.containsKey("commentPhotoId") && !subCommentPhoto.getString("commentPhotoId").startsWith("-")){
                    continue;
                }
                subCommentPhoto.put("commentPhotoId",GenerateCodeFactory.getCommentPhotoId());
            }
        }

        //刷新 commentPhotoId
        if(data.containsKey("commentScore")){
            JSONArray commentScores = data.getJSONArray("commentScore");
            for (int commentScoreIndex = 0;commentScoreIndex < commentScores.size();commentScoreIndex ++){
                JSONObject commentScore = commentScores.getJSONObject(commentScoreIndex);
                if(commentScore.containsKey("commentScoreId") && !commentScore.getString("commentScoreId").startsWith("-")){
                    continue;
                }
                commentScore.put("commentScoreId",GenerateCodeFactory.getCommentScoreId());
            }
        }


    }

    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        // nothing to do
    }

    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        // nothing to do
    }

    /**
     * 刷新 commentID
     * @param data
     */
    private void flushCommentId(JSONObject data) {

        String commentId = GenerateCodeFactory.getCommentId();
        JSONObject comment = data.getJSONObject("comment");
        comment.put("commentId",commentId);
        //刷商品属性
        if(data.containsKey("subComment")) {
            JSONObject subCommentObj = data.getJSONObject("subComment");
            subCommentObj.put("commentId",commentId);
        }

        //刷 是商品照片 的 shopId
        if(data.containsKey("commentScore")) {
            JSONArray commentScores = data.getJSONArray("commentScore");
            for(int commentScoreIndex = 0;commentScoreIndex < commentScores.size();commentScoreIndex++) {
                JSONObject commentScore = commentScores.getJSONObject(commentScoreIndex);
                commentScore.put("commentId", commentId);
            }
        }
    }

    /**
     * 刷新 subCommentId
     *
     * @param data 数据节点
     */
    private void flushSubCommentId(JSONObject data){

        String subCommentId = GenerateCodeFactory.getSubCommentId();
        JSONObject subComment = data.getJSONObject("subComment");
        subComment.put("subCommentId",subCommentId);
        //刷评论属性
        if(data.containsKey("subCommentAttr")) {
            JSONArray subCommentAttrs = data.getJSONArray("subCommentAttr");
            for(int subCommentAttrIndex = 0;subCommentAttrIndex < subCommentAttrs.size();subCommentAttrIndex++) {
                JSONObject subCommentAttr = subCommentAttrs.getJSONObject(subCommentAttrIndex);
                subCommentAttr.put("subCommentId", subCommentId);
            }
        }

        //刷照片
        if(data.containsKey("subCommentPhoto")) {
            JSONArray subCommentPhotos = data.getJSONArray("subCommentPhoto");
            for(int subCommentPhotoIndex = 0;subCommentPhotoIndex < subCommentPhotos.size();subCommentPhotoIndex++) {
                JSONObject subCommentPhoto = subCommentPhotos.getJSONObject(subCommentPhotoIndex);
                subCommentPhoto.put("subCommentId", subCommentId);
            }
        }
    }
}
