package com.java110.store.cmd.resourceStore;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.resourceStore.ResourceStoreDto;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.store.IResourceStoreInnerServiceSMO;
import com.java110.intf.store.IResourceStoreV1InnerServiceSMO;
import com.java110.po.file.FileRelPo;
import com.java110.po.purchase.ResourceStorePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@Java110Cmd(serviceCode = "resourceStore.updateResourceStore")
public class UpdateResourceStoreCmd extends Cmd {

    @Autowired
    private IResourceStoreInnerServiceSMO resourceStoreInnerServiceSMOImpl;

    @Autowired
    private IResourceStoreV1InnerServiceSMO resourceStoreV1InnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "resId", "物品ID不能为空");
        Assert.hasKeyAndValue(reqJson, "resName", "必填，请填写物品名称");
        Assert.hasKeyAndValue(reqJson, "price", "必填，请填写物品价格");
        Assert.hasKeyAndValue(reqJson, "storeId", "商户信息不能为空");

        //获取最低收费标准
        double outLowPrice = Double.parseDouble(reqJson.getString("outLowPrice"));
        //获取最高收费标准
        double outHighPrice = Double.parseDouble(reqJson.getString("outHighPrice"));
        if (outLowPrice > outHighPrice) {
            throw new IllegalArgumentException("最低收费标准不能大于最高收费标准！");
        }

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        ResourceStoreDto resourceStoreDto = new ResourceStoreDto();
        resourceStoreDto.setResId(reqJson.getString("resId"));
        resourceStoreDto.setStoreId(reqJson.getString("storeId"));
        List<ResourceStoreDto> resourceStoreDtos = resourceStoreInnerServiceSMOImpl.queryResourceStores(resourceStoreDto);
        Assert.isOne(resourceStoreDtos, "查询到多条物品 或未查到物品，resId=" + resourceStoreDto.getResId());
        JSONObject businessResourceStore = new JSONObject();
        businessResourceStore.putAll(reqJson);
        businessResourceStore.put("stock", resourceStoreDtos.get(0).getStock());
        ResourceStorePo resourceStorePo = BeanConvertUtil.covertBean(businessResourceStore, ResourceStorePo.class);

        int flag = resourceStoreV1InnerServiceSMOImpl.updateResourceStore(resourceStorePo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }
        //获取图片信息
        List<String> photos = resourceStorePo.getFileUrls();
        //图片更新
        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setObjId(reqJson.getString("resId"));
        //查询文件表
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
        //如果文件表里有这个资源的记录，就先删掉原先的
        if (fileRelDtos != null && fileRelDtos.size() > 0) {
            //先把删除文件表图片
            for (FileRelDto fileRel : fileRelDtos) {
                FileRelPo fileRelPo = new FileRelPo();
                fileRelPo.setFileRelId(fileRel.getFileRelId());
                flag = fileRelInnerServiceSMOImpl.deleteFileRel(fileRelPo);
                if (flag < 1) {
                    throw new CmdException("保存数据失败");
                }
            }
            FileRelPo fileRel = new FileRelPo();
            fileRel.setObjId(resourceStorePo.getResId());
            //table表示表存储 ftp表示ftp文件存储
            fileRel.setSaveWay("ftp");
            fileRel.setCreateTime(new Date());
            if (photos != null && photos.size() > 0) {
                //22000表示物品图片
                fileRel.setRelTypeCd("22000");
                for (String photo : photos) {
                    fileRel.setFileRelId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_relId));
                    fileRel.setFileRealName(photo);
                    fileRel.setFileSaveName(photo);
                    flag = fileRelInnerServiceSMOImpl.saveFileRel(fileRel);
                    if (flag < 1) {
                        throw new CmdException("保存数据失败");
                    }
                }
            }
        } else if (photos != null && photos.size() > 0) {  //如果文件表里没有这个资源的记录，就判断是否有图片传过来，有就插入到文件表里
            FileRelPo fileRel = new FileRelPo();
            fileRel.setObjId(resourceStorePo.getResId());
            //table表示表存储 ftp表示ftp文件存储
            fileRel.setSaveWay("ftp");
            fileRel.setCreateTime(new Date());
            //22000表示物品图片
            fileRel.setRelTypeCd("22000");
            for (String photo : photos) {
                fileRel.setFileRelId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_relId));
                fileRel.setFileRealName(photo);
                fileRel.setFileSaveName(photo);
                flag = fileRelInnerServiceSMOImpl.saveFileRel(fileRel);
                if (flag < 1) {
                    throw new CmdException("保存数据失败");
                }
            }
        }
    }
}