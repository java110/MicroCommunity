package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.room.ApplyRoomDiscountDto;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.fee.IApplyRoomDiscountInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "fee.queryAdminApplyRoomDiscount")
public class QueryAdminApplyRoomDiscountCmd extends Cmd {


    @Autowired
    private IApplyRoomDiscountInnerServiceSMO applyRoomDiscountInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        super.validateAdmin(context);
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        ApplyRoomDiscountDto applyRoomDiscountDto = BeanConvertUtil.covertBean(reqJson, ApplyRoomDiscountDto.class);
        int count = applyRoomDiscountInnerServiceSMOImpl.queryApplyRoomDiscountsCount(applyRoomDiscountDto);

        List<ApplyRoomDiscountDto> applyRoomDiscountDtos = new ArrayList<>();
        if (count > 0) {
            List<ApplyRoomDiscountDto> applyRoomDiscounts = applyRoomDiscountInnerServiceSMOImpl.queryApplyRoomDiscounts(applyRoomDiscountDto);
//            String imgUrl = MappingCache.getValue("IMG_PATH");
            for (ApplyRoomDiscountDto applyRoomDiscount : applyRoomDiscounts) {
                FileRelDto fileRelDto = new FileRelDto();
                fileRelDto.setObjId(applyRoomDiscount.getArdId());
                List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
                List<String> urls = new ArrayList<>();
                for (FileRelDto fileRel : fileRelDtos) {
                    if (!StringUtil.isEmpty(fileRel.getFileRealName()) && fileRel.getRelTypeCd().equals("19000")) {
                        urls.add(fileRel.getFileRealName());
                    } else if (!StringUtil.isEmpty(fileRel.getFileRealName()) && fileRel.getRelTypeCd().equals("21000")) {
                        urls.add(fileRel.getFileRealName());
                    }
                }
                applyRoomDiscount.setUrls(urls);
                applyRoomDiscountDtos.add(applyRoomDiscount);
            }
        } else {
            applyRoomDiscountDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) applyRoomDiscountDto.getRow()), count, applyRoomDiscountDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
