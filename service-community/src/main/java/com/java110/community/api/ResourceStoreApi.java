package com.java110.community.api;

import com.java110.community.bmo.resourceStore.IGetResourceStoreBMO;
import com.java110.po.purchase.ResourceStorePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/resourceStore")
public class ResourceStoreApi {

    @Autowired
    private IGetResourceStoreBMO getResourceStoreBMO;

    /**
     * 根据商品类型查询商品信息
     *
     * @param
     * @return
     * @serviceCode /resourceStore/queryResourceStoreResName
     * @path /app/resourceStore/queryResourceStoreResName
     */
    @RequestMapping(value = "/queryResourceStoreResName", method = RequestMethod.GET)
    public ResponseEntity<String> queryResourceStoreResName(@RequestParam(value = "goodsType", required = false) String goodsType) {
        ResourceStorePo resourceStorePo = new ResourceStorePo();
//        resourceStorePo.setGoodsType(goodsType);
        resourceStorePo.setShowMobile("Y");
        return getResourceStoreBMO.get(resourceStorePo);
    }

    /**
     * 根据资源id查询商品信息
     *
     * @param
     * @return
     * @serviceCode /resourceStore/queryResourceStorePrice
     * @path /app/resourceStore/queryResourceStorePrice
     */
    @RequestMapping(value = "/queryResourceStorePrice", method = RequestMethod.GET)
    public ResponseEntity<String> queryResourceStorePrice(@RequestParam(value = "resId", required = false) String resId) {
        ResourceStorePo resourceStorePo = new ResourceStorePo();
        resourceStorePo.setResId(resId);
        return getResourceStoreBMO.get(resourceStorePo);
    }
}
