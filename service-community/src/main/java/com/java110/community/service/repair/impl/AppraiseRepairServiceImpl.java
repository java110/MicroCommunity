package com.java110.community.service.repair.impl;

import com.java110.community.dao.IRepairServiceDao;
import com.java110.community.dao.IRepairUserServiceDao;
import com.java110.community.service.repair.IAppraiseRepairService;
import com.java110.core.annotation.Java110Transactional;
import com.java110.dto.appraise.AppraiseDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.intf.common.appraise.IAppraiseApi;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;


/**
 * 报修评价
 */
@Service("appraiseRepairServiceImpl")
public class AppraiseRepairServiceImpl implements IAppraiseRepairService {

    @Autowired
    private IAppraiseApi appraiseApi;
    @Autowired
    private IRepairServiceDao repairServiceDaoImpl;

    @Autowired
    private IRepairUserServiceDao repairUserServiceDaoImpl;

    @Override
    @Java110Transactional
    public AppraiseDto appraiseRepair(@RequestBody AppraiseDto appraiseDto) {


        Map info = new HashMap();
        info.put("repairId", appraiseDto.getObjId());
        info.put("staffId", appraiseDto.getAppraiseUserId());
        info.put("state", RepairUserDto.STATE_DOING);
        info.put("endTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        info.put("context", "评价完成");
        info.put("statusCd","0");
        repairUserServiceDaoImpl.updateRepairUserInfoInstance(info);
        //将工单表的状态修改为完成
        info = new HashMap();
        info.put("repairId", appraiseDto.getObjId());
        info.put("statusCd", "0");
        info.put("state", RepairDto.STATE_RETURN_VISIT);
        repairServiceDaoImpl.updateRepairInfoInstance(info);


        //评价表中加入评价内容
        appraiseDto.setParentAppraiseId("-1");
        appraiseDto.setObjType(AppraiseDto.OBJ_TYPE_REPAIR);
        appraiseDto.setAppraiseType(AppraiseDto.APPRAISE_TYPE_PUBLIC);
        return appraiseApi.saveAppraise(appraiseDto);

    }
}
