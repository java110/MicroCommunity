package com.java110.common.listener.file;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IFileRelServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.file.FileRelPo;
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
 * 保存 文件存放信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveFileRelInfoListener")
@Transactional
public class SaveFileRelInfoListener extends AbstractFileRelBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveFileRelInfoListener.class);

    @Autowired
    private IFileRelServiceDao fileRelServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_FILE_REL;
    }

    /**
     * 保存文件存放信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessFileRel 节点
        if (data.containsKey(FileRelPo.class.getSimpleName())) {
            Object bObj = data.get(FileRelPo.class.getSimpleName());
            JSONArray businessFileRels = null;
            if (bObj instanceof JSONObject) {
                businessFileRels = new JSONArray();
                businessFileRels.add(bObj);
            } else {
                businessFileRels = (JSONArray) bObj;
            }
            //JSONObject businessFileRel = data.getJSONObject("businessFileRel");
            for (int bFileRelIndex = 0; bFileRelIndex < businessFileRels.size(); bFileRelIndex++) {
                JSONObject businessFileRel = businessFileRels.getJSONObject(bFileRelIndex);
                doBusinessFileRel(business, businessFileRel);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("fileRelId", businessFileRel.getString("fileRelId"));
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

        //文件存放信息
        List<Map> businessFileRelInfo = fileRelServiceDaoImpl.getBusinessFileRelInfo(info);
        if (businessFileRelInfo != null && businessFileRelInfo.size() > 0) {
            reFreshShareColumn(info, businessFileRelInfo.get(0));
            fileRelServiceDaoImpl.saveFileRelInfoInstance(info);
            if (businessFileRelInfo.size() == 1) {
                dataFlowContext.addParamOut("fileRelId", businessFileRelInfo.get(0).get("file_rel_id"));
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

        if (info.containsKey("objId")) {
            return;
        }

        if (!businessInfo.containsKey("obj_id")) {
            return;
        }

        info.put("objId", businessInfo.get("obj_id"));
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
        //文件存放信息
        List<Map> fileRelInfo = fileRelServiceDaoImpl.getFileRelInfo(info);
        if (fileRelInfo != null && fileRelInfo.size() > 0) {
            reFreshShareColumn(paramIn, fileRelInfo.get(0));
            fileRelServiceDaoImpl.updateFileRelInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessFileRel 节点
     *
     * @param business        总的数据节点
     * @param businessFileRel 文件存放节点
     */
    private void doBusinessFileRel(Business business, JSONObject businessFileRel) {

        Assert.jsonObjectHaveKey(businessFileRel, "fileRelId", "businessFileRel 节点下没有包含 fileRelId 节点");

        if (businessFileRel.getString("fileRelId").startsWith("-")) {
            //刷新缓存
            //flushFileRelId(business.getDatas());

            businessFileRel.put("fileRelId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_fileRelId));

        }

        businessFileRel.put("bId", business.getbId());
        businessFileRel.put("operate", StatusConstant.OPERATE_ADD);
        //保存文件存放信息
        fileRelServiceDaoImpl.saveBusinessFileRelInfo(businessFileRel);

    }

    public IFileRelServiceDao getFileRelServiceDaoImpl() {
        return fileRelServiceDaoImpl;
    }

    public void setFileRelServiceDaoImpl(IFileRelServiceDao fileRelServiceDaoImpl) {
        this.fileRelServiceDaoImpl = fileRelServiceDaoImpl;
    }
}
