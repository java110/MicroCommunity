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
import com.java110.dto.file.FileDto;
import com.java110.dto.machine.CarBlackWhiteDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.visit.VisitDto;
import com.java110.dto.visitSetting.VisitSettingDto;
import com.java110.intf.common.ICarBlackWhiteInnerServiceSMO;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IVisitInnerServiceSMO;
import com.java110.intf.community.IVisitSettingV1InnerServiceSMO;
import com.java110.intf.community.IVisitV1InnerServiceSMO;
import com.java110.intf.user.ICarBlackWhiteV1InnerServiceSMO;
import com.java110.intf.user.IOwnerCarAttrInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerCarV1InnerServiceSMO;
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

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
        if (hasAuditVisit(visitPo, reqJson)) {
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
        synchronousVisitFace(visitPo, faceWay);

    }

    private void synchronousVisitFace(VisitPo visitPo, String faceWay) {
        if (VisitSettingDto.FACE_WAY_NO.equals(faceWay)) {
            return;
        }
    }

    /**
     * 预约车辆 加入 白名单 是最合适的
     * 不应该加入到业主车辆中
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
    private boolean hasAuditVisit(VisitPo visitPo, JSONObject reqJson) {


        VisitSettingDto visitSettingDto = new VisitSettingDto();
        visitSettingDto.setCommunityId(reqJson.getString("communityId"));
        List<VisitSettingDto> visitSettingDtos = visitSettingV1InnerServiceSMOImpl.queryVisitSettings(visitSettingDto);

        if (visitSettingDtos == null || visitSettingDtos.size() < 1) {
            return false;
        }

        // 需要审核
        if (VisitSettingDto.AUDIT_WAY_YES.equals(visitSettingDtos.get(0).getAuditWay())) {
            return false;
        }

        return true;

    }


}
