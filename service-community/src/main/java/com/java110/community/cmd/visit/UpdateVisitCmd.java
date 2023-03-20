package com.java110.community.cmd.visit;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.IPhotoSMO;
import com.java110.dto.RoomDto;
import com.java110.dto.accessControlWhite.AccessControlWhiteDto;
import com.java110.dto.machine.CarBlackWhiteDto;
import com.java110.dto.machine.MachineDto;
import com.java110.dto.visit.VisitDto;
import com.java110.dto.visitSetting.VisitSettingDto;
import com.java110.intf.common.IAccessControlWhiteV1InnerServiceSMO;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.community.IVisitSettingV1InnerServiceSMO;
import com.java110.intf.community.IVisitV1InnerServiceSMO;
import com.java110.intf.user.ICarBlackWhiteV1InnerServiceSMO;
import com.java110.po.accessControlWhite.AccessControlWhitePo;
import com.java110.po.car.CarBlackWhitePo;
import com.java110.po.owner.VisitPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Java110Cmd(serviceCode = "visit.updateVisit")
public class UpdateVisitCmd extends Cmd {

    @Autowired
    private IVisitV1InnerServiceSMO visitV1InnerServiceSMOImpl;

    @Autowired
    private IPhotoSMO photoSMOImpl;

    @Autowired
    private IVisitSettingV1InnerServiceSMO visitSettingV1InnerServiceSMOImpl;

    @Autowired
    private ICarBlackWhiteV1InnerServiceSMO carBlackWhiteV1InnerServiceSMOImpl;

    @Autowired
    private IAccessControlWhiteV1InnerServiceSMO accessControlWhiteV1InnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    public static final String CODE_PREFIX_ID = "10";

    //键
    public static final String CAR_FREE_TIME = "CAR_FREE_TIME";

    //键
    public static final String ASCRIPTION_CAR_AREA_ID = "ASCRIPTION_CAR_AREA_ID";

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "vId", "访客记录ID不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");
        Assert.hasKeyAndValue(reqJson, "vName", "必填，请填写访客姓名");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        VisitDto visitDto = new VisitDto();
        visitDto.setvId(reqJson.getString("vId"));
        visitDto.setCommunityId(reqJson.getString("communityId"));
        List<VisitDto> visitDtos = visitV1InnerServiceSMOImpl.queryVisits(visitDto);
        Assert.listOnlyOne(visitDtos, "访客不存在");
        VisitPo visitPo = BeanConvertUtil.covertBean(reqJson, VisitPo.class);
        visitPo.setState(visitDtos.get(0).getState());
        int flag = visitV1InnerServiceSMOImpl.updateVisit(visitPo);
        if (flag < 1) {
            throw new CmdException("保存访客失败");
        }
        photoSMOImpl.savePhoto(reqJson, reqJson.getString("vId"), reqJson.getString("communityId"));
        if (!VisitDto.STATE_C.equals(visitDtos.get(0).getState())) {
            return;
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
        if (StringUtil.isEmpty(visitPo.getOwnerId())) {
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
        if (StringUtil.isEmpty(visitPo.getCarNum())) {
            return;
        }
        CarBlackWhiteDto carBlackWhiteDto = new CarBlackWhiteDto();
        carBlackWhiteDto.setBlackWhite(CarBlackWhiteDto.BLACK_WHITE_WHITE);
        carBlackWhiteDto.setCarNum(visitPo.getCarNum());
        carBlackWhiteDto.setPaId(visitSettingDto.getPaId());
        List<CarBlackWhiteDto> carBlackWhiteDtos = carBlackWhiteV1InnerServiceSMOImpl.queryCarBlackWhites(carBlackWhiteDto);
        CarBlackWhitePo carBlackWhitePo = new CarBlackWhitePo();
        carBlackWhitePo.setCarNum(visitPo.getCarNum());
        carBlackWhitePo.setBlackWhite(CarBlackWhiteDto.BLACK_WHITE_WHITE);
        carBlackWhitePo.setCommunityId(visitPo.getCommunityId());
        carBlackWhitePo.setPaId(visitSettingDto.getPaId());
        carBlackWhitePo.setStartTime(visitPo.getVisitTime());
        carBlackWhitePo.setEndTime(visitPo.getDepartureTime());
        int flag = 0;
        if (carBlackWhiteDtos == null || carBlackWhiteDtos.size() < 1) {
            carBlackWhitePo.setBwId(GenerateCodeFactory.getGeneratorId("11"));
            flag = carBlackWhiteV1InnerServiceSMOImpl.saveCarBlackWhite(carBlackWhitePo);
        } else {
            carBlackWhitePo.setBwId(carBlackWhiteDtos.get(0).getBwId());
            flag = carBlackWhiteV1InnerServiceSMOImpl.updateCarBlackWhite(carBlackWhitePo);
        }

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
    private boolean hasAuditVisit(VisitPo visitPo, JSONObject reqJson, String storeId, String userId) {
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
        return true;
    }
}
