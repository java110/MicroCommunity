package com.java110.job.task.fee;

import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.RoomDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.fee.*;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.task.TaskDto;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IFeeDetailInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.TaskTemplateException;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName GenerateOwnerBillTemplate
 * @Description TODO  房屋费用账单生成
 * @Author wuxw
 * @Date 2020/6/4 8:33
 * @Version 1.0
 * add by wuxw 2020/6/4
 **/
@Component
public class GenerateOweFeeTemplate extends TaskSystemQuartz {


    private static final String TASK_ATTR_VALUE_ONCE_MONTH = "005"; //一次性按月出账

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;


    @Override
    protected void process(TaskDto taskDto) throws Exception {

        // 获取小区
        List<CommunityDto> communityDtos = getAllCommunity();

        for (CommunityDto communityDto : communityDtos) {
            GenerateOweFee(taskDto, communityDto);
        }

    }

    /**
     * 根据小区生成账单
     *
     * @param communityDto
     */
    private void GenerateOweFee(TaskDto taskDto, CommunityDto communityDto) {

        //查询费用项
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(communityDto.getCommunityId());
        if (StringUtil.isEmpty(feeConfigDto.getBillType())) {
            throw new IllegalArgumentException("配置错误 未拿到属性值");
        }
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);

        for (FeeConfigDto tmpFeeConfigDto : feeConfigDtos) {
            try {
                GenerateOweFeeByFeeConfig(taskDto, tmpFeeConfigDto);
            } catch (Exception e) {
                logger.error("费用出账失败" + tmpFeeConfigDto.getConfigId(), e);
            }
        }


    }

    /**
     * 按费用项来出账
     *
     * @param taskDto
     * @param feeConfigDto
     */
    private void GenerateOweFeeByFeeConfig(TaskDto taskDto, FeeConfigDto feeConfigDto) throws Exception {

        //当前费用项是否

        FeeDto feeDto = new FeeDto();
        feeDto.setConfigId(feeConfigDto.getConfigId());
        feeDto.setCommunityId(feeConfigDto.getCommunityId());
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        //没有关联费用
        if (feeDto == null || feeDtos.size() < 1) {
            return;
        }
        for (FeeDto tmpFeeDto : feeDtos) {
            try {
                generateFee(tmpFeeDto, feeConfigDto);
            } catch (Exception e) {
                logger.error("生成费用失败", e);
            }
        }

    }

    /**
     * 生成 费用
     *
     * @param feeDto
     */
    private void generateFee(FeeDto feeDto, FeeConfigDto feeConfigDto) {

        computeFeeSMOImpl.computeEveryOweFee(feeDto);


    }

    /**
     * 查询车位信息
     *
     * @param billOweFeeDto
     * @param feeDto
     */
    private void getParkingSpaceInfo(BillOweFeeDto billOweFeeDto, FeeDto feeDto) {


        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setWithOwner(true);
        ownerCarDto.setCarId(feeDto.getPayerObjId());
        ownerCarDto.setCommunityId(feeDto.getCommunityId());

        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);


        if (ownerCarDtos == null || ownerCarDtos.size() < 1) {
            //房屋可能被删除了
            billOweFeeDto.setOwnerId("1");
            billOweFeeDto.setOwnerName("未知");
            billOweFeeDto.setOwnerTel("19999999999");
            return;
        }

        billOweFeeDto.setPayerObjName(ownerCarDtos.get(0).getCarNum());
        billOweFeeDto.setOwnerId(ownerCarDtos.get(0).getOwnerId());
        billOweFeeDto.setOwnerName(ownerCarDtos.get(0).getOwnerName());
        billOweFeeDto.setOwnerTel(ownerCarDtos.get(0).getLink());
    }

    /**
     * 查询房屋信息
     *
     * @param billOweFeeDto
     * @param feeDto
     */
    private void getRoomInfo(BillOweFeeDto billOweFeeDto, FeeDto feeDto) {
        RoomDto roomDto = new RoomDto();
        roomDto.setRoomId(feeDto.getPayerObjId());
        roomDto.setCommunityId(feeDto.getCommunityId());
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

        if (roomDtos == null || roomDtos.size() < 1) {
            //房屋可能被删除了
            billOweFeeDto.setOweId("1");
            billOweFeeDto.setOwnerName("未知");
            billOweFeeDto.setOwnerTel("19999999999");
            billOweFeeDto.setPayerObjName("未知");
            return;
        }

        RoomDto tmpRoomDto = roomDtos.get(0);
        if (RoomDto.ROOM_TYPE_ROOM.equals(tmpRoomDto.getRoomType())) {
            billOweFeeDto.setPayerObjName(tmpRoomDto.getFloorNum() + "栋" + tmpRoomDto.getUnitNum() + "单元" + tmpRoomDto.getRoomNum() + "室");
        } else {
            billOweFeeDto.setPayerObjName(tmpRoomDto.getFloorNum() + "栋" + tmpRoomDto.getRoomNum() + "室");
        }

        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setByOwnerInfo(true);
        ownerRoomRelDto.setRoomId(tmpRoomDto.getRoomId());
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);

        if (ownerRoomRelDtos == null || ownerRoomRelDtos.size() < 1) {
            //房屋可能被删除了
            billOweFeeDto.setOweId("1");
            billOweFeeDto.setOwnerName("未知");
            billOweFeeDto.setOwnerTel("19999999999");
            return;
        }

        billOweFeeDto.setOwnerId(ownerRoomRelDtos.get(0).getOwnerId());
        billOweFeeDto.setOwnerName(ownerRoomRelDtos.get(0).getOwnerName());
        billOweFeeDto.setOwnerTel(ownerRoomRelDtos.get(0).getLink());

    }



}
