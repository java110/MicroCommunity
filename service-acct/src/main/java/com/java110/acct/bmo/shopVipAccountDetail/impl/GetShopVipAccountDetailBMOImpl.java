package com.java110.acct.bmo.shopVipAccountDetail.impl;

import com.java110.acct.bmo.shopVipAccountDetail.IGetShopVipAccountDetailBMO;
import com.java110.dto.shopVipAccount.ShopUserAccountVipDto;
import com.java110.dto.shopVipAccount.ShopVipAccountDetailDto;
import com.java110.dto.shopVipAccount.ShopVipAccountDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.acct.IShopVipAccountDetailInnerServiceSMO;
import com.java110.intf.acct.IShopVipAccountInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getShopVipAccountDetailBMOImpl")
public class GetShopVipAccountDetailBMOImpl implements IGetShopVipAccountDetailBMO {

    @Autowired
    private IShopVipAccountDetailInnerServiceSMO shopVipAccountDetailInnerServiceSMOImpl;



    /**
     * @param shopVipAccountDetailDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ShopVipAccountDetailDto shopVipAccountDetailDto) {


        int count = shopVipAccountDetailInnerServiceSMOImpl.queryShopVipAccountDetailsCount(shopVipAccountDetailDto);

        List<ShopVipAccountDetailDto> shopVipAccountDetailDtos = null;
        if (count > 0) {
            shopVipAccountDetailDtos = shopVipAccountDetailInnerServiceSMOImpl.queryShopVipAccountDetails(shopVipAccountDetailDto);
        } else {
            shopVipAccountDetailDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) shopVipAccountDetailDto.getRow()), count, shopVipAccountDetailDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }


}
