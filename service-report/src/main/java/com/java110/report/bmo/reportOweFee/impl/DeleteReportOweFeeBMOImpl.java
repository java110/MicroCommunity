package com.java110.report.bmo.reportOweFee.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.report.IReportOweFeeInnerServiceSMO;
import com.java110.po.reportOweFee.ReportOweFeePo;
import com.java110.report.bmo.reportOweFee.IDeleteReportOweFeeBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteReportOweFeeBMOImpl")
public class DeleteReportOweFeeBMOImpl implements IDeleteReportOweFeeBMO {

    @Autowired
    private IReportOweFeeInnerServiceSMO reportOweFeeInnerServiceSMOImpl;

    /**
     * @param reportOweFeePo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(ReportOweFeePo reportOweFeePo) {

        int flag = reportOweFeeInnerServiceSMOImpl.deleteReportOweFee(reportOweFeePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
