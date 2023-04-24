package com.java110.fee.bmo.feeCollectionOrder.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeCollectionOrderDto;
import com.java110.dto.user.UserDto;
import com.java110.fee.bmo.feeCollectionOrder.ISaveFeeCollectionOrderBMO;
import com.java110.intf.fee.IFeeCollectionOrderInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.feeCollectionOrder.FeeCollectionOrderPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("saveFeeCollectionOrderBMOImpl")
public class SaveFeeCollectionOrderBMOImpl implements ISaveFeeCollectionOrderBMO {

    @Autowired
    private IFeeCollectionOrderInnerServiceSMO feeCollectionOrderInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param feeCollectionOrderPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(FeeCollectionOrderPo feeCollectionOrderPo) {

        //查询用户ID
        UserDto userDto = new UserDto();
        userDto.setUserId(feeCollectionOrderPo.getStaffId());
        List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);

        Assert.listOnlyOne(userDtos, "员工不存在");

        feeCollectionOrderPo.setOrderId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_orderId));
        feeCollectionOrderPo.setCollectionName(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B) + "催缴单");
        feeCollectionOrderPo.setState(FeeCollectionOrderDto.STATE_WAIT);
        feeCollectionOrderPo.setStaffName(userDtos.get(0).getName());
        int flag = feeCollectionOrderInnerServiceSMOImpl.saveFeeCollectionOrder(feeCollectionOrderPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
