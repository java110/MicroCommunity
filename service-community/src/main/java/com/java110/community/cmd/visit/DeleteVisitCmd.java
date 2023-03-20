package com.java110.community.cmd.visit;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.machine.CarBlackWhiteDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.visit.VisitDto;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IVisitV1InnerServiceSMO;
import com.java110.intf.user.ICarBlackWhiteV1InnerServiceSMO;
import com.java110.po.car.CarBlackWhitePo;
import com.java110.po.owner.VisitPo;
import com.java110.po.parking.ParkingSpacePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Java110Cmd(serviceCode = "visit.deleteVisit")
public class DeleteVisitCmd extends Cmd {

    @Autowired
    private IVisitV1InnerServiceSMO visitV1InnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private ICarBlackWhiteV1InnerServiceSMO carBlackWhiteV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "vId", "访客记录ID不能为空");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        VisitDto visitDto = new VisitDto();
        visitDto.setvId(reqJson.getString("vId"));
        List<VisitDto> visitDtos = visitV1InnerServiceSMOImpl.queryVisits(visitDto);
        Assert.listOnlyOne(visitDtos, "查询访客信息错误！");
        VisitPo visitPo = BeanConvertUtil.covertBean(reqJson, VisitPo.class);
        visitPo.setStatusCd("1");
        int flag = visitV1InnerServiceSMOImpl.deleteVisit(visitPo);
        if (flag < 1) {
            throw new CmdException("删除异常");
        }
        if (!StringUtil.isEmpty(visitDtos.get(0).getCarNum()) && !StringUtil.isEmpty(visitDtos.get(0).getPsId()) && !visitDtos.get(0).getPsId().equals("-1")) { //有车辆，且有车位
            ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
            parkingSpaceDto.setPsId(visitDtos.get(0).getPsId());
            List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto); //查询车位信息
            Assert.listOnlyOne(parkingSpaceDtos, "车位信息不存在或存在多条！");
            if (parkingSpaceDtos.get(0).getState().equals("H")) { //车位状态 出售 S，出租 H ，空闲 F
                ParkingSpacePo parkingSpacePo = new ParkingSpacePo();
                parkingSpacePo.setPsId(parkingSpaceDtos.get(0).getPsId());
                parkingSpacePo.setState("F");
                parkingSpaceInnerServiceSMOImpl.updateParkingSpace(parkingSpacePo); //释放车位
            }
            CarBlackWhiteDto carBlackWhiteDto = new CarBlackWhiteDto();
            carBlackWhiteDto.setCarNum(visitDtos.get(0).getCarNum());
            carBlackWhiteDto.setPaId(parkingSpaceDtos.get(0).getPaId());
            List<CarBlackWhiteDto> carBlackWhiteDtos = carBlackWhiteV1InnerServiceSMOImpl.queryCarBlackWhites(carBlackWhiteDto);
            if (carBlackWhiteDtos != null && carBlackWhiteDtos.size() == 1) {
                CarBlackWhitePo carBlackWhitePo = new CarBlackWhitePo();
                carBlackWhitePo.setBwId(carBlackWhiteDtos.get(0).getBwId());
                carBlackWhiteV1InnerServiceSMOImpl.deleteCarBlackWhite(carBlackWhitePo); //删除白名单
            } else if (carBlackWhiteDtos != null && carBlackWhiteDtos.size() > 1) {
                throw new IllegalArgumentException("查询出多条白名单信息！");
            } else {
                throw new IllegalArgumentException("查询不到白名单信息！");
            }
        }
    }
}
