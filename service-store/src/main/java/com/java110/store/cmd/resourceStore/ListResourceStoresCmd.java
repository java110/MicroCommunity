package com.java110.store.cmd.resourceStore;

import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.Api;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.PageDto;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.dto.resourceStore.ResourceStoreDto;
import com.java110.dto.resourceStoreTimes.ResourceStoreTimesDto;
import com.java110.dto.storehouse.StorehouseDto;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.store.IResourceStoreInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.resourceStore.ApiResourceStoreDataVo;
import com.java110.vo.api.resourceStore.ApiResourceStoreVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 查询物品
 * add by wuxw 2023-01-19
 */
@Java110Cmd(serviceCode = "resourceStore.listResourceStores")
public class ListResourceStoresCmd extends Cmd {

    @Autowired
    private IResourceStoreInnerServiceSMO resourceStoreInnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区ID");
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含商户ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        ResourceStoreDto resourceStoreDto = BeanConvertUtil.covertBean(reqJson, ResourceStoreDto.class);
        //采购2806集团仓库 物品领用2807小区仓库  默认查询当前小区所有商品
        //是否具有查看集团仓库物品权限
        String userId = reqJson.getString("userId");
        BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
        basePrivilegeDto.setResource("/viewGroupResource");
        basePrivilegeDto.setUserId(userId);
        List<Map> privileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);
        if ("1000".equals(reqJson.getString("operationType")) && privileges.size() > 0) {
            resourceStoreDto.setShType("");
            resourceStoreDto.setShObjIds(new String[]{reqJson.getString("communityId"), reqJson.getString("storeId")});
        } else if (StorehouseDto.SH_TYPE_COMMUNITY.equals(resourceStoreDto.getShType()) || privileges.size() == 0) {
            //add by wuxw 这里 修改为 小区仓库也能采购 模式 所以这里不限制
            //resourceStoreDto.setShType(StorehouseDto.SH_TYPE_COMMUNITY);
            resourceStoreDto.setShObjId(reqJson.getString("communityId"));
        }
        BasePrivilegeDto basePrivilegeDto1 = new BasePrivilegeDto();
        basePrivilegeDto1.setResource("/viewHiddenWarehouse");
        basePrivilegeDto1.setUserId(userId);
        List<Map> viewHiddenWarehousePrivileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto1);
        if (viewHiddenWarehousePrivileges.size() == 0) {
            resourceStoreDto.setIsShow("true");
        }
        int count = resourceStoreInnerServiceSMOImpl.queryResourceStoresCount(resourceStoreDto);
        List<ApiResourceStoreDataVo> resourceStores = new ArrayList<>();
        //计算总价(小计)
        BigDecimal subTotalPrice = BigDecimal.ZERO;
        //计算总价(大计)
        BigDecimal totalPrice = BigDecimal.ZERO;
        if (count > 0) {
            resourceStores = BeanConvertUtil.covertBeanList(resourceStoreInnerServiceSMOImpl.queryResourceStores(resourceStoreDto), ApiResourceStoreDataVo.class);
            //todo 计算物品 库存和 价格
            queryResourceStoreAndResourceTotalPrice(resourceStores);
            resourceStoreDto.setPage(Integer.valueOf(reqJson.getString("page")));
            subTotalPrice = new BigDecimal(resourceStoreInnerServiceSMOImpl.queryResourceStoresTotalPrice(resourceStoreDto));
            resourceStoreDto.setPage(PageDto.DEFAULT_PAGE);
            totalPrice = new BigDecimal(resourceStoreInnerServiceSMOImpl.queryResourceStoresTotalPrice(resourceStoreDto));
        } else {
            resourceStores = new ArrayList<>();
        }
        ApiResourceStoreVo apiResourceStoreVo = new ApiResourceStoreVo();
        apiResourceStoreVo.setTotal(count);
        apiResourceStoreVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiResourceStoreVo.setResourceStores(resourceStores);
        apiResourceStoreVo.setTotalPrice(String.format("%.2f", totalPrice));
        apiResourceStoreVo.setSubTotal(String.format("%.2f", subTotalPrice));
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiResourceStoreVo), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }

    /**
     * 计算物品的数量和金额
     *
     * @param resourceStores
     */
    private void queryResourceStoreAndResourceTotalPrice(List<ApiResourceStoreDataVo> resourceStores) {

        if (resourceStores == null || resourceStores.size() < 1) {
            return;
        }


        BigDecimal stock = new BigDecimal(0.0);
        BigDecimal totalPrice = new BigDecimal(0.0);
        for (ApiResourceStoreDataVo resourceStore : resourceStores) {
            List<ResourceStoreTimesDto> resourceStoreTimesDtos = resourceStore.getTimes();
            if (resourceStoreTimesDtos == null || resourceStoreTimesDtos.size() < 1) {
                continue;
            }

            for (ResourceStoreTimesDto resourceStoreTimesDto : resourceStoreTimesDtos) {
                stock = stock.add(new BigDecimal(resourceStoreTimesDto.getStock()));
                totalPrice = totalPrice.add(new BigDecimal(resourceStoreTimesDto.getTotalPrice()));
            }

            resourceStore.setStock(stock.doubleValue() + "");
            resourceStore.setTotalPrice(totalPrice.doubleValue() + "");
        }

    }
}
