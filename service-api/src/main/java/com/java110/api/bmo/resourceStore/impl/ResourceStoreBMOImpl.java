package com.java110.api.bmo.resourceStore.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.resourceStore.IResourceStoreBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.allocationStorehouse.AllocationStorehouseDto;
import com.java110.dto.file.FileDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.purchaseApplyDetail.PurchaseApplyDetailDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.store.IAllocationStorehouseInnerServiceSMO;
import com.java110.intf.store.IPurchaseApplyDetailInnerServiceSMO;
import com.java110.intf.store.IResourceStoreInnerServiceSMO;
import com.java110.dto.resourceStore.ResourceStoreDto;
import com.java110.po.file.FileRelPo;
import com.java110.po.purchase.ResourceStorePo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @ClassName ResourceStoreBMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 23:40
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
@Service("resourceStoreBMOImpl")
public class ResourceStoreBMOImpl extends ApiBaseBMO implements IResourceStoreBMO {

    @Autowired
    private IResourceStoreInnerServiceSMO resourceStoreInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IPurchaseApplyDetailInnerServiceSMO purchaseApplyDetailInnerServiceSMOImpl;

    @Autowired
    private IAllocationStorehouseInnerServiceSMO allocationStorehouseInnerServiceSMOImpl;

    /**
     * 删除资源信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteResourceStore(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        //获取资源id
        String resId = paramInJson.getString("resId");
        paramInJson.put("statusCd", "1");
        ResourceStorePo resourceStorePo = BeanConvertUtil.covertBean(paramInJson, ResourceStorePo.class);
        //根据物品id查询采购明细表
        PurchaseApplyDetailDto purchaseApplyDetailDto = new PurchaseApplyDetailDto();
        purchaseApplyDetailDto.setResId(resId);
        List<PurchaseApplyDetailDto> purchaseApplyDetailDtos = purchaseApplyDetailInnerServiceSMOImpl.queryPurchaseApplyDetails(purchaseApplyDetailDto);
        Assert.listIsNull(purchaseApplyDetailDtos, "该物品存在采购或领用记录，不能删除！");
        //根据物品id查询调拨记录
        AllocationStorehouseDto allocationStorehouseDto = new AllocationStorehouseDto();
        allocationStorehouseDto.setResId(resId);
        List<AllocationStorehouseDto> allocationStorehouseDtos = allocationStorehouseInnerServiceSMOImpl.queryAllocationStorehouses(allocationStorehouseDto);
        Assert.listIsNull(allocationStorehouseDtos, "该物品存在调拨记录，不能删除！");
        super.delete(dataFlowContext, resourceStorePo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_RESOURCE_STORE);
        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setObjId(resId);
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
        if (fileRelDtos != null && fileRelDtos.size() > 0) {
            //删除文件表图片
            for (FileRelDto fileRel : fileRelDtos) {
                FileRelPo fileRelpo = new FileRelPo();
                fileRelpo.setFileRelId(fileRel.getFileRelId());
                super.delete(dataFlowContext, fileRelpo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_FILE_REL);
            }
        }
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addResourceStore(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        //获取物品编码
        String resCode = paramInJson.getString("resCode");
        //根据物品编码查询物品资源表
        ResourceStoreDto resourceStoreDto = new ResourceStoreDto();
        resourceStoreDto.setResCode(resCode);
        List<ResourceStoreDto> resourceStoreDtos = resourceStoreInnerServiceSMOImpl.queryResourceStores(resourceStoreDto);
        //判断资源表里是否有该物品编码，避免物品编码重复
        Assert.listIsNull(resourceStoreDtos, "物品编码重复，请重新添加！");
        JSONObject businessResourceStore = new JSONObject();
        businessResourceStore.putAll(paramInJson);
        businessResourceStore.put("resId", GenerateCodeFactory.getResId(GenerateCodeFactory.CODE_PREFIX_resId));
        businessResourceStore.put("stock", "0");
        businessResourceStore.put("miniStock", "0");
        businessResourceStore.put("createTime", new Date());
        ResourceStorePo resourceStorePo = BeanConvertUtil.covertBean(businessResourceStore, ResourceStorePo.class);
        resourceStorePo.setAveragePrice("0");
        super.insert(dataFlowContext, resourceStorePo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_RESOURCE_STORE);
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
                FileDto fileDto = new FileDto();
                fileDto.setCommunityId("-1");
                fileDto.setContext(photo);
                fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
                fileDto.setFileName(fileDto.getFileId());
                String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);
                fileRelPo.setFileRealName(fileName);
                fileRelPo.setFileSaveName(fileName);
                super.insert(dataFlowContext, fileRelPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FILE_REL);
            }
        }
    }

    /**
     * 添加物品管理信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateResourceStore(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        ResourceStoreDto resourceStoreDto = new ResourceStoreDto();
        resourceStoreDto.setResId(paramInJson.getString("resId"));
        resourceStoreDto.setStoreId(paramInJson.getString("storeId"));
        List<ResourceStoreDto> resourceStoreDtos = resourceStoreInnerServiceSMOImpl.queryResourceStores(resourceStoreDto);
        Assert.isOne(resourceStoreDtos, "查询到多条物品 或未查到物品，resId=" + resourceStoreDto.getResId());
        JSONObject businessResourceStore = new JSONObject();
        businessResourceStore.putAll(paramInJson);
        businessResourceStore.put("stock", resourceStoreDtos.get(0).getStock());
        ResourceStorePo resourceStorePo = BeanConvertUtil.covertBean(businessResourceStore, ResourceStorePo.class);
        super.update(dataFlowContext, resourceStorePo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_RESOURCE_STORE);
        //获取图片信息
        List<String> photos = resourceStorePo.getFileUrls();
        //图片更新
        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setObjId(paramInJson.getString("resId"));
        //查询文件表
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
        //如果文件表里有这个资源的记录，就先删掉原先的
        if (fileRelDtos != null && fileRelDtos.size() > 0) {
            //先把删除文件表图片
            for (FileRelDto fileRel : fileRelDtos) {
                FileRelPo fileRelPo = new FileRelPo();
                fileRelPo.setFileRelId(fileRel.getFileRelId());
                super.delete(dataFlowContext, fileRelPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_FILE_REL);
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
                    FileDto fileDto = new FileDto();
                    fileDto.setCommunityId("-1");
                    fileDto.setContext(photo);
                    fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
                    fileDto.setFileName(fileDto.getFileId());
                    String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);
                    fileRel.setFileRealName(fileName);
                    fileRel.setFileSaveName(fileName);
                    /*fileRelInnerServiceSMOImpl.saveFileRel(fileRel);*/
                    super.insert(dataFlowContext, fileRel, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FILE_REL);
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
                FileDto fileDto = new FileDto();
                fileDto.setCommunityId("-1");
                fileDto.setContext(photo);
                fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
                fileDto.setFileName(fileDto.getFileId());
                String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);
                fileRel.setFileRealName(fileName);
                fileRel.setFileSaveName(fileName);
                super.insert(dataFlowContext, fileRel, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FILE_REL);
            }
        }
    }
}
