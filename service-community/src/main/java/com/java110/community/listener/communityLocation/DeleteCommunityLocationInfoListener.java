package com.java110.community.listener.communityLocation;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.ICommunityLocationServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.community.CommunityLocationPo;
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
 * 删除小区位置信息 侦听
 * <p>
 * 处理节点
 * 1、businessCommunityLocation:{} 小区位置基本信息节点
 * 2、businessCommunityLocationAttr:[{}] 小区位置属性信息节点
 * 3、businessCommunityLocationPhoto:[{}] 小区位置照片信息节点
 * 4、businessCommunityLocationCerdentials:[{}] 小区位置证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("deleteCommunityLocationInfoListener")
@Transactional
public class DeleteCommunityLocationInfoListener extends AbstractCommunityLocationBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(DeleteCommunityLocationInfoListener.class);
    @Autowired
    ICommunityLocationServiceDao communityLocationServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_LOCATION;
    }

    /**
     * 根据删除信息 查出Instance表中数据 保存至business表 （状态写DEL） 方便撤单时直接更新回去
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();

        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessCommunityLocation 节点
        if (data.containsKey(CommunityLocationPo.class.getSimpleName())) {
            Object _obj = data.get(CommunityLocationPo.class.getSimpleName());
            JSONArray businessCommunityLocations = null;
            if (_obj instanceof JSONObject) {
                businessCommunityLocations = new JSONArray();
                businessCommunityLocations.add(_obj);
            } else {
                businessCommunityLocations = (JSONArray) _obj;
            }
            //JSONObject businessCommunityLocation = data.getJSONObject(CommunityLocationPo.class.getSimpleName());
            for (int _communityLocationIndex = 0; _communityLocationIndex < businessCommunityLocations.size(); _communityLocationIndex++) {
                JSONObject businessCommunityLocation = businessCommunityLocations.getJSONObject(_communityLocationIndex);
                doBusinessCommunityLocation(business, businessCommunityLocation);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("locationId", businessCommunityLocation.getString("locationId"));
                }
            }

        }


    }

    /**
     * 删除 instance数据
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");

        //小区位置信息
        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_DEL);

        //小区位置信息
        List<Map> businessCommunityLocationInfos = communityLocationServiceDaoImpl.getBusinessCommunityLocationInfo(info);
        if (businessCommunityLocationInfos != null && businessCommunityLocationInfos.size() > 0) {
            for (int _communityLocationIndex = 0; _communityLocationIndex < businessCommunityLocationInfos.size(); _communityLocationIndex++) {
                Map businessCommunityLocationInfo = businessCommunityLocationInfos.get(_communityLocationIndex);
                flushBusinessCommunityLocationInfo(businessCommunityLocationInfo, StatusConstant.STATUS_CD_INVALID);
                communityLocationServiceDaoImpl.updateCommunityLocationInfoInstance(businessCommunityLocationInfo);
                dataFlowContext.addParamOut("locationId", businessCommunityLocationInfo.get("location_id"));
            }
        }

    }

    /**
     * 撤单
     * 从business表中查询到DEL的数据 将instance中的数据更新回来
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
        info.put("statusCd", StatusConstant.STATUS_CD_INVALID);

        Map delInfo = new HashMap();
        delInfo.put("bId", business.getbId());
        delInfo.put("operate", StatusConstant.OPERATE_DEL);
        //小区位置信息
        List<Map> communityLocationInfo = communityLocationServiceDaoImpl.getCommunityLocationInfo(info);
        if (communityLocationInfo != null && communityLocationInfo.size() > 0) {

            //小区位置信息
            List<Map> businessCommunityLocationInfos = communityLocationServiceDaoImpl.getBusinessCommunityLocationInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessCommunityLocationInfos == null || businessCommunityLocationInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（communityLocation），程序内部异常,请检查！ " + delInfo);
            }
            for (int _communityLocationIndex = 0; _communityLocationIndex < businessCommunityLocationInfos.size(); _communityLocationIndex++) {
                Map businessCommunityLocationInfo = businessCommunityLocationInfos.get(_communityLocationIndex);
                flushBusinessCommunityLocationInfo(businessCommunityLocationInfo, StatusConstant.STATUS_CD_VALID);
                communityLocationServiceDaoImpl.updateCommunityLocationInfoInstance(businessCommunityLocationInfo);
            }
        }
    }


    /**
     * 处理 businessCommunityLocation 节点
     *
     * @param business                  总的数据节点
     * @param businessCommunityLocation 小区位置节点
     */
    private void doBusinessCommunityLocation(Business business, JSONObject businessCommunityLocation) {

        Assert.jsonObjectHaveKey(businessCommunityLocation, "locationId", "businessCommunityLocation 节点下没有包含 locationId 节点");

        if (businessCommunityLocation.getString("locationId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "locationId 错误，不能自动生成（必须已经存在的locationId）" + businessCommunityLocation);
        }
        //自动插入DEL
        autoSaveDelBusinessCommunityLocation(business, businessCommunityLocation);
    }

    @Override
    public ICommunityLocationServiceDao getCommunityLocationServiceDaoImpl() {
        return communityLocationServiceDaoImpl;
    }

    public void setCommunityLocationServiceDaoImpl(ICommunityLocationServiceDao communityLocationServiceDaoImpl) {
        this.communityLocationServiceDaoImpl = communityLocationServiceDaoImpl;
    }
}
