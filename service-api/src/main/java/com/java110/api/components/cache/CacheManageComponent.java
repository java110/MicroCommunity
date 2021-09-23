package com.java110.api.components.cache;


import com.java110.core.context.IPageData;
import com.java110.api.smo.cache.IListCachesSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 服务绑定组件管理类
 *
 * add by wuxw
 *
 * 2019-06-29
 */
@Component("cacheManage")
public class CacheManageComponent {

    @Autowired
    private IListCachesSMO listCachesSMOImpl;



    /**
     * 查询服务绑定列表
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd){
        return listCachesSMOImpl.listCaches(pd);
    }


    /**
     * 刷新缓存
     * @param pd
     * @return
     */
    public ResponseEntity<String> flushCache(IPageData pd){
        return listCachesSMOImpl.flushCache(pd);

    }

    public IListCachesSMO getListCachesSMOImpl() {
        return listCachesSMOImpl;
    }

    public void setListCachesSMOImpl(IListCachesSMO listCachesSMOImpl) {
        this.listCachesSMOImpl = listCachesSMOImpl;
    }

}
