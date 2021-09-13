package com.java110.api.components.machine;


import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import com.java110.api.smo.machine.IListMachineRecordsSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 开门记录组件管理类
 *
 * add by wuxw
 *
 * 2019-06-29
 */
@Component("machineRecordManage")
public class MachineRecordManageComponent {

    @Autowired
    private IListMachineRecordsSMO listMachineRecordsSMOImpl;

    /**
     * 查询开门记录列表
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd){
        JSONObject reqParam = JSONObject.parseObject(pd.getReqData());
        reqParam.put("recordTypeCd", "8888");

        IPageData newPd = PageData.newInstance().builder(pd.getUserId(),pd.getUserName(), pd.getToken(),
                reqParam.toJSONString(), pd.getComponentCode(), pd.getComponentMethod(),
                "", pd.getSessionId(),pd.getAppId(),pd.getHeaders());

        return listMachineRecordsSMOImpl.listMachineRecords(newPd);
    }

    public IListMachineRecordsSMO getListMachineRecordsSMOImpl() {
        return listMachineRecordsSMOImpl;
    }

    public void setListMachineRecordsSMOImpl(IListMachineRecordsSMO listMachineRecordsSMOImpl) {
        this.listMachineRecordsSMOImpl = listMachineRecordsSMOImpl;
    }
}
