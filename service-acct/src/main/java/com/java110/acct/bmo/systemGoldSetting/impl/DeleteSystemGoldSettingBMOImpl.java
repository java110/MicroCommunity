package com.java110.acct.bmo.systemGoldSetting.impl;

import com.java110.acct.bmo.systemGoldSetting.IDeleteSystemGoldSettingBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.acct.ISystemGoldSettingInnerServiceSMO;
import com.java110.po.systemGoldSetting.SystemGoldSettingPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteSystemGoldSettingBMOImpl")
public class DeleteSystemGoldSettingBMOImpl implements IDeleteSystemGoldSettingBMO {

    @Autowired
    private ISystemGoldSettingInnerServiceSMO systemGoldSettingInnerServiceSMOImpl;

    /**
     * @param systemGoldSettingPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(SystemGoldSettingPo systemGoldSettingPo) {

        int flag = systemGoldSettingInnerServiceSMOImpl.deleteSystemGoldSetting(systemGoldSettingPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
