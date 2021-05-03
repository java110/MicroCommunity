package com.java110.acct.bmo.systemGoldSetting.impl;

import com.java110.acct.bmo.systemGoldSetting.ISaveSystemGoldSettingBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.acct.ISystemGoldSettingInnerServiceSMO;
import com.java110.po.systemGoldSetting.SystemGoldSettingPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveSystemGoldSettingBMOImpl")
public class SaveSystemGoldSettingBMOImpl implements ISaveSystemGoldSettingBMO {

    @Autowired
    private ISystemGoldSettingInnerServiceSMO systemGoldSettingInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param systemGoldSettingPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(SystemGoldSettingPo systemGoldSettingPo) {

        systemGoldSettingPo.setSettingId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_settingId));
        int flag = systemGoldSettingInnerServiceSMOImpl.saveSystemGoldSetting(systemGoldSettingPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
