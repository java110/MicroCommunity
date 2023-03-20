package com.java110.job.task.parkingSpace;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.machine.CarBlackWhiteDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.task.TaskDto;
import com.java110.dto.visit.VisitDto;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IVisitInnerServiceSMO;
import com.java110.intf.user.ICarBlackWhiteV1InnerServiceSMO;
import com.java110.job.adapt.hcIot.asyn.IIotSendAsyn;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.po.car.CarBlackWhitePo;
import com.java110.po.parking.ParkingSpacePo;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author fqz
 * @Description 释放预约车车位（依据停车免费结束时间）
 * @date 2022-04-18 17:08
 */
@Component
public class ReleaseParkingSpaceTemplate extends TaskSystemQuartz {

    @Autowired
    private IVisitInnerServiceSMO visitInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private ICarBlackWhiteV1InnerServiceSMO carBlackWhiteV1InnerServiceSMOImpl;

    @Autowired
    private IIotSendAsyn hcCarBlackWhiteAsynImpl;

    private static Logger logger = LoggerFactory.getLogger(ReleaseParkingSpaceTemplate.class);

    @Override
    protected void process(TaskDto taskDto) throws Exception {
        logger.debug("开始执行微信模板信息推送" + taskDto.toString());
        // 获取小区
        List<CommunityDto> communityDtos = getAllCommunity();
        for (CommunityDto communityDto : communityDtos) {
            doReleaseParkingSpace(taskDto, communityDto);
        }
    }

    /**
     * 根据小区释放预约车辆车位
     *
     * @param taskDto
     * @param communityDto
     */
    private void doReleaseParkingSpace(TaskDto taskDto, CommunityDto communityDto) throws ParseException {
        //查询小区下的访客信息
        VisitDto visitDto = new VisitDto();
        visitDto.setCommunityId(communityDto.getCommunityId());
        visitDto.setCarNumNoEmpty("1"); //车辆不为空
        visitDto.setFlag("1"); //车位不为空
        List<VisitDto> visitDtos = visitInnerServiceSMOImpl.queryVisits(visitDto);
        if (visitDtos == null || visitDtos.size() < 1) {
            return;
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (VisitDto visit : visitDtos) {
            //查看是否有车位
            ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
            parkingSpaceDto.setPsId(visit.getPsId());
            List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
            if (parkingSpaceDtos == null || parkingSpaceDtos.size() != 1) {
                continue;
            }
            if (StringUtil.isEmpty(parkingSpaceDtos.get(0).getState()) || !parkingSpaceDtos.get(0).getState().equals("H")) { //出售 S，出租 H ，空闲 F
                continue;
            }
            if (StringUtil.isEmpty(visit.getFreeTime())) {
                continue;
            }
            //查看车辆是否在黑白名单里
            CarBlackWhiteDto carBlackWhiteDto = new CarBlackWhiteDto();
            carBlackWhiteDto.setCarNum(visit.getCarNum());
            carBlackWhiteDto.setCommunityId(visit.getCommunityId());
            carBlackWhiteDto.setBlackWhite("2222"); //1111 黑名单 2222 白名单
            carBlackWhiteDto.setStartTime(visit.getVisitTime());
            carBlackWhiteDto.setEndTime(visit.getFreeTime());
            List<CarBlackWhiteDto> carBlackWhiteDtos = carBlackWhiteV1InnerServiceSMOImpl.queryCarBlackWhites(carBlackWhiteDto);
            if (carBlackWhiteDtos == null || carBlackWhiteDtos.size() != 1) {
                continue;
            }
            //获取预约车免费时间
            Date freeTime = df.parse(visit.getFreeTime());
            //当前时间
            Date date = new Date();
            if (freeTime.getTime() <= date.getTime()) { //如果预约车免费时间小于等于当前时间，说明到期了，需要释放掉
                ParkingSpacePo parkingSpacePo = new ParkingSpacePo();
                parkingSpacePo.setPsId(visit.getPsId());
                parkingSpacePo.setState("F");//车位状态 出售 S，出租 H ，空闲 F
                //车位状态改为空闲状态
                parkingSpaceInnerServiceSMOImpl.updateParkingSpace(parkingSpacePo);
                //删除白名单
                CarBlackWhitePo carBlackWhitePo = new CarBlackWhitePo();
                carBlackWhitePo.setBwId(carBlackWhiteDtos.get(0).getBwId());
                carBlackWhitePo.setStatusCd("1");
                carBlackWhiteV1InnerServiceSMOImpl.deleteCarBlackWhite(carBlackWhitePo);
                //向第三方物联网推送删除白名单信息
                JSONObject postParameters = new JSONObject();
                postParameters.put("extBwId", carBlackWhiteDtos.get(0).getBwId());
                postParameters.put("carNum", carBlackWhiteDtos.get(0).getCarNum());
                postParameters.put("extPaId", carBlackWhiteDtos.get(0).getPaId());
                postParameters.put("extCommunityId", carBlackWhiteDtos.get(0).getCommunityId());
                postParameters.put("startTime", carBlackWhiteDtos.get(0).getStartTime());
                postParameters.put("endTime", carBlackWhiteDtos.get(0).getEndTime());
                postParameters.put("blackWhite", carBlackWhiteDtos.get(0).getBlackWhite());
                hcCarBlackWhiteAsynImpl.deleteCarBlackWhite(postParameters);
            }
        }
    }

}
