package com.java110.store.cmd.resourceStore;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.resource.ResourceStoreTypeDto;
import com.java110.intf.store.IResourceStoreTypeInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询类型树
 */
@Java110Cmd(serviceCode = "resourceStore.listResourceStoreTypeTree")
public class ListResourceStoreTypeTreeCmd extends Cmd {

    @Autowired
    private IResourceStoreTypeInnerServiceSMO resourceStoreTypeInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        String storeId = CmdContextUtils.getStoreId(context);
        Assert.hasLength(storeId, "未包含商户ID");
        reqJson.put("storeId", storeId);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        ResourceStoreTypeDto resourceStoreTypeDto = BeanConvertUtil.covertBean(reqJson, ResourceStoreTypeDto.class);

        List<ResourceStoreTypeDto> storeTypeDtos = resourceStoreTypeInnerServiceSMOImpl.queryResourceStoreTypeTree(resourceStoreTypeDto);

        if (storeTypeDtos == null || storeTypeDtos.size() < 1) {
            return;
        }


        List<ResourceStoreTypeDto> storeTypes = new ArrayList<>();
        ResourceStoreTypeDto tResourceStoreTypeDto = null;
        List<ResourceStoreTypeDto> subStoreTypes = null;
        for (ResourceStoreTypeDto tmpResourceStoreTypeDto : storeTypeDtos) {

            //todo 一级分类
            tResourceStoreTypeDto = getParentResoureceStoreTypes(storeTypes, tmpResourceStoreTypeDto);

            //todo 如果现在就是一级分类 跳过
            if (tmpResourceStoreTypeDto.getRstId().equals(tResourceStoreTypeDto.getRstId())) {
                subStoreTypes = new ArrayList<>();
                ResourceStoreTypeDto selfResourceStoreTypeDto = BeanConvertUtil.covertBean(tmpResourceStoreTypeDto,ResourceStoreTypeDto.class);
                subStoreTypes.add(selfResourceStoreTypeDto);
                tResourceStoreTypeDto.setSubTypes(subStoreTypes);
                continue;
            }

            if (tResourceStoreTypeDto.getSubTypes() == null) {
                tResourceStoreTypeDto.setSubTypes(new ArrayList<>());
            }
            subStoreTypes = tResourceStoreTypeDto.getSubTypes();
            subStoreTypes.add(tmpResourceStoreTypeDto);
        }

        context.setResponseEntity(ResultVo.createResponseEntity(storeTypes));


    }

    private ResourceStoreTypeDto getParentResoureceStoreTypes(List<ResourceStoreTypeDto> storeTypes, ResourceStoreTypeDto tmpResourceStoreTypeDto) {
        //todo 没有数据直接写入
        if (storeTypes.size() < 1) {
            storeTypes.add(tmpResourceStoreTypeDto);
            return tmpResourceStoreTypeDto;
        }

        for (ResourceStoreTypeDto storeType : storeTypes) {
            if (storeType.getParentRstId().equals(tmpResourceStoreTypeDto.getParentRstId())) {
                return storeType;
            }
        }
        storeTypes.add(tmpResourceStoreTypeDto);
        return tmpResourceStoreTypeDto;
    }
}
