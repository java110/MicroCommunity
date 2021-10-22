package com.java110.api.components.mapping;


import com.java110.core.context.IPageData;
import com.java110.api.smo.mapping.IListMappingsSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 编码映射组件管理类
 * <p>
 * add by wuxw
 * <p>
 * 2019-06-29
 */
@Component("mappingManage")
public class MappingManageComponent {

    @Autowired
    private IListMappingsSMO listMappingsSMOImpl;

    /**
     * 查询编码映射列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listMappingsSMOImpl.listMappings(pd);
    }

    public IListMappingsSMO getListMappingsSMOImpl() {
        return listMappingsSMOImpl;
    }

    public void setListMappingsSMOImpl(IListMappingsSMO listMappingsSMOImpl) {
        this.listMappingsSMOImpl = listMappingsSMOImpl;
    }
}
