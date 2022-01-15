package com.java110.common.listener.msgRead;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IMsgReadServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.message.MsgReadPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 修改消息阅读信息 侦听
 * <p>
 * 处理节点
 * 1、businessMsgRead:{} 消息阅读基本信息节点
 * 2、businessMsgReadAttr:[{}] 消息阅读属性信息节点
 * 3、businessMsgReadPhoto:[{}] 消息阅读照片信息节点
 * 4、businessMsgReadCerdentials:[{}] 消息阅读证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateMsgReadInfoListener")
@Transactional
public class UpdateMsgReadInfoListener extends AbstractMsgReadBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateMsgReadInfoListener.class);
    @Autowired
    private IMsgReadServiceDao msgReadServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_MSG_READ;
    }

    /**
     * business过程
     *
     * @param dataFlowContext 上下文对象
     * @param business        业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {

        JSONObject data = business.getDatas();

        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");


        //处理 businessMsgRead 节点
        if (data.containsKey(MsgReadPo.class.getSimpleName())) {
            Object _obj = data.get(MsgReadPo.class.getSimpleName());
            JSONArray businessMsgReads = null;
            if (_obj instanceof JSONObject) {
                businessMsgReads = new JSONArray();
                businessMsgReads.add(_obj);
            } else {
                businessMsgReads = (JSONArray) _obj;
            }
            //JSONObject businessMsgRead = data.getJSONObject("businessMsgRead");
            for (int _msgReadIndex = 0; _msgReadIndex < businessMsgReads.size(); _msgReadIndex++) {
                JSONObject businessMsgRead = businessMsgReads.getJSONObject(_msgReadIndex);
                doBusinessMsgRead(business, businessMsgRead);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("msgReadId", businessMsgRead.getString("msgReadId"));
                }
            }

        }
    }


    /**
     * business to instance 过程
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
        List<Map> businessMsgReadInfos = msgReadServiceDaoImpl.getBusinessMsgReadInfo(info);
        if (businessMsgReadInfos != null && businessMsgReadInfos.size() > 0) {
            for (int _msgReadIndex = 0; _msgReadIndex < businessMsgReadInfos.size(); _msgReadIndex++) {
                Map businessMsgReadInfo = businessMsgReadInfos.get(_msgReadIndex);
                flushBusinessMsgReadInfo(businessMsgReadInfo, StatusConstant.STATUS_CD_VALID);
                msgReadServiceDaoImpl.updateMsgReadInfoInstance(businessMsgReadInfo);
                if (businessMsgReadInfo.size() == 1) {
                    dataFlowContext.addParamOut("msgReadId", businessMsgReadInfo.get("msg_read_id"));
                }
            }
        }

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
        Map delInfo = new HashMap();
        delInfo.put("bId", business.getbId());
        delInfo.put("operate", StatusConstant.OPERATE_DEL);
        //消息阅读信息
        List<Map> msgReadInfo = msgReadServiceDaoImpl.getMsgReadInfo(info);
        if (msgReadInfo != null && msgReadInfo.size() > 0) {

            //消息阅读信息
            List<Map> businessMsgReadInfos = msgReadServiceDaoImpl.getBusinessMsgReadInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessMsgReadInfos == null || businessMsgReadInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（msgRead），程序内部异常,请检查！ " + delInfo);
            }
            for (int _msgReadIndex = 0; _msgReadIndex < businessMsgReadInfos.size(); _msgReadIndex++) {
                Map businessMsgReadInfo = businessMsgReadInfos.get(_msgReadIndex);
                flushBusinessMsgReadInfo(businessMsgReadInfo, StatusConstant.STATUS_CD_VALID);
                msgReadServiceDaoImpl.updateMsgReadInfoInstance(businessMsgReadInfo);
            }
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
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "msgReadId 错误，不能自动生成（必须已经存在的msgReadId）" + businessMsgRead);
        }
        //自动保存DEL
        autoSaveDelBusinessMsgRead(business, businessMsgRead);

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
