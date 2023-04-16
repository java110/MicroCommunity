package com.java110.store.bmo.contractFile.impl;

import com.java110.dto.contract.ContractFileDto;
import com.java110.intf.store.IContractFileInnerServiceSMO;
import com.java110.store.bmo.contractFile.IGetContractFileBMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getContractFileBMOImpl")
public class GetContractFileBMOImpl implements IGetContractFileBMO {

    @Autowired
    private IContractFileInnerServiceSMO contractFileInnerServiceSMOImpl;

    /**
     *
     *
     * @param  contractFileDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ContractFileDto contractFileDto) {


        int count = contractFileInnerServiceSMOImpl.queryContractFilesCount(contractFileDto);

        List<ContractFileDto> contractFileDtos = null;
        if (count > 0) {
            contractFileDtos = contractFileInnerServiceSMOImpl.queryContractFiles(contractFileDto);
            freshUrl(contractFileDtos);
        } else {
            contractFileDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) contractFileDto.getRow()), count, contractFileDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    private void freshUrl(List<ContractFileDto> contractFileDtos) {
        String imgPath = MappingCache.getValue(MappingConstant.FILE_DOMAIN,"IMG_PATH");
        for(ContractFileDto contractFileDto : contractFileDtos){
            contractFileDto.setFileSaveName(imgPath+contractFileDto.getFileSaveName());
        }
    }

}
