package com.java110.comment.listener;

import com.alibaba.fastjson.JSONObject;
import com.java110.comment.dao.ICommentServiceDao;
import com.java110.common.constant.BusinessTypeConstant;
import com.java110.common.constant.ServiceCodeConstant;
import com.java110.common.constant.StatusConstant;
import com.java110.common.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 保存 评论 侦听
 * 处理 comment 和 subComment 节点
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveCommentAndSubCommentListener")
@Transactional
public class SaveCommentAndSubCommentListener extends AbstractCommentBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(SaveCommentAndSubCommentListener.class);

    @Autowired
    ICommentServiceDao commentServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_COMMENT_INFO;
    }

    /**
     * 保存评论信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessShop 节点
        if(data.containsKey("comment")){
            JSONObject comment = data.getJSONObject("comment");
            doComment(business,comment);
            dataFlowContext.addParamOut("commentId",comment.getString("commentId"));
        }

        if(data.containsKey("subComment")){
            JSONObject subComment = data.getJSONObject("subComment");
            doSubComment(business,subComment);
        }

    }

    /**
     * business 数据转移到 instance
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        //todo buy 没有business过程，所以这里不做处理
    }

    /**
     * 撤单
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");
        Map info = new HashMap();
        info.put("bId",bId);
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        Map paramIn = new HashMap();
        paramIn.put("bId",bId);
        paramIn.put("statusCd",StatusConstant.STATUS_CD_INVALID);
        //商户信息
        Map comment = commentServiceDaoImpl.getComment(info);
        if(comment != null && !comment.isEmpty()){
            paramIn.put("commentId",comment.get("comment_id").toString());
            commentServiceDaoImpl.updateCommentInstance(paramIn);
            dataFlowContext.addParamOut("commentId",comment.get("comment_id"));
        }
        //评论内容
        Map subComment = commentServiceDaoImpl.getSubComment(info);
        if(subComment != null && !subComment.isEmpty()){
            paramIn.put("subCommentId",subComment.get("sub_comment_id").toString());
            commentServiceDaoImpl.updateSubCommentInstance(paramIn);
            dataFlowContext.addParamOut("subCommentId",subComment.get("sub_comment_id"));
        }
    }



    /**
     * 处理 comment 节点
     * @param business 总的数据节点
     * @param comment 评论信息
     */
    private void doComment(Business business,JSONObject comment){

        Assert.jsonObjectHaveKey(comment,"commentId","comment 节点下没有包含 commentId 节点");

        Assert.jsonObjectHaveKey(comment,"outId","comment 节点下没有包含 outId 节点");

        Assert.jsonObjectHaveKey(comment,"userId","comment 节点下没有包含 userId 节点");

        comment.put("bId",business.getbId());

        //保存商户信息
        commentServiceDaoImpl.saveCommentInstance(comment);

    }

    /**
     * 处理 subComment 节点
     * @param business 总的数据节点
     * @param subComment 评论信息
     */
    private void doSubComment(Business business,JSONObject subComment){

        Assert.jsonObjectHaveKey(subComment,"commentId","subComment 节点下没有包含 commentId 节点");

        Assert.jsonObjectHaveKey(subComment,"subCommentTypeCd","subComment 节点下没有包含 subCommentTypeCd 节点");

        Assert.jsonObjectHaveKey(subComment,"commentContext","subComment 节点下没有包含 commentContext 节点");

        subComment.put("bId",business.getbId());

        //保存商户信息
        commentServiceDaoImpl.saveSubCommentInstance(subComment);

    }




    @Override
    public ICommentServiceDao getCommentServiceDaoImpl() {
        return commentServiceDaoImpl;
    }

    public void setCommentServiceDaoImpl(ICommentServiceDao commentServiceDaoImpl) {
        this.commentServiceDaoImpl = commentServiceDaoImpl;
    }
}
