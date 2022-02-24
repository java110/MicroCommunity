package com.java110.common.listener.msg;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IMsgServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.message.MsgPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 消息信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveMsgInfoListener")
@Transactional
public class SaveMsgInfoListener extends AbstractMsgBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveMsgInfoListener.class);

    @Autowired
    private IMsgServiceDao msgServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_MSG;
    }

    /**
     * 保存消息信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessMsg 节点
        if (data.containsKey(MsgPo.class.getSimpleName())) {
            Object bObj = data.get(MsgPo.class.getSimpleName());
            JSONArray businessMsgs = null;
            if (bObj instanceof JSONObject) {
                businessMsgs = new JSONArray();
                businessMsgs.add(bObj);
            } else {
                businessMsgs = (JSONArray) bObj;
            }
            //JSONObject businessMsg = data.getJSONObject("businessMsg");
            for (int bMsgIndex = 0; bMsgIndex < businessMsgs.size(); bMsgIndex++) {
                JSONObject businessMsg = businessMsgs.getJSONObject(bMsgIndex);
                doBusinessMsg(business, businessMsg);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("msgId", businessMsg.getString("msgId"));
                }
            }
        }
    }

    /**
     * business 数据转移到 instance
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();

        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_ADD);

        //消息信息
        List<Map> businessMsgInfo = msgServiceDaoImpl.getBusinessMsgInfo(info);
        if (businessMsgInfo != null && businessMsgInfo.size() > 0) {
            reFreshShareColumn(info, businessMsgInfo.get(0));
            msgServiceDaoImpl.saveMsgInfoInstance(info);
            if (businessMsgInfo.size() == 1) {
                dataFlowContext.addParamOut("msgId", businessMsgInfo.get(0).get("msg_id"));
            }
        }
    }


    /**
     * 刷 分片字段
     *
     * @param info         查询对象
     * @param businessInfo 小区ID
     */
    private void reFreshShareColumn(Map info, Map businessInfo) {

        if (info.containsKey("msgType")) {
            return;
        }

        if (!businessInfo.containsKey("msg_type")) {
            return;
        }

        info.put("msgType", businessInfo.get("msg_type"));
    }

    /**
     * 撤单
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");
        Map info = new HashMap();
        info.put("bId", bId);
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        Map paramIn = new HashMap();
        paramIn.put("bId", bId);
        paramIn.put("statusCd", StatusConstant.STATUS_CD_INVALID);
        //消息信息
        List<Map> msgInfo = msgServiceDaoImpl.getMsgInfo(info);
        if (msgInfo != null && msgInfo.size() > 0) {
            reFreshShareColumn(paramIn, msgInfo.get(0));
            msgServiceDaoImpl.updateMsgInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessMsg 节点
     *
     * @param business    总的数据节点
     * @param businessMsg 消息节点
     */
    private void doBusinessMsg(Business business, JSONObject businessMsg) {

        Assert.jsonObjectHaveKey(businessMsg, "msgId", "businessMsg 节点下没有包含 msgId 节点");

        if (businessMsg.getString("msgId").startsWith("-")) {
            //刷新缓存
            //flushMsgId(business.getDatas());

            businessMsg.put("msgId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_msgId));

        }

        businessMsg.put("bId", business.getbId());
        businessMsg.put("operate", StatusConstant.OPERATE_ADD);
        //保存消息信息
        msgServiceDaoImpl.saveBusinessMsgInfo(businessMsg);

    }

    public IMsgServiceDao getMsgServiceDaoImpl() {
        return msgServiceDaoImpl;
    }

    public void setMsgServiceDaoImpl(IMsgServiceDao msgServiceDaoImpl) {
        this.msgServiceDaoImpl = msgServiceDaoImpl;
    }
}
