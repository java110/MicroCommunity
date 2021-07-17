package com.java110.api.listener.resourceStore;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.PageDto;
import com.java110.dto.allocationStorehouse.AllocationStorehouseDto;
import com.java110.dto.allocationUserStorehouse.AllocationUserStorehouseDto;
import com.java110.intf.store.IAllocationUserStorehouseInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeAllocationUserStorehouseConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询小区侦听类
 */
@Java110Listener("listAllocationUserStorehousesListener")
public class ListAllocationUserStorehousesListener extends AbstractServiceApiListener {

    @Autowired
    private IAllocationUserStorehouseInnerServiceSMO allocationUserStorehouseInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeAllocationUserStorehouseConstant.LIST_ALLOCATIONUSERSTOREHOUSES;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IAllocationUserStorehouseInnerServiceSMO getAllocationUserStorehouseInnerServiceSMOImpl() {
        return allocationUserStorehouseInnerServiceSMOImpl;
    }

    public void setAllocationUserStorehouseInnerServiceSMOImpl(IAllocationUserStorehouseInnerServiceSMO allocationUserStorehouseInnerServiceSMOImpl) {
        this.allocationUserStorehouseInnerServiceSMOImpl = allocationUserStorehouseInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        AllocationUserStorehouseDto allocationUserStorehouseDto = BeanConvertUtil.covertBean(reqJson, AllocationUserStorehouseDto.class);

        int count = allocationUserStorehouseInnerServiceSMOImpl.queryAllocationUserStorehousesCount(allocationUserStorehouseDto);

        List<AllocationUserStorehouseDto> allocationUserStorehouseDtos = new ArrayList<>();

        if (count > 0) {
            List<AllocationUserStorehouseDto> allocationUserStorehouseList = allocationUserStorehouseInnerServiceSMOImpl.queryAllocationUserStorehouses(allocationUserStorehouseDto);
            //转赠总数量(小计)
            BigDecimal subTotalQuantity = BigDecimal.ZERO;
            //转赠总数量(小计)
            BigDecimal totalQuantity = BigDecimal.ZERO;
            for (AllocationUserStorehouseDto allocationUserStorehouse : allocationUserStorehouseList) {
                //获取转赠数量
                String giveQuantity = allocationUserStorehouse.getGiveQuantity();
                BigDecimal quantity = new BigDecimal(giveQuantity);
                //计算转赠总数量(小计)
                subTotalQuantity = subTotalQuantity.add(quantity);
            }
            //查询所有转赠记录
            allocationUserStorehouseDto.setPage(PageDto.DEFAULT_PAGE);
            List<AllocationUserStorehouseDto> allocationUserStorehouses = allocationUserStorehouseInnerServiceSMOImpl.queryAllocationUserStorehouses(allocationUserStorehouseDto);
            for (AllocationUserStorehouseDto allocationUserStorehouse : allocationUserStorehouses) {
                //获取转赠数量
                String giveQuantity = allocationUserStorehouse.getGiveQuantity();
                BigDecimal quantity = new BigDecimal(giveQuantity);
                //计算转赠总数量(大计)
                totalQuantity = totalQuantity.add(quantity);
            }
            for (AllocationUserStorehouseDto allocationUserStorehouse : allocationUserStorehouseList) {
                allocationUserStorehouse.setSubTotalQuantity(subTotalQuantity.toString());
                allocationUserStorehouse.setHighTotalQuantity(totalQuantity.toString());
                allocationUserStorehouseDtos.add(allocationUserStorehouse);
            }
        } else {
            allocationUserStorehouseDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, allocationUserStorehouseDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
