package com.java110.comment.listener;

import com.alibaba.fastjson.JSONArray;
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
 * 保存 评论 侦听
 * 处理 comment 和 subComment 节点
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveCommentScoreListener")
@Transactional
public class SaveCommentScoreListener extends AbstractCommentBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(SaveCommentScoreListener.class);

    @Autowired
    ICommentServiceDao commentServiceDaoImpl;

    @Override
    public int getOrder() {
        return 5;
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
        if(data.containsKey("commentScore")){
            JSONArray commentScores = data.getJSONArray("commentScore");
            doCommentScore(business,commentScores);
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
        Map info = new HashMap();
        info.put("bId",bId);
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        Map paramIn = new HashMap();
        paramIn.put("bId",bId);
        paramIn.put("statusCd",StatusConstant.STATUS_CD_INVALID);

        //评论照片
        List<Map> commentScores = commentServiceDaoImpl.getCommentScores(info);
        if(commentScores != null && commentScores.size()>0){
            commentServiceDaoImpl.updateCommentScoreInstance(paramIn);
        }
    }




    /**
     * 处理商品 属性
     * @param business 当前业务
     * @param commentScores 评论分数
     */
    private void doCommentScore(Business business, JSONArray commentScores) {

        for(int commentScoreIndex = 0 ; commentScoreIndex < commentScores.size();commentScoreIndex ++){
            JSONObject commentScore = commentScores.getJSONObject(commentScoreIndex);
            Assert.jsonObjectHaveKey(commentScore,"scoreTypeCd","commentScores 节点下没有包含 scoreTypeCd 节点");
            Assert.jsonObjectHaveKey(commentScore,"value","commentScores 节点下没有包含 value 节点");
            commentScore.put("bId",business.getbId());
            commentServiceDaoImpl.saveCommentScoreInstance(commentScore);
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
