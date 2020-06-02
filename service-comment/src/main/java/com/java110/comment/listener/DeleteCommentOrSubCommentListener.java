package com.java110.comment.listener;

import com.alibaba.fastjson.JSONObject;
import com.java110.comment.dao.ICommentServiceDao;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 删除商户信息 侦听
 *
 * 处理节点
 * 1、businessShop:{} 商户基本信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("deleteCommentOrSubCommentListener")
@Transactional
public class DeleteCommentOrSubCommentListener extends AbstractCommentBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(DeleteCommentOrSubCommentListener.class);
    @Autowired
    ICommentServiceDao commentServiceDaoImpl;

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_COMMENT_INFO;
    }

    /**
     * 根据删除信息 查出Instance表中数据 保存至business表 （状态写DEL） 方便撤单时直接更新回去
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();

        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessShop 节点 按理这里不应该处理，程序上支持，以防真有这种业务
        if(data.containsKey("comment")){
            JSONObject comment = data.getJSONObject("comment");
            doComment(business,comment);
            dataFlowContext.addParamOut("commentId",comment.getString("commentId"));
            return ;
        }

        if(data.containsKey("subComment")){
            JSONObject subComment = data.getJSONObject("subComment");
            doSubComment(business,subComment);
            dataFlowContext.addParamOut("subCommentId",subComment.getString("subCommentId"));
        }
    }

    /**
     * 删除 instance数据
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        // nothing to do
    }

    /**
     * 撤单
     * 从business表中查询到DEL的数据 将instance中的数据更新回来
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");
        Map info = new HashMap();
        info.put("bId",bId);
        info.put("statusCd",StatusConstant.STATUS_CD_INVALID);

        Map delInfo = new HashMap();
        delInfo.put("bId",business.getbId());
        delInfo.put("operate",StatusConstant.STATUS_CD_VALID);
        //评论信息
        Map comment = commentServiceDaoImpl.getComment(info);
        if(comment != null && !comment.isEmpty()){
            commentServiceDaoImpl.updateCommentInstance(delInfo);
        }

        //评论信息
        List<Map> subComments = commentServiceDaoImpl.getSubComments(info);
        if(subComments != null && subComments.size() > 0){
            commentServiceDaoImpl.updateSubCommentInstance(delInfo);
        }

        //评论属性信息
        List<Map> commentAttrs = commentServiceDaoImpl.getSubCommentAttrs(info);
        if(commentAttrs != null && commentAttrs.size() > 0){
            commentServiceDaoImpl.updateSubCommentAttrInstance(delInfo);
        }

        //评论照片信息
        List<Map> commentPhotos = commentServiceDaoImpl.getSubCommentPhotos(info);
        if(commentPhotos != null && commentPhotos.size() > 0){
            commentServiceDaoImpl.updateSubCommentPhotoInstance(delInfo);
        }
        //评论分数信息
        List<Map> commentScores = commentServiceDaoImpl.getCommentScores(info);
        if(commentScores != null && commentScores.size() > 0){
            commentServiceDaoImpl.updateCommentScoreInstance(delInfo);
        }
    }


    /**
     * 处理 businessShop 节点
     * @param business 总的数据节点
     * @param comment 商户节点
     */
    private void doComment(Business business,JSONObject comment){

        Assert.jsonObjectHaveKey(comment,"commentId","comment 节点下没有包含 commentId 节点");

        //作废 comment
        Map info = new HashMap();
        info.put("commentId",comment.getString("commentId"));
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);

        Map delInfo = new HashMap();
        //delInfo.put("bId",business.getbId());
        delInfo.put("statusCd",StatusConstant.STATUS_CD_INVALID);

        Map commentMap = commentServiceDaoImpl.getComment(info);

        Assert.notEmpty(commentMap,"没有找到需要作废的评论，入参错误：" + comment);

        delInfo.put("commentId",comment.getString("commentId"));
        commentServiceDaoImpl.updateCommentInstance(delInfo);

        // 作废 comment_score

        List<Map> commentScores = commentServiceDaoImpl.getCommentScores(info);
        if(commentScores != null && commentScores.size()>0){
            commentServiceDaoImpl.updateCommentScoreInstance(delInfo);
        }

        // 作废 sub_comment
        List<Map> subComments = commentServiceDaoImpl.getSubComments(info);

        if(subComments == null || subComments.size() == 0){
            return ;
        }
        commentServiceDaoImpl.updateSubCommentInstance(delInfo);

        for(Map subComment : subComments){
            // 作废 sub_comment_attr
            doDeleteCommentAttrAndCommentPhoto(info,delInfo,subComment);
        }

    }


    /**
     * 处理 businessShop 节点
     * @param business 总的数据节点
     * @param subComment 商户节点
     */
    private void doSubComment(Business business,JSONObject subComment){

        Assert.jsonObjectHaveKey(subComment,"subCommentId","subComment 节点下没有包含 subCommentId 节点");

        //作废 comment
        Map info = new HashMap();
        info.put("subCommentId",subComment.getString("subCommentId"));
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);

        Map delInfo = new HashMap();
        //delInfo.put("bId",business.getbId());
        delInfo.put("subCommentId",subComment.getString("subCommentId"));
        delInfo.put("statusCd",StatusConstant.STATUS_CD_INVALID);


        // 作废 sub_comment
        Map subCommentMap = commentServiceDaoImpl.getSubComment(info);

        Assert.notEmpty(subCommentMap,"没有找到需要作废的评论，入参错误：" + subComment);

        commentServiceDaoImpl.updateSubCommentInstance(delInfo);

        doDeleteCommentAttrAndCommentPhoto(info,delInfo,subComment);

    }

    private void doDeleteCommentAttrAndCommentPhoto(Map info,Map delInfo,Map subComment){
        // 作废 sub_comment_attr
        info.put("subCommentId",subComment.get("sub_comment_id"));
        delInfo.put("subCommentId",subComment.get("sub_comment_id"));
        List<Map> commentAttrs = commentServiceDaoImpl.getSubCommentAttrs(info);
        if(commentAttrs != null && commentAttrs.size() >0){
            commentServiceDaoImpl.updateSubCommentAttrInstance(delInfo);
        }
        // 作废 sub_comment_photo
        List<Map> commentPhotos = commentServiceDaoImpl.getSubCommentPhotos(info);
        if(commentPhotos != null && commentPhotos.size() >0){
            commentServiceDaoImpl.updateSubCommentPhotoInstance(delInfo);
        }
    }

    @Override
    public ICommentServiceDao getCommentServiceDaoImpl() {
        return commentServiceDaoImpl;
    }

    public void setCommentServiceDaoImpl(ICommentServiceDao commentServiceDaoImpl) {
        this.commentServiceDaoImpl = commentServiceDaoImpl;
    }
}
