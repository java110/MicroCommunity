package com.java110.api.listener.machineTranslate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.machineTranslate.IMachineTranslateBMO;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.machine.CarInoutDto;
import com.java110.dto.machine.MachineDto;
import com.java110.intf.common.*;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.ServiceCodeMachineTranslateConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * 调用地址
 * http://api.demo.winqi.cn/api/machineTranslate.machineUploadFaceLog?app_id=992019111002270001&communityId=7020181217000001&transaction_id=-1&req_time=20181113225612&user_id=-1
 * 硬件获取用户信息
 */
@Java110Listener("machineUploadCarLogListener")
public class MachineUploadCarLogListener extends BaseMachineListener {

    @Autowired
    private IMachineTranslateInnerServiceSMO machineTranslateInnerServiceSMOImpl;

    @Autowired
    private IMachineTranslateBMO machineTranslateBMOImpl;

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    @Autowired
    private ICarInoutInnerServiceSMO carInoutInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IApplicationKeyInnerServiceSMO applicationKeyInnerServiceSMOImpl;

    /**
     * @param event   事件对象
     * @param reqJson 请求报文数据
     */
    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        // Assert.hasKeyAndValue(reqJson, "faceid", "请求报文中未包含用户ID");
        super.validateMachineHeader(event, reqJson);
        //Assert.hasKeyAndValue(reqJson, "machineCode", "必填，请填写设备编码");
        //Assert.hasKeyAndValue(reqJson, "photo", "必填，请填写用户照片信息");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        ResponseEntity<String> responseEntity = null;
        JSONObject outParam = null;

        try {
            Map<String, String> reqHeader = context.getRequestHeaders();
            //判断是否是心跳类过来的
            if (!super.validateMachineBody(event, context, reqJson, machineInnerServiceSMOImpl)) {
                return;
            }
            outParam = new JSONObject();
            outParam.put("code", 0);
            outParam.put("message", "success");
            JSONArray data = null;
            reqJson.put("communityId", reqJson.containsKey("communityId") ? reqJson.getString("communityId") : reqHeader.get("communityId"));
            HttpHeaders httpHeaders = super.getHeader(context);

            reqJson.put("fileId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));

            //判断是否有 进场记录
            CarInoutDto carInoutDto = new CarInoutDto();
            carInoutDto.setCommunityId(reqJson.getString("communityId"));
            carInoutDto.setCarNum(reqJson.getString("carNum"));
            carInoutDto.setStates(new String[]{"100300", "100400", "100600"});
            List<CarInoutDto> carInoutDtos = carInoutInnerServiceSMOImpl.queryCarInouts(carInoutDto);
            MachineDto machineDto = new MachineDto();
            machineDto.setMachineId(reqJson.getString("communityId"));
            machineDto.setMachineCode(reqJson.getString("communityId"));

            if (carInoutDtos == null || carInoutDtos.size() < 1) {
                //添加单元信息
                machineTranslateBMOImpl.addCarInout(reqJson, context, reqJson.getString("communityId"));
                machineDto.setDirection("3306");
            } else {
                CarInoutDto tmpCarInoutDto = new CarInoutDto();
                tmpCarInoutDto.setInoutId(carInoutDtos.get(0).getInoutId());
                tmpCarInoutDto.setCommunityId(reqJson.getString("communityId"));
                machineTranslateBMOImpl.modifyCarInout(reqJson, context, tmpCarInoutDto, "100500", reqJson.getString("outTime"));
                machineDto.setDirection("3307");
            }

            machineTranslateBMOImpl.addCarInoutDetail(reqJson, context, reqJson.getString("communityId"), machineDto);


            reqJson.put("machineRecordId", reqJson.getString("detailId"));
            reqJson.put("relTypeCd", "15000");
            //保存文件信息
            machineTranslateBMOImpl.savePhoto(reqJson, context);
            commit(context);
            responseEntity = context.getResponseEntity();
            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "上报记录失败" + responseEntity);
            }
            outParam = new JSONObject();
            outParam.put("code", 0);
            outParam.put("message", "success");
            outParam.put("data", data);
            responseEntity = new ResponseEntity<>(outParam.toJSONString(), httpHeaders, HttpStatus.OK);
            context.setResponseEntity(responseEntity);
        } catch (Exception e) {
            outParam.put("code", -1);
            outParam.put("message", e.getMessage());
            responseEntity = new ResponseEntity<>(outParam.toJSONString(), HttpStatus.OK);
            context.setResponseEntity(responseEntity);
        }
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeMachineTranslateConstant.MACHINE_UPLOAD_CAR_LOG;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    public IMachineTranslateInnerServiceSMO getMachineTranslateInnerServiceSMOImpl() {
        return machineTranslateInnerServiceSMOImpl;
    }

    public void setMachineTranslateInnerServiceSMOImpl(IMachineTranslateInnerServiceSMO machineTranslateInnerServiceSMOImpl) {
        this.machineTranslateInnerServiceSMOImpl = machineTranslateInnerServiceSMOImpl;
    }

    public IMachineInnerServiceSMO getMachineInnerServiceSMOImpl() {
        return machineInnerServiceSMOImpl;
    }

    public void setMachineInnerServiceSMOImpl(IMachineInnerServiceSMO machineInnerServiceSMOImpl) {
        this.machineInnerServiceSMOImpl = machineInnerServiceSMOImpl;
    }

    public ICommunityInnerServiceSMO getCommunityInnerServiceSMOImpl() {
        return communityInnerServiceSMOImpl;
    }

    public void setCommunityInnerServiceSMOImpl(ICommunityInnerServiceSMO communityInnerServiceSMOImpl) {
        this.communityInnerServiceSMOImpl = communityInnerServiceSMOImpl;
    }

    public IFileRelInnerServiceSMO getFileRelInnerServiceSMOImpl() {
        return fileRelInnerServiceSMOImpl;
    }

    public void setFileRelInnerServiceSMOImpl(IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl) {
        this.fileRelInnerServiceSMOImpl = fileRelInnerServiceSMOImpl;
    }

    public IFileInnerServiceSMO getFileInnerServiceSMOImpl() {
        return fileInnerServiceSMOImpl;
    }

    public void setFileInnerServiceSMOImpl(IFileInnerServiceSMO fileInnerServiceSMOImpl) {
        this.fileInnerServiceSMOImpl = fileInnerServiceSMOImpl;
    }
}
