package com.java110.goods.bmo.storeOrderAddress.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.goods.bmo.storeOrderAddress.ISaveStoreOrderAddressBMO;
import com.java110.intf.goods.IStoreOrderAddressInnerServiceSMO;
import com.java110.po.storeOrderAddress.StoreOrderAddressPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveStoreOrderAddressBMOImpl")
public class SaveStoreOrderAddressBMOImpl implements ISaveStoreOrderAddressBMO {

    @Autowired
    private IStoreOrderAddressInnerServiceSMO storeOrderAddressInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param storeOrderAddressPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(StoreOrderAddressPo storeOrderAddressPo) {

        storeOrderAddressPo.setOaId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_oaId));
        int flag = storeOrderAddressInnerServiceSMOImpl.saveStoreOrderAddress(storeOrderAddressPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
