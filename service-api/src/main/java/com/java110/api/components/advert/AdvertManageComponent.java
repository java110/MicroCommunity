package com.java110.api.components.advert;


import com.java110.core.context.IPageData;
import com.java110.api.smo.advert.IListAdvertsSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 发布广告组件管理类
 * <p>
 * add by wuxw
 * <p>
 * 2019-06-29
 */
@Component("advertManage")
public class AdvertManageComponent {

    @Autowired
    private IListAdvertsSMO listAdvertsSMOImpl;

    /**
     * 查询发布广告列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listAdvertsSMOImpl.listAdverts(pd);
    }

    public IListAdvertsSMO getListAdvertsSMOImpl() {
        return listAdvertsSMOImpl;
    }

    public void setListAdvertsSMOImpl(IListAdvertsSMO listAdvertsSMOImpl) {
        this.listAdvertsSMOImpl = listAdvertsSMOImpl;
    }
}
