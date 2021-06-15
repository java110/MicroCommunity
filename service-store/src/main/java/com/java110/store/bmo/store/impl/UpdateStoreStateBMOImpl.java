package com.java110.store.bmo.store.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.store.IStoreInnerServiceSMO;
import com.java110.po.store.StorePo;
import com.java110.store.bmo.store.IUpdateStoreStateBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @ClassName GetStoreStaffBMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2021/1/1 17:41
 * @Version 1.0
 * add by wuxw 2021/1/1
 **/
@Service(value = "updateStoreStateBMOImpl")
public class UpdateStoreStateBMOImpl implements IUpdateStoreStateBMO {

    @Autowired
    private IStoreInnerServiceSMO storeInnerServiceSMOImpl;

    @Override
    @Java110Transactional
    public ResponseEntity<String> update(StorePo storePo) {
        int flag = storeInnerServiceSMOImpl.updateStore(storePo);
        if (flag > 0) {
            return ResultVo.success();
        }
        return ResultVo.error("修改状态失败");
    }
}
