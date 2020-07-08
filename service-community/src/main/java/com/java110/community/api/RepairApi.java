package com.java110.community.api;

import com.java110.community.service.repair.IAppraiseRepairService;
import com.java110.dto.appraise.AppraiseDto;
import com.java110.intf.community.repair.IRepairApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 报修 控制类
 */
@RestController
public class RepairApi implements IRepairApi {
    @Autowired
    private IAppraiseRepairService appraiseRepairServiceImpl;

    /**
     * 对外 报修评价
     * @param appraiseDto
     * @return
     */
    @Override
    public AppraiseDto appraiseRepair(@RequestBody AppraiseDto appraiseDto) {
        return appraiseRepairServiceImpl.appraiseRepair(appraiseDto);
    }
}
