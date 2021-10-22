package com.java110.api.components.mapping;

import com.java110.core.context.IPageData;
import com.java110.api.smo.mapping.IEditMappingSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 编辑小区组件
 */
@Component("editMapping")
public class EditMappingComponent {

    @Autowired
    private IEditMappingSMO editMappingSMOImpl;

    /**
     * 添加小区数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> update(IPageData pd){
        return editMappingSMOImpl.updateMapping(pd);
    }

    public IEditMappingSMO getEditMappingSMOImpl() {
        return editMappingSMOImpl;
    }

    public void setEditMappingSMOImpl(IEditMappingSMO editMappingSMOImpl) {
        this.editMappingSMOImpl = editMappingSMOImpl;
    }
}
