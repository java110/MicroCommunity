package com.java110.community.listener.notice;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.INoticeServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.notice.NoticePo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 通知信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveNoticeInfoListener")
@Transactional
public class SaveNoticeInfoListener extends AbstractNoticeBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveNoticeInfoListener.class);

    @Autowired
    private INoticeServiceDao noticeServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_NOTICE;
    }

    /**
     * 保存通知信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessNotice 节点
        if (data.containsKey(NoticePo.class.getSimpleName())) {
            Object bObj = data.get(NoticePo.class.getSimpleName());
            JSONArray businessNotices = null;
            if (bObj instanceof JSONObject) {
                businessNotices = new JSONArray();
                businessNotices.add(bObj);
            } else {
                businessNotices = (JSONArray) bObj;
            }
            //JSONObject businessNotice = data.getJSONObject("businessNotice");
            for (int bNoticeIndex = 0; bNoticeIndex < businessNotices.size(); bNoticeIndex++) {
                JSONObject businessNotice = businessNotices.getJSONObject(bNoticeIndex);
                doBusinessNotice(business, businessNotice);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("noticeId", businessNotice.getString("noticeId"));
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

        //通知信息
        List<Map> businessNoticeInfo = noticeServiceDaoImpl.getBusinessNoticeInfo(info);

        //判断是否夸分片处理数据
        if (businessNoticeInfo != null && businessNoticeInfo.size() > 1) {
            Map<String, List<Map>> maps = new HashMap<>();
            for (Map businessNotice : businessNoticeInfo) {
                if (maps.containsKey(businessNotice.get("community_id"))) {
                    List<Map> businessNoticeInfos = maps.get(businessNotice.get("community_id"));
                    businessNoticeInfos.add(businessNotice);
                    maps.put(businessNotice.get("community_id").toString(), businessNoticeInfos);
                } else {
                    List<Map> businessNoticeInfos = new ArrayList<>();
                    businessNoticeInfos.add(businessNotice);
                    maps.put(businessNotice.get("community_id").toString(), businessNoticeInfos);
                }
            }

            for (String key : maps.keySet()) {
                reFreshShareColumn(info, maps.get(key).get(0));
                noticeServiceDaoImpl.saveNoticeInfoInstance(info);
            }
            return;
        }
        if (businessNoticeInfo != null && businessNoticeInfo.size() > 0) {
            reFreshShareColumn(info, businessNoticeInfo.get(0));
            noticeServiceDaoImpl.saveNoticeInfoInstance(info);
            if (businessNoticeInfo.size() == 1) {
                dataFlowContext.addParamOut("noticeId", businessNoticeInfo.get(0).get("notice_id"));
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

//        if (info.containsKey("communityId")) {
//            return;
//        }

        if (!businessInfo.containsKey("community_id")) {
            return;
        }

        info.put("communityId", businessInfo.get("community_id"));
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
        //通知信息
        List<Map> noticeInfo = noticeServiceDaoImpl.getNoticeInfo(info);
        if (noticeInfo != null && noticeInfo.size() > 0) {
            reFreshShareColumn(paramIn, noticeInfo.get(0));
            noticeServiceDaoImpl.updateNoticeInfoInstance(paramIn);
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
            //刷新缓存
            //flushNoticeId(business.getDatas());

            businessNotice.put("noticeId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_noticeId));

        }

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
