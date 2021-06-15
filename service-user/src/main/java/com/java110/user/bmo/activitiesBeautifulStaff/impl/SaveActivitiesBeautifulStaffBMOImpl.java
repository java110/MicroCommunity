package com.java110.user.bmo.activitiesBeautifulStaff.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.user.IActivitiesBeautifulStaffInnerServiceSMO;
import com.java110.po.activitiesBeautifulStaff.ActivitiesBeautifulStaffPo;
import com.java110.user.bmo.activitiesBeautifulStaff.ISaveActivitiesBeautifulStaffBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveActivitiesBeautifulStaffBMOImpl")
public class SaveActivitiesBeautifulStaffBMOImpl implements ISaveActivitiesBeautifulStaffBMO {

    @Autowired
    private IActivitiesBeautifulStaffInnerServiceSMO activitiesBeautifulStaffInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param activitiesBeautifulStaffPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(ActivitiesBeautifulStaffPo activitiesBeautifulStaffPo) {

        activitiesBeautifulStaffPo.setBeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_beId));
        activitiesBeautifulStaffPo.setPoll("0");
        int flag = activitiesBeautifulStaffInnerServiceSMOImpl.saveActivitiesBeautifulStaff(activitiesBeautifulStaffPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
