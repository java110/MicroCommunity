package com.java110.job.adapt.hcIotNew;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.community.CommunityMemberDto;
import com.java110.dto.file.FileDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.machine.MachineTranslateDto;
import com.java110.dto.store.StoreUserDto;
import com.java110.dto.system.Business;
import com.java110.dto.user.UserDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.common.IMachineTranslateInnerServiceSMO;
import com.java110.intf.community.ICommunityMemberV1InnerServiceSMO;
import com.java110.intf.store.IStoreUserV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
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
 * 200100080001
 */
@Component(value = "sendDeleteStaffDataToIotAdapt")
public class SendDeleteStaffDataToIotAdapt extends DatabusAdaptImpl {

    @Autowired
    private IMachineTranslateInnerServiceSMO machineTranslateInnerServiceSMOImpl;

    @Autowired
    private ISendIot sendIotImpl;

    @Autowired
    private IStoreUserV1InnerServiceSMO storeUserV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityMemberV1InnerServiceSMO communityMemberV1InnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;


    @Override
    public void execute(Business business, List<Business> businesses) {
        String iotSwitch = MappingCache.getValue("IOT", "IOT_SWITCH");
        if (!"ON".equals(iotSwitch)) {
            return;
        }

        JSONObject data = business.getData();
        String userId = data.getString("userId");
        if (StringUtil.isEmpty(userId)) {
            throw new IllegalArgumentException("未包含员工信息");
        }

        StoreUserDto storeUserDto = new StoreUserDto();
        storeUserDto.setUserId(userId);
        storeUserDto.setStatusCd("1");
        List<StoreUserDto> storeUserDtos = storeUserV1InnerServiceSMOImpl.queryStoreUsers(storeUserDto);

        if (ListUtil.isNull(storeUserDtos)) {
            throw new IllegalArgumentException("员工不存在");
        }

        JSONObject staff = new JSONObject();
        staff.put("propertyId", storeUserDtos.get(0).getStoreId());
        staff.put("staffId", userId);


        ResultVo resultVo = sendIotImpl.post("/iot/api/staff.deleteStaffApi", staff);

        CommunityMemberDto communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setMemberId(storeUserDtos.get(0).getStoreId());
        List<CommunityMemberDto> communityMemberDtos = communityMemberV1InnerServiceSMOImpl.queryCommunityMembers(communityMemberDto);

        if (ListUtil.isNull(communityMemberDtos)) {
            return;
        }

        if (resultVo.getCode() != ResultVo.CODE_OK) {
            saveTranslateLog(communityMemberDtos.get(0).getCommunityId(), MachineTranslateDto.CMD_DELETE_COMMUNITY,
                    staff.getString("staffId"), staff.getString("name"),
                    MachineTranslateDto.STATE_ERROR, resultVo.getMsg());
            return;
        }

        saveTranslateLog(communityMemberDtos.get(0).getCommunityId(), MachineTranslateDto.CMD_DELETE_COMMUNITY,
                staff.getString("staffId"), staff.getString("name"),
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
        machineTranslateDto.setTypeCd(MachineTranslateDto.TYPE_COMMUNITY);
        machineTranslateDto.setRemark(remark);
        machineTranslateDto.setState(state);
        machineTranslateDto.setbId("-1");
        machineTranslateDto.setObjBId("-1");
        machineTranslateDto.setUpdateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        machineTranslateInnerServiceSMOImpl.saveMachineTranslate(machineTranslateDto);
    }


}
