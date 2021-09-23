package com.java110.api.components.carBlackWhite;


import com.java110.core.context.IPageData;
import com.java110.api.smo.carBlackWhite.IListCarBlackWhitesSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 黑白名单组件管理类
 * <p>
 * add by wuxw
 * <p>
 * 2019-06-29
 */
@Component("carBlackWhiteManage")
public class CarBlackWhiteManageComponent {

    @Autowired
    private IListCarBlackWhitesSMO listCarBlackWhitesSMOImpl;

    /**
     * 查询黑白名单列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listCarBlackWhitesSMOImpl.listCarBlackWhites(pd);
    }

    public IListCarBlackWhitesSMO getListCarBlackWhitesSMOImpl() {
        return listCarBlackWhitesSMOImpl;
    }

    public void setListCarBlackWhitesSMOImpl(IListCarBlackWhitesSMO listCarBlackWhitesSMOImpl) {
        this.listCarBlackWhitesSMOImpl = listCarBlackWhitesSMOImpl;
    }
}
