package com.java110.api.components.mapping;

import com.java110.core.context.IPageData;
import com.java110.api.smo.mapping.IAddMappingSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加编码映射组件
 */
@Component("addMapping")
public class AddMappingComponent {

    @Autowired
    private IAddMappingSMO addMappingSMOImpl;

    /**
     * 添加编码映射数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd){
        return addMappingSMOImpl.saveMapping(pd);
    }

    public IAddMappingSMO getAddMappingSMOImpl() {
        return addMappingSMOImpl;
    }

    public void setAddMappingSMOImpl(IAddMappingSMO addMappingSMOImpl) {
        this.addMappingSMOImpl = addMappingSMOImpl;
    }
}
