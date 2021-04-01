package com.java110.community.bmo.resourceStore.impl;

import com.java110.community.bmo.resourceStore.IGetResourceStoreBMO;
import com.java110.intf.community.IResourceStoreServiceSMO;
import com.java110.po.purchase.ResourceStorePo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询资源物品信息
 *
 * @author fqz
 * @date 2021-03-17 11:27
 */
@Service("getResourceStoreBMOImpl")
public class GetResourceStoreBMOImpl implements IGetResourceStoreBMO {

    @Autowired
    private IResourceStoreServiceSMO resourceStoreServiceSMO;

    @Override
    public ResponseEntity<String> get(ResourceStorePo resourceStorePo) {
        int count = resourceStoreServiceSMO.getResourceStoresCount(resourceStorePo);
        List<ResourceStorePo> resourceStorePos = null;
        if (count > 0) {
            resourceStorePos = resourceStoreServiceSMO.getResourceStores(resourceStorePo);
        } else {
            resourceStorePos = new ArrayList<>();
        }
        ResultVo resultVo = new ResultVo(count, count, resourceStorePos);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        return responseEntity;
    }
}
