package com.java110.job.importData.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.importData.ImportMeterMachineDto;
import com.java110.dto.importData.ImportOwnerRoomDto;
import com.java110.dto.log.AssetImportLogDetailDto;
import com.java110.job.importData.DefaultImportData;
import com.java110.job.importData.IImportDataAdapt;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("importMeterMachineQueueData")
public class ImportMeterMachineQueueDataAdapt extends DefaultImportData implements IImportDataAdapt {
    @Override
    public void importData(List<AssetImportLogDetailDto> assetImportLogDetailDtos) {
        for (AssetImportLogDetailDto assetImportLogDetailDto : assetImportLogDetailDtos) {

            try {
                doImportData(assetImportLogDetailDto);
                updateImportLogDetailState(assetImportLogDetailDto.getDetailId());
            } catch (Exception e) {
                e.printStackTrace();
                updateImportLogDetailState(assetImportLogDetailDto.getDetailId(), e);
            }
        }
    }

    /**
     * 导入数据
     *
     * @param assetImportLogDetailDto
     */
    private void doImportData(AssetImportLogDetailDto assetImportLogDetailDto) {
        JSONObject data = JSONObject.parseObject(assetImportLogDetailDto.getContent());
        ImportMeterMachineDto importOwnerRoomDto = BeanConvertUtil.covertBean(data, ImportMeterMachineDto.class);
//        int flag = importOwnerRoomInnerServiceSMOImpl.saveOwnerRoom(importOwnerRoomDto);
//        if (flag < 1) {
//            throw new IllegalArgumentException("导入失败");
//        }

    }
}
