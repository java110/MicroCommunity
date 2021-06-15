package com.java110.store.bmo.contractTypeSpec.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.store.IContractTypeSpecInnerServiceSMO;
import com.java110.po.contractTypeSpec.ContractTypeSpecPo;
import com.java110.store.bmo.contractTypeSpec.IDeleteContractTypeSpecBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteContractTypeSpecBMOImpl")
public class DeleteContractTypeSpecBMOImpl implements IDeleteContractTypeSpecBMO {

    @Autowired
    private IContractTypeSpecInnerServiceSMO contractTypeSpecInnerServiceSMOImpl;

    /**
     * @param contractTypeSpecPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(ContractTypeSpecPo contractTypeSpecPo) {

        int flag = contractTypeSpecInnerServiceSMOImpl.deleteContractTypeSpec(contractTypeSpecPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
