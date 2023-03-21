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
package com.java110.store.cmd.assetInventory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.resourceStoreTimes.ResourceStoreTimesDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.store.*;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.assetInventory.AssetInventoryPo;
import com.java110.po.assetInventoryDetail.AssetInventoryDetailPo;
import com.java110.po.purchase.ResourceStorePo;
import com.java110.po.resourceStoreTimes.ResourceStoreTimesPo;
import com.java110.po.user.UserPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;


/**
 * 类表述：更新
 * 服务编码：assetInventory.updateAssetInventory
 * 请求路劲：/app/assetInventory.UpdateAssetInventory
 * add by 吴学文 at 2022-09-22 14:23:28 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "assetInventory.updateAssetInventory")
public class UpdateAssetInventoryCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(UpdateAssetInventoryCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IAssetInventoryV1InnerServiceSMO assetInventoryV1InnerServiceSMOImpl;

    @Autowired
    private IAssetInventoryDetailV1InnerServiceSMO assetInventoryDetailV1InnerServiceSMOImpl;

    @Autowired
    private IResourceStoreV1InnerServiceSMO resourceStoreV1InnerServiceSMOImpl;

    @Autowired
    private IPurchaseApplyInnerServiceSMO purchaseApplyInnerServiceSMOImpl;

    @Autowired
    private IAllocationStorehouseInnerServiceSMO allocationStorehouseInnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMO;

    @Autowired
    private IResourceStoreTimesV1InnerServiceSMO resourceStoreTimesV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "aiId", "请求报文中未包含aiId");
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        AssetInventoryPo assetInventoryPo = BeanConvertUtil.covertBean(reqJson, AssetInventoryPo.class);

        // 后面调整
/*        if (assetInventoryPo.getState() != null && assetInventoryPo.getState().equals("2000")) { //提交申请的
            PurchaseApplyDto purchaseApplyDto = new PurchaseApplyDto();
            purchaseApplyDto.setCommunityId(assetInventoryPo.getCommunityId());
            purchaseApplyDto.setState(purchaseApplyDto.STATE_DEALING);//审核中
            List<PurchaseApplyDto> purchaseApplyDtos = purchaseApplyInnerServiceSMOImpl.queryPurchaseApplyAndDetails(purchaseApplyDto);
            for (PurchaseApplyDto applyDto : purchaseApplyDtos) {
                List<PurchaseApplyDetailVo> purchaseApplyDetailVos = applyDto.getPurchaseApplyDetailVo();
                for (PurchaseApplyDetailVo applyDetailVo : purchaseApplyDetailVos) {
                    if (applyDetailVo.getShId() != null && assetInventoryPo.getShId() != null && applyDetailVo.getShId().equals(assetInventoryPo.getShId())) {
                        String resOrderType = applyDto.getResOrderType().equals("10000") ? "入库" : "出库";
                        throw new CmdException("盘存失败"+applyDetailVo.getShName() + "有"+ resOrderType +"物品");
                        //cmdDataFlowContext.setResponseEntity(ResultVo.error("盘存失败" + applyDetailVo.getShName() + "有" + resOrderType + "物品"));
                    }
                }
            }
            AllocationStorehouseDto allocationStorehouseDto = new AllocationStorehouseDto();
            allocationStorehouseDto.setShIda(assetInventoryPo.getShId());
            allocationStorehouseDto.setState(allocationStorehouseDto.STATE_AUDIT);
            List<AllocationStorehouseDto> allocationStorehouseDtosa = allocationStorehouseInnerServiceSMOImpl.queryAllocationStorehouses(allocationStorehouseDto);
            if (allocationStorehouseDtosa.size() > 0) {
                throw new CmdException("盘存失败"+assetInventoryPo.getShName() + "有调拨物品。");
                //cmdDataFlowContext.setResponseEntity(ResultVo.error("盘存失败" + assetInventoryPo.getShName() + "有调拨物品。"));
            }
            allocationStorehouseDto.setShIdz(assetInventoryPo.getShId());
            allocationStorehouseDtosa = allocationStorehouseInnerServiceSMOImpl.queryAllocationStorehouses(allocationStorehouseDto);
            if (allocationStorehouseDtosa.size() > 0) {
                throw new CmdException("盘存失败"+assetInventoryPo.getShName() + "有调拨物品。");
                //cmdDataFlowContext.setResponseEntity(ResultVo.error("盘存失败" + assetInventoryPo.getShName() + "有调拨物品。"));
            }
        }*/

        JSONArray resourceStores = reqJson.getJSONArray("resourceStores");
        if (resourceStores != null && resourceStores.size() > 0 && assetInventoryPo.getState() != null && !assetInventoryPo.getState().equals("4000")) {
            AssetInventoryDetailPo assetInventoryDetailPo = new AssetInventoryDetailPo();
            assetInventoryDetailPo.setApplyOrderId(assetInventoryPo.getAiId());
            assetInventoryDetailV1InnerServiceSMOImpl.deleteAssetInventoryDetail(assetInventoryDetailPo);
            for (int resourceStoreIndex = 0; resourceStoreIndex < resourceStores.size(); resourceStoreIndex++) {
                JSONObject resourceStore = resourceStores.getJSONObject(resourceStoreIndex);
                resourceStore.put("originalStock", resourceStore.getString("stock"));
                assetInventoryDetailPo = BeanConvertUtil.covertBean(resourceStore, AssetInventoryDetailPo.class);
                assetInventoryDetailPo.setId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
                assetInventoryDetailPo.setApplyOrderId(assetInventoryPo.getAiId());
                int flag = assetInventoryDetailV1InnerServiceSMOImpl.saveAssetInventoryDetail(assetInventoryDetailPo);
                if (flag < 1) {
                    throw new CmdException("保存数据盘点明细失败");
                }
            }
        }
        if (assetInventoryPo.getState() != null && (assetInventoryPo.getState().equals("4000")||assetInventoryPo.getState().equals("3000"))){
            String currentUserId = reqJson.getString("userId");
            UserDto userDto =  new UserDto();
            userDto.setUserId(currentUserId);
            List<UserDto> userDtos = userV1InnerServiceSMO.queryUsers(userDto);
            Assert.listOnlyOne(userDtos,"用户不存在");
            assetInventoryPo.setAuditId(currentUserId);
            assetInventoryPo.setAuditName(userDtos.get(0).getName());
            assetInventoryPo.setAuditTel(userDtos.get(0).getTel());
            assetInventoryPo.setAuditTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        }
        int flag = assetInventoryV1InnerServiceSMOImpl.updateAssetInventory(assetInventoryPo);
        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }
        if (assetInventoryPo.getState() != null && assetInventoryPo.getState().equals("4000")){ //审核通过，更新库存
            if (resourceStores != null && resourceStores.size() > 0 ) {
                for (int resourceStoreIndex = 0; resourceStoreIndex < resourceStores.size(); resourceStoreIndex++) {
                    AssetInventoryDetailPo assetInventoryDetailPo = new AssetInventoryDetailPo();
                    JSONObject resourceStore = resourceStores.getJSONObject(resourceStoreIndex);
                    resourceStore.put("originalStock", resourceStore.getString("stock"));
                    assetInventoryDetailPo = BeanConvertUtil.covertBean(resourceStore, AssetInventoryDetailPo.class);
                    assetInventoryDetailPo.setId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
                    assetInventoryDetailPo.setApplyOrderId(assetInventoryPo.getAiId());

                    //更新批次库存
                    ResourceStoreTimesDto resourceStoreTimesDto = new ResourceStoreTimesDto();
                    resourceStoreTimesDto.setTimesId(assetInventoryDetailPo.getTimesId());
                    List<ResourceStoreTimesDto> resourceStoreTimesDtos = resourceStoreTimesV1InnerServiceSMOImpl.queryResourceStoreTimess(resourceStoreTimesDto);
                    if(resourceStoreTimesDtos.size()>0){
                        ResourceStoreTimesPo resourceStoreTimesPo = new ResourceStoreTimesPo();
                        resourceStoreTimesPo.setTimesId(resourceStoreTimesDtos.get(0).getTimesId());
                        resourceStoreTimesPo.setStock(assetInventoryDetailPo.getQuantity());;
                        resourceStoreTimesV1InnerServiceSMOImpl.updateResourceStoreTimes(resourceStoreTimesPo);
                    }

                    //查询批次库存总和
                    ResourceStoreTimesDto resourceStoreTimesDto1 = new ResourceStoreTimesDto();
                    resourceStoreTimesDto1.setShId(assetInventoryDetailPo.getShId());
                    resourceStoreTimesDto1.setResCode(assetInventoryDetailPo.getResCode());
                    Integer timessCountStock = resourceStoreTimesV1InnerServiceSMOImpl.queryResourceStoreTimessCountStock(resourceStoreTimesDto1);


                    //更新某个仓库某个商品
                    ResourceStorePo resourceStorePo = new ResourceStorePo();
                    resourceStorePo.setResId(assetInventoryDetailPo.getResId());
                    resourceStorePo.setStock(Integer.toString(timessCountStock));

                    //获取紧急采购数量
                    BigDecimal quantity = new BigDecimal(timessCountStock);
                    BigDecimal miniUnitStock = new BigDecimal(resourceStore.getString("miniUnitStock"));
                    //计算最小计量总数
                    BigDecimal miniStock = quantity.multiply(miniUnitStock);
                    resourceStorePo.setMiniStock(String.valueOf(miniStock));
                    resourceStorePo.setStatusCd("0");
                    resourceStoreV1InnerServiceSMOImpl.updateResourceStore(resourceStorePo);



                }
            }
        }
        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
