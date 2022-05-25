package com.java110.job.task.parkingSpace;

import com.java110.core.log.LoggerFactory;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.ownerCarAttr.OwnerCarAttrDto;
import com.java110.dto.task.TaskDto;
import com.java110.dto.visit.VisitDto;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IVisitInnerServiceSMO;
import com.java110.intf.user.IOwnerCarAttrInnerServiceSMO;
import com.java110.intf.user.IOwnerCarAttrV1InnerServiceSMO;
import com.java110.intf.user.IOwnerCarV1InnerServiceSMO;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.po.car.OwnerCarPo;
import com.java110.po.ownerCarAttr.OwnerCarAttrPo;
import com.java110.po.parking.ParkingSpacePo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 预约车定时释放车位
 *
 * @author fqz
 * @date 2022-04-21
 */
@Component
public class ReleaseCarParkingSpaceTemplate extends TaskSystemQuartz {

    @Autowired
    private IVisitInnerServiceSMO visitInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarAttrInnerServiceSMO ownerCarAttrInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarAttrV1InnerServiceSMO ownerCarAttrV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerCarV1InnerServiceSMO ownerCarV1InnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    private static Logger logger = LoggerFactory.getLogger(ReleaseCarParkingSpaceTemplate.class);

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
    private void doReleaseParkingSpace(TaskDto taskDto, CommunityDto communityDto) {
        //查询小区下的访客信息
        VisitDto visitDto = new VisitDto();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        visitDto.setCommunityId(communityDto.getCommunityId());
        visitDto.setDepartureEndTime(df.format(new Date()));
        visitDto.setState("1"); //审核通过
        visitDto.setFlag("1");
        List<VisitDto> visitDtos = visitInnerServiceSMOImpl.queryVisits(visitDto);
        if (visitDtos == null || visitDtos.size() < 1) {
            return;
        }
        for (VisitDto visit : visitDtos) {
            OwnerCarDto ownerCarDto = new OwnerCarDto();
            ownerCarDto.setCommunityId(communityDto.getCommunityId());
            ownerCarDto.setCarTypeCd(OwnerCarDto.CAR_TYPE_TEMP); //临时车
            ownerCarDto.setCarNum(visit.getCarNum());
            List<OwnerCarDto> ownerCarDtos = ownerCarV1InnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
            if (ownerCarDtos == null || ownerCarDtos.size() != 1) {
                continue;
            }
            OwnerCarAttrDto ownerCarAttrDto = new OwnerCarAttrDto();
            ownerCarAttrDto.setCarId(ownerCarDtos.get(0).getCarId());
            ownerCarAttrDto.setSpecCd("6443000036"); //是否是预约车
            //查询车辆属性
            List<OwnerCarAttrDto> ownerCarAttrDtos = ownerCarAttrInnerServiceSMOImpl.queryOwnerCarAttrs(ownerCarAttrDto);
            if (ownerCarAttrDtos == null || ownerCarAttrDtos.size() != 1) { //只看是否是预约车属性
                continue;
            }
            if (!ownerCarAttrDtos.get(0).getValue().equals("true")) { //不是预约车的不走定时任务
                continue;
            }
            //释放车位并删除过期的车辆
            OwnerCarPo ownerCarPo = new OwnerCarPo();
            ownerCarPo.setCarId(ownerCarDtos.get(0).getCarId());
            ownerCarPo.setCommunityId(communityDto.getCommunityId());
            ownerCarPo.setCarTypeCd(OwnerCarDto.CAR_TYPE_TEMP);
            ownerCarPo.setState(OwnerCarDto.STATE_FINISH); //车位释放状态
            ownerCarPo.setStatusCd("1"); //不可用状态
            //修改车辆状态，改为车辆释放状态并删除
            ownerCarV1InnerServiceSMOImpl.updateOwnerCar(ownerCarPo);
            //修改车辆属性状态
            OwnerCarAttrPo ownerCarAttrPo = new OwnerCarAttrPo();
            ownerCarAttrPo.setCarId(ownerCarDtos.get(0).getCarId());
            ownerCarAttrPo.setStatusCd("1");
            //删除过期车辆属性
            ownerCarAttrV1InnerServiceSMOImpl.updateOwnerCarAttr(ownerCarAttrPo);
            //修改车位状态
            ParkingSpacePo parkingSpacePo = new ParkingSpacePo();
            parkingSpacePo.setPsId(ownerCarDtos.get(0).getPsId());
            parkingSpacePo.setState("F");//车位状态 出售 S，出租 H ，空闲 F
            //车位状态改为空闲状态
            parkingSpaceInnerServiceSMOImpl.updateParkingSpace(parkingSpacePo);
        }
    }

}
