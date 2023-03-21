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
package com.java110.store.cmd.purchaseApply;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.dto.resourceStore.ResourceStoreDto;
import com.java110.intf.common.IPurchaseApplyUserInnerServiceSMO;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.store.IPurchaseApplyInnerServiceSMO;
import com.java110.intf.store.IPurchaseApplyV1InnerServiceSMO;
import com.java110.intf.store.IResourceStoreInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import com.java110.vo.api.purchaseApply.ApiPurchaseApplyDataVo;
import com.java110.vo.api.purchaseApply.ApiPurchaseApplyVo;
import com.java110.vo.api.purchaseApply.PurchaseApplyDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import com.java110.dto.purchaseApply.PurchaseApplyDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 类表述：查询
 * 服务编码：purchaseApply.listPurchaseApply
 * 请求路劲：/app/purchaseApply.ListPurchaseApply
 * add by 吴学文 at 2022-08-08 13:21:13 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "purchaseApply.listPurchaseApplys")
public class ListPurchaseApplysCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ListPurchaseApplysCmd.class);
    @Autowired
    private IPurchaseApplyV1InnerServiceSMO purchaseApplyV1InnerServiceSMOImpl;

    @Autowired
    private IPurchaseApplyInnerServiceSMO purchaseApplyInnerServiceSMOImpl;

    @Autowired
    private IPurchaseApplyUserInnerServiceSMO purchaseApplyUserInnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Autowired
    private IResourceStoreInnerServiceSMO resourceStoreInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "resOrderType", "必填，请填写订单类型");
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        PurchaseApplyDto purchaseApplyDto = BeanConvertUtil.covertBean(reqJson, PurchaseApplyDto.class);
        purchaseApplyDto.setUserName("");//解除与用户名相关问题
        //获取用户id
        String userId = reqJson.getString("userId");
        //采购申请、物品领用（管理员查看所有，员工查看当前用户相关）
        //采购申请查看、采购待办查看、采购已办查看、物品领用查看、领用待办查看、领用已办查看（不用限制员工）
        List<Map> privileges = new ArrayList<>();
        if (purchaseApplyDto.getResOrderType().equals(PurchaseApplyDto.RES_ORDER_TYPE_ENTER)) {
            //采购申请所有记录权限
            BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
            basePrivilegeDto.setResource("/viewPurchaseApplyManage");
            basePrivilegeDto.setUserId(userId);
            privileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);
        }
        if (purchaseApplyDto.getResOrderType().equals(PurchaseApplyDto.RES_ORDER_TYPE_OUT)) {
            //物品领用所有记录权限
            BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
            basePrivilegeDto.setResource("/viewAllItemUse");
            basePrivilegeDto.setUserId(userId);
            privileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);
        }
        if (privileges.size() != 0 || (!StringUtil.isEmpty(reqJson.getString("applyOrderId")))) {
            purchaseApplyDto.setUserId("");
        }
        int count = purchaseApplyInnerServiceSMOImpl.queryPurchaseApplysCount(purchaseApplyDto);
        List<ApiPurchaseApplyDataVo> purchaseApplys = null;
        if (count > 0) {
            List<PurchaseApplyDto> purchaseApplyDtos = purchaseApplyInnerServiceSMOImpl.queryPurchaseApplyAndDetails(purchaseApplyDto);
            purchaseApplyDtos = freshCurrentUser(purchaseApplyDtos);
            purchaseApplys = BeanConvertUtil.covertBeanList(purchaseApplyDtos, ApiPurchaseApplyDataVo.class);
            for (ApiPurchaseApplyDataVo apiPurchaseApplyDataVo : purchaseApplys) {
                List<PurchaseApplyDetailVo> applyDetailList = apiPurchaseApplyDataVo.getPurchaseApplyDetailVo();
                if (applyDetailList.size() > 0) {
                    StringBuffer resNames = new StringBuffer();
                    BigDecimal totalPrice = new BigDecimal(0);
                    BigDecimal purchaseTotalPrice = new BigDecimal(0);
                    Integer cursor = 0;
                    for (PurchaseApplyDetailVo purchaseApplyDetailVo : applyDetailList) {
                        ResourceStoreDto resourceStoreDto = new ResourceStoreDto();
                        resourceStoreDto.setResId(purchaseApplyDetailVo.getResId());
                        List<ResourceStoreDto> resourceStoreDtos = resourceStoreInnerServiceSMOImpl.queryResourceStores(resourceStoreDto);
                        Assert.listOnlyOne(resourceStoreDtos, "查询物品信息错误！");
                        //是否是固定物品
                        apiPurchaseApplyDataVo.setIsFixed(resourceStoreDtos.get(0).getIsFixed());
                        apiPurchaseApplyDataVo.setIsFixedName(resourceStoreDtos.get(0).getIsFixedName());
                        purchaseApplyDetailVo.setIsFixed(resourceStoreDtos.get(0).getIsFixed());
                        purchaseApplyDetailVo.setIsFixedName(resourceStoreDtos.get(0).getIsFixedName());
                        //获取仓库名称
                        String shName = resourceStoreDtos.get(0).getShName();
                        String shId = resourceStoreDtos.get(0).getShId();
                        purchaseApplyDetailVo.setShName(shName);
                        purchaseApplyDetailVo.setShId(shId);
                        cursor++;
                        if (applyDetailList.size() > 1) {
                            resNames.append(cursor + "：" + purchaseApplyDetailVo.getResName() + "      ");
                        } else {
                            resNames.append(purchaseApplyDetailVo.getResName());
                        }
                        BigDecimal price = new BigDecimal(purchaseApplyDetailVo.getPrice());
                        BigDecimal quantity = new BigDecimal(purchaseApplyDetailVo.getQuantity());
                        totalPrice = totalPrice.add(price.multiply(quantity));
                        if (!StringUtil.isEmpty(purchaseApplyDetailVo.getPurchasePrice()) && !StringUtil.isEmpty(purchaseApplyDetailVo.getPurchaseQuantity())) {
                            BigDecimal purchasePrice = new BigDecimal(purchaseApplyDetailVo.getPurchasePrice());
                            BigDecimal purchaseQuantity = new BigDecimal(purchaseApplyDetailVo.getPurchaseQuantity());
                            purchaseTotalPrice = purchaseTotalPrice.add(purchasePrice.multiply(purchaseQuantity));
                        }
                    }
                    apiPurchaseApplyDataVo.setResourceNames(resNames.toString());
                    apiPurchaseApplyDataVo.setTotalPrice(totalPrice.toString());
                    apiPurchaseApplyDataVo.setPurchaseTotalPrice(purchaseTotalPrice.toString());
                }
            }
        } else {
            purchaseApplys = new ArrayList<>();
        }
        ApiPurchaseApplyVo apiPurchaseApplyVo = new ApiPurchaseApplyVo();
        apiPurchaseApplyVo.setTotal(count);
        apiPurchaseApplyVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiPurchaseApplyVo.setPurchaseApplys(purchaseApplys);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiPurchaseApplyVo), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }

    private List<PurchaseApplyDto> freshCurrentUser(List<PurchaseApplyDto> purchaseApplyDtos) {
        List<PurchaseApplyDto> tmpPurchaseApplyDtos = new ArrayList<>();
        for (PurchaseApplyDto purchaseApplyDto : purchaseApplyDtos) {
            purchaseApplyDto = purchaseApplyUserInnerServiceSMOImpl.getTaskCurrentUser(purchaseApplyDto);
            tmpPurchaseApplyDtos.add(purchaseApplyDto);
        }
        return tmpPurchaseApplyDtos;
    }
}
