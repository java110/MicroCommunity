package com.java110.store.cmd.resourceStore;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.dto.PageDto;
import com.java110.dto.privilege.BasePrivilegeDto;
import com.java110.dto.resource.ResourceStoreDto;
import com.java110.dto.resource.ResourceStoreTimesDto;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.store.IResourceStoreInnerServiceSMO;
import com.java110.intf.store.IResourceStoreTimesV1InnerServiceSMO;
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

@Java110CmdDoc(title = "查询物品",
        description = "外部系统通过此接口查询物品",
        httpMethod = "get",
        url = "http://{ip}:{port}/app/resourceStore.listResourceStores",
        resource = "storeDoc",
        author = "吴学文",
        serviceCode = "resourceStore.listResourceStores",
        seq = 8
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "page", length = 11, type = "int", remark = "页数"),
        @Java110ParamDoc(name = "row", length = 11, type = "int", remark = "行业数"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @Java110ParamDoc(name = "data", type = "Object", remark = "有效数据"),
                @Java110ParamDoc(parentNodeName = "data", name = "resCode", type = "String", remark = "物品编号"),
                @Java110ParamDoc(parentNodeName = "data", name = "resName", type = "String", remark = "物品名称"),
        }
)

@Java110ExampleDoc(
        reqBody = "http://localhost:3000/app/resourceStore.listResourceStores?resId=&resName=&resCode=&shId=&parentRstId=&rstId=&rssId=&isFixed=&page=1&row=10",
        resBody = "{\n" +
                "    \"page\": 0,\n" +
                "    \"records\": 1,\n" +
                "    \"resourceStores\": [\n" +
                "        {\n" +
                "            \"averagePrice\": \"0.00\",\n" +
                "            \"createTime\": 1692976986000,\n" +
                "            \"description\": \"\",\n" +
                "            \"isFixed\": \"N\",\n" +
                "            \"isFixedName\": \"否\",\n" +
                "            \"miniStock\": \"0\",\n" +
                "            \"miniUnitCode\": \"1001\",\n" +
                "            \"miniUnitCodeName\": \"个\",\n" +
                "            \"miniUnitStock\": \"1\",\n" +
                "            \"outHighPrice\": \"2.00\",\n" +
                "            \"outLowPrice\": \"1.00\",\n" +
                "            \"parentRstId\": \"282023082523150002\",\n" +
                "            \"parentRstName\": \"77\",\n" +
                "            \"price\": \"1.00\",\n" +
                "            \"remark\": \"\",\n" +
                "            \"resCode\": \"001\",\n" +
                "            \"resId\": \"852023082533060002\",\n" +
                "            \"resName\": \"钢笔\",\n" +
                "            \"rssId\": \"\",\n" +
                "            \"rstId\": \"282023082516650004\",\n" +
                "            \"rstName\": \"mm\",\n" +
                "            \"shId\": \"102023082412640003\",\n" +
                "            \"shName\": \"A仓库\",\n" +
                "            \"showMobile\": \"N\",\n" +
                "            \"stock\": \"0.0\",\n" +
                "            \"times\": [\n" +
                "                {\n" +
                "                    \"applyOrderId\": \"-1\",\n" +
                "                    \"averagePrice\": \"0.00\",\n" +
                "                    \"createTime\": 1692976987000,\n" +
                "                    \"description\": \"\",\n" +
                "                    \"isFixed\": \"N\",\n" +
                "                    \"isFixedName\": \"否\",\n" +
                "                    \"miniStock\": \"0\",\n" +
                "                    \"miniUnitCode\": \"1001\",\n" +
                "                    \"miniUnitCodeName\": \"个\",\n" +
                "                    \"miniUnitStock\": \"1\",\n" +
                "                    \"outHighPrice\": \"2.00\",\n" +
                "                    \"outLowPrice\": \"1.00\",\n" +
                "                    \"page\": -1,\n" +
                "                    \"parentRstId\": \"282023082523150002\",\n" +
                "                    \"parentRstName\": \"77\",\n" +
                "                    \"price\": \"1.00\",\n" +
                "                    \"records\": 0,\n" +
                "                    \"remark\": \"\",\n" +
                "                    \"resCode\": \"001\",\n" +
                "                    \"resId\": \"852023082533060002\",\n" +
                "                    \"resName\": \"钢笔\",\n" +
                "                    \"row\": 0,\n" +
                "                    \"rssId\": \"\",\n" +
                "                    \"rstId\": \"282023082516650004\",\n" +
                "                    \"rstName\": \"mm\",\n" +
                "                    \"shId\": \"102023082412640003\",\n" +
                "                    \"shName\": \"A仓库\",\n" +
                "                    \"showMobile\": \"N\",\n" +
                "                    \"statusCd\": \"0\",\n" +
                "                    \"stock\": \"0\",\n" +
                "                    \"storeId\": \"10202305221014329108000168\",\n" +
                "                    \"timesId\": \"112023082573090004\",\n" +
                "                    \"total\": 0,\n" +
                "                    \"totalPrice\": \"0.0\",\n" +
                "                    \"unitCode\": \"1002\",\n" +
                "                    \"unitCodeName\": \"次\",\n" +
                "                    \"warningStock\": \"10\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"totalPrice\": \"0.0\",\n" +
                "            \"unitCode\": \"1002\",\n" +
                "            \"unitCodeName\": \"次\",\n" +
                "            \"warningStock\": \"10\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"rows\": 0,\n" +
                "    \"subTotal\": \"0.00\",\n" +
                "    \"total\": 1,\n" +
                "    \"totalPrice\": \"0.00\"\n" +
                "}"
)
@Java110Cmd(serviceCode = "resourceStore.listResourceStores")
public class ListResourceStoresCmd extends Cmd {

