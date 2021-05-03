package com.java110.acct.bmo.systemGoldSetting.impl;

import com.java110.acct.bmo.systemGoldSetting.IUpdateSystemGoldSettingBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.acct.ISystemGoldSettingInnerServiceSMO;
import com.java110.po.systemGoldSetting.SystemGoldSettingPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateSystemGoldSettingBMOImpl")
public class UpdateSystemGoldSettingBMOImpl implements IUpdateSystemGoldSettingBMO {

    @Autowired
    private ISystemGoldSettingInnerServiceSMO systemGoldSettingInnerServiceSMOImpl;

    /**
     * @param systemGoldSettingPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(SystemGoldSettingPo systemGoldSettingPo) {

        int flag = systemGoldSettingInnerServiceSMOImpl.updateSystemGoldSetting(systemGoldSettingPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
