package com.java110.community.bmo.applyRoomDiscountRecord.impl;

import com.java110.community.bmo.applyRoomDiscountRecord.IGetApplyRoomDiscountRecordBMO;
import com.java110.dto.applyRoomDiscount.ApplyRoomDiscountRecordDto;
import com.java110.intf.community.IApplyRoomDiscountRecordInnerServiceSMO;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询装修记录信息
 *
 * @author fqz
 * @date 2021-08-31 17:44
 */
@Service("getApplyRoomDiscountRecordBMOImpl")
public class GetApplyRoomDiscountRecordBMOImpl implements IGetApplyRoomDiscountRecordBMO {

    @Autowired
    private IApplyRoomDiscountRecordInnerServiceSMO applyRoomDiscountRecordInnerServiceSMOImpl;

    @Override
    public ResponseEntity<String> getRecord(ApplyRoomDiscountRecordDto applyRoomDiscountRecordDto) {
        int count = applyRoomDiscountRecordInnerServiceSMOImpl.queryApplyRoomDiscountRecordsCount(applyRoomDiscountRecordDto);
        List<ApplyRoomDiscountRecordDto> applyRoomDiscountRecordDtos = new ArrayList<>();
        if (count > 0) {
            List<ApplyRoomDiscountRecordDto> applyRoomDiscountRecordDtoList = applyRoomDiscountRecordInnerServiceSMOImpl.queryApplyRoomDiscountRecords(applyRoomDiscountRecordDto);
            for (ApplyRoomDiscountRecordDto applyRoomDiscountRecord : applyRoomDiscountRecordDtoList) {
                if (!StringUtil.isEmpty(applyRoomDiscountRecord.getFileRealName()) && applyRoomDiscountRecord.getRelTypeCd().equals("19000")) {
                    applyRoomDiscountRecord.setUrl("/callComponent/download/getFile/file?fileId=" +
                            applyRoomDiscountRecord.getFileRealName() + "&communityId=-1");
                } else if (!StringUtil.isEmpty(applyRoomDiscountRecord.getFileRealName()) && applyRoomDiscountRecord.getRelTypeCd().equals("21000")) {
                    applyRoomDiscountRecord.setUrl("/video/" + applyRoomDiscountRecord.getFileRealName());
                }
                applyRoomDiscountRecordDtos.add(applyRoomDiscountRecord);
            }
        } else {
            applyRoomDiscountRecordDtos = new ArrayList<>();
        }
        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) applyRoomDiscountRecordDto.getRow()), count, applyRoomDiscountRecordDtos);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> get(ApplyRoomDiscountRecordDto applyRoomDiscountRecordDto) {
        int count = applyRoomDiscountRecordInnerServiceSMOImpl.selectApplyRoomDiscountRecordsCount(applyRoomDiscountRecordDto);
        List<ApplyRoomDiscountRecordDto> applyRoomDiscountRecordDtos = null;
        if (count > 0) {
            applyRoomDiscountRecordDtos = applyRoomDiscountRecordInnerServiceSMOImpl.selectApplyRoomDiscountRecords(applyRoomDiscountRecordDto);
        } else {
            applyRoomDiscountRecordDtos = new ArrayList<>();
        }
        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) applyRoomDiscountRecordDto.getRow()), count, applyRoomDiscountRecordDtos);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        return responseEntity;
    }

}
