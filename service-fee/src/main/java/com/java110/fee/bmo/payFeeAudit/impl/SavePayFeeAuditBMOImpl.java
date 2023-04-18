package com.java110.fee.bmo.payFeeAudit.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.fee.bmo.payFeeAudit.ISavePayFeeAuditBMO;
import com.java110.intf.fee.IFeeDetailInnerServiceSMO;
import com.java110.intf.fee.IPayFeeAuditInnerServiceSMO;
import com.java110.intf.fee.IPayFeeDetailV1InnerServiceSMO;
import com.java110.po.payFeeAudit.PayFeeAuditPo;
import com.java110.utils.exception.CmdException;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("savePayFeeAuditBMOImpl")
public class SavePayFeeAuditBMOImpl implements ISavePayFeeAuditBMO {

    @Autowired
    private IPayFeeAuditInnerServiceSMO payFeeAuditInnerServiceSMOImpl;

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param payFeeAuditPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(PayFeeAuditPo payFeeAuditPo) {

        String feeDetailId = payFeeAuditPo.getFeeDetailId();
        String[] feeDetailIds = feeDetailId.split(",");

        for (String tmpFeeDetailId : feeDetailIds) {

            FeeDetailDto feeDetailDto = new FeeDetailDto();
            feeDetailDto.setDetailId(tmpFeeDetailId);
            feeDetailDto.setCommunityId(payFeeAuditPo.getCommunityId());
            List<FeeDetailDto> feeDetailDtos = feeDetailInnerServiceSMOImpl.queryFeeDetails(feeDetailDto);

            if(feeDetailDtos == null || feeDetailDtos.size()<1){
                continue;
            }
            payFeeAuditPo.setFeeId(feeDetailDtos.get(0).getFeeId());
            payFeeAuditPo.setFeeDetailId(feeDetailDtos.get(0).getDetailId());
            payFeeAuditPo.setAuditId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_auditId));
            int flag = payFeeAuditInnerServiceSMOImpl.savePayFeeAudit(payFeeAuditPo);

            if (flag < 1) {
                throw new CmdException("审核保存失败");
            }
        }
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
    }

}
