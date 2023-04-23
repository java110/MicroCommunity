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

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.resourceStore.ResourceStoreDto;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.store.IResourceStoreInnerServiceSMO;
import com.java110.intf.store.IResourceStoreTimesV1InnerServiceSMO;
import com.java110.intf.store.IResourceStoreV1InnerServiceSMO;
import com.java110.po.file.FileRelPo;
import com.java110.po.purchase.ResourceStorePo;
import com.java110.po.resourceStoreTimes.ResourceStoreTimesPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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
@Java110Cmd(serviceCode = "resourceStore.saveResourceStore")
public class SaveResourceStoreCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveResourceStoreCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IResourceStoreV1InnerServiceSMO resourceStoreV1InnerServiceSMOImpl;

    @Autowired
    private IResourceStoreInnerServiceSMO resourceStoreInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;
    @Autowired
    private IResourceStoreTimesV1InnerServiceSMO resourceStoreTimesV1InnerServiceSMOImpl;
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");
        Assert.hasKeyAndValue(reqJson, "resName", "必填，请填写物品名称");
        Assert.hasKeyAndValue(reqJson, "storeId", "必填，请填写商户信息");
        Assert.hasKeyAndValue(reqJson, "price", "必填，请填写物品价格");
        Assert.hasKeyAndValue(reqJson, "shId", "必填，请填写仓库");

        //获取最低收费标准
        double outLowPrice = Double.parseDouble(reqJson.getString("outLowPrice"));
        //获取最高收费标准
        double outHighPrice = Double.parseDouble(reqJson.getString("outHighPrice"));
        if (outLowPrice > outHighPrice) {
            throw new IllegalArgumentException("最低收费标准不能大于最高收费标准！");
        }

        String resCode = reqJson.getString("resCode");
        String storeId = cmdDataFlowContext.getReqHeaders().get("store-id");
        //根据物品编码查询物品资源表
        ResourceStoreDto resourceStoreDto = new ResourceStoreDto();
        resourceStoreDto.setResCode(resCode);
        resourceStoreDto.setStoreId(storeId);
        List<ResourceStoreDto> resourceStoreDtos = resourceStoreInnerServiceSMOImpl.queryResourceStores(resourceStoreDto);
        //判断资源表里是否有该物品编码，避免物品编码重复
        Assert.listIsNull(resourceStoreDtos, "物品编码重复，请重新添加！");

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        JSONObject businessResourceStore = new JSONObject();
        businessResourceStore.putAll(reqJson);
        businessResourceStore.put("resId", GenerateCodeFactory.getResId(GenerateCodeFactory.CODE_PREFIX_resId));
        businessResourceStore.put("stock", "0");
        businessResourceStore.put("miniStock", "0");
        businessResourceStore.put("createTime", new Date());
        ResourceStorePo resourceStorePo = BeanConvertUtil.covertBean(businessResourceStore, ResourceStorePo.class);
        if (resourceStorePo.getAveragePrice() == null || resourceStorePo.getAveragePrice().equals("")) {
            resourceStorePo.setAveragePrice("0.00");
        }
        int flag = resourceStoreV1InnerServiceSMOImpl.saveResourceStore(resourceStorePo);
        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        // 保存至 物品 times表
        ResourceStoreTimesPo resourceStoreTimesPo = new ResourceStoreTimesPo();
        resourceStoreTimesPo.setApplyOrderId("-1");
        resourceStoreTimesPo.setPrice(resourceStorePo.getPrice());
        resourceStoreTimesPo.setStock(resourceStorePo.getStock());
        resourceStoreTimesPo.setResCode(resourceStorePo.getResCode());
        resourceStoreTimesPo.setStoreId(resourceStorePo.getStoreId());
        resourceStoreTimesPo.setShId(resourceStorePo.getShId());
        resourceStoreTimesV1InnerServiceSMOImpl.saveOrUpdateResourceStoreTimes(resourceStoreTimesPo);

        //将图片插入文件表里
        FileRelPo fileRelPo = new FileRelPo();
        fileRelPo.setObjId(resourceStorePo.getResId());
        //table表示表存储 ftp表示ftp文件存储
        fileRelPo.setSaveWay("ftp");
        fileRelPo.setCreateTime(new Date());
        //图片
        List<String> photos = resourceStorePo.getPhotos();
        if (photos != null && photos.size() > 0) {
            //22000表示物品图片
            fileRelPo.setRelTypeCd("22000");
            for (String photo : photos) {
                fileRelPo.setFileRelId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_relId));
                fileRelPo.setFileRealName(photo);
                fileRelPo.setFileSaveName(photo);
                flag = fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
                if (flag < 1) {
                    throw new CmdException("保存数据失败");
                }
            }
        }
        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
