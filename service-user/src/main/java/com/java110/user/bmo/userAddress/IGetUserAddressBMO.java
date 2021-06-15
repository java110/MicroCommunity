package com.java110.user.bmo.userAddress;
import com.java110.dto.userAddress.UserAddressDto;
import org.springframework.http.ResponseEntity;
public interface IGetUserAddressBMO {


    /**
     * 查询用户联系地址
     * add by wuxw
     * @param  userAddressDto
     * @return
     */
    ResponseEntity<String> get(UserAddressDto userAddressDto);


}
