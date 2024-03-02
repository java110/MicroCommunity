package com.java110.job.adapt.hcIotNew;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.machine.MachineTranslateDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.system.Business;
import com.java110.intf.common.IMachineTranslateInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerCarV1InnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.adapt.hcIotNew.http.ISendIot;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 同步车辆信息
 */
@Component(value = "sendCarDataToIotAdapt")
public class SendCarDataToIotAdapt extends DatabusAdaptImpl {

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerDataToIot ownerDataToIotImpl;

    @Autowired
    private IMachineTranslateInnerServiceSMO machineTranslateInnerServiceSMOImpl;

    @Autowired
    private ISendIot sendIotImpl;

    @Override
    public void execute(Business business, List<Business> businesses) {
        String iotSwitch = MappingCache.getValue("IOT", "IOT_SWITCH");
        if (!"ON".equals(iotSwitch)) {
            return;
        }

        JSONObject data = business.getData();
        String memberId = data.getString("memberId");
        if (StringUtil.isEmpty(memberId)) {
            throw new IllegalArgumentException("车辆不存在");
        }

        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setMemberId(memberId);
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        if (ListUtil.isNull(ownerCarDtos)) {
            throw new IllegalArgumentException("车辆不存在");
        }

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(ownerCarDtos.get(0).getOwnerId());
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);
        if (ListUtil.isNull(ownerDtos)) {
            throw new IllegalArgumentException("业主不存在");
        }

        /**
         * {
         * “communityId”:”12313”,
         * “ownerId”:”123123”,
         * “name”:”张三”,
         * “link”:”18909711445”,
         * “carMemberId”:”1231”,
         * “carNum”:”青A88488”,
         * “paId”:”1231”,
         * “psId”:”1231”,
         * “paNum”:”1231”,
         * “startTime”:”2024-01-01 11:22:22”,
         * “endTime”:”2024-12-01 11:22:22”,
         * “leaseType”:”1231”,
         * “carTypeCd”:”123123”
         * }
         */

        OwnerCarDto tmpOwnerCarDto = ownerCarDtos.get(0);

        JSONObject car = new JSONObject();
        car.put("communityId", ownerDtos.get(0).getCommunityId());
        car.put("ownerId", ownerDtos.get(0).getMemberId());
        car.put("name", ownerDtos.get(0).getName());
        car.put("link", ownerDtos.get(0).getLink());
        car.put("carMemberId", tmpOwnerCarDto.getMemberId());
        car.put("carId", tmpOwnerCarDto.getCarId());
        car.put("carNum", tmpOwnerCarDto.getCarNum());
        car.put("paId", tmpOwnerCarDto.getPaId());
        car.put("psId", tmpOwnerCarDto.getPsId());
        car.put("paNum", tmpOwnerCarDto.getAreaNum());
        car.put("psNum", tmpOwnerCarDto.getNum());
        car.put("carTypeCd", tmpOwnerCarDto.getCarTypeCd());
        car.put("startTime", DateUtil.getFormatTimeStringA(tmpOwnerCarDto.getStartTime()));
        car.put("endTime", DateUtil.getFormatTimeStringA(tmpOwnerCarDto.getEndTime()));
        car.put("leaseType", tmpOwnerCarDto.getLeaseType());

        ResultVo resultVo = sendIotImpl.post("/iot/api/car.addCarApi", car);
        if (resultVo.getCode() != ResultVo.CODE_OK) {
            saveTranslateLog(ownerDtos.get(0).getCommunityId(), MachineTranslateDto.CMD_ADD_OWNER_CAR,
                    car.getString("carMemberId"), car.getString("carNum"),
                    MachineTranslateDto.STATE_ERROR, resultVo.getMsg());
            return;
        }

        saveTranslateLog(ownerDtos.get(0).getCommunityId(), MachineTranslateDto.CMD_ADD_OWNER_CAR,
                car.getString("carMemberId"), car.getString("carNum"),
                MachineTranslateDto.STATE_SUCCESS, resultVo.getMsg());
    }

    /**
     * 存储交互 记录
     *
     * @param communityId
     */
    public void saveTranslateLog(String communityId, String cmd, String objId, String objName, String state, String remark) {
        MachineTranslateDto machineTranslateDto = new MachineTranslateDto();
        machineTranslateDto.setMachineTranslateId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_machineTranslateId));
        machineTranslateDto.setCommunityId(communityId);
        machineTranslateDto.setMachineCmd(cmd);
        machineTranslateDto.setMachineCode("-1");
        machineTranslateDto.setMachineId("-1");
        machineTranslateDto.setObjId(objId);
        machineTranslateDto.setObjName(objName);
        machineTranslateDto.setTypeCd(MachineTranslateDto.TYPE_OWNER_CAR);
        machineTranslateDto.setRemark(remark);
        machineTranslateDto.setState(state);
        machineTranslateDto.setbId("-1");
        machineTranslateDto.setObjBId("-1");
        machineTranslateDto.setUpdateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        machineTranslateInnerServiceSMOImpl.saveMachineTranslate(machineTranslateDto);
    }
}
