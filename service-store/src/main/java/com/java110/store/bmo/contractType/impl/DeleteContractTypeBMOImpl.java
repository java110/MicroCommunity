package com.java110.store.bmo.contractType.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.store.IContractTypeInnerServiceSMO;
import com.java110.po.contractType.ContractTypePo;
import com.java110.store.bmo.contractType.IDeleteContractTypeBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteContractTypeBMOImpl")
public class DeleteContractTypeBMOImpl implements IDeleteContractTypeBMO {

    @Autowired
    private IContractTypeInnerServiceSMO contractTypeInnerServiceSMOImpl;

    /**
     * @param contractTypePo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(ContractTypePo contractTypePo) {

        int flag = contractTypeInnerServiceSMOImpl.deleteContractType(contractTypePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
