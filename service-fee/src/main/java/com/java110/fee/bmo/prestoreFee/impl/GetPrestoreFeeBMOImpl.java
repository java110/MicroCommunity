package com.java110.fee.bmo.prestoreFee.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.fee.bmo.prestoreFee.IGetPrestoreFeeBMO;
import com.java110.intf.fee.IPrestoreFeeInnerServiceSMO;
import com.java110.intf.store.IWechatSmsTemplateInnerServiceSMO;
import com.java110.po.wechatSmsTemplate.WechatSmsTemplatePo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.java110.core.context.DataFlowContext;
import com.java110.dto.prestoreFee.PrestoreFeeDto;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getPrestoreFeeBMOImpl")
public class GetPrestoreFeeBMOImpl implements IGetPrestoreFeeBMO {

    @Autowired
    private IPrestoreFeeInnerServiceSMO prestoreFeeInnerServiceSMOImpl;

    /**
     *
     *
     * @param  prestoreFeeDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(PrestoreFeeDto prestoreFeeDto) {


        int count = prestoreFeeInnerServiceSMOImpl.queryPrestoreFeesCount(prestoreFeeDto);

        List<PrestoreFeeDto> prestoreFeeDtos = null;
        if (count > 0) {
            prestoreFeeDtos = prestoreFeeInnerServiceSMOImpl.queryPrestoreFees(prestoreFeeDto);
        } else {
            prestoreFeeDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) prestoreFeeDto.getRow()), count, prestoreFeeDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
