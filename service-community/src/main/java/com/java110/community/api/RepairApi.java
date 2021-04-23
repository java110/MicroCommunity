package com.java110.community.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.bmo.repair.IAppraiseRepairBMO;
import com.java110.dto.appraise.AppraiseDto;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 报修 控制类
 */
@RestController
@RequestMapping("/repair")
public class RepairApi {
    @Autowired
    private IAppraiseRepairBMO appraiseRepairBMOImpl;

    /**
     * 报修评价
     *
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/appraiseRepair", method = RequestMethod.POST)
    public ResponseEntity<String> appraiseRepair(@RequestBody JSONObject reqJson) {
        AppraiseDto appraiseDto = BeanConvertUtil.covertBean(reqJson,AppraiseDto.class);
        return appraiseRepairBMOImpl.appraiseRepair(appraiseDto);
    }
}
