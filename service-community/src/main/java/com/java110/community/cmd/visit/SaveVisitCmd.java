package com.java110.community.cmd.visit;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.IPhotoSMO;
import com.java110.dto.RoomDto;
import com.java110.dto.accessControlWhite.AccessControlWhiteDto;
import com.java110.dto.file.FileDto;
import com.java110.dto.machine.CarBlackWhiteDto;
import com.java110.dto.machine.MachineDto;
import com.java110.dto.oaWorkflow.OaWorkflowDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.visit.VisitDto;
import com.java110.dto.visitSetting.VisitSettingDto;
import com.java110.intf.common.*;
import com.java110.intf.community.*;
import com.java110.intf.oa.IOaWorkflowInnerServiceSMO;
import com.java110.intf.user.*;
import com.java110.po.accessControlWhite.AccessControlWhitePo;
import com.java110.po.car.CarBlackWhitePo;
import com.java110.po.car.OwnerCarPo;
import com.java110.po.file.FileRelPo;
import com.java110.po.owner.VisitPo;
import com.java110.po.ownerCarAttr.OwnerCarAttrPo;
import com.java110.po.parking.ParkingSpacePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.*;

@Java110Cmd(serviceCode = "visit.saveVisit")
public class SaveVisitCmd extends Cmd {

    @Autowired
    private IVisitInnerServiceSMO visitInnerServiceSMOImpl;

    @Autowired
    private IVisitV1InnerServiceSMO visitV1InnerServiceSMOImpl;

    @Autowired
    private IPhotoSMO photoSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarV1InnerServiceSMO ownerCarV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerCarAttrInnerServiceSMO ownerCarAttrInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IVisitSettingV1InnerServiceSMO visitSettingV1InnerServiceSMOImpl;

    @Autowired
    private ICarBlackWhiteV1InnerServiceSMO carBlackWhiteV1InnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    @Autowired
    private IAccessControlWhiteV1InnerServiceSMO accessControlWhiteV1InnerServiceSMOImpl;

    @Autowired
    private IOaWorkflowInnerServiceSMO oaWorkflowInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IOaWorkflowActivitiInnerServiceSMO oaWorkflowActivitiInnerServiceSMOImpl;

    public static final String CODE_PREFIX_ID = "10";

    //键
    public static final String IS_NEED_REVIEW = "IS_NEED_REVIEW";

    //键
    public static final String VISIT_NUMBER = "VISIT_NUMBER";

    //键
    public static final String CAR_FREE_TIME = "CAR_FREE_TIME";

