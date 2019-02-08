package com.java110.comment.listener;

import com.alibaba.fastjson.JSONArray;
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
import java.util.List;
import java.util.Map;

/**
 * 保存 评论 侦听
 * 处理 comment 和 subComment 节点
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveSubCommentAttrListener")
@Transactional
public class SaveSubCommentAttrListener extends AbstractCommentBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(SaveSubCommentAttrListener.class);

    @Autowired
    ICommentServiceDao commentServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
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
        if(data.containsKey("subCommentAttr")){
            JSONArray subCommentAttrs = data.getJSONArray("subCommentAttr");
            doSubCommentAttr(business,subCommentAttrs);
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

        //商品属性
        List<Map> subCommentAttrs = commentServiceDaoImpl.getSubCommentAttrs(info);
        if(subCommentAttrs != null && subCommentAttrs.size()>0){
            commentServiceDaoImpl.updateSubCommentAttrInstance(paramIn);
        }
    }




    /**
     * 处理商品 属性
     * @param business 当前业务
     * @param subCommentAttrs 评论属性
     */
    private void doSubCommentAttr(Business business, JSONArray subCommentAttrs) {

        for(int subCommentAttrIndex = 0 ; subCommentAttrIndex < subCommentAttrs.size();subCommentAttrIndex ++){
            JSONObject subCommentAttr = subCommentAttrs.getJSONObject(subCommentAttrIndex);
            Assert.jsonObjectHaveKey(subCommentAttr,"attrId","subCommentAttr 节点下没有包含 attrId 节点");
            subCommentAttr.put("bId",business.getbId());
            commentServiceDaoImpl.saveSubCommentAttrInstance(subCommentAttr);
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
