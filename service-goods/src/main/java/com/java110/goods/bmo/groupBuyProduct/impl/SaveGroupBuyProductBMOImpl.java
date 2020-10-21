package com.java110.goods.bmo.groupBuyProduct.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.groupBuyBatch.GroupBuyBatchDto;
import com.java110.goods.bmo.groupBuyProduct.ISaveGroupBuyProductBMO;
import com.java110.intf.IGroupBuyBatchInnerServiceSMO;
import com.java110.intf.IGroupBuyProductInnerServiceSMO;
import com.java110.intf.IGroupBuyProductSpecInnerServiceSMO;
import com.java110.po.groupBuyProduct.GroupBuyProductPo;
import com.java110.po.groupBuyProductSpec.GroupBuyProductSpecPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("saveGroupBuyProductBMOImpl")
public class SaveGroupBuyProductBMOImpl implements ISaveGroupBuyProductBMO {

    @Autowired
    private IGroupBuyProductInnerServiceSMO groupBuyProductInnerServiceSMOImpl;

    @Autowired
    private IGroupBuyProductSpecInnerServiceSMO groupBuyProductSpecInnerServiceSMOImpl;

    @Autowired
    private IGroupBuyBatchInnerServiceSMO groupBuyBatchInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param groupBuyProductPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(GroupBuyProductPo groupBuyProductPo,
                                       List<GroupBuyProductSpecPo> groupBuyProductSpecPos) {
        GroupBuyBatchDto groupBuyBatchDto = new GroupBuyBatchDto();
        groupBuyBatchDto.setCurBatch("Y");
        groupBuyBatchDto.setStoreId(groupBuyProductPo.getStoreId());
        List<GroupBuyBatchDto> groupBuyBatchDtos = groupBuyBatchInnerServiceSMOImpl.queryGroupBuyBatchs(groupBuyBatchDto);

        if (groupBuyBatchDtos.size() < 1) {
            throw new IllegalArgumentException("未找到批次信息");
        }

        groupBuyProductPo.setBatchId(groupBuyBatchDtos.get(0).getBatchId());

        groupBuyProductPo.setGroupId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_groupId));
        int flag = groupBuyProductInnerServiceSMOImpl.saveGroupBuyProduct(groupBuyProductPo);

        if (flag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
        }

        for (GroupBuyProductSpecPo groupBuyProductSpecPo : groupBuyProductSpecPos) {
            flag = groupBuyProductSpecInnerServiceSMOImpl.saveGroupBuyProductSpec(groupBuyProductSpecPo);
            if (flag < 1) {
                return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
            }
        }
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");

    }

}
