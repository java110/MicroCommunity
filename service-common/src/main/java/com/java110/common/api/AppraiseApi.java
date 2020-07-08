package com.java110.common.api;

import com.java110.common.service.appraise.ISaveAppraiseService;
import com.java110.dto.appraise.AppraiseDto;
import com.java110.intf.common.appraise.IAppraiseApi;
import com.java110.intf.community.repair.IRepairApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppraiseApi implements IAppraiseApi {
    @Autowired
    private ISaveAppraiseService saveAppraiseServiceImpl;

    @Override
    public AppraiseDto saveAppraise(@RequestBody AppraiseDto appraiseDto) {
        return saveAppraiseServiceImpl.saveAppraise(appraiseDto);
    }
}
