package com.java110.job.adapt.hcIotNew;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.machine.MachineTranslateDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.system.Business;
import com.java110.intf.common.IMachineTranslateInnerServiceSMO;
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
 * 同步业主信息
 */
@Component(value = "sendDeleteOwnerDataToIotAdapt")
public class SendDeleteOwnerDataToIotAdapt extends DatabusAdaptImpl {

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
            throw new IllegalArgumentException("未包含业主信息");
        }

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(memberId);
        ownerDto.setStatusCd("1");
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);

        if (ListUtil.isNull(ownerDtos)) {
            throw new IllegalArgumentException("业主不存在");
        }

        JSONObject car = new JSONObject();
        car.put("communityId", ownerDtos.get(0).getCommunityId());
        car.put("ownerId", ownerDtos.get(0).getOwnerId());
        car.put("memberId", ownerDtos.get(0).getMemberId());


       ResultVo resultVo = sendIotImpl.post("/iot/api/owner.deleteOwnerApi", car);

        if(resultVo.getCode() != ResultVo.CODE_OK){
            saveTranslateLog(ownerDto.getCommunityId(),MachineTranslateDto.CMD_DELETE_OWNER_FACE,
                    ownerDtos.get(0).getMemberId(),ownerDtos.get(0).getName(),
                    MachineTranslateDto.STATE_ERROR,resultVo.getMsg());
            return ;
        }

        saveTranslateLog(ownerDto.getCommunityId(),MachineTranslateDto.CMD_DELETE_OWNER_FACE,
                ownerDtos.get(0).getMemberId(),ownerDtos.get(0).getName(),
                MachineTranslateDto.STATE_SUCCESS,resultVo.getMsg());
    }


    /**
     * 存储交互 记录
     *
     * @param communityId
     */
    public void saveTranslateLog(String communityId,String cmd,String objId,String objName,String state,String remark) {
        MachineTranslateDto machineTranslateDto = new MachineTranslateDto();
        machineTranslateDto.setMachineTranslateId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_machineTranslateId));
        machineTranslateDto.setCommunityId(communityId);
        machineTranslateDto.setMachineCmd(cmd);
        machineTranslateDto.setMachineCode("-1");
        machineTranslateDto.setMachineId("-1");
        machineTranslateDto.setObjId(objId);
        machineTranslateDto.setObjName(objName);
        machineTranslateDto.setTypeCd(MachineTranslateDto.TYPE_OWNER);
        machineTranslateDto.setRemark(remark);
        machineTranslateDto.setState(state);
        machineTranslateDto.setbId("-1");
        machineTranslateDto.setObjBId("-1");
        machineTranslateDto.setUpdateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        machineTranslateInnerServiceSMOImpl.saveMachineTranslate(machineTranslateDto);
    }
}
