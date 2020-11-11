package com.java110.store.bmo.contractTypeTemplate.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.store.IContractTypeTemplateInnerServiceSMO;
import com.java110.po.contractTypeTemplate.ContractTypeTemplatePo;
import com.java110.store.bmo.contractTypeTemplate.IDeleteContractTypeTemplateBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteContractTypeTemplateBMOImpl")
public class DeleteContractTypeTemplateBMOImpl implements IDeleteContractTypeTemplateBMO {

    @Autowired
    private IContractTypeTemplateInnerServiceSMO contractTypeTemplateInnerServiceSMOImpl;

    /**
     * @param contractTypeTemplatePo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(ContractTypeTemplatePo contractTypeTemplatePo) {

        int flag = contractTypeTemplateInnerServiceSMOImpl.deleteContractTypeTemplate(contractTypeTemplatePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