    //键
    public static final String ASCRIPTION_CAR_AREA_ID = "ASCRIPTION_CAR_AREA_ID";

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "vName", "必填，请填写访客姓名");
        Assert.hasKeyAndValue(reqJson, "visitGender", "必填，请填写访客姓名");
        Assert.hasKeyAndValue(reqJson, "phoneNumber", "必填，请填写访客联系方式");
        Assert.hasKeyAndValue(reqJson, "visitTime", "必填，请填写访客拜访时间");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        String userId = context.getReqHeaders().get("user-id");
        String storeId = context.getReqHeaders().get("store-id");
        reqJson.put("vId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_vId));
        //随行人数
        if (StringUtil.isEmpty(reqJson.getString("entourage"))) {
            reqJson.put("entourage", "0");
        }
        reqJson.put("recordState", "0");
        VisitPo visitPo = BeanConvertUtil.covertBean(reqJson, VisitPo.class);
        visitPo.setState(VisitDto.STATE_W);
        visitPo.setUserId(userId);
        int flag = visitV1InnerServiceSMOImpl.saveVisit(visitPo);
        if (flag < 1) {
            throw new CmdException("保存访客失败");
        }
        photoSMOImpl.savePhoto(reqJson, reqJson.getString("vId"), reqJson.getString("communityId"));

        // 是否需要审核
        if (hasAuditVisit(visitPo, reqJson,storeId,userId)) {
            return; // 需要审核结束，审核时处理 相应 送图片 和车牌数据
        }

        String faceWay = "Y";
        String carNumWay = "N";

        // 查询访客设置
        VisitSettingDto visitSettingDto = new VisitSettingDto();
        visitSettingDto.setCommunityId(reqJson.getString("communityId"));
        List<VisitSettingDto> visitSettingDtos = visitSettingV1InnerServiceSMOImpl.queryVisitSettings(visitSettingDto);

        if (visitSettingDtos != null && visitSettingDtos.size() > 0) {
            faceWay = visitSettingDtos.get(0).getFaceWay();
            carNumWay = visitSettingDtos.get(0).getCarNumWay();
            // 同步车牌 这里需要停车场，所以没有配置访客设置，不同步
            synchronizedVisitCarNum(visitPo, carNumWay, visitSettingDtos.get(0));
        }

        // 同步访客人脸
        synchronousVisitFace(visitPo, faceWay, reqJson.getString("photo"));

    }

    private void synchronousVisitFace(VisitPo visitPo, String faceWay, String photo) {
        if (VisitSettingDto.FACE_WAY_NO.equals(faceWay) || StringUtil.isEmpty(photo)) {
            return;
        }
        // 查询 访问业主可以访问的门禁设备
        RoomDto roomDto = new RoomDto();
        roomDto.setOwnerId(visitPo.getOwnerId());
        //这种情况说明 业主已经删掉了 需要查询状态为 1 的数据
        List<RoomDto> rooms = roomInnerServiceSMOImpl.queryRoomsByOwner(roomDto);

        //拿到小区ID
        String communityId = visitPo.getCommunityId();
        //根据小区ID查询现有设备
        MachineDto machineDto = new MachineDto();
        machineDto.setCommunityId(communityId);
        //String[] locationObjIds = new String[]{communityId};
        List<String> locationObjIds = new ArrayList<>();
        locationObjIds.add(communityId);
        for (RoomDto tRoomDto : rooms) {
            locationObjIds.add(tRoomDto.getUnitId());
            locationObjIds.add(tRoomDto.getRoomId());
            locationObjIds.add(tRoomDto.getFloorId());
        }

        machineDto.setLocationObjIds(locationObjIds.toArray(new String[locationObjIds.size()]));
        List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);
        if (machineDtos == null || machineDtos.size() < 1) {
            return;
        }

        // 同步到 门禁白名单中
        for (MachineDto tmpMachineDto : machineDtos) {
            if (!"9999".equals(tmpMachineDto.getMachineTypeCd())) {
                continue;
            }
            AccessControlWhiteDto accessControlWhiteDto = new AccessControlWhiteDto();
            accessControlWhiteDto.setCommunityId(communityId);
            accessControlWhiteDto.setTel(visitPo.getPhoneNumber());
            accessControlWhiteDto.setMachineId(tmpMachineDto.getMachineId());
            List<AccessControlWhiteDto> accessControlWhiteDtos = accessControlWhiteV1InnerServiceSMOImpl.queryAccessControlWhites(accessControlWhiteDto);
            AccessControlWhitePo accessControlWhitePo = new AccessControlWhitePo();
            if (accessControlWhiteDtos == null || accessControlWhiteDtos.size() < 1) {
                accessControlWhitePo.setAcwId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
                accessControlWhitePo.setCommunityId(visitPo.getCommunityId());
                accessControlWhitePo.setEndTime(visitPo.getDepartureTime());
                accessControlWhitePo.setIdCard("");
                accessControlWhitePo.setMachineId(tmpMachineDto.getMachineId());
                accessControlWhitePo.setPersonName(visitPo.getvName());
                accessControlWhitePo.setPersonType(AccessControlWhiteDto.PERSON_TYPE_VISIT);
                accessControlWhitePo.setStartTime(visitPo.getVisitTime());
                accessControlWhitePo.setTel(visitPo.getPhoneNumber());
                accessControlWhitePo.setThirdId(visitPo.getvId());
                int flag = accessControlWhiteV1InnerServiceSMOImpl.saveAccessControlWhite(accessControlWhitePo);
                if (flag < 1) {
                    throw new CmdException("同步门禁白名单失败");
                }
            } else {
                accessControlWhitePo.setAcwId(accessControlWhiteDtos.get(0).getAcwId());
                accessControlWhitePo.setStartTime(visitPo.getVisitTime());
                accessControlWhitePo.setEndTime(visitPo.getDepartureTime());
                int flag = accessControlWhiteV1InnerServiceSMOImpl.updateAccessControlWhite(accessControlWhitePo);
                if (flag < 1) {
                    throw new CmdException("保存数据失败");
                }
            }

            photoSMOImpl.savePhoto(photo, accessControlWhitePo.getAcwId(), accessControlWhitePo.getCommunityId());

        }
    }

    /**
     * 预约车辆 加入 白名单 是最合适的
     * 不应该加入到业主车辆中
     *
     * @param visitPo
     * @param carNumWay
     * @param visitSettingDto
     */
    private void synchronizedVisitCarNum(VisitPo visitPo, String carNumWay, VisitSettingDto visitSettingDto) {
        if (VisitSettingDto.CAR_NUM_WAY_NO.equals(carNumWay)) {
            return;
        }

        CarBlackWhitePo carBlackWhitePo = new CarBlackWhitePo();
        carBlackWhitePo.setCarNum(visitPo.getCarNum());
        carBlackWhitePo.setBlackWhite(CarBlackWhiteDto.BLACK_WHITE_WHITE);
        carBlackWhitePo.setCommunityId(visitPo.getCommunityId());
        carBlackWhitePo.setPaId(visitSettingDto.getPaId());
        carBlackWhitePo.setBwId(GenerateCodeFactory.getGeneratorId("11"));
        carBlackWhitePo.setStartTime(visitPo.getVisitTime());
        carBlackWhitePo.setEndTime(visitPo.getDepartureTime());
        int flag = carBlackWhiteV1InnerServiceSMOImpl.saveCarBlackWhite(carBlackWhitePo);
        if (flag < 1) {
            throw new CmdException("预约车辆添加白名单失败");
        }


    }

    /**
     * 是否需要审核
     *
     * @param visitPo
     * @param reqJson
     */
    private boolean hasAuditVisit(VisitPo visitPo, JSONObject reqJson,String storeId,String userId) {


        VisitSettingDto visitSettingDto = new VisitSettingDto();
        visitSettingDto.setCommunityId(reqJson.getString("communityId"));
        List<VisitSettingDto> visitSettingDtos = visitSettingV1InnerServiceSMOImpl.queryVisitSettings(visitSettingDto);

        if (visitSettingDtos == null || visitSettingDtos.size() < 1) {
            return false;
        }

        // 需要审核
        if (!VisitSettingDto.AUDIT_WAY_YES.equals(visitSettingDtos.get(0).getAuditWay())) {
            return false;
        }


        //触发 审批流程
        OaWorkflowDto oaWorkflowDto = new OaWorkflowDto();
        oaWorkflowDto.setStoreId(storeId);
        oaWorkflowDto.setFlowId(visitPo.getvId());
        List<OaWorkflowDto> oaWorkflowDtos = oaWorkflowInnerServiceSMOImpl.queryOaWorkflows(oaWorkflowDto);
        Assert.listOnlyOne(oaWorkflowDtos, "流程不存在");
        if (!OaWorkflowDto.STATE_COMPLAINT.equals(oaWorkflowDtos.get(0).getState())) {
            throw new IllegalArgumentException(oaWorkflowDtos.get(0).getFlowName() + "流程未部署");
        }

        if (StringUtil.isEmpty(oaWorkflowDtos.get(0).getProcessDefinitionKey())) {
            throw new IllegalArgumentException(oaWorkflowDtos.get(0).getFlowName() + "流程未部署");
        }

        //启动任务
        JSONObject flowJson = new JSONObject();
        flowJson.put("processDefinitionKey", oaWorkflowDtos.get(0).getProcessDefinitionKey());
        flowJson.put("createUserId",userId);
        flowJson.put("flowId",oaWorkflowDtos.get(0).getFlowId());
        flowJson.put("id",visitPo.getvId());
        flowJson.put("auditMessage","提交审核");
        flowJson.put("storeId",storeId);
        reqJson.put("processDefinitionKey", oaWorkflowDtos.get(0).getProcessDefinitionKey());
        JSONObject result = oaWorkflowActivitiInnerServiceSMOImpl.startProcess(flowJson);

        //提交者提交
        flowJson = new JSONObject();
        flowJson.put("processInstanceId",result.getString("processInstanceId"));
        flowJson.put("createUserId",userId);
        flowJson.put("nextUserId",userId); // 这里要求流程 下一处理人必须要指定
        flowJson.put("storeId",storeId);
        flowJson.put("id",visitPo.getvId());
        flowJson.put("flowId",oaWorkflowDtos.get(0).getFlowId());

        oaWorkflowActivitiInnerServiceSMOImpl.autoFinishFirstTask(flowJson);

        return true;

    }


}
