package com.java110.common.listener.msgRead;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IMsgReadServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.message.MsgReadPo;
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
 * 保存 消息阅读信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveMsgReadInfoListener")
@Transactional
public class SaveMsgReadInfoListener extends AbstractMsgReadBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveMsgReadInfoListener.class);

    @Autowired
    private IMsgReadServiceDao msgReadServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_MSG_READ;
    }

    /**
     * 保存消息阅读信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessMsgRead 节点
        if (data.containsKey(MsgReadPo.class.getSimpleName())) {
            Object bObj = data.get(MsgReadPo.class.getSimpleName());
            JSONArray businessMsgReads = null;
            if (bObj instanceof JSONObject) {
                businessMsgReads = new JSONArray();
                businessMsgReads.add(bObj);
            } else {
                businessMsgReads = (JSONArray) bObj;
            }
            //JSONObject businessMsgRead = data.getJSONObject("businessMsgRead");
            for (int bMsgReadIndex = 0; bMsgReadIndex < businessMsgReads.size(); bMsgReadIndex++) {
                JSONObject businessMsgRead = businessMsgReads.getJSONObject(bMsgReadIndex);
                doBusinessMsgRead(business, businessMsgRead);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("msgReadId", businessMsgRead.getString("msgReadId"));
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

        //消息阅读信息
        List<Map> businessMsgReadInfo = msgReadServiceDaoImpl.getBusinessMsgReadInfo(info);
        if (businessMsgReadInfo != null && businessMsgReadInfo.size() > 0) {
            reFreshShareColumn(info, businessMsgReadInfo.get(0));
            msgReadServiceDaoImpl.saveMsgReadInfoInstance(info);
            if (businessMsgReadInfo.size() == 1) {
                dataFlowContext.addParamOut("msgReadId", businessMsgReadInfo.get(0).get("msg_read_id"));
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

        if (info.containsKey("msgId")) {
            return;
        }

        if (!businessInfo.containsKey("msg_id")) {
            return;
        }

        info.put("msgId", businessInfo.get("msg_id"));
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
        //消息阅读信息
        List<Map> msgReadInfo = msgReadServiceDaoImpl.getMsgReadInfo(info);
        if (msgReadInfo != null && msgReadInfo.size() > 0) {
            reFreshShareColumn(paramIn, msgReadInfo.get(0));
            msgReadServiceDaoImpl.updateMsgReadInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessMsgRead 节点
     *
     * @param business        总的数据节点
     * @param businessMsgRead 消息阅读节点
     */
    private void doBusinessMsgRead(Business business, JSONObject businessMsgRead) {

        Assert.jsonObjectHaveKey(businessMsgRead, "msgReadId", "businessMsgRead 节点下没有包含 msgReadId 节点");

        if (businessMsgRead.getString("msgReadId").startsWith("-")) {
            //刷新缓存
            //flushMsgReadId(business.getDatas());

            businessMsgRead.put("msgReadId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_msgReadId));

        }

        businessMsgRead.put("bId", business.getbId());
        businessMsgRead.put("operate", StatusConstant.OPERATE_ADD);
        //保存消息阅读信息
        msgReadServiceDaoImpl.saveBusinessMsgReadInfo(businessMsgRead);

    }

    public IMsgReadServiceDao getMsgReadServiceDaoImpl() {
        return msgReadServiceDaoImpl;
    }

    public void setMsgReadServiceDaoImpl(IMsgReadServiceDao msgReadServiceDaoImpl) {
        this.msgReadServiceDaoImpl = msgReadServiceDaoImpl;
    }
}
