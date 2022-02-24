package com.java110.store.listener.demo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.store.dao.IDemoServiceDao;
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
 * 删除demo信息 侦听
 * <p>
 * 处理节点
 * 1、businessDemo:{} demo基本信息节点
 * 2、businessDemoAttr:[{}] demo属性信息节点
 * 3、businessDemoPhoto:[{}] demo照片信息节点
 * 4、businessDemoCerdentials:[{}] demo证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("deleteDemoInfoListener")
@Transactional
public class DeleteDemoInfoListener extends AbstractDemoBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(DeleteDemoInfoListener.class);
    @Autowired
    IDemoServiceDao demoServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_DEMO_INFO;
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


        //处理 businessDemo 节点
        if (data.containsKey(BusinessTypeConstant.BUSINESS_TYPE_DELETE_DEMO_INFO)) {
            Object _obj = data.get(BusinessTypeConstant.BUSINESS_TYPE_DELETE_DEMO_INFO);
            JSONArray businessDemos = null;
            if (_obj instanceof JSONObject) {
                businessDemos = new JSONArray();
                businessDemos.add(_obj);
            } else {
                businessDemos = (JSONArray) _obj;
            }
            //JSONObject businessDemo = data.getJSONObject("businessDemo");
            for (int _demoIndex = 0; _demoIndex < businessDemos.size(); _demoIndex++) {
                JSONObject businessDemo = businessDemos.getJSONObject(_demoIndex);
                doBusinessDemo(business, businessDemo);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("demoId", businessDemo.getString("demoId"));
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

        //demo信息
        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_DEL);

        //demo信息
        List<Map> businessDemoInfos = demoServiceDaoImpl.getBusinessDemoInfo(info);
        if (businessDemoInfos != null && businessDemoInfos.size() > 0) {
            for (int _demoIndex = 0; _demoIndex < businessDemoInfos.size(); _demoIndex++) {
                Map businessDemoInfo = businessDemoInfos.get(_demoIndex);
                flushBusinessDemoInfo(businessDemoInfo, StatusConstant.STATUS_CD_INVALID);
                demoServiceDaoImpl.updateDemoInfoInstance(businessDemoInfo);
                dataFlowContext.addParamOut("demoId", businessDemoInfo.get("demo_id"));
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
        //demo信息
        List<Map> demoInfo = demoServiceDaoImpl.getDemoInfo(info);
        if (demoInfo != null && demoInfo.size() > 0) {

            //demo信息
            List<Map> businessDemoInfos = demoServiceDaoImpl.getBusinessDemoInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessDemoInfos == null || businessDemoInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（demo），程序内部异常,请检查！ " + delInfo);
            }
            for (int _demoIndex = 0; _demoIndex < businessDemoInfos.size(); _demoIndex++) {
                Map businessDemoInfo = businessDemoInfos.get(_demoIndex);
                flushBusinessDemoInfo(businessDemoInfo, StatusConstant.STATUS_CD_VALID);
                demoServiceDaoImpl.updateDemoInfoInstance(businessDemoInfo);
            }
        }
    }


    /**
     * 处理 businessDemo 节点
     *
     * @param business     总的数据节点
     * @param businessDemo demo节点
     */
    private void doBusinessDemo(Business business, JSONObject businessDemo) {

        Assert.jsonObjectHaveKey(businessDemo, "demoId", "businessDemo 节点下没有包含 demoId 节点");

        if (businessDemo.getString("demoId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "demoId 错误，不能自动生成（必须已经存在的demoId）" + businessDemo);
        }
        //自动插入DEL
        autoSaveDelBusinessDemo(business, businessDemo);
    }

    public IDemoServiceDao getDemoServiceDaoImpl() {
        return demoServiceDaoImpl;
    }

    public void setDemoServiceDaoImpl(IDemoServiceDao demoServiceDaoImpl) {
        this.demoServiceDaoImpl = demoServiceDaoImpl;
    }
}