    @Autowired
    private IResourceStoreInnerServiceSMO resourceStoreInnerServiceSMOImpl;

    @Autowired
    private IResourceStoreTimesV1InnerServiceSMO resourceStoreTimesV1InnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含商户ID");
        /*if (!reqJson.containsKey("shId") || StringUtil.isEmpty(reqJson.getString("shId"))) {
            Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区ID");
        }*/
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String storeId = CmdContextUtils.getStoreId(context);
        ResourceStoreDto resourceStoreDto = BeanConvertUtil.covertBean(reqJson, ResourceStoreDto.class);
        resourceStoreDto.setStoreId(storeId);
        //采购2806集团仓库 物品领用2807小区仓库  默认查询当前小区所有商品
        //是否具有查看集团仓库物品权限
        String userId = reqJson.getString("userId");
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
            List<ResourceStoreDto> resourceStoreDtos = resourceStoreInnerServiceSMOImpl.queryResourceStores(resourceStoreDto);
            resourceStores = BeanConvertUtil.covertBeanList(resourceStoreDtos, ApiResourceStoreDataVo.class);
            //todo 计算物品 库存和 价格
            queryResourceStoreAndResourceTotalPrice(resourceStores);
            BigDecimal number = BigDecimal.ZERO;
            for (ResourceStoreDto resourceStore : resourceStoreDtos) {
                ResourceStoreTimesDto resourceStoreTimesDto = new ResourceStoreTimesDto();
                resourceStoreTimesDto.setResCode(resourceStore.getResCode());
                resourceStoreTimesDto.setShId(resourceStore.getShId());
                //查询批次表
                List<ResourceStoreTimesDto> resourceStoreTimesDtos = resourceStoreTimesV1InnerServiceSMOImpl.queryResourceStoreTimess(resourceStoreTimesDto);
                for (ResourceStoreTimesDto resourceStoreTimes : resourceStoreTimesDtos) {
                    BigDecimal stock = new BigDecimal(resourceStoreTimes.getStock());
                    BigDecimal price = new BigDecimal(resourceStoreTimes.getPrice());
                    BigDecimal multiply = stock.multiply(price).setScale(2, BigDecimal.ROUND_HALF_UP);
                    number = number.add(multiply);
                }
            }
            subTotalPrice = number;
            resourceStoreDto.setPage(PageDto.DEFAULT_PAGE);
            String price = resourceStoreInnerServiceSMOImpl.queryResourceStoresTotalPrice(resourceStoreDto);
            totalPrice = new BigDecimal(price);
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
        BigDecimal stock = null;
        BigDecimal totalPrice = null;
        for (ApiResourceStoreDataVo resourceStore : resourceStores) {
            stock = new BigDecimal(0.0);
            totalPrice = new BigDecimal(0.0);
            List<ResourceStoreTimesDto> resourceStoreTimesDtos = resourceStore.getTimes();
            if (resourceStoreTimesDtos == null || resourceStoreTimesDtos.size() < 1) {
                continue;
            }
            for (ResourceStoreTimesDto resourceStoreTimesDto : resourceStoreTimesDtos) {
                stock = stock.add(new BigDecimal(resourceStoreTimesDto.getStock())).setScale(2, BigDecimal.ROUND_HALF_EVEN);
                totalPrice = totalPrice.add(new BigDecimal(resourceStoreTimesDto.getTotalPrice())).setScale(2, BigDecimal.ROUND_HALF_EVEN);
            }
            resourceStore.setStock(stock.doubleValue() + "");
            resourceStore.setTotalPrice(totalPrice.doubleValue() + "");
        }
    }
}
