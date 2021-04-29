package com.java110.fee.api;

import com.java110.dto.fee.FeeDto;
import com.java110.fee.bmo.rentingFee.IQueryRentingFee;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rentingFee")
public class RentingFeeApi {

    @Autowired
    private IQueryRentingFee queryRentingFeeImpl;


    /**
     * 查询租赁费
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /rentingFee/queryFee
     * @path /app/rentingFee/queryFee
     */
    @RequestMapping(value = "/queryFee", method = RequestMethod.GET)
    public ResponseEntity<String> queryFee(@RequestParam(value = "communityId") String communityId,
                                           @RequestParam(value = "rentingId", required = false) String rentingId,
                                           @RequestParam(value = "page") int page,
                                           @RequestParam(value = "row") int row) {
        FeeDto feeDto = new FeeDto();
        feeDto.setPage(page);
        feeDto.setRow(row);
        feeDto.setCommunityId(communityId);
        feeDto.setPayerObjId(rentingId);
        feeDto.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_RENTING);
        return queryRentingFeeImpl.queryFees(feeDto);
    }


}
