package com.java110.api.listener.visit;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.visit.IVisitBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.visit.VisitDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.dto.file.FileDto;
import com.java110.intf.community.IVisitInnerServiceSMO;
import com.java110.po.file.FileRelPo;
import com.java110.po.owner.VisitPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ServiceCodeVisitConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveVisitListener")
public class SaveVisitListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IVisitBMO visitBMOImpl;

    @Autowired
    private IVisitInnerServiceSMO visitInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    //键
    public static final String IS_NEED_REVIEW = "IS_NEED_REVIEW";

    //键
    public static final String VISIT_NUMBER = "VISIT_NUMBER";

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "vName", "必填，请填写访客姓名");
        Assert.hasKeyAndValue(reqJson, "visitGender", "必填，请填写访客姓名");
        Assert.hasKeyAndValue(reqJson, "phoneNumber", "必填，请填写访客联系方式");
        Assert.hasKeyAndValue(reqJson, "visitTime", "必填，请填写访客拜访时间");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) throws ParseException {
        reqJson.put("vId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_vId));
        //是否需要审核
        String val = CommunitySettingFactory.getValue(reqJson.getString("communityId"), IS_NEED_REVIEW);
        if (!StringUtils.isEmpty(val) && val.equals("true") && reqJson.containsKey("carNum") && !StringUtil.isEmpty(reqJson.getString("carNum"))) {
            reqJson.put("state", "0"); //0表示未审核；1表示审核通过；2表示审核拒绝
        } else {
            reqJson.put("state", "1");
        }
        //随行人数
        if (reqJson.containsKey("entourage") && reqJson.getString("entourage").equals("")) {
            reqJson.put("entourage", "0");
        }
        //是否超过规定次数
        boolean specifiedTimes = false;
        if (reqJson.containsKey("carNum") && !StringUtil.isEmpty(reqJson.getString("carNum"))) {
            //查询预约车辆登记次数
            int number = Integer.parseInt(CommunitySettingFactory.getValue(reqJson.getString("communityId"), VISIT_NUMBER));
            VisitDto visitDto = new VisitDto();
            //查询当天车辆登记次数
            visitDto.setOwnerId(reqJson.getString("ownerId"));
            visitDto.setCarNumNoEmpty("1");
            visitDto.setSameDay("1");
            List<VisitDto> visitDtos = visitInnerServiceSMOImpl.queryVisits(visitDto);
            int count = visitDtos.size();
            //预约车辆登记次数0不做限制
            if (count >= number && number>0) {
                if (!StringUtils.isEmpty(val) && val.equals("false")) {
                    reqJson.put("state", "0");
                }
                specifiedTimes = true;
            }
        }
        visitBMOImpl.addVisit(reqJson, context);
        if (reqJson.containsKey("photo") && !StringUtils.isEmpty(reqJson.getString("photo"))) {
            FileDto fileDto = new FileDto();
            fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
            fileDto.setFileName(fileDto.getFileId());
            fileDto.setContext(reqJson.getString("photo"));
            fileDto.setSuffix("jpeg");
            fileDto.setCommunityId(reqJson.getString("communityId"));
            String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);
            reqJson.put("photoId", fileDto.getFileId());
            reqJson.put("fileSaveName", fileName);

            JSONObject businessUnit = new JSONObject();
            businessUnit.put("fileRelId", "-1");
            businessUnit.put("relTypeCd", "11000");
            businessUnit.put("saveWay", "table");
            businessUnit.put("objId", reqJson.getString("vId"));
            businessUnit.put("fileRealName", fileDto.getFileId());
            businessUnit.put("fileSaveName", fileName);
            FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);
            super.insert(context, fileRelPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FILE_REL);
        }
        commit(context);
        if (specifiedTimes) {
            ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "登记成功,您已经超过预约车辆登记次数限制，车辆将无法自动审核！");
            context.setResponseEntity(responseEntity);
            return;
        }

    }

    @Override
    public String getServiceCode() {
        return ServiceCodeVisitConstant.ADD_VISIT;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

}
