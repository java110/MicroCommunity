package com.java110.community.bmo.repair.impl;

import com.java110.community.bmo.repair.IAppraiseRepairBMO;
import com.java110.community.dao.IRepairServiceDao;
import com.java110.community.dao.IRepairUserServiceDao;
import com.java110.core.annotation.Java110Transactional;
import com.java110.dto.appraise.AppraiseDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.common.IAppraiseInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.appraise.AppraisePo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报修评价
 */
@Service("appraiseRepairServiceImpl")
public class AppraiseRepairBMOImpl implements IAppraiseRepairBMO {

    @Autowired
    private IAppraiseInnerServiceSMO appraiseInnerServiceSMOImpl;

    @Autowired
    private IRepairServiceDao repairServiceDaoImpl;

    @Autowired
    private IRepairUserServiceDao repairUserServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMO;

    @Override
    @Java110Transactional
    public ResponseEntity<String> appraiseRepair(@RequestBody AppraiseDto appraiseDto) {
        //获取装修id
        String repairId = appraiseDto.getObjId();
        Map repairInfo = new HashMap();
        repairInfo.put("repairId", repairId);
        repairInfo.put("state", RepairUserDto.STATE_EVALUATE);
        List<Map> repairUserInfo = repairUserServiceDaoImpl.getRepairUserInfo(repairInfo);
        Assert.listOnlyOne(repairUserInfo, "该用户没有待评价的报修单");
        Map info = new HashMap();
        info.put("ruId", repairUserInfo.get(0).get("ruId"));
        info.put("repairId", appraiseDto.getObjId());
        info.put("state", RepairUserDto.STATE_FINISH);
        info.put("endTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        info.put("context", appraiseDto.getContext());
        info.put("statusCd", "0");
        repairUserServiceDaoImpl.updateRepairUserInfoInstance(info);
        //将工单表的状态修改为完成
        info = new HashMap();
        info.put("repairId", appraiseDto.getObjId());
        info.put("statusCd", "0");
        info.put("state", RepairDto.STATE_RETURN_VISIT);
        repairServiceDaoImpl.updateRepairInfoInstance(info);
        //评价表中加入评价内容
        AppraisePo appraisePo = BeanConvertUtil.covertBean(appraiseDto, AppraisePo.class);
        UserDto userDto = new UserDto();
        userDto.setUserId(appraisePo.getAppraiseUserId());
        List<UserDto> users = userInnerServiceSMO.getUsers(userDto);
        Assert.listOnlyOne(users, "信息错误，用户不存在！");
        appraisePo.setAppraiseUserName(users.get(0).getName());
        appraisePo.setParentAppraiseId("-1");
        appraisePo.setObjType(AppraiseDto.OBJ_TYPE_REPAIR);
        appraisePo.setAppraiseType(AppraiseDto.APPRAISE_TYPE_PUBLIC);
        appraiseInnerServiceSMOImpl.saveAppraise(appraisePo);
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, ResultVo.MSG_OK);

    }
}
