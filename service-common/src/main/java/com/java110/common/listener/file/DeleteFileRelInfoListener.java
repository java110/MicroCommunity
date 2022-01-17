package com.java110.common.listener.file;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IFileRelServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.file.FileRelPo;
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
 * 删除文件存放信息 侦听
 * <p>
 * 处理节点
 * 1、businessFileRel:{} 文件存放基本信息节点
 * 2、businessFileRelAttr:[{}] 文件存放属性信息节点
 * 3、businessFileRelPhoto:[{}] 文件存放照片信息节点
 * 4、businessFileRelCerdentials:[{}] 文件存放证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("deleteFileRelInfoListener")
@Transactional
public class DeleteFileRelInfoListener extends AbstractFileRelBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(DeleteFileRelInfoListener.class);
    @Autowired
    IFileRelServiceDao fileRelServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_FILE_REL;
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


        //处理 businessFileRel 节点
        if (data.containsKey(FileRelPo.class.getSimpleName())) {
            Object _obj = data.get(FileRelPo.class.getSimpleName());
            JSONArray businessFileRels = null;
            if (_obj instanceof JSONObject) {
                businessFileRels = new JSONArray();
                businessFileRels.add(_obj);
            } else {
                businessFileRels = (JSONArray) _obj;
            }
            //JSONObject businessFileRel = data.getJSONObject("businessFileRel");
            for (int _fileRelIndex = 0; _fileRelIndex < businessFileRels.size(); _fileRelIndex++) {
                JSONObject businessFileRel = businessFileRels.getJSONObject(_fileRelIndex);
                doBusinessFileRel(business, businessFileRel);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("fileRelId", businessFileRel.getString("fileRelId"));
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

        //文件存放信息
        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_DEL);

        //文件存放信息
        List<Map> businessFileRelInfos = fileRelServiceDaoImpl.getBusinessFileRelInfo(info);
        if (businessFileRelInfos != null && businessFileRelInfos.size() > 0) {
            for (int _fileRelIndex = 0; _fileRelIndex < businessFileRelInfos.size(); _fileRelIndex++) {
                Map businessFileRelInfo = businessFileRelInfos.get(_fileRelIndex);
                flushBusinessFileRelInfo(businessFileRelInfo, StatusConstant.STATUS_CD_INVALID);
                fileRelServiceDaoImpl.updateFileRelInfoInstance(businessFileRelInfo);
                dataFlowContext.addParamOut("fileRelId", businessFileRelInfo.get("file_rel_id"));
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
        //文件存放信息
        List<Map> fileRelInfo = fileRelServiceDaoImpl.getFileRelInfo(info);
        if (fileRelInfo != null && fileRelInfo.size() > 0) {

            //文件存放信息
            List<Map> businessFileRelInfos = fileRelServiceDaoImpl.getBusinessFileRelInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessFileRelInfos == null || businessFileRelInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（fileRel），程序内部异常,请检查！ " + delInfo);
            }
            for (int _fileRelIndex = 0; _fileRelIndex < businessFileRelInfos.size(); _fileRelIndex++) {
                Map businessFileRelInfo = businessFileRelInfos.get(_fileRelIndex);
                flushBusinessFileRelInfo(businessFileRelInfo, StatusConstant.STATUS_CD_VALID);
                fileRelServiceDaoImpl.updateFileRelInfoInstance(businessFileRelInfo);
            }
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
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "fileRelId 错误，不能自动生成（必须已经存在的fileRelId）" + businessFileRel);
        }
        //自动插入DEL
        autoSaveDelBusinessFileRel(business, businessFileRel);
    }

    public IFileRelServiceDao getFileRelServiceDaoImpl() {
        return fileRelServiceDaoImpl;
    }

    public void setFileRelServiceDaoImpl(IFileRelServiceDao fileRelServiceDaoImpl) {
        this.fileRelServiceDaoImpl = fileRelServiceDaoImpl;
    }
}
