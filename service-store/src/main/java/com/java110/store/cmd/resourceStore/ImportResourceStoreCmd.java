/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.store.cmd.resourceStore;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.Dict.DictDto;
import com.java110.dto.Dict.DictQueryDto;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.dto.resourceStore.ResourceStoreDto;
import com.java110.dto.resourceStoreType.ResourceStoreTypeDto;
import com.java110.dto.user.UserDto;
import com.java110.entity.assetImport.ImportResourceStoreDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.DictInnerServiceSMO;
import com.java110.intf.store.*;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.purchase.PurchaseApplyDetailPo;
import com.java110.po.purchase.PurchaseApplyPo;
import com.java110.po.purchase.ResourceStorePo;
import com.java110.po.resourceStoreTimes.ResourceStoreTimesPo;
import com.java110.po.resourceStoreType.ResourceStoreTypePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 类表述：保存
 * 服务编码：resourceStore.saveResourceStore
 * 请求路劲：/app/resourceStore.SaveResourceStore
 * add by 吴学文 at 2022-06-30 12:03:19 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "resourceStore.importResourceStore")
public class ImportResourceStoreCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ImportResourceStoreCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IResourceStoreV1InnerServiceSMO resourceStoreV1InnerServiceSMOImpl;

    @Autowired
    private IResourceStoreInnerServiceSMO resourceStoreInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IPurchaseApplyInnerServiceSMO purchaseApplyInnerServiceSMOImpl;

    @Autowired
    private DictInnerServiceSMO dictInnerServiceSMOImpl;

    @Autowired
    private IResourceStoreTypeV1InnerServiceSMO resourceStoreTypeV1InnerServiceSMOImpl;

    @Autowired
    private IResourceStoreTimesV1InnerServiceSMO resourceStoreTimesV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");


    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        String userId = reqJson.getString("userId");
        String userName = reqJson.getString("userName");
        String storeId = reqJson.getString("storeId");

        JSONArray importResourceStoreDtos = reqJson.getJSONArray("importResourceStoreDtos");
        List<ImportResourceStoreDto> tmpImportResourceStoreDtos = JSONArray.parseArray(importResourceStoreDtos.toJSONString(), ImportResourceStoreDto.class);
        if (tmpImportResourceStoreDtos == null || tmpImportResourceStoreDtos.size() < 1) {
            cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(0));
            return;
        }
        DictQueryDto dictDto = new DictQueryDto();
        dictDto.setName("resource_store");
        dictDto.setType("unit_code");
        List<DictDto> dictDtos = dictInnerServiceSMOImpl.queryDict(dictDto);

        List<ResourceStorePo> resourceStorePos = new ArrayList<>();
        for (ImportResourceStoreDto importResourceStoreDto : tmpImportResourceStoreDtos) {
            //根据物品编码查询物品资源表
            ResourceStoreDto resourceStoreDto = new ResourceStoreDto();
            resourceStoreDto.setResCode(importResourceStoreDto.getResName());
            resourceStoreDto.setShId(reqJson.getString("shId"));
            List<ResourceStoreDto> resourceStoreDtos = resourceStoreInnerServiceSMOImpl.queryResourceStores(resourceStoreDto);
            //判断资源表里是否有该物品编码，避免物品编码重复
            JSONObject businessResourceStore = new JSONObject();
            //businessResourceStore.put("stock", "0");
            businessResourceStore.put("miniStock", "100");
            businessResourceStore.put("outLowPrice", importResourceStoreDto.getOutLowPrice());
            businessResourceStore.put("outHighPrice", importResourceStoreDto.getOutHighPrice());
            businessResourceStore.put("showMobile", "N");
            businessResourceStore.put("description", importResourceStoreDto.getResName());
            businessResourceStore.put("isFixed", importResourceStoreDto.getIsFixed());
            businessResourceStore.put("miniUnitCode", "1");
            businessResourceStore.put("miniUnitStock", "1");
            businessResourceStore.put("storeId", storeId);
            businessResourceStore.put("createTime", new Date());
            ResourceStorePo resourceStorePo = BeanConvertUtil.covertBean(businessResourceStore, ResourceStorePo.class);
            resourceStorePo.setShId(reqJson.getString("shId"));
            resourceStorePo.setStock(importResourceStoreDto.getStock());
            resourceStorePo.setResName(importResourceStoreDto.getResName());
            resourceStorePo.setResCode(importResourceStoreDto.getResCode());
            resourceStorePo.setRstId(getRstId(importResourceStoreDto.getRstName(), storeId));
            resourceStorePo.setParentRstId(resourceStorePo.getRstId());
            resourceStorePo.setPrice(importResourceStoreDto.getPrice());
            resourceStorePo.setUnitCode(getUnitCode(importResourceStoreDto.getUnitCode(), dictDtos));
            resourceStorePo.setRemark(importResourceStoreDto.getRemark());
            resourceStorePo.setWarningStock(importResourceStoreDto.getWarningStock());
            resourceStorePo.setAveragePrice(importResourceStoreDto.getPrice());
            int flag = 0;
            if (resourceStoreDtos == null || resourceStoreDtos.size() < 1) {
                resourceStorePo.setResId(GenerateCodeFactory.getResId(GenerateCodeFactory.CODE_PREFIX_resId));
                flag = resourceStoreV1InnerServiceSMOImpl.saveResourceStore(resourceStorePo);


            } else {
                resourceStorePo.setResId(resourceStoreDtos.get(0).getResId());
                double stock = Double.parseDouble(resourceStoreDtos.get(0).getStock()) + Double.parseDouble(importResourceStoreDto.getStock());
                resourceStorePo.setStock(stock + "");
                resourceStorePo.setOutLowPrice("");
                resourceStorePo.setOutHighPrice("");
               resourceStorePo.setMiniUnitCode("");
               resourceStorePo.setMiniStock("");

                flag = resourceStoreV1InnerServiceSMOImpl.updateResourceStore(resourceStorePo);

                resourceStorePo.setStock(importResourceStoreDto.getStock());
            }

            if (flag < 1) {
                throw new CmdException("保存数据失败");
            }

            resourceStorePos.add(resourceStorePo);
        }

        inStore(reqJson, userId, userName, storeId, resourceStorePos);
        cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(tmpImportResourceStoreDtos.size()));

    }

    private String getRstId(String rstName, String storeId) {

        ResourceStoreTypeDto resourceStoreTypeDto = new ResourceStoreTypeDto();
        resourceStoreTypeDto.setStoreId(storeId);
        resourceStoreTypeDto.setName(rstName);
        List<ResourceStoreTypeDto> resourceStoreTypeDtos = resourceStoreTypeV1InnerServiceSMOImpl.queryResourceStoreTypes(resourceStoreTypeDto);
        if (resourceStoreTypeDtos != null && resourceStoreTypeDtos.size() > 0) {
            return resourceStoreTypeDtos.get(0).getRstId();
        }

        ResourceStoreTypePo resourceStoreTypePo = new ResourceStoreTypePo();
        resourceStoreTypePo.setDescription(rstName);
        resourceStoreTypePo.setName(rstName);
        resourceStoreTypePo.setStoreId(storeId);
        resourceStoreTypePo.setRstId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_rstId));
        resourceStoreTypePo.setParentId("0");
        resourceStoreTypeV1InnerServiceSMOImpl.saveResourceStoreType(resourceStoreTypePo);
        return resourceStoreTypePo.getRstId();
    }

    private String getUnitCode(String unitCode, List<DictDto> dictDtos) {
        for (DictDto dictDto : dictDtos) {
            if (unitCode.equals(dictDto.getName())) {
                return dictDto.getStatusCd();
            }
        }

        return "无";
    }

    /**
     * 直接入库
     *
     * @param reqJson
     * @param userId
     * @param userName
     * @param storeId
     */
    private void inStore(JSONObject reqJson, String userId, String userName, String storeId, List<ResourceStorePo> resourceStorePos) {

        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        userDto.setPage(1);
        userDto.setRow(1);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户不存在");

        PurchaseApplyPo purchaseApplyPo = new PurchaseApplyPo();
        purchaseApplyPo.setApplyOrderId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_applyOrderId));
        purchaseApplyPo.setDescription("入库单（物品导入）");
        purchaseApplyPo.setUserId(userId);
        purchaseApplyPo.setUserName(userDtos.get(0).getName());
        purchaseApplyPo.setEndUserName(userDtos.get(0).getName());
        purchaseApplyPo.setEndUserTel(userDtos.get(0).getTel());
        purchaseApplyPo.setStoreId(storeId);
        purchaseApplyPo.setResOrderType(PurchaseApplyDto.RES_ORDER_TYPE_ENTER);
        purchaseApplyPo.setState(PurchaseApplyDto.STATE_END);
        purchaseApplyPo.setCreateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        purchaseApplyPo.setCreateUserId(userId);
        purchaseApplyPo.setCreateUserName(userName);
        purchaseApplyPo.setWarehousingWay(PurchaseApplyDto.WAREHOUSING_TYPE_URGENT);
        purchaseApplyPo.setCommunityId(reqJson.getString("communityId"));
        //获取采购物品信息
        List<PurchaseApplyDetailPo> purchaseApplyDetailPos = new ArrayList<>();
        for (ResourceStorePo resourceStorePo : resourceStorePos) {
            PurchaseApplyDetailPo purchaseApplyDetailPo = new PurchaseApplyDetailPo();
            purchaseApplyDetailPo.setApplyOrderId(purchaseApplyPo.getApplyOrderId());
            purchaseApplyDetailPo.setId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_applyOrderId));
            purchaseApplyDetailPo.setPurchaseQuantity(resourceStorePo.getStock());
            purchaseApplyDetailPo.setPurchaseRemark(resourceStorePo.getRemark());
            purchaseApplyDetailPo.setOriginalStock(resourceStorePo.getStock());
            purchaseApplyDetailPo.setQuantity(resourceStorePo.getStock());
            purchaseApplyDetailPo.setPrice(resourceStorePo.getPrice());
            purchaseApplyDetailPo.setResId(resourceStorePo.getResId());
            purchaseApplyDetailPo.setRsId(resourceStorePo.getRssId());
            purchaseApplyDetailPos.add(purchaseApplyDetailPo);
            purchaseApplyPo.setPurchaseApplyDetailPos(purchaseApplyDetailPos);

            // 保存至 物品 times表
            ResourceStoreTimesPo resourceStoreTimesPo = new ResourceStoreTimesPo();
            resourceStoreTimesPo.setApplyOrderId(purchaseApplyPo.getApplyOrderId());
            resourceStoreTimesPo.setPrice(purchaseApplyDetailPo.getPrice());
            resourceStoreTimesPo.setStock(purchaseApplyDetailPo.getPurchaseQuantity());
            resourceStoreTimesPo.setResCode(resourceStorePo.getResCode());
            resourceStoreTimesPo.setStoreId(resourceStorePo.getStoreId());
            resourceStoreTimesPo.setTimesId(GenerateCodeFactory.getGeneratorId("10"));
            resourceStoreTimesV1InnerServiceSMOImpl.saveOrUpdateResourceStoreTimes(resourceStoreTimesPo);
        }
        int saveFlag = purchaseApplyInnerServiceSMOImpl.savePurchaseApply(purchaseApplyPo);
        if (saveFlag < 1) {
            throw new CmdException("采购申请失败");
        }


    }
}
