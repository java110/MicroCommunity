package com.java110.community.listener.notice;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.INoticeServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.notice.NoticePo;
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
 * 修改通知信息 侦听
 * <p>
 * 处理节点
 * 1、businessNotice:{} 通知基本信息节点
 * 2、businessNoticeAttr:[{}] 通知属性信息节点
 * 3、businessNoticePhoto:[{}] 通知照片信息节点
 * 4、businessNoticeCerdentials:[{}] 通知证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateNoticeInfoListener")
@Transactional
public class UpdateNoticeInfoListener extends AbstractNoticeBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateNoticeInfoListener.class);
    @Autowired
    private INoticeServiceDao noticeServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_NOTICE;
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

        //处理 businessNotice 节点
        if (data.containsKey(NoticePo.class.getSimpleName())) {
            Object _obj = data.get(NoticePo.class.getSimpleName());
            JSONArray businessNotices = null;
            if (_obj instanceof JSONObject) {
                businessNotices = new JSONArray();
                businessNotices.add(_obj);
            } else {
                businessNotices = (JSONArray) _obj;
            }
            //JSONObject businessNotice = data.getJSONObject("businessNotice");
            for (int _noticeIndex = 0; _noticeIndex < businessNotices.size(); _noticeIndex++) {
                JSONObject businessNotice = businessNotices.getJSONObject(_noticeIndex);
                doBusinessNotice(business, businessNotice);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("noticeId", businessNotice.getString("noticeId"));
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

        //通知信息
        List<Map> businessNoticeInfos = noticeServiceDaoImpl.getBusinessNoticeInfo(info);
        if (businessNoticeInfos != null && businessNoticeInfos.size() > 0) {
            for (int _noticeIndex = 0; _noticeIndex < businessNoticeInfos.size(); _noticeIndex++) {
                Map businessNoticeInfo = businessNoticeInfos.get(_noticeIndex);
                flushBusinessNoticeInfo(businessNoticeInfo, StatusConstant.STATUS_CD_VALID);
                noticeServiceDaoImpl.updateNoticeInfoInstance(businessNoticeInfo);
                if (businessNoticeInfo.size() == 1) {
                    dataFlowContext.addParamOut("noticeId", businessNoticeInfo.get("notice_id"));
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
        //通知信息
        List<Map> noticeInfo = noticeServiceDaoImpl.getNoticeInfo(info);
        if (noticeInfo != null && noticeInfo.size() > 0) {

            //通知信息
            List<Map> businessNoticeInfos = noticeServiceDaoImpl.getBusinessNoticeInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessNoticeInfos == null || businessNoticeInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（notice），程序内部异常,请检查！ " + delInfo);
            }
            for (int _noticeIndex = 0; _noticeIndex < businessNoticeInfos.size(); _noticeIndex++) {
                Map businessNoticeInfo = businessNoticeInfos.get(_noticeIndex);
                flushBusinessNoticeInfo(businessNoticeInfo, StatusConstant.STATUS_CD_VALID);
                noticeServiceDaoImpl.updateNoticeInfoInstance(businessNoticeInfo);
            }
        }

    }


    /**
     * 处理 businessNotice 节点
     *
     * @param business       总的数据节点
     * @param businessNotice 通知节点
     */
    private void doBusinessNotice(Business business, JSONObject businessNotice) {

        Assert.jsonObjectHaveKey(businessNotice, "noticeId", "businessNotice 节点下没有包含 noticeId 节点");

        if (businessNotice.getString("noticeId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "noticeId 错误，不能自动生成（必须已经存在的noticeId）" + businessNotice);
        }
        //自动保存DEL
        autoSaveDelBusinessNotice(business, businessNotice);

        businessNotice.put("bId", business.getbId());
        businessNotice.put("operate", StatusConstant.OPERATE_ADD);
        //保存通知信息
        noticeServiceDaoImpl.saveBusinessNoticeInfo(businessNotice);

    }


    public INoticeServiceDao getNoticeServiceDaoImpl() {
        return noticeServiceDaoImpl;
    }

    public void setNoticeServiceDaoImpl(INoticeServiceDao noticeServiceDaoImpl) {
        this.noticeServiceDaoImpl = noticeServiceDaoImpl;
    }


}
