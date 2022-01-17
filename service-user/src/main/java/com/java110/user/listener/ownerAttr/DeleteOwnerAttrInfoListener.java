package com.java110.user.listener.ownerAttr;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.owner.OwnerAttrPo;
import com.java110.user.dao.IOwnerAttrServiceDao;
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
 * 删除业主属性信息 侦听
 * <p>
 * 处理节点
 * 1、businessOwnerAttr:{} 业主属性基本信息节点
 * 2、businessOwnerAttrAttr:[{}] 业主属性属性信息节点
 * 3、businessOwnerAttrPhoto:[{}] 业主属性照片信息节点
 * 4、businessOwnerAttrCerdentials:[{}] 业主属性证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("deleteOwnerAttrInfoListener")
@Transactional
public class DeleteOwnerAttrInfoListener extends AbstractOwnerAttrBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(DeleteOwnerAttrInfoListener.class);
    @Autowired
    IOwnerAttrServiceDao ownerAttrServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_OWNER_ATTR_INFO;
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

        //处理 businessOwnerAttr 节点
        if (data.containsKey(OwnerAttrPo.class.getSimpleName())) {
            Object _obj = data.get(OwnerAttrPo.class.getSimpleName());
            JSONArray businessOwnerAttrs = null;
            if (_obj instanceof JSONObject) {
                businessOwnerAttrs = new JSONArray();
                businessOwnerAttrs.add(_obj);
            } else {
                businessOwnerAttrs = (JSONArray) _obj;
            }
            //JSONObject businessOwnerAttr = data.getJSONObject(OwnerAttrPo.class.getSimpleName());
            for (int _ownerAttrIndex = 0; _ownerAttrIndex < businessOwnerAttrs.size(); _ownerAttrIndex++) {
                JSONObject businessOwnerAttr = businessOwnerAttrs.getJSONObject(_ownerAttrIndex);
                doBusinessOwnerAttr(business, businessOwnerAttr);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("attrId", businessOwnerAttr.getString("attrId"));
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

        //业主属性信息
        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_DEL);

        //业主属性信息
        List<Map> businessOwnerAttrInfos = ownerAttrServiceDaoImpl.getBusinessOwnerAttrInfo(info);
        if (businessOwnerAttrInfos != null && businessOwnerAttrInfos.size() > 0) {
            for (int _ownerAttrIndex = 0; _ownerAttrIndex < businessOwnerAttrInfos.size(); _ownerAttrIndex++) {
                Map businessOwnerAttrInfo = businessOwnerAttrInfos.get(_ownerAttrIndex);
                flushBusinessOwnerAttrInfo(businessOwnerAttrInfo, StatusConstant.STATUS_CD_INVALID);
                ownerAttrServiceDaoImpl.updateOwnerAttrInfoInstance(businessOwnerAttrInfo);
                dataFlowContext.addParamOut("attrId", businessOwnerAttrInfo.get("attr_id"));
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
        //业主属性信息
        List<Map> ownerAttrInfo = ownerAttrServiceDaoImpl.getOwnerAttrInfo(info);
        if (ownerAttrInfo != null && ownerAttrInfo.size() > 0) {

            //业主属性信息
            List<Map> businessOwnerAttrInfos = ownerAttrServiceDaoImpl.getBusinessOwnerAttrInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessOwnerAttrInfos == null || businessOwnerAttrInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（ownerAttr），程序内部异常,请检查！ " + delInfo);
            }
            for (int _ownerAttrIndex = 0; _ownerAttrIndex < businessOwnerAttrInfos.size(); _ownerAttrIndex++) {
                Map businessOwnerAttrInfo = businessOwnerAttrInfos.get(_ownerAttrIndex);
                flushBusinessOwnerAttrInfo(businessOwnerAttrInfo, StatusConstant.STATUS_CD_VALID);
                ownerAttrServiceDaoImpl.updateOwnerAttrInfoInstance(businessOwnerAttrInfo);
            }
        }
    }


    /**
     * 处理 businessOwnerAttr 节点
     *
     * @param business          总的数据节点
     * @param businessOwnerAttr 业主属性节点
     */
    private void doBusinessOwnerAttr(Business business, JSONObject businessOwnerAttr) {

        Assert.jsonObjectHaveKey(businessOwnerAttr, "attrId", "businessOwnerAttr 节点下没有包含 attrId 节点");

        if (businessOwnerAttr.getString("attrId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "attrId 错误，不能自动生成（必须已经存在的attrId）" + businessOwnerAttr);
        }
        //自动插入DEL
        autoSaveDelBusinessOwnerAttr(business, businessOwnerAttr);
    }

    @Override
    public IOwnerAttrServiceDao getOwnerAttrServiceDaoImpl() {
        return ownerAttrServiceDaoImpl;
    }

    public void setOwnerAttrServiceDaoImpl(IOwnerAttrServiceDao ownerAttrServiceDaoImpl) {
        this.ownerAttrServiceDaoImpl = ownerAttrServiceDaoImpl;
    }
}
