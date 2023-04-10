package com.java110.fee.cmd.feeConfig;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.PageDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.api.feeConfig.ApiFeeConfigDataVo;
import com.java110.vo.api.feeConfig.ApiFeeConfigVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "feeConfig.listFeeConfigs")
public class ListFeeConfigsCmd extends Cmd {

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        FeeConfigDto feeConfigDto = BeanConvertUtil.covertBean(reqJson, FeeConfigDto.class);

        if ("0".equals(reqJson.getString("isFlag"))) {
            feeConfigDto.setPage(PageDto.DEFAULT_PAGE);
        }

        int count = feeConfigInnerServiceSMOImpl.queryFeeConfigsCount(feeConfigDto);

        List<ApiFeeConfigDataVo> feeConfigs = null;

        if (count > 0) {
            feeConfigs = BeanConvertUtil.covertBeanList(feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto), ApiFeeConfigDataVo.class);
            //处理 小数点后无效的0
            for (ApiFeeConfigDataVo feeConfig : feeConfigs) {
                if (!StringUtil.isEmpty(feeConfig.getAdditionalAmount())) {
                    feeConfig.setAdditionalAmount(Double.parseDouble(feeConfig.getAdditionalAmount()) + "");
                }

                if (!StringUtil.isEmpty(feeConfig.getSquarePrice())) {
                    feeConfig.setSquarePrice(Double.parseDouble(feeConfig.getSquarePrice()) + "");
                }
            }
        } else {
            feeConfigs = new ArrayList<>();
        }

        ApiFeeConfigVo apiFeeConfigVo = new ApiFeeConfigVo();

        apiFeeConfigVo.setTotal(count);
        apiFeeConfigVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiFeeConfigVo.setFeeConfigs(feeConfigs);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiFeeConfigVo), HttpStatus.OK);

        cmdDataFlowContext.setResponseEntity(responseEntity);
    }
}
