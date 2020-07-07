package com.java110.community.service.repair;

import com.java110.community.dao.IRepairServiceDao;
import com.java110.core.annotation.Java110Transactional;
import com.java110.dto.appraise.AppraiseDto;
import com.java110.dto.repair.RepairDto;
import com.java110.intf.common.appraise.ISaveAppraiseService;
import com.java110.intf.community.repair.IAppraiseRepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


/**
 * 报修评价
 */
@RestController
public class AppraiseRepairServiceImpl implements IAppraiseRepairService {

    @Autowired
    private ISaveAppraiseService saveAppraiseServiceImpl;
    @Autowired
    private IRepairServiceDao repairServiceDaoImpl;

    @Override
    @Java110Transactional
    public AppraiseDto appraiseRepair(@RequestBody AppraiseDto appraiseDto) {

        //将工单表的状态修改为完成
        Map info = new HashMap();
        info.put("repairId", appraiseDto.getObjId());
        info.put("statusCd", "0");
        info.put("state", RepairDto.STATE_RETURN_VISIT);
        repairServiceDaoImpl.updateRepairInfoInstance(info);

        //评价表中加入评价内容
        appraiseDto.setParentAppraiseId("-1");
        appraiseDto.setObjType(AppraiseDto.OBJ_TYPE_REPAIR);
        appraiseDto.setAppraiseType(AppraiseDto.APPRAISE_TYPE_PUBLIC);
        return saveAppraiseServiceImpl.saveAppraise(appraiseDto);

    }
}
